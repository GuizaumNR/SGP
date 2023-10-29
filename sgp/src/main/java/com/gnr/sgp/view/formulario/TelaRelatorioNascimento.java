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
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Guilherme
 */
public class TelaRelatorioNascimento extends javax.swing.JInternalFrame {

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

    public TelaRelatorioNascimento() {

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
    
    public void deletar() throws SQLException {
        int setar = jTableRelComp.getSelectedRow();
        String valorId = jTableRelComp.getModel().getValueAt(setar, 0).toString();

        String sqlAnimal = "UPDATE animais a "
                + "JOIN nascimentos n ON a.id = n.id_animal "
                + "SET a.quantidade = a.quantidade - n.quantidade "
                + "WHERE n.id = '" + valorId + "' ";

        String sqlCompra = "DELETE FROM nascimentos WHERE id = '" + valorId + "' ";

        pst = conexao.obterConexao().prepareStatement(sqlAnimal);
        int resultado = pst.executeUpdate();

        PreparedStatement pstmt = conexao.obterConexao().prepareStatement(sqlCompra);
        int resultado2 = pstmt.executeUpdate();

        System.out.println(valorId);

        if (resultado > 0 && resultado2 > 0) {
            JOptionPane.showMessageDialog(null, "Registro deletado, e quantidade de animais ajustada.");
            try {
                criarLista(jFormattedRelNascInicio.getText(), jFormattedRelNascFim.getText(), jComboRelNascOrdem.getSelectedItem().toString());

            } catch (SQLException ex) {
                Logger.getLogger(TelaRelatorioVenda.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (ParseException ex) {
                Logger.getLogger(TelaRelatorioVenda.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

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
    
    public void criarLista(String inicio, String fim, String ordem) throws SQLException, ParseException {

        Date dataInicio = new Date();
        Date dataFim = new Date();
// Convertendo as strings para o formato de data padrão
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (!jRadioButtonRelNascHoje.isSelected()) {

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

                if (jRadioButtonRelNascDesc.isSelected()) {
                    sql = "SELECT "
                            + "n.id, "
                            + "DATE_FORMAT(data_nascimento, '%d/%m/%Y') as data_formatada, "
                            + "a.sexo as animal_sexo, "
                            + "n.quantidade, "
                            + "observacao, "
                            + "local_nasc, "
                            + "operador "
                            + "FROM nascimentos n "
                            + "JOIN animais a ON n.id_animal = a.id "
                            + "WHERE n.data_nascimento BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                            + "ORDER BY " + ordem + " DESC";
                } else {
                    sql = "SELECT "
                            + "n.id, "
                            + "DATE_FORMAT(data_nascimento, '%d/%m/%Y') as data_formatada, "
                            + "a.sexo as animal_sexo, "
                            + "n.quantidade, "
                            + "observacao, "
                            + "local_nasc, "
                            + "operador "
                            + "FROM nascimentos n "
                            + "JOIN animais a ON n.id_animal = a.id "
                            + "WHERE n.data_nascimento BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                            + "ORDER BY " + ordem;
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
    
    public void criarDocumento(String inicio, String fim, String ordem) throws SQLException, ParseException {

        Date dataInicio = new Date();
        Date dataFim = new Date();
// Convertendo as strings para o formato de data padrão
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (!jRadioButtonRelNascHoje.isSelected()) {

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

                if (jRadioButtonRelNascDesc.isSelected()) {
                    sqlPDF = "SELECT "
                            + "n.id, "
                            + "DATE_FORMAT(data_nascimento, '%d/%m/%Y') as data_formatada, "
                            + "a.sexo as animal_sexo, "
                            + "n.quantidade, "
                            + "observacao, "
                            + "local_nasc, "
                            + "operador "
                            + "FROM nascimentos n "
                            + "JOIN animais a ON n.id_animal = a.id "
                            + "WHERE n.data_nascimento BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                            + "ORDER BY " + ordem + " DESC";
                } else {
                    sqlPDF = "SELECT "
                            + "n.id, "
                            + "DATE_FORMAT(data_nascimento, '%d/%m/%Y') as data_formatada, "
                            + "a.sexo as animal_sexo, "
                            + "n.quantidade, "
                            + "observacao, "
                            + "local_nasc, "
                            + "operador "
                            + "FROM nascimentos n "
                            + "JOIN animais a ON n.id_animal = a.id "
                            + "WHERE n.data_nascimento BETWEEN '" + inicioFormatado + "' AND '" + fimFormatado + "' "
                            + "ORDER BY " + ordem;
                }

                try {
                    String username = System.getProperty("user.name");
                    String data = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
                    String path = "C:\\Users\\" + username + "\\Documents\\Relatorio_Nascimentos_" + data + ".pdf";

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

                    document.add(new Paragraph("Relatório de Nascimentos")
                            .setFont(fontBold)
                            .setFontSize(18)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph("Período: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " a " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim))
                            .setFont(fontNormal)
                            .setFontSize(12)
                            .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));
                    document.add(new Paragraph(""));

                    PdfFont headerFont = PdfFontFactory.createFont();
                    String[] headers = {"ID", "Data", "Sexo", "Qtde", "Observação", "Local", "Operador"};
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
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("animal_sexo")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(18).add(resultPDF.getString("quantidade")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(30).add(resultPDF.getString("observacao")));
                        table.addCell(new Cell().setFont(dataFont).setFontSize(8).setWidth(25).add(resultPDF.getString("local_nasc")));
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

        jFormattedRelNascInicio = new javax.swing.JFormattedTextField(mfData);
        jLabelRelNascPeriodoA = new javax.swing.JLabel();
        jFormattedRelNascFim = new javax.swing.JFormattedTextField(mfData);
        jButtonRelNasc = new javax.swing.JButton();
        jLabelRelNascPeriodo = new javax.swing.JLabel();
        jComboRelNascOrdem = new javax.swing.JComboBox<>();
        jLabelRelNascOrdem = new javax.swing.JLabel();
        jRadioButtonRelNascHoje = new javax.swing.JRadioButton();
        jRadioButtonRelNascDesc = new javax.swing.JRadioButton();
        jButtonRelCompraLista = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRelComp = new javax.swing.JTable();
        jButtonRelVendaExcluir = new javax.swing.JButton();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Relatório de Nascimentos");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        //jFormattedRelVendInicio.setText(formato.format(dataSistema));
        jFormattedRelNascInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelNascInicioActionPerformed(evt);
            }
        });

        jLabelRelNascPeriodoA.setText("a");

        jFormattedRelNascFim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedRelNascFimActionPerformed(evt);
            }
        });

        jButtonRelNasc.setText("Gerar PDF");
        jButtonRelNasc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRelNascActionPerformed(evt);
            }
        });

        jLabelRelNascPeriodo.setText("Período:");

        jComboRelNascOrdem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "data_nascimento", "quantidade", "animal_descricao", "local_nasc" }));
        jComboRelNascOrdem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboRelNascOrdemActionPerformed(evt);
            }
        });

        jLabelRelNascOrdem.setText("Ordem:");

        jRadioButtonRelNascHoje.setText("Hoje");

        jRadioButtonRelNascDesc.setText("Decrescente");

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
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Data", "Sexo", "Qtde", "Observação ", "Local", "Operador"
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelNascOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboRelNascOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelNascDesc))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelRelNascPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedRelNascInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelRelNascPeriodoA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jFormattedRelNascFim, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButtonRelNascHoje)))
                        .addGap(0, 409, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonRelVendaExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonRelCompraLista)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRelNasc)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedRelNascInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelNascPeriodoA)
                    .addComponent(jFormattedRelNascFim, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelNascPeriodo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButtonRelNascHoje))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboRelNascOrdem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelRelNascOrdem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButtonRelNascDesc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonRelNasc, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRelCompraLista, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRelVendaExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFormattedRelNascInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelNascInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelNascInicioActionPerformed

    private void jFormattedRelNascFimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedRelNascFimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedRelNascFimActionPerformed

    private void jButtonRelNascActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelNascActionPerformed

        try {
            criarDocumento(jFormattedRelNascInicio.getText(), jFormattedRelNascFim.getText(), jComboRelNascOrdem.getSelectedItem().toString());

        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioNascimento.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioNascimento.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonRelNascActionPerformed

    private void jComboRelNascOrdemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboRelNascOrdemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboRelNascOrdemActionPerformed

    private void jButtonRelCompraListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRelCompraListaActionPerformed
        try {
            criarLista(jFormattedRelNascInicio.getText(), jFormattedRelNascFim.getText(), jComboRelNascOrdem.getSelectedItem().toString());

        } catch (SQLException ex) {
            Logger.getLogger(TelaRelatorioVenda.class
                .getName()).log(Level.SEVERE, null, ex);

        } catch (ParseException ex) {
            Logger.getLogger(TelaRelatorioVenda.class
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
    private javax.swing.JButton jButtonRelNasc;
    private javax.swing.JButton jButtonRelVendaExcluir;
    private javax.swing.JComboBox<String> jComboRelNascOrdem;
    private javax.swing.JFormattedTextField jFormattedRelNascFim;
    public javax.swing.JFormattedTextField jFormattedRelNascInicio;
    private javax.swing.JLabel jLabelRelNascOrdem;
    private javax.swing.JLabel jLabelRelNascPeriodo;
    private javax.swing.JLabel jLabelRelNascPeriodoA;
    private javax.swing.JRadioButton jRadioButtonRelNascDesc;
    private javax.swing.JRadioButton jRadioButtonRelNascHoje;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableRelComp;
    // End of variables declaration//GEN-END:variables
}
