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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 * Classe responsavel pelos relatorios de estoque.
 *
 * @author Guilherme
 */
public class TelaRelatorioEstoque extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaVenda
     */
    private final Conexao conexao;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaRelatorioEstoque() {

        this.conexao = new ConexaoMysql();

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
     * Método para criar uma lista de nascimentos e exibir no jTable.
     * 
     * @param ordem Ordem de classificação.
     * @throws SQLException Exceção em caso de erro no banco de dados.
     * @throws ParseException Exceção em caso de erro ao converter a data.
     */
    public void criarLista(String ordem, String sexo) throws SQLException, ParseException {

        String sql = "";
        try {

            if (jRadioButtonRelEstDesc.isSelected()) {
                    if (sexo != "*") {
                    sql = "SELECT id as ID, "
                        + "sexo as Sexo, "
                        + "quantidade as Qtde, "
                        + "idade as Idade "
                        + "FROM animais "
                        + "WHERE sexo = '" + sexo + "' " 
                        + "ORDER BY " + ordem + " DESC";
                    }else{
                       sql = "SELECT id as ID, "
                        + "sexo as Sexo, "
                        + "quantidade as Qtde, "
                        + "idade as Idade "
                        + "FROM animais "
                        + "ORDER BY " + ordem + " DESC"; 
                    }
                } else {
                    if (sexo != "*") {
                    sql = "SELECT id as ID, "
                        + "sexo as Sexo, "
                        + "quantidade as Qtde, "
                        + "idade as Idade "
                        + "FROM animais "
                        + "WHERE sexo = '" + sexo + "' " 
                        + "ORDER BY " + ordem;
                    }else{
                       sql = "SELECT id as ID, "
                        + "sexo as Sexo, "
                        + "quantidade as Qtde, "
                        + "idade as Idade "
                        + "FROM animais "
                        + "ORDER BY " + ordem; 
                    }
                }

            pst = conexao.obterConexao().prepareStatement(sql);
            rs = pst.executeQuery();

            jTableRelEstoque.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Data inválida.");
        }

    }

    /**
     * Método para criar um documento PDF com o relatório de nascimentos.
     *
     * @param ordem Ordem de classificação.
     * @throws SQLException Exceção em caso de erro no banco de dados.
     * @throws ParseException Exceção em caso de erro ao converter a data.
     */
    public void criarDocumento(String ordem, String sexo) throws SQLException, ParseException {


                SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String sqlPDF = "";

                if (jRadioButtonRelEstDesc.isSelected()) {
                    if (sexo != "*") {
                    sqlPDF = "SELECT id as ID, "
                        + "sexo as Sexo, "
                        + "quantidade as Qtde, "
                        + "idade as Idade "
                        + "FROM animais "
                        + "WHERE sexo = '" + sexo + "' " 
                        + "ORDER BY " + ordem + " DESC";
                    }else{
                       sqlPDF = "SELECT id as ID, "
                        + "sexo as Sexo, "
                        + "quantidade as Qtde, "
                        + "idade as Idade "
                        + "FROM animais "
                        + "ORDER BY " + ordem + " DESC"; 
                    }
                } else {
                    if (sexo != "*") {
                    sqlPDF = "SELECT id as ID, "
                        + "sexo as Sexo, "
                        + "quantidade as Qtde, "
                        + "idade as Idade "
                        + "FROM animais "
                        + "WHERE sexo = '" + sexo + "' " 
                        + "ORDER BY " + ordem;
                    }else{
                       sqlPDF = "SELECT id as ID, "
                        + "sexo as Sexo, "
                        + "quantidade as Qtde, "
                        + "idade as Idade "
                        + "FROM animais "
                        + "ORDER BY " + ordem; 
                    }
                }

                try {
                    String username = System.getProperty("user.name");
                    String data = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
                    String path = "C:\\Users\\" + username + "\\Documents\\Relatorio_Estoque_" + data + ".pdf";

                    PdfWriter pdfWriter = new PdfWriter(path);
                    PdfDocument documentoPDF = new PdfDocument(pdfWriter);
                    Document document = new Document(documentoPDF, PageSize.A4);

                    float[] columnWidths = {1,3,3,3};
                    Table table = new Table(columnWidths);
                    table.setWidthPercent(100);
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER);
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

                    document.add(new Paragraph("Relatório de Estoque")
                            .setFont(fontBold)
                            .setFontSize(18)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph(""));

                    PdfFont headerFont = PdfFontFactory.createFont();
                    String[] headers = {"ID", "Sexo", "Qtde", "Idade"};
                    for (String header : headers) {
                        Cell cell = new Cell().add(header).setFont(headerFont).setFontSize(10).setBackgroundColor(DeviceGray.BLACK).setTextAlignment(TextAlignment.CENTER).setFontColor(DeviceGray.WHITE);
                        table.addCell(cell);
                    }

                    PdfFont dataFont = PdfFontFactory.createFont();
                    PreparedStatement pstPDF = conexao.obterConexao().prepareStatement(sqlPDF);
                    ResultSet resultPDF = pstPDF.executeQuery();

                    int totalQuantidade = 0;

                    while (resultPDF.next()) {
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("ID")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(resultPDF.getString("Sexo")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("Qtde")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("Idade")));

                        int quantidade = resultPDF.getInt("Qtde"); // Substitua o nome da coluna conforme necessário
                        totalQuantidade += quantidade;

                    }

                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add("Totais"));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(""));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(String.valueOf(totalQuantidade)));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(""));

                    table.setAutoLayout();
                    document.add(table);
                    document.close();

                    JOptionPane.showMessageDialog(null, "PDF criado em " + path);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao criar o PDF. " + e);
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

        jButtonRelEst = new javax.swing.JButton();
        jComboRelEstOrdem = new javax.swing.JComboBox<>();
        jLabelRelEstOrdem = new javax.swing.JLabel();
        jRadioButtonRelEstDesc = new javax.swing.JRadioButton();
        jButtonRelEstLista = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRelEstoque = new javax.swing.JTable();
        jLabelRelVendaPagamento = new javax.swing.JLabel();
        jComboRelEstSexo = new javax.swing.JComboBox<>();
        jLabelRelVendaPagamentoParenteses = new javax.swing.JLabel();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Relatório de Estoque");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        jButtonRelEst.setText("Gerar PDF");
        jButtonRelEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelEstActionPerformed(evt);
            }
        });

        jComboRelEstOrdem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Sexo", "Qtde", "Idade" }));
        jComboRelEstOrdem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRelEstOrdemActionPerformed(evt);
            }
        });

        jLabelRelEstOrdem.setText("Ordem:");

        jRadioButtonRelEstDesc.setText("Decrescente");

        jButtonRelEstLista.setText("Gerar Lista");
        jButtonRelEstLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelEstListaActionPerformed(evt);
            }
        });

        jTableRelEstoque = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jTableRelEstoque.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTableRelEstoque.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Sexo", "Qtde", "Idade"
            }
        ));
        jTableRelEstoque.setFocusable(false);
        jTableRelEstoque.getTableHeader().setReorderingAllowed(false);
        jTableRelEstoque.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRelEstoqueMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableRelEstoque);

        jLabelRelVendaPagamento.setText("Sexo:");

        jComboRelEstSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "*", "Boi", "Touro", "Fêmea" }));
        jComboRelEstSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRelEstSexoActionPerformed(evt);
            }
        });

        jLabelRelVendaPagamentoParenteses.setText("(\" * \" Todos)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelRelEstOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboRelEstOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButtonRelEstDesc)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelRelVendaPagamento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboRelEstSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelRelVendaPagamentoParenteses)
                        .addGap(0, 278, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonRelEstLista)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRelEst))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelRelVendaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboRelEstSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelRelVendaPagamentoParenteses, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboRelEstOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelRelEstOrdem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jRadioButtonRelEstDesc)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRelEst, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRelEstLista, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRelEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelEstActionPerformed

        try {
            
            criarDocumento(jComboRelEstOrdem.getSelectedItem().toString(), jComboRelEstSexo.getSelectedItem().toString());

        } catch (SQLException ex) {
           
        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioEstoque.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonRelEstActionPerformed

    private void jComboRelEstOrdemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRelEstOrdemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboRelEstOrdemActionPerformed

    private void jButtonRelEstListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelEstListaActionPerformed
        try {
            criarLista(jComboRelEstOrdem.getSelectedItem().toString(), jComboRelEstSexo.getSelectedItem().toString());

        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioVenda.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioVenda.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonRelEstListaActionPerformed

    private void jTableRelEstoqueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRelEstoqueMouseClicked

    }//GEN-LAST:event_jTableRelEstoqueMouseClicked

    private void jComboRelEstSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRelEstSexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboRelEstSexoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRelEst;
    private javax.swing.JButton jButtonRelEstLista;
    private javax.swing.JComboBox<String> jComboRelEstOrdem;
    private javax.swing.JComboBox<String> jComboRelEstSexo;
    private javax.swing.JLabel jLabelRelEstOrdem;
    private javax.swing.JLabel jLabelRelVendaPagamento;
    private javax.swing.JLabel jLabelRelVendaPagamentoParenteses;
    private javax.swing.JRadioButton jRadioButtonRelEstDesc;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableRelEstoque;
    // End of variables declaration//GEN-END:variables
}
