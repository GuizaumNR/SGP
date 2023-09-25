/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Guilherme
 */
public class TelaRelatorioFalecimento extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaVenda
     */
    private final Conexao conexao;
    PreparedStatement pst = null;
    ResultSet rs = null;

    MaskFormatter mfData;

    Date dataSistema = new Date();
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
//    Calendar now = Calendar.getInstance();

    public TelaRelatorioFalecimento() {

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

    public void criarDocumento(String inicio, String fim, String ordem) throws SQLException, ParseException {

        Date dataInicio = new Date();
        Date dataFim = new Date();
// Convertendo as strings para o formato de data padrão
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (!jRadioButtonRelFaleHoje.isSelected()) {

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

                if (jRadioButtonRelFaleDesc.isSelected()) {
                    sqlPDF = "SELECT "
                            + "f.id, "
                            + "	DATE_FORMAT(data_morte, '%d/%m/%Y') as data_formatada, "
                            + "	a.descricao as animal_descricao, "
                            + "f.quantidade, "
                            + "observacao, "
                            + "local_falecimento, "
                            + "operador "
                            + "FROM falecimentos f "
                            + "JOIN animais a ON f.id_animal = a.id "
                            + "WHERE f.data_morte BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                            + "ORDER BY " + ordem + " DESC";
                } else {
                    sqlPDF = "SELECT "
                            + "f.id, "
                            + "	DATE_FORMAT(data_morte, '%d/%m/%Y') as data_formatada, "
                            + "	a.descricao as animal_descricao, "
                            + "f.quantidade, "
                            + "observacao, "
                            + "local_falecimento, "
                            + "operador "
                            + "FROM falecimentos f "
                            + "JOIN animais a ON f.id_animal = a.id "
                            + "WHERE f.data_morte BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                            + "ORDER BY " + ordem;
                }

                try {
                    String username = System.getProperty("user.name");
                    String data = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
                    String path = "C:\\Users\\" + username + "\\Documents\\Relatorio_Falecimentos_" + data + ".pdf";

                    PdfWriter pdfWriter = new PdfWriter(path);
                    PdfDocument documentoPDF = new PdfDocument(pdfWriter);
                    Document document = new Document(documentoPDF, PageSize.A4);

                    float[] columnWidths = {1, 3, 4, 2, 4, 2, 2};
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

                    document.add(new Paragraph("Relatório de Falecimentos")
                            .setFont(fontBold)
                            .setFontSize(18)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph("Período: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " a " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim))
                            .setFont(fontNormal)
                            .setFontSize(12)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph(""));

                    PdfFont headerFont = PdfFontFactory.createFont();
                    String[] headers = {"ID", "Data", "Animal", "Qtde", "Observação", "Local", "Operador"};
                    for (String header : headers) {
                        Cell cell = new Cell().add(header).setFont(headerFont).setFontSize(10).setBackgroundColor(DeviceGray.BLACK).setTextAlignment(TextAlignment.CENTER).setFontColor(DeviceGray.WHITE);
                        table.addCell(cell);
                    }

                    // Linhas da tabela com os dados do ResultSet
                    PdfFont dataFont = PdfFontFactory.createFont();
                    PreparedStatement pstPDF = conexao.obterConexao().prepareStatement(sqlPDF);
                    ResultSet resultPDF = pstPDF.executeQuery();
                    
                    int totalQuantidade = 0;
                    
                    while (resultPDF.next()) {
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("id")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(resultPDF.getString("data_formatada")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("animal_descricao")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("quantidade")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("observacao")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(25).add(resultPDF.getString("local_falecimento")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(resultPDF.getString("operador")));

                        int quantidade = resultPDF.getInt("quantidade"); // Substitua o nome da coluna conforme necessário
                        totalQuantidade += quantidade;

                    }

                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add("Totais"));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(String.valueOf(totalQuantidade)));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(25).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.setAutoLayout();
                    document.add(table);
                    document.close();

                    JOptionPane.showMessageDialog(null, "PDF criado em " + path);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao criar o PDF. " + e);
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

        jFormattedRelFaleInicio = new javax.swing.JFormattedTextField(mfData);
        jLabelRelFalePeriodoA = new javax.swing.JLabel();
        jFormattedRelFaleFim = new javax.swing.JFormattedTextField(mfData);
        jButtonRelFale = new javax.swing.JButton();
        jLabelRelFalePeriodo = new javax.swing.JLabel();
        jComboRelFaleOrdem = new javax.swing.JComboBox<>();
        jLabelRelFaleOrdem = new javax.swing.JLabel();
        jRadioButtonRelFaleHoje = new javax.swing.JRadioButton();
        jRadioButtonRelFaleDesc = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Relatório de Falecimentos");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        //jFormattedRelVendInicio.setText(formato.format(dataSistema));
        jFormattedRelFaleInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelFaleInicioActionPerformed(evt);
            }
        });

        jLabelRelFalePeriodoA.setText("a");

        jFormattedRelFaleFim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelFaleFimActionPerformed(evt);
            }
        });

        jButtonRelFale.setText("Gerar PDF");
        jButtonRelFale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelFaleActionPerformed(evt);
            }
        });

        jLabelRelFalePeriodo.setText("Período:");

        jComboRelFaleOrdem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "data_morte", "quantidade", "animal_descricao", "local_falecimento" }));
        jComboRelFaleOrdem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRelFaleOrdemActionPerformed(evt);
            }
        });

        jLabelRelFaleOrdem.setText("Ordem:");

        jRadioButtonRelFaleHoje.setText("Hoje");

        jRadioButtonRelFaleDesc.setText("Decrescente");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelFaleOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboRelFaleOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelFaleDesc))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelFalePeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedRelFaleInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelRelFalePeriodoA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jFormattedRelFaleFim, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelFaleHoje))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(272, 272, 272)
                        .addComponent(jButtonRelFale)))
                .addContainerGap(372, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedRelFaleInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelFalePeriodoA)
                    .addComponent(jFormattedRelFaleFim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelFalePeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonRelFaleHoje))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboRelFaleOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelFaleOrdem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButtonRelFaleDesc))
                .addGap(102, 102, 102)
                .addComponent(jButtonRelFale, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(294, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedRelFaleInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelFaleInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelFaleInicioActionPerformed

    private void jFormattedRelFaleFimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelFaleFimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelFaleFimActionPerformed

    private void jButtonRelFaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelFaleActionPerformed

        try {
            criarDocumento(jFormattedRelFaleInicio.getText(), jFormattedRelFaleFim.getText(), jComboRelFaleOrdem.getSelectedItem().toString());

        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioFalecimento.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioFalecimento.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonRelFaleActionPerformed

    private void jComboRelFaleOrdemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRelFaleOrdemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboRelFaleOrdemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRelFale;
    private javax.swing.JComboBox<String> jComboRelFaleOrdem;
    private javax.swing.JFormattedTextField jFormattedRelFaleFim;
    public javax.swing.JFormattedTextField jFormattedRelFaleInicio;
    private javax.swing.JLabel jLabelRelFaleOrdem;
    private javax.swing.JLabel jLabelRelFalePeriodo;
    private javax.swing.JLabel jLabelRelFalePeriodoA;
    private javax.swing.JRadioButton jRadioButtonRelFaleDesc;
    private javax.swing.JRadioButton jRadioButtonRelFaleHoje;
    // End of variables declaration//GEN-END:variables
}
