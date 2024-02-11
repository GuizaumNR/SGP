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
import com.gnr.sgp.modelo.dominio.Despesas;
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
 * Classse responsavel pelos relatorios de depesas.
 *
 * @author Guilherme
 */
public class TelaRelatorioDespesa extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaRelatorioDespesa
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
     * Deleta um registro de despesa.
     *
     * @throws SQLException
     */
    public void deletar() throws SQLException {
        int setar = jTableRelDespesa.getSelectedRow();
        String valorId = jTableRelDespesa.getModel().getValueAt(setar, 0).toString();

        String sql = "DELETE FROM despesa WHERE id = ?";
        pst.setString(1, valorId);

        pst = conexao.obterConexao().prepareStatement(sql);
        int resultado = pst.executeUpdate();

        if (resultado > 0) {
            JOptionPane.showMessageDialog(null, "Registro deletado, e quantidade de animais ajustada.");
            try {
                criarLista(jFormattedRelDespesaInicio.getText(), jFormattedRelDespesaFim.getText(), jComboRelDespesaOrdem.getSelectedItem().toString(), jComboRelDespesaPagador.getSelectedItem().toString());

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
     * Cria uma lista de despesas com base nos parâmetros fornecidos e a exibe em
     * um JTable.
     *
     * @param inicio Data de início no formato "dd/MM/yyyy".
     * @param fim Data de término no formato "dd/MM/yyyy".
     * @param ordem Coluna pela qual os resultados devem ser ordenados.
     * @param pagador a ser filtrado.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     * @throws ParseException Se ocorrer um erro ao fazer parsing das datas.
     */
    public void criarLista(String inicio, String fim, String ordem, String pagador) throws SQLException, ParseException {

        Date dataInicio = new Date();
        Date dataFim = new Date();
// Convertendo as strings para o formato de data padrão
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (!jRadioButtonRelDespesaHoje.isSelected()) {

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

                if (jRadioButtonRelDespesaDesc.isSelected()) {
                    if (pagador != "*") {
                        sql = "SELECT id as ID, DATE_FORMAT(data_despesa, '%d/%m/%Y') as Data, descricao as Descrição, CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Valor, "
                                + "categoria as Categoria, pagador as Pagador, operador as Operador "
                                + "FROM despesas d "
                                + "WHERE data_despesa BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "AND pagador = '" + pagador + "' "
                                + "ORDER BY d." + ordem + " DESC";

                    } else {
                        sql = "SELECT id as ID, DATE_FORMAT(data_despesa, '%d/%m/%Y') as Data, descricao as Descrição, CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Valor, "
                                + "categoria as Categoria, pagador as Pagador, operador as Operador "
                                + "FROM despesas d "
                                + "WHERE data_despesa BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "ORDER BY d." + ordem + " DESC";
                    }

                } else {
                    if (pagador != "*") {
                        sql = "SELECT id as ID, DATE_FORMAT(data_despesa, '%d/%m/%Y') as Data, descricao as Descrição, CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Valor, "
                                + "categoria as Categoria, pagador as Pagador, operador as Operador "
                                + "FROM despesas d "
                                + "WHERE data_despesa BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "AND pagador = '" + pagador + "' "
                                + "ORDER BY d." + ordem;
                    } else {
                        sql = "SELECT id as ID, DATE_FORMAT(data_despesa, '%d/%m/%Y') as Data, descricao as Descrição, CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Valor, "
                                + "categoria as Categoria, pagador as Pagador, operador as Operador "
                                + "FROM despesas d "
                                + "WHERE data_despesa BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "ORDER BY d." + ordem;
                    }
                }

                pst = conexao.obterConexao().prepareStatement(sql);
                rs = pst.executeQuery();

                jTableRelDespesa.setModel(DbUtils.resultSetToTableModel(rs));

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
     * @param pagador a ser filtrado.
     * @throws SQLException Se ocorrer um erro ao acessar o banco de dados.
     * @throws ParseException Se ocorrer um erro ao fazer parsing das datas.
     */
    public void criarDocumento(String inicio, String fim, String ordem, String pagador) throws SQLException, ParseException {

        Date dataInicio = new Date();
        Date dataFim = new Date();
// Convertendo as strings para o formato de data padrão
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (!jRadioButtonRelDespesaHoje.isSelected()) {

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

                if (jRadioButtonRelDespesaDesc.isSelected()) {
                    if (pagador != "*") {
                        sqlPDF = "SELECT id as ID, DATE_FORMAT(data_despesa, '%d/%m/%Y') as Data, descricao as Descrição, CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Valor, "
                                + "categoria as Categoria, pagador as Pagador, operador as Operador "
                                + "FROM despesas d "
                                + "WHERE data_despesa BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "AND pagador = '" + pagador + "' "
                                + "ORDER BY d." + ordem + " DESC";

                    } else {
                        sqlPDF = "SELECT id as ID, DATE_FORMAT(data_despesa, '%d/%m/%Y') as Data, descricao as Descrição, CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Valor, "
                                + "categoria as Categoria, pagador as Pagador, operador as Operador "
                                + "FROM despesas d "
                                + "WHERE data_despesa BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "ORDER BY d." + ordem + " DESC";
                    }

                } else {
                    if (pagador != "*") {
                        sqlPDF = "SELECT id as ID, DATE_FORMAT(data_despesa, '%d/%m/%Y') as Data, descricao as Descrição, CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Valor, "
                                + "categoria as Categoria, pagador as Pagador, operador as Operador "
                                + "FROM despesas d "
                                + "WHERE data_despesa BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "AND pagador = '" + pagador + "' "
                                + "ORDER BY d." + ordem;
                    } else {
                        sqlPDF = "SELECT id as ID, DATE_FORMAT(data_despesa, '%d/%m/%Y') as Data, descricao as Descrição, CONCAT('R$ ', REPLACE(REPLACE(REPLACE(FORMAT(valor, 2), '.', 'temp'), ',', '.'), 'temp', ',')) as Valor, "
                                + "categoria as Categoria, pagador as Pagador, operador as Operador "
                                + "FROM despesas d "
                                + "WHERE data_despesa BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                                + "ORDER BY d." + ordem;
                    }
                }
                try {
                    String username = System.getProperty("user.name");
                    String data = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
                    String path = "C:\\Users\\" + username + "\\Documents\\Relatorio_Despesas_" + data + ".pdf";

                    PdfWriter pdfWriter = new PdfWriter(path);
                    PdfDocument documentoPDF = new PdfDocument(pdfWriter);
                    Document document = new Document(documentoPDF, PageSize.A4);

                    float[] columnWidths = {1, 2, 4, 4, 3, 3, 3};
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

                    document.add(new Paragraph("Relatório de Despesas")
                            .setFont(fontBold)
                            .setFontSize(18)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph("Período: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " a " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim))
                            .setFont(fontNormal)
                            .setFontSize(12)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph(""));

                    PdfFont headerFont = PdfFontFactory.createFont();
                    String[] headers = {"ID", "Data", "Descrição", "Valor", "Categoria", "Pagador", "Operador"};
                    for (String header : headers) {
                        Cell cell = new Cell().add(header).setFont(headerFont).setFontSize(10).setBackgroundColor(DeviceGray.BLACK).setTextAlignment(TextAlignment.CENTER).setFontColor(DeviceGray.WHITE);
                        table.addCell(cell);
                    }

                    // Linhas da tabela com os dados do ResultSet
                    PdfFont dataFont = PdfFontFactory.createFont();
                    PreparedStatement pstPDF = conexao.obterConexao().prepareStatement(sqlPDF);
                    ResultSet resultPDF = pstPDF.executeQuery();

                    double totalValor = 0;

                    while (resultPDF.next()) {
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("ID")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(resultPDF.getString("Data")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("Descrição")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("Valor")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("Categoria")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("Pagador")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("Operador")));
                       
                        String total = resultPDF.getString("Valor");
                        double totalFormatado = reverterValorFormatado(total);
                        totalValor += totalFormatado;

                    }

                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add("Totais"));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(formatarValor(totalValor)));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(25).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                   
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

        jFormattedRelDespesaInicio = new javax.swing.JFormattedTextField(mfData);
        jLabelRelDespesaPeriodoA = new javax.swing.JLabel();
        jFormattedRelDespesaFim = new javax.swing.JFormattedTextField(mfData);
        jButtonRelDespesa = new javax.swing.JButton();
        jLabelRelDespesaPagador = new javax.swing.JLabel();
        jLabelRelDespesaPeriodo = new javax.swing.JLabel();
        jComboRelDespesaOrdem = new javax.swing.JComboBox<>();
        jLabelRelDespesaOrdem = new javax.swing.JLabel();
        jComboRelDespesaPagador = new javax.swing.JComboBox<>();
        jLabelRelDespesaPagadorParenteses = new javax.swing.JLabel();
        jRadioButtonRelDespesaHoje = new javax.swing.JRadioButton();
        jRadioButtonRelDespesaDesc = new javax.swing.JRadioButton();
        jButtonRelDespesaLista = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRelDespesa = new javax.swing.JTable();
        jButtonRelDespesaExcluir = new javax.swing.JButton();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Relatório de Despesas");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        //jFormattedRelVendInicio.setText(formato.format(dataSistema));
        jFormattedRelDespesaInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelDespesaInicioActionPerformed(evt);
            }
        });

        jLabelRelDespesaPeriodoA.setText("a");

        jFormattedRelDespesaFim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelDespesaFimActionPerformed(evt);
            }
        });

        jButtonRelDespesa.setText("Gerar PDF");
        jButtonRelDespesa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelDespesaActionPerformed(evt);
            }
        });

        jLabelRelDespesaPagador.setText("Pagador:");

        jLabelRelDespesaPeriodo.setText("Período:");

        jComboRelDespesaOrdem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "data_despesa", "descricao", "valor", "categoria", "pagador" }));
        jComboRelDespesaOrdem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRelDespesaOrdemActionPerformed(evt);
            }
        });

        jLabelRelDespesaOrdem.setText("Ordem:");

        jComboRelDespesaPagador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "*", "Marcos", "Negocio", "Adiantamento" }));
        jComboRelDespesaPagador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRelDespesaPagadorActionPerformed(evt);
            }
        });

        jLabelRelDespesaPagadorParenteses.setText("(\" * \" Todos)");

        jRadioButtonRelDespesaHoje.setText("Hoje");

        jRadioButtonRelDespesaDesc.setText("Decrescente");

        jButtonRelDespesaLista.setText("Gerar Lista");
        jButtonRelDespesaLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelDespesaListaActionPerformed(evt);
            }
        });

        jTableRelDespesa = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jTableRelDespesa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Data", "Descrição", "Valor", "Categoria", "Pagador", "Operador"
            }
        ));
        jTableRelDespesa.setFocusable(false);
        jTableRelDespesa.getTableHeader().setReorderingAllowed(false);
        jTableRelDespesa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRelDespesaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableRelDespesa);

        jButtonRelDespesaExcluir.setText("Excluir Registro");
        jButtonRelDespesaExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelDespesaExcluirActionPerformed(evt);
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
                        .addComponent(jButtonRelDespesaExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonRelDespesaLista)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRelDespesa))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelDespesaOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboRelDespesaOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelDespesaDesc))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelDespesaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedRelDespesaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelRelDespesaPeriodoA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jFormattedRelDespesaFim, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelDespesaHoje)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelRelDespesaPagador)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboRelDespesaPagador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelRelDespesaPagadorParenteses)))
                        .addGap(0, 152, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedRelDespesaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelDespesaPeriodoA)
                    .addComponent(jFormattedRelDespesaFim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelDespesaPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonRelDespesaHoje)
                    .addComponent(jLabelRelDespesaPagador, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboRelDespesaPagador, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelDespesaPagadorParenteses, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboRelDespesaOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelDespesaOrdem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButtonRelDespesaDesc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRelDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRelDespesaLista, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRelDespesaExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedRelDespesaInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelDespesaInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelDespesaInicioActionPerformed

    private void jFormattedRelDespesaFimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelDespesaFimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelDespesaFimActionPerformed

    private void jButtonRelDespesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelDespesaActionPerformed

        try {
            criarDocumento(jFormattedRelDespesaInicio.getText(), jFormattedRelDespesaFim.getText(), jComboRelDespesaOrdem.getSelectedItem().toString(), jComboRelDespesaPagador.getSelectedItem().toString());

        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioDespesa.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioDespesa.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonRelDespesaActionPerformed

    private void jComboRelDespesaPagadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRelDespesaPagadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboRelDespesaPagadorActionPerformed

    private void jButtonRelDespesaListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelDespesaListaActionPerformed
        try {
            criarLista(jFormattedRelDespesaInicio.getText(), jFormattedRelDespesaFim.getText(), jComboRelDespesaOrdem.getSelectedItem().toString(), jComboRelDespesaPagador.getSelectedItem().toString());

        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioDespesa.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioDespesa.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRelDespesaListaActionPerformed

    private void jTableRelDespesaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRelDespesaMouseClicked

    }//GEN-LAST:event_jTableRelDespesaMouseClicked

    private void jButtonRelDespesaExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelDespesaExcluirActionPerformed
        try {
            deletar();
        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRelDespesaExcluirActionPerformed

    private void jComboRelDespesaOrdemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRelDespesaOrdemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboRelDespesaOrdemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRelDespesa;
    private javax.swing.JButton jButtonRelDespesaExcluir;
    private javax.swing.JButton jButtonRelDespesaLista;
    private javax.swing.JComboBox<String> jComboRelDespesaOrdem;
    private javax.swing.JComboBox<String> jComboRelDespesaPagador;
    private javax.swing.JFormattedTextField jFormattedRelDespesaFim;
    public javax.swing.JFormattedTextField jFormattedRelDespesaInicio;
    private javax.swing.JLabel jLabelRelDespesaOrdem;
    private javax.swing.JLabel jLabelRelDespesaPagador;
    private javax.swing.JLabel jLabelRelDespesaPagadorParenteses;
    private javax.swing.JLabel jLabelRelDespesaPeriodo;
    private javax.swing.JLabel jLabelRelDespesaPeriodoA;
    private javax.swing.JRadioButton jRadioButtonRelDespesaDesc;
    private javax.swing.JRadioButton jRadioButtonRelDespesaHoje;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableRelDespesa;
    // End of variables declaration//GEN-END:variables
}
