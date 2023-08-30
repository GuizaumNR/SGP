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
public class TelaRelatorioCompra extends javax.swing.JInternalFrame {

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

    public TelaRelatorioCompra() {

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

    public void criarDocumento(String inicio, String fim, String ordem, String pagamento) throws SQLException, ParseException {

        Date dataInicio = new Date();
        Date dataFim = new Date();
// Convertendo as strings para o formato de data padrão
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            if (!jRadioButtonRelCompraHoje.isSelected()) {
                if (validaData(inicio) && validaData(fim)) {
                    dataInicio = dateFormat.parse(inicio);
                    dataFim = dateFormat.parse(fim);
                } else {
                    JOptionPane.showMessageDialog(null, "Data inválida.");
                }
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

// Convertendo as datas de volta para o formato do banco de dados
            SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String inicioFormatado = dbDateFormat.format(dataInicio);
            String fimFormatado = dbDateFormat.format(dataFim);
            String sqlPDF = "";

           if (jRadioButtonRelCompraDesc.isSelected()) {
    if (!pagamento.equals("*")) {
        sqlPDF = "SELECT id_compra, DATE_FORMAT(data_compra, '%d/%m/%Y %H:%i:%s') as data_formatada, a.descricao as animal_descricao, c.quantidade, media_kg, preco_kg, valor_total, criador, pagador, pagamento, local_compra, operador "
                + "FROM compras_animais c "
                + "JOIN animais a ON c.id_animal = a.id "
                + "WHERE data_compra BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                + "AND pagamento = '" + pagamento + "' "
                + "ORDER BY " + ordem + " DESC";
    } else {
        sqlPDF = "SELECT id_compra, DATE_FORMAT(data_compra, '%d/%m/%Y %H:%i:%s') as data_formatada, a.descricao as animal_descricao, c.quantidade, media_kg, preco_kg, valor_total, criador, pagador, pagamento, local_compra, operador "
                + "FROM compras_animais c "
                + "JOIN animais a ON c.id_animal = a.id "
                + "WHERE data_compra BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                + "ORDER BY " + ordem + " DESC";
    }

} else {
    if (!pagamento.equals("*")) {
        sqlPDF = "SELECT id_compra, DATE_FORMAT(data_compra, '%d/%m/%Y %H:%i:%s') as data_formatada, a.descricao as animal_descricao, c.quantidade, media_kg, preco_kg, valor_total, criador, pagador, pagamento, local_compra, operador "
                + "FROM compras_animais c "
                + "JOIN animais a ON c.id_animal = a.id "
                + "WHERE data_compra BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                + "AND pagamento = '" + pagamento + "' "
                + "ORDER BY " + ordem;
    } else {
        sqlPDF = "SELECT id_compra, DATE_FORMAT(data_compra, '%d/%m/%Y %H:%i:%s') as data_formatada, a.descricao as animal_descricao, c.quantidade, media_kg, preco_kg, valor_total, criador, pagador, pagamento, local_compra, operador "
                + "FROM compras_animais c "
                + "JOIN animais a ON c.id_animal = a.id "
                + "WHERE data_compra BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                + "ORDER BY " + ordem;
    }
}

            try {
                String username = System.getProperty("user.name");
                String data = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
                String path = "C:\\Users\\" + username + "\\Documents\\Relatorio_Compras_" + data + ".pdf";

                PdfWriter pdfWriter = new PdfWriter(path);
                PdfDocument documentoPDF = new PdfDocument(pdfWriter);
                Document document = new Document(documentoPDF, PageSize.A4);

                float[] columnWidths = {1, 3, 4, 2, 2, 1, 2, 3, 3, 3, 3, 2};
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

                document.add(new Paragraph("Relatório de Compras")
                        .setFont(fontBold)
                        .setFontSize(18)
                        .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                document.add(new Paragraph("Período: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " a " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim))
                        .setFont(fontNormal)
                        .setFontSize(12)
                        .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                document.add(new Paragraph(""));

                PdfFont headerFont = PdfFontFactory.createFont();
                String[] headers = {"ID", "Data", "Animal", "Qtde", "Média Kg", "Preço Kg", "Total", "Criador", "Pagador", "Pagamento", "Local", "Operador"};
                for (String header : headers) {
                    Cell cell = new Cell().add(header).setFont(headerFont).setFontSize(10).setBackgroundColor(DeviceGray.BLACK).setTextAlignment(TextAlignment.CENTER).setFontColor(DeviceGray.WHITE);
                    table.addCell(cell);
                }

                // Linhas da tabela com os dados do ResultSet
                PdfFont dataFont = PdfFontFactory.createFont();
                PreparedStatement pstPDF = conexao.obterConexao().prepareStatement(sqlPDF);
                ResultSet resultPDF = pstPDF.executeQuery();
                while (resultPDF.next()) {
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("id_compra")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(resultPDF.getString("data_formatada")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("animal_descricao")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("quantidade")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("media_kg")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(14).add(resultPDF.getString("preco_kg")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(40).add(resultPDF.getString("valor_total")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("criador")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("pagador")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("pagamento")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(25).add(resultPDF.getString("local_compra")));
                    table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(20).add(resultPDF.getString("operador")));
                }

                table.setAutoLayout();
                document.add(table);
                document.close();

                JOptionPane.showMessageDialog(null, "PDF criado em " + path);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao criar o PDF. " + e);
            }

        } catch (Exception e) {
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

        jFormattedRelCompInicio = new javax.swing.JFormattedTextField(mfData);
        jLabelRelCompPeriodoA = new javax.swing.JLabel();
        jFormattedRelCompraFim = new javax.swing.JFormattedTextField(mfData);
        jButtonRelCompra = new javax.swing.JButton();
        jLabelRelCompraPagamento = new javax.swing.JLabel();
        jLabelRelCompraPeriodo = new javax.swing.JLabel();
        jComboRelCompraOrdem = new javax.swing.JComboBox<>();
        jLabelRelCompraOrdem = new javax.swing.JLabel();
        jComboRelCompraPagamento = new javax.swing.JComboBox<>();
        jLabelRelCompraPagamentoParenteses = new javax.swing.JLabel();
        jRadioButtonRelCompraHoje = new javax.swing.JRadioButton();
        jRadioButtonRelCompraDesc = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Relatório de Compras");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        //jFormattedRelVendInicio.setText(formato.format(dataSistema));
        jFormattedRelCompInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelCompInicioActionPerformed(evt);
            }
        });

        jLabelRelCompPeriodoA.setText("a");

        jFormattedRelCompraFim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelCompraFimActionPerformed(evt);
            }
        });

        jButtonRelCompra.setText("Gerar PDF");
        jButtonRelCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelCompraActionPerformed(evt);
            }
        });

        jLabelRelCompraPagamento.setText("Pagamento:");

        jLabelRelCompraPeriodo.setText("Período:");

        jComboRelCompraOrdem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "data_compra", "quantidade", "valor_total", "criador", "pagador" }));

        jLabelRelCompraOrdem.setText("Ordem:");

        jComboRelCompraPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "*", "Dinheiro", "Cartão Débito", "Cartão Crédito", "Pix", "Permuta" }));
        jComboRelCompraPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRelCompraPagamentoActionPerformed(evt);
            }
        });

        jLabelRelCompraPagamentoParenteses.setText("(\" * \" Todas formas )");

        jRadioButtonRelCompraHoje.setText("Hoje");

        jRadioButtonRelCompraDesc.setText("Decrescente");

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
                                .addComponent(jLabelRelCompraOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboRelCompraOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelCompraDesc))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelCompraPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedRelCompInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelRelCompPeriodoA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jFormattedRelCompraFim, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelCompraHoje))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelCompraPagamento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboRelCompraPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelRelCompraPagamentoParenteses))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(272, 272, 272)
                        .addComponent(jButtonRelCompra)))
                .addContainerGap(372, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedRelCompInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelCompPeriodoA)
                    .addComponent(jFormattedRelCompraFim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelCompraPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonRelCompraHoje))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboRelCompraOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelCompraOrdem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButtonRelCompraDesc))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRelCompraPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboRelCompraPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelCompraPagamentoParenteses, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addComponent(jButtonRelCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(294, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedRelCompInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelCompInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelCompInicioActionPerformed

    private void jFormattedRelCompraFimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelCompraFimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelCompraFimActionPerformed

    private void jButtonRelCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelCompraActionPerformed

        try {
            criarDocumento(jFormattedRelCompInicio.getText(), jFormattedRelCompraFim.getText(), jComboRelCompraOrdem.getSelectedItem().toString(), jComboRelCompraPagamento.getSelectedItem().toString());
        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioCompra.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioCompra.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonRelCompraActionPerformed

    private void jComboRelCompraPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRelCompraPagamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboRelCompraPagamentoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRelCompra;
    private javax.swing.JComboBox<String> jComboRelCompraOrdem;
    private javax.swing.JComboBox<String> jComboRelCompraPagamento;
    public javax.swing.JFormattedTextField jFormattedRelCompInicio;
    private javax.swing.JFormattedTextField jFormattedRelCompraFim;
    private javax.swing.JLabel jLabelRelCompPeriodoA;
    private javax.swing.JLabel jLabelRelCompraOrdem;
    private javax.swing.JLabel jLabelRelCompraPagamento;
    private javax.swing.JLabel jLabelRelCompraPagamentoParenteses;
    private javax.swing.JLabel jLabelRelCompraPeriodo;
    private javax.swing.JRadioButton jRadioButtonRelCompraDesc;
    private javax.swing.JRadioButton jRadioButtonRelCompraHoje;
    // End of variables declaration//GEN-END:variables
}
