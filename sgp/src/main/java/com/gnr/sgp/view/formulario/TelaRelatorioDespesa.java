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
package com.gnr.sgp.view.formulario;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;
import net.proteanit.sql.DbUtils;

/**
 * Classse responsavel pelos relatorios de vendas.
 *
 * @author Guilherme
 */
public class TelaRelatorioDespesa extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaVenda
     */
    private final Conexao conexao;
    PreparedStatement pst = null;
    ResultSet rs = null;

    MaskFormatter mfData;

    Date dataSistema = new Date();
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

    public TelaRelatorioDespesa() {

        this.conexao = new ConexaoMysql();

        try {

            mfData = new MaskFormatter(" ##/##/####");
        } catch (ParseException ex) {
            System.out.println("Ocorreu um erro ao criar a mascara. " + ex);
        }

        initComponents();

        setLocation(-5, -5);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                // Define a posição do componente para (0, 0)
                setLocation(-5, -5);
            }
        });

    }

    /**
     * Deleta um registro de venda e ajusta a quantidade de animais.
     *
     * @throws SQLException
     */
    public void deletar() throws SQLException {
        int setar = jTableRelComp.getSelectedRow();
        String valorId = jTableRelComp.getModel().getValueAt(setar, 0).toString();

        String sqlAnimal = "UPDATE animais a "
                + "JOIN vendas_animais v ON a.id = v.id_animal "
                + "SET a.quantidade = a.quantidade + v.quantidade "
                + "WHERE v.id_venda = '" + valorId + "' ";

        String sqlCompra = "DELETE FROM vendas_animais WHERE id_venda = '" + valorId + "' ";

        pst = conexao.obterConexao().prepareStatement(sqlAnimal);
        int resultado = pst.executeUpdate();

        PreparedStatement pstmt = conexao.obterConexao().prepareStatement(sqlCompra);
        int resultado2 = pstmt.executeUpdate();

        System.out.println(valorId);

        if (resultado > 0 && resultado2 > 0) {
            JOptionPane.showMessageDialog(null, "Registro deletado, e quantidade de animais ajustada.");
            try {
                criarLista(jFormattedRelVendInicio.getText(), jFormattedRelVendaFim.getText(), jComboRelVendaOrdem.getSelectedItem().toString(), jComboRelVendaPagamento.getSelectedItem().toString());

            } catch (SQLException ex) {
                Logger.getLogger(TelaRelatorioDespesa.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (ParseException ex) {
                Logger.getLogger(TelaRelatorioDespesa.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Valida se a data está no formato correto.
     *
     * @param dateStr Data no formato string.
     * @return true se a data for válida, false caso contrário.
     */
    private static boolean validaData(String dateStr) {
        String[] parts = dateStr.split("/");

        try {
            int dia = Integer.parseInt(parts[0].trim()); // Remova espaços em branco
            int mes = Integer.parseInt(parts[1].trim()); // Remova espaços em branco

            return (dia >= 1 && dia <= 31) && (mes >= 1 && mes <= 12);
        } catch (NumberFormatException e) {
            return false; // Não é possível converter para números inteiros
        }
    }

    /**
     * Formata o valor do peso.
     *
     * @param valor Valor do peso.
     * @return Valor formatado.
     */
    public static String formatarPeso(double valor) {
        // Crie um objeto DecimalFormat com o formato desejado
        DecimalFormat df = new DecimalFormat("#,##0.00 kg");

        // Formate o valor como uma string
        String valorFormatado = df.format(valor);

        return valorFormatado;
    }

    /**
     * Formata o valor em moeda.
     *
     * @param valor Valor a ser formatado.
     * @return Valor formatado em moeda.
     */
    public static String formatarValor(double valor) {
        // Crie um objeto DecimalFormat com o formato desejado
        DecimalFormat df = new DecimalFormat("R$ #,##0.00");

        // Formate o valor como uma string
        String valorFormatado = df.format(valor);

        return valorFormatado;
    }

    /**
     * Reverte o valor formatado para um double.
     *
     * @param valorFormatado Valor formatado.
     * @return Valor revertido para double.
     */
    public static double reverterValorFormatado(String valorFormatado) {
        // Remova o prefixo "R$"
        valorFormatado = valorFormatado.replace("R$", "").trim();

        // Substitua o ponto pelo ponto como separador decimal
        valorFormatado = valorFormatado.replace(".", "");

        // Substitua a vírgula por ponto como separador decimal
        valorFormatado = valorFormatado.replace(",", ".");

        try {
            // Converta a string para um tipo double
            double valor = Double.parseDouble(valorFormatado);

            return valor;
        } catch (NumberFormatException e) {
            // Lida com valores que não podem ser convertidos para double
            System.out.println("Erro ao reverter o valor formatado em double: " + e.getMessage());
            return 0.0; // ou outro valor padrão de sua escolha
        }
    }

    /**
     * Cria uma lista de vendas com base nos parâmetros fornecidos e a exibe em
     * um JTable.
     *
     * @param inicio Data de início no formato "dd/MM/yyyy".
     * @param fim Data de término no formato "dd/MM/yyyy".
     * @param ordem Coluna pela qual os resultados devem ser ordenados.
     * @param pagamento Método de pagamento a ser filtrado.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     * @throws ParseException Se ocorrer um erro ao fazer parsing das datas.
     */
    public void criarLista(String inicio, String fim, String ordem, String pagamento) throws SQLException, ParseException {

        Date dataInicio = new Date();
        Date dataFim = new Date();
// Convertendo as strings para o formato de data padrão
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (!jRadioButtonRelVendaHoje.isSelected()) {

            dataInicio = dateFormat.parse(inicio);
            dataFim = dateFormat.parse(fim);

        } else {
            // Defina a data atual como base

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataSistema);

            // Obtenha o último dia do mês
            int ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            // Configure o dia para o último dia do mês
            calendar.set(Calendar.DAY_OF_MONTH, ultimoDia);
            // Obtenha a data do último dia do mês
            Date ultimoDiaDoMes = calendar.getTime();
            String ultimoDiaFormatado = formato.format(ultimoDiaDoMes);

            dataInicio = dateFormat.parse(formato.format(dataSistema));
            dataFim = dateFormat.parse(ultimoDiaFormatado);
        }
        try {
            if (validaData(dateFormat.format(dataInicio)) && validaData(dateFormat.format(dataFim))) {

                if (dateFormat.format(dataInicio).equals(dateFormat.format(dataFim))) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dataFim);

                    // Adicione um mês
                    calendar.add(Calendar.MONTH, 1);

                    // Acesse a nova data após adicionar um mês
                    Date dataAposUmMes = calendar.getTime();

                    dataFim = dataAposUmMes;
                }

// Convertendo as datas de volta para o formato do banco de dados
                SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String inicioFormatado = dbDateFormat.format(dataInicio);
                String fimFormatado = dbDateFormat.format(dataFim);
                String sql = "";

                if (jRadioButtonRelVendaDesc.isSelected()) {
                    if (pagamento != "*") {
                        sql = "SELECT id_venda as ID, DATE_FORMAT(data_venda, '%d/%m/%Y') as Data,  a.sexo as Sexo, a.idade as Idade, v.quantidade as Qtde, "
                                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(v.kg_totais, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Kg_Totais, "
                                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(v.media_kg, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Média_Kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Preço_Kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Total, "
                                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(v.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Porce, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(v.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Comissão, "
                                + "vendedor as Vendedor, comprador as Comprador, pagamento as Pagamento, operador as Operador "
                                + "FROM vendas_animais v "
                                + "JOIN animais a ON v.id_animal = a.id "
                                + "WHERE data_venda BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "AND pagamento = '" + pagamento + "' "
                                + "ORDER BY v." + ordem + " DESC";

                    } else {
                        sql = "SELECT id_venda as ID, DATE_FORMAT(data_venda, '%d/%m/%Y') as Data,  a.sexo as Sexo, a.idade as Idade, v.quantidade as Qtde, "
                                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(v.kg_totais, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Kg_Totais, "
                                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(v.media_kg, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Média_Kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Preço_Kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Total, "
                                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(v.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Porce, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(v.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Comissão, "
                                + "vendedor as Vendedor, comprador as Comprador, pagamento as Pagamento, operador as Operador "
                                + "FROM vendas_animais v "
                                + "JOIN animais a ON v.id_animal = a.id "
                                + "WHERE data_venda BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "ORDER BY v." + ordem + " DESC";
                    }

                } else {
                    if (pagamento != "*") {
                        sql = "SELECT id_venda as ID, DATE_FORMAT(data_venda, '%d/%m/%Y') as Data,  a.sexo as Sexo, a.idade as Idade, v.quantidade as Qtde, "
                                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(v.kg_totais, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Kg_Totais, "
                                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(v.media_kg, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Média_Kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Preço_Kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Total, "
                                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(v.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Porce, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(v.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Comissão, "
                                + "vendedor as Vendedor, comprador as Comprador, pagamento as Pagamento, operador as Operador "
                                + "FROM vendas_animais v "
                                + "JOIN animais a ON v.id_animal = a.id "
                                + "WHERE data_venda BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "AND pagamento = '" + pagamento + "' "
                                + "ORDER BY v." + ordem;
                    } else {
                        sql = "SELECT id_venda as ID, DATE_FORMAT(data_venda, '%d/%m/%Y') as Data,  a.sexo as Sexo, a.idade as Idade, v.quantidade as Qtde, "
                                + "CONCAT(REPLACE(REPLACE(REPLACE(FORMAT(v.kg_totais, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Kg_Totais, "
                                + "CONCAT('Kg ', REPLACE(REPLACE(REPLACE(FORMAT(v.media_kg, 2), '.', 'temp'), ',', '.'), 'temp', ','), ' Kg') as Média_Kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Preço_Kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Total, "
                                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(v.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Porce, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(v.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Comissão, "
                                + "vendedor as Vendedor, comprador as Comprador, pagamento as Pagamento, operador as Operador "
                                + "FROM vendas_animais v "
                                + "JOIN animais a ON v.id_animal = a.id "
                                + "WHERE data_venda BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "ORDER BY v." + ordem;
                    }
                }

                pst = conexao.obterConexao().prepareStatement(sql);
                rs = pst.executeQuery();

                jTableRelComp.setModel(DbUtils.resultSetToTableModel(rs));

            } else {
                JOptionPane.showMessageDialog(null, "Data inválida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data inválida.");
        }

    }

    /**
     * Cria um documento PDF com base nos parâmetros fornecidos e os exibe.
     *
     * @param inicio Data de início no formato "dd/MM/yyyy".
     * @param fim Data de término no formato "dd/MM/yyyy".
     * @param ordem Coluna pela qual os resultados devem ser ordenados.
     * @param pagamento Método de pagamento a ser filtrado.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     * @throws ParseException Se ocorrer um erro ao fazer parsing das datas.
     */
    public void criarDocumento(String inicio, String fim, String ordem, String pagamento) throws SQLException, ParseException {

        Date dataInicio = new Date();
        Date dataFim = new Date();
// Convertendo as strings para o formato de data padrão
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (!jRadioButtonRelVendaHoje.isSelected()) {

            dataInicio = dateFormat.parse(inicio);
            dataFim = dateFormat.parse(fim);

        } else {
            // Defina a data atual como base

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataSistema);

            // Obtenha o último dia do mês
            int ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            // Configure o dia para o último dia do mês
            calendar.set(Calendar.DAY_OF_MONTH, ultimoDia);
            // Obtenha a data do último dia do mês
            Date ultimoDiaDoMes = calendar.getTime();
            String ultimoDiaFormatado = formato.format(ultimoDiaDoMes);

            dataInicio = dateFormat.parse(formato.format(dataSistema));
            dataFim = dateFormat.parse(ultimoDiaFormatado);
        }
        try {
            if (validaData(dateFormat.format(dataInicio)) && validaData(dateFormat.format(dataFim))) {

                if (dateFormat.format(dataInicio).equals(dateFormat.format(dataFim))) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dataFim);

                    // Adicione um mês
                    calendar.add(Calendar.MONTH, 1);

                    // Acesse a nova data após adicionar um mês
                    Date dataAposUmMes = calendar.getTime();

                    dataFim = dataAposUmMes;
                }

// Convertendo as datas de volta para o formato do banco de dados
                SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String inicioFormatado = dbDateFormat.format(dataInicio);
                String fimFormatado = dbDateFormat.format(dataFim);
                String sqlPDF = "";

                if (jRadioButtonRelVendaDesc.isSelected()) {
                    if (pagamento != "*") {
                        sqlPDF = "SELECT id_venda, DATE_FORMAT(data_venda, '%d/%m/%Y') as data_formatada,  a.sexo as sexo_animal, a.idade as idade_animal, v.quantidade, v.kg_totais, v.quantidade, v.media_kg, v.preco_kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as preco_kg_formatado, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as valor_total_formatado, "
                                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(v.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as porce_formatado, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(v.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as comissao_formatado, "
                                + "vendedor, comprador, pagamento, operador "
                                + "FROM vendas_animais v "
                                + "JOIN animais a ON v.id_animal = a.id "
                                + "WHERE data_venda BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "AND pagamento = '" + pagamento + "' "
                                + "ORDER BY v." + ordem + " DESC";

                    } else {
                        sqlPDF = "SELECT id_venda, DATE_FORMAT(data_venda, '%d/%m/%Y') as data_formatada,  a.sexo as sexo_animal, a.idade as idade_animal, v.quantidade, v.kg_totais, v.quantidade, v.media_kg, v.preco_kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as preco_kg_formatado, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as valor_total_formatado, "
                                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(v.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as porce_formatado, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(v.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as comissao_formatado, "
                                + "vendedor, comprador, pagamento, operador "
                                + "FROM vendas_animais v "
                                + "JOIN animais a ON v.id_animal = a.id "
                                + "WHERE data_venda BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "ORDER BY v." + ordem + " DESC";
                    }

                } else {
                    if (pagamento != "*") {
                        sqlPDF = "SELECT id_venda, DATE_FORMAT(data_venda, '%d/%m/%Y') as data_formatada,  a.sexo as sexo_animal, a.idade as idade_animal, v.quantidade, v.kg_totais, v.quantidade, v.media_kg, v.preco_kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as preco_kg_formatado, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as valor_total_formatado, "
                                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(v.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as porce_formatado, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(v.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as comissao_formatado, "
                                + "vendedor, comprador, pagamento, operador "
                                + "FROM vendas_animais v "
                                + "JOIN animais a ON v.id_animal = a.id "
                                + "WHERE data_venda BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "AND pagamento = '" + pagamento + "' "
                                + "ORDER BY v." + ordem;
                    } else {
                        sqlPDF = "SELECT id_venda, DATE_FORMAT(data_venda, '%d/%m/%Y') as data_formatada,  a.sexo as sexo_animal, a.idade as idade_animal, v.quantidade, v.kg_totais, v.quantidade, v.media_kg, v.preco_kg, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(preco_kg, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as preco_kg_formatado, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor_total, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as valor_total_formatado, "
                                + "CONCAT('% ', REPLACE(REPLACE(REPLACE(FORMAT(v.porce_comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as porce_formatado, "
                                + "CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(v.comissao, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as comissao_formatado, "
                                + "vendedor, comprador, pagamento, operador "
                                + "FROM vendas_animais v "
                                + "JOIN animais a ON v.id_animal = a.id "
                                + "WHERE data_venda BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "ORDER BY v." + ordem;
                    }
                }
                try {
                    String username = System.getProperty("user.name");
                    String data = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
                    String path = "C:\\Users\\" + username + "\\Documents\\Relatorio_Vendas_" + data + ".pdf";

                    PdfWriter pdfWriter = new PdfWriter(path);
                    PdfDocument documentoPDF = new PdfDocument(pdfWriter);
                    Document document = new Document(documentoPDF, PageSize.A4);

                    float[] columnWidths = {1, 3, 1, 1, 1, 3, 3, 3, 4, 2, 3, 4, 4, 4, 4};
                    Table table = new Table(columnWidths);
                    table.setWidthPercent(100);
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    // Definindo fontes
                    PdfFont fontBold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
                    PdfFont fontNormal = PdfFontFactory.createFont(FontConstants.HELVETICA);

                    PdfPage firstPage = documentoPDF.addNewPage();
                    PdfCanvas canvas = new PdfCanvas(firstPage);
                    canvas.beginText()
                            .setFontAndSize(fontNormal, 8)
                            .moveText(36, 806)
                            .showText("Pecuária MML")
                            .endText();

                    String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    canvas.beginText()
                            .setFontAndSize(fontNormal, 8)
                            .moveText(484, 806)
                            .showText("Emissão: " + currentDate)
                            .endText();

                    SolidLine separatorLine = new SolidLine(1);
                    document.add(new Paragraph("")).add(new LineSeparator(separatorLine));

                    document.add(new Paragraph("Relatório de Vendas")
                            .setFont(fontBold)
                            .setFontSize(18)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph("Período: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " a " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim))
                            .setFont(fontNormal)
                            .setFontSize(12)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph(""));

                    PdfFont headerFont = PdfFontFactory.createFont();
                    String[] headers = {"ID", "Data", "Sexo", "Idade", "Qtde", "Kg Totais", "Média Kg", "Preço Kg", "Total", "%", "Comissão", "Vend", "Comp", "Pag", "Operador"};
                    for (String header : headers) {
                        Cell cell = new Cell().add(header).setFont(headerFont).setFontSize(10).setBackgroundColor(DeviceGray.BLACK).setTextAlignment(TextAlignment.CENTER).setFontColor(DeviceGray.WHITE);
                        table.addCell(cell);
                    }

                    // Linhas da tabela com os dados do ResultSet
                    PdfFont dataFont = PdfFontFactory.createFont();
                    PreparedStatement pstPDF = conexao.obterConexao().prepareStatement(sqlPDF);
                    ResultSet resultPDF = pstPDF.executeQuery();

                    int totalQuantidade = 0;
                    double totalKilo = 0;
                    double totalValor = 0;
                    double totalComissao = 0;

                    while (resultPDF.next()) {
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("id_venda")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(resultPDF.getString("data_formatada")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("sexo_animal")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("idade_animal")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("quantidade")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(formatarPeso(Double.parseDouble(resultPDF.getString("kg_totais")))));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(formatarPeso(Double.parseDouble(resultPDF.getString("media_kg")))));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(14).add(resultPDF.getString("preco_kg_formatado")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(40).add(resultPDF.getString("valor_total_formatado")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("porce_formatado")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("comissao_formatado")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("vendedor")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("comprador")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("pagamento")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(resultPDF.getString("operador")));

                        int quantidade = resultPDF.getInt("quantidade"); // Substitua o nome da coluna conforme necessário
                        totalQuantidade += quantidade;

                        String totais = resultPDF.getString("kg_totais");
                        Double totaisFormatado = Double.parseDouble(totais);
                        totalKilo += totaisFormatado;

                        String total = resultPDF.getString("valor_total_formatado");
                        double totalFormatado = reverterValorFormatado(total);
                        totalValor += totalFormatado;

                        String comissao = resultPDF.getString("comissao_formatado");
                        double comissaoFormatado = reverterValorFormatado(comissao);
                        totalComissao += comissaoFormatado;

                    }

                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add("Totais"));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(String.valueOf(totalQuantidade)));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(formatarPeso(totalKilo)));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(25).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(formatarValor(totalValor)));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(formatarValor(totalComissao)));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(""));

                    table.setAutoLayout();
                    document.add(table);
                    document.close();

                    JOptionPane.showMessageDialog(null, "PDF criado em " + path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Data inválida.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data inválida.");
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFormattedRelVendInicio = new javax.swing.JFormattedTextField(mfData);
        jLabelRelVendaPeriodoA = new javax.swing.JLabel();
        jFormattedRelVendaFim = new javax.swing.JFormattedTextField(mfData);
        jButtonRelVenda = new javax.swing.JButton();
        jLabelRelVendaPagamento = new javax.swing.JLabel();
        jLabelRelVendaPeriodo = new javax.swing.JLabel();
        jComboRelVendaOrdem = new javax.swing.JComboBox<>();
        jLabelRelVendaOrdem = new javax.swing.JLabel();
        jComboRelVendaPagamento = new javax.swing.JComboBox<>();
        jLabelRelVendaPagamentoParenteses = new javax.swing.JLabel();
        jRadioButtonRelVendaHoje = new javax.swing.JRadioButton();
        jRadioButtonRelVendaDesc = new javax.swing.JRadioButton();
        jButtonRelCompraLista = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRelComp = new javax.swing.JTable();
        jButtonRelVendaExcluir = new javax.swing.JButton();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Relatório de Vendas");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        //jFormattedRelVendInicio.setText(formato.format(dataSistema));
        jFormattedRelVendInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelVendInicioActionPerformed(evt);
            }
        });

        jLabelRelVendaPeriodoA.setText("a");

        jFormattedRelVendaFim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelVendaFimActionPerformed(evt);
            }
        });

        jButtonRelVenda.setText("Gerar PDF");
        jButtonRelVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelVendaActionPerformed(evt);
            }
        });

        jLabelRelVendaPagamento.setText("Pagamento:");

        jLabelRelVendaPeriodo.setText("Período:");

        jComboRelVendaOrdem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "data_venda", "quantidade", "valor_total", "vendedor", "comprador" }));

        jLabelRelVendaOrdem.setText("Ordem:");

        jComboRelVendaPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "*", "Dinheiro", "Cartão Débito", "Cartão Crédito", "Pix", "Permuta" }));
        jComboRelVendaPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRelVendaPagamentoActionPerformed(evt);
            }
        });

        jLabelRelVendaPagamentoParenteses.setText("(\" * \" Todas formas )");

        jRadioButtonRelVendaHoje.setText("Hoje");

        jRadioButtonRelVendaDesc.setText("Decrescente");

        jButtonRelCompraLista.setText("Gerar Lista");
        jButtonRelCompraLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelCompraListaActionPerformed(evt);
            }
        });

        jTableRelComp = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jTableRelComp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Data", "Sexo", "Idade", "Qtde", "Kg_Totais", "Média_Kg", "Preço_Kg", "Total", "Porce", "Comissão", "Vendedor", "Comprador", "Pagamento", "Operador"
            }
        ));
        jTableRelComp.setFocusable(false);
        jTableRelComp.getTableHeader().setReorderingAllowed(false);
        jTableRelComp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRelCompMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableRelComp);

        jButtonRelVendaExcluir.setText("Excluir Registro");
        jButtonRelVendaExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelVendaExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonRelVendaExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonRelCompraLista)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRelVenda))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelVendaOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboRelVendaOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelVendaDesc))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelVendaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedRelVendInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelRelVendaPeriodoA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jFormattedRelVendaFim, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelVendaHoje)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelRelVendaPagamento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboRelVendaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelRelVendaPagamentoParenteses)))
                        .addGap(0, 356, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedRelVendInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelVendaPeriodoA)
                    .addComponent(jFormattedRelVendaFim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelVendaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonRelVendaHoje)
                    .addComponent(jLabelRelVendaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboRelVendaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelVendaPagamentoParenteses, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboRelVendaOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelVendaOrdem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButtonRelVendaDesc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRelVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRelCompraLista, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRelVendaExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedRelVendInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelVendInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelVendInicioActionPerformed

    private void jFormattedRelVendaFimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelVendaFimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelVendaFimActionPerformed

    private void jButtonRelVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelVendaActionPerformed

        try {
            criarDocumento(jFormattedRelVendInicio.getText(), jFormattedRelVendaFim.getText(), jComboRelVendaOrdem.getSelectedItem().toString(), jComboRelVendaPagamento.getSelectedItem().toString());

        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioDespesa.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioDespesa.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonRelVendaActionPerformed

    private void jComboRelVendaPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRelVendaPagamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboRelVendaPagamentoActionPerformed

    private void jButtonRelCompraListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelCompraListaActionPerformed
        try {
            criarLista(jFormattedRelVendInicio.getText(), jFormattedRelVendaFim.getText(), jComboRelVendaOrdem.getSelectedItem().toString(), jComboRelVendaPagamento.getSelectedItem().toString());

        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioDespesa.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioDespesa.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRelCompraListaActionPerformed

    private void jTableRelCompMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRelCompMouseClicked

    }//GEN-LAST:event_jTableRelCompMouseClicked

    private void jButtonRelVendaExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelVendaExcluirActionPerformed
        try {
            deletar();
        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRelVendaExcluirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRelCompraLista;
    private javax.swing.JButton jButtonRelVenda;
    private javax.swing.JButton jButtonRelVendaExcluir;
    private javax.swing.JComboBox<String> jComboRelVendaOrdem;
    private javax.swing.JComboBox<String> jComboRelVendaPagamento;
    public javax.swing.JFormattedTextField jFormattedRelVendInicio;
    private javax.swing.JFormattedTextField jFormattedRelVendaFim;
    private javax.swing.JLabel jLabelRelVendaOrdem;
    private javax.swing.JLabel jLabelRelVendaPagamento;
    private javax.swing.JLabel jLabelRelVendaPagamentoParenteses;
    private javax.swing.JLabel jLabelRelVendaPeriodo;
    private javax.swing.JLabel jLabelRelVendaPeriodoA;
    private javax.swing.JRadioButton jRadioButtonRelVendaDesc;
    private javax.swing.JRadioButton jRadioButtonRelVendaHoje;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableRelComp;
    // End of variables declaration//GEN-END:variables
}
