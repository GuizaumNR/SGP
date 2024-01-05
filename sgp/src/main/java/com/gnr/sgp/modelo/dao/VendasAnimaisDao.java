/*
 * The MIT License
 *
 * Copyright 2023 Guilherme Rodrigues.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.gnr.sgp.modelo.dao;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dominio.VendasAnimais;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * Classe responsavel pela ligacao com a tabela vendasAnimais no banco de dados.
 *
 * @author Guilherme
 */
public class VendasAnimaisDao {

    private final Conexao conexao;

    public VendasAnimaisDao() {
        this.conexao = new ConexaoMysql();
    }

    public String Adicionar(VendasAnimais venda) throws ParseException {
        String sql = "INSERT INTO vendas_animais (id_animal, quantidade, kg_totais, media_kg, preco_kg, valor_total, porce_comissao, comissao, comprador, vendedor, pagamento, operador) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (verificarQuantidadeMenorZero(venda.getId_animal(), venda.getQuantidade()) && venda.getQuantidade() > 0) {
            try {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
                pst.setInt(1, venda.getId_animal());
                pst.setInt(2, venda.getQuantidade());
                pst.setDouble(3, venda.getKg_totais());
                pst.setDouble(4, venda.getMedia_kg());
                pst.setDouble(5, venda.getPreco_peso());
                pst.setDouble(6, venda.getValor_total());
                pst.setDouble(7, venda.getPorce_comissao());
                pst.setDouble(8, venda.getComissao());
                pst.setString(9, venda.getComprador());
                pst.setString(10, venda.getVendedor());
                pst.setString(11, venda.getPagamento());
                pst.setString(12, venda.getOperador());
                
                int resultado = pst.executeUpdate();
                
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Venda finalizada com sucesso!");
                    
                    String sqlUpdate = "UPDATE animais SET quantidade = quantidade - ? WHERE id = ?";
                    try {
                        PreparedStatement pstmt = conexao.obterConexao().prepareStatement(sqlUpdate);
                        pstmt.setInt(1, venda.getQuantidade());
                        pstmt.setInt(2, venda.getId_animal());

                        int resultado2 = pstmt.executeUpdate();

                        if (resultado2 > 0) {
                            JOptionPane.showMessageDialog(null, "Quantidade de animais atualizada com sucesso!");
                            criarCupom();
                        } else {
                            JOptionPane.showMessageDialog(null, "Não foi possível atualizar a quantidade de animais.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar a quantidade de animais: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível finalizar o falecimento.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar o venda.", "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Quantidade de animais insuficiente para esta operação.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public boolean verificarQuantidadeMenorZero(int id_animal, int quantidadeVenda) {
        String sql = "SELECT quantidade FROM animais WHERE id = ?";
        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setInt(1, id_animal);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int quantidade = rs.getInt("quantidade") - quantidadeVenda;
                return quantidade >= 0;
            } else {
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar quantidade de animais: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
     /**
     * Cria um cupom em PDF com os detalhes da última venda.
     *
     * @throws SQLException Se ocorrer um erro de SQL durante a execução.
     * @throws ParseException Se ocorrer um erro de parse durante a execução.
     */
    public void criarCupom() throws SQLException, ParseException {

        String path = "";

        String sqlPDF = "SELECT id_venda, DATE_FORMAT(data_venda, '%d/%m/%Y') as data_formatada,  a.sexo as sexo_animal, a.idade as idade_animal, v.quantidade, v.quantidade, v.preco_kg, "
                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(v.kg_totais, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Kg_Totais, "
                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(v.media_kg, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Média_Kg, "
                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as preco_kg_formatado, "
                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as valor_total_formatado, "
                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(v.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as porce_formatado, "
                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(v.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as comissao_formatado, "
                + "vendedor, comprador, pagamento, operador "
                + "FROM vendas_animais v "
                + "JOIN animais a ON v.id_animal = a.id "
                + "ORDER BY id_venda DESC LIMIT 1";

        try {
            // Linhas da tabela com os dados do ResultSet
            PdfFont dataFont = PdfFontFactory.createFont();
            PreparedStatement pstPDF = conexao.obterConexao().prepareStatement(sqlPDF);
            ResultSet resultPDF = pstPDF.executeQuery();

            if (resultPDF.next()) {

                String username = System.getProperty("user.name");
                Calendar now = Calendar.getInstance();
                String hora = String.format("%1$tH:%1$tM:%1$tS", now);
                String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + " " + hora;

                String idVenda = resultPDF.getString("id_venda");
                String dataVenda = data;
                String sexoAnimal = resultPDF.getString("sexo_animal");
                String idadeAnimal = resultPDF.getString("idade_animal");
                String quantidade = resultPDF.getString("quantidade");
                String kgTotais = resultPDF.getString("Kg_Totais");
                String mediaKg = resultPDF.getString("Média_Kg");
                String precoKgFormatado = resultPDF.getString("preco_kg_formatado");
                String valorTotalFormatado = resultPDF.getString("valor_total_formatado");
                String porcentagemComissao = resultPDF.getString("porce_formatado");
                String comissaoFormatada = resultPDF.getString("comissao_formatado");
                String vendedor = resultPDF.getString("vendedor");
                String comprador = resultPDF.getString("comprador");
                String pagamento = resultPDF.getString("pagamento");
                String operador = resultPDF.getString("operador");

                // Criar o documento PDF
                path = "C:\\Users\\" + username + "\\Documents\\Venda_N" + idVenda + ".pdf";
                PdfWriter pdfWriter = new PdfWriter(path);
                PdfDocument documentoPDF = new PdfDocument(pdfWriter);
                Document document = new Document(documentoPDF, PageSize.A6);

                // Adicionar conteúdo ao documento
                document.add(new Paragraph("CUPOM NÃO FISCAL").setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("--------------------------------------------------------"));

                // Detalhes da venda
                float[] vendaColumnWidths = {1, 1, 1};
                Table vendaTable = new Table(vendaColumnWidths);
                vendaTable.setWidthPercent(100);

                vendaTable.addCell(new Cell().add("Pecuária MML").setFontSize(8).setBorder(Border.NO_BORDER));
                vendaTable.addCell(new Cell().add("Venda n°: " + idVenda).setFontSize(8).setBorder(Border.NO_BORDER));
                vendaTable.addCell(new Cell().add("Data: " + dataVenda).setFontSize(8).setBorder(Border.NO_BORDER));

                document.add(vendaTable);
                document.add(new Paragraph("--------------------------------------------------------"));

                // Detalhes do animal
                float[] animalColumnWidths = {3, 3, 1, 1, 1, 1, 1};
                Table animalTable = new Table(animalColumnWidths);
                animalTable.setWidthPercent(100);

                animalTable.addCell(new Cell().add("Sexo: " + sexoAnimal).setFontSize(8).setBorder(Border.NO_BORDER));
                animalTable.addCell(new Cell().add("Idade: " + idadeAnimal).setFontSize(8).setBorder(Border.NO_BORDER));
                animalTable.addCell(new Cell().add("Quantidade: " + quantidade).setFontSize(8).setBorder(Border.NO_BORDER));
                animalTable.addCell(new Cell().add(" ").setFontSize(8).setBorder(Border.NO_BORDER));
                animalTable.addCell(new Cell().add(" ").setFontSize(8).setBorder(Border.NO_BORDER));
                animalTable.addCell(new Cell().add("Kilos Totais: " + kgTotais).setFontSize(8).setBorder(Border.NO_BORDER));
                animalTable.addCell(new Cell().add("Média dos Kilos: " + mediaKg).setFontSize(8).setBorder(Border.NO_BORDER));

                document.add(animalTable);
                document.add(new Paragraph("--------------------------------------------------------"));

                // Valores financeiros
                float[] valoresColumnWidths = {1, 1};
                Table valoresTable = new Table(valoresColumnWidths);
                valoresTable.setWidthPercent(100);

                valoresTable.addCell(new Cell().add("Preço por Kg: " + precoKgFormatado).setFontSize(8).setBorder(Border.NO_BORDER));
                valoresTable.addCell(new Cell().add("Valor Total: " + valorTotalFormatado).setFontSize(8).setBorder(Border.NO_BORDER));

                document.add(new Paragraph("Valores:").setFontSize(8));
                document.add(valoresTable);
                document.add(new Paragraph("--------------------------------------------------------"));

                // Comissão
                float[] comissaoColumnWidths = {1, 1};
                Table comissaoTable = new Table(comissaoColumnWidths);
                comissaoTable.setWidthPercent(100);

                comissaoTable.addCell(new Cell().add("Porcentagem: " + porcentagemComissao).setFontSize(8).setBorder(Border.NO_BORDER));
                comissaoTable.addCell(new Cell().add("Valor: " + comissaoFormatada).setFontSize(8).setBorder(Border.NO_BORDER));

                document.add(new Paragraph("Comissão:").setFontSize(8));
                document.add(comissaoTable);
                document.add(new Paragraph("--------------------------------------------------------"));

                // Informações adicionais
                float[] infoColumnWidths = {1, 1, 1, 1};
                Table infoTable = new Table(infoColumnWidths);
                infoTable.setWidthPercent(100);

                infoTable.addCell(new Cell().add("Vendedor: " + vendedor).setFontSize(8).setBorder(Border.NO_BORDER));
                infoTable.addCell(new Cell().add("Comprador: " + comprador).setFontSize(8).setBorder(Border.NO_BORDER));
                infoTable.addCell(new Cell().add("Pagamento: " + pagamento).setFontSize(8).setBorder(Border.NO_BORDER));
                infoTable.addCell(new Cell().add("Operador: " + operador).setFontSize(8).setBorder(Border.NO_BORDER));

                document.add(new Paragraph("Informações adicionais:").setFontSize(8));
                document.add(infoTable);
                // Fechar o documento
                document.close();
            }
            JOptionPane.showMessageDialog(null, "PDF criado em " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
