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
import com.gnr.sgp.modelo.dominio.Animais;
import com.gnr.sgp.modelo.dominio.ComprasAnimais;
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
 * Classe responsavel pela ligacao com a tabela comprasAnimais no banco de
 * dados.
 *
 * @author Guilherme
 */
public class ComprasAnimaisDao {

    private final Conexao conexao;

    public ComprasAnimaisDao() {
        this.conexao = new ConexaoMysql();
    }

    public String Adicionar(ComprasAnimais compra) throws ParseException {
        String sql = "INSERT INTO compras_animais (id_animal, quantidade, kg_totais, media_kg, preco_kg, valor_total, criador, porce_comissao, comissao, pagador, pagamento, operador) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Animais animalTemp = buscarAnimaisId(compra.getId_animal());

            if (animalTemp == null) {
                JOptionPane.showMessageDialog(null, "Erro: Este ID de animal não existe no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
                pst.setInt(1, compra.getId_animal());
                pst.setInt(2, compra.getQuantidade());
                pst.setDouble(3, compra.getKg_totais());
                pst.setDouble(4, compra.getMedia_kg());
                pst.setDouble(5, compra.getPreco_kg());
                pst.setDouble(6, compra.getValor_total());
                pst.setString(7, compra.getCriador());
                pst.setDouble(8, compra.getPorce_comissao());
                pst.setDouble(9, compra.getComissao());
                pst.setString(10, compra.getPagador());
                pst.setString(11, compra.getPagamento());
                pst.setString(12, compra.getOperador());

                int resultado = pst.executeUpdate();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Compra finalizada com sucesso!");

                    String sqlUpdate = "UPDATE animais SET quantidade = quantidade + ? WHERE id = ?";
                    try {
                        PreparedStatement pstmt = conexao.obterConexao().prepareStatement(sqlUpdate);
                        pstmt.setInt(1, compra.getQuantidade());
                        pstmt.setInt(2, compra.getId_animal());

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
//                TelaFornecedor.limpaCampos(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível finalizar a compra.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar a compra.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return null;
    }

    private Animais getAnimais(ResultSet result) throws SQLException {
        Animais animal = new Animais();
        animal.setId(result.getLong("id"));
        animal.setDescricao(result.getString("descricao"));
        animal.setQuantidade(result.getString("quantidade"));
        animal.setIdade(result.getString("idade"));
        animal.setSexo(result.getString("sexo"));
        return animal;
    }

    public Animais buscarAnimaisId(int id) {
        String sql = String.format("SELECT * FROM animais WHERE id = '%s'", id);
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            if (result.next()) {
                return getAnimais(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cria um cupom em formato PDF com os detalhes da última compra.
     *
     * @throws SQLException Exceção relacionada ao acesso ao banco de dados.
     * @throws ParseException Exceção relacionada à análise de strings.
     */
    public void criarCupom() throws SQLException, ParseException {

        String path = "";

        String sqlPDF = "SELECT id_compra as ID, DATE_FORMAT(data_compra, '%d/%m/%Y') as Data, a.sexo as Sexo, a.idade as Idade, c.quantidade as Qtde, "
                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(c.kg_totais, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Kg_Totais, "
                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(c.media_kg, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Média_Kg, "
                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Preço_Kg, "
                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Total, "
                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(c.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Porce, "
                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(c.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Comissão, "
                + "criador as Criador, pagador as Pagador, pagamento as Pagamento, operador as Operador "
                + "FROM compras_animais c "
                + "JOIN animais a ON c.id_animal = a.id "
                + "ORDER BY id_compra DESC LIMIT 1";

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

                String idCompra = resultPDF.getString("ID");
                String dataCompra = data;
                String sexoAnimal = resultPDF.getString("Sexo");
                String idadeAnimal = resultPDF.getString("Idade");
                String quantidade = resultPDF.getString("Qtde");
                String kgTotais = resultPDF.getString("Kg_Totais");
                String mediaKg = resultPDF.getString("Média_Kg");
                String precoKgFormatado = resultPDF.getString("Preço_Kg");
                String valorTotalFormatado = resultPDF.getString("Total");
                String porcentagemComissao = resultPDF.getString("Porce");
                String comissaoFormatada = resultPDF.getString("Comissão");
                String criador = resultPDF.getString("Criador");
                String pagador = resultPDF.getString("Pagador");
                String pagamento = resultPDF.getString("Pagamento");
                String operador = resultPDF.getString("Operador");

                // Criar o documento PDF
                path = "C:\\Users\\" + username + "\\Documents\\Compra_N" + idCompra + ".pdf";
                PdfWriter pdfWriter = new PdfWriter(path);
                PdfDocument documentoPDF = new PdfDocument(pdfWriter);
                Document document = new Document(documentoPDF, PageSize.A6);

                // Adicionar conteúdo ao documento
                document.add(new Paragraph("CUPOM NÃO FISCAL").setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER));
                document.add(new Paragraph("--------------------------------------------------------"));

                // Detalhes da venda
                float[] vendaColumnWidths = {1, 1, 1};
                Table compraTable = new Table(vendaColumnWidths);
                compraTable.setWidthPercent(100);

                compraTable.addCell(new Cell().add("Pecuária MML").setFontSize(8).setBorder(Border.NO_BORDER));
                compraTable.addCell(new Cell().add("Compra n°: " + idCompra).setFontSize(8).setBorder(Border.NO_BORDER));
                compraTable.addCell(new Cell().add("Data: " + dataCompra).setFontSize(8).setBorder(Border.NO_BORDER));

                document.add(compraTable);
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

                infoTable.addCell(new Cell().add("Criador: " + criador).setFontSize(8).setBorder(Border.NO_BORDER));
                infoTable.addCell(new Cell().add("Pagador: " + pagador).setFontSize(8).setBorder(Border.NO_BORDER));
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
