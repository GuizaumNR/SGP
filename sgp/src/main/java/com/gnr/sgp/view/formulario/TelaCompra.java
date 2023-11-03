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
import com.gnr.sgp.modelo.dao.ComprasAnimaisDao;
import com.gnr.sgp.modelo.dominio.ComprasAnimais;
import com.gnr.sgp.view.modelo.ValidadorNumerico;
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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * A classe TelaCompra representa uma tela de interface gráfica para
 * gerenciamento de compras de animais.
 *
 * @author Guilherme
 */
public class TelaCompra extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaCompra
     */
    private final Conexao conexao;
    PreparedStatement pst = null;
    ResultSet rs = null;

    int quantidade = 0;
    double mediaKg = 0;
    double precoKg = 0;
    double valorTotal = 0;
    double kgTotais = 0;
    double percentual = 0;
    double comissao = 0;

    String operador;

    ValidadorNumerico validaNumeros = new ValidadorNumerico();

    public TelaCompra() {

        this.conexao = new ConexaoMysql();
        initComponents();
//        componentMoved(e);
        // Adiciona o ouvinte de evento ao jTextFieldVendaQuantidade
        jTextFieldCompQuantidade.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarValorTotal();
                atualizarMedia();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarValorTotal();
                atualizarMedia();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarValorTotal();
                atualizarMedia();
            }
        });

        // Adiciona o ouvinte de evento ao jTextFieldVendaPrecoKg
        jTextFieldCompPrecoKg.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }

        });

        jTextFieldCompTotal.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarValorComissao();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarValorComissao();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarValorComissao();
            }
        });

        // Adiciona o ouvinte de evento ao jTextFieldVendaPorcentagem
        jTextFieldCompPorcentagem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarValorComissao();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarValorComissao();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarValorComissao();
            }
        });

        jTextFieldCompKgTotais.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarValorTotal();
                atualizarMedia();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarValorTotal();
                atualizarMedia();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarValorTotal();
                atualizarMedia();
            }
        });

        setLocation(-5, -5);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                // Define a posição do componente para (0, 0)
                setLocation(-5, -5);
            }
        });

    }

    private void atualizarValorTotal() {
        try {
            int quantidade = Integer.parseInt(jTextFieldCompQuantidade.getText());
            kgTotais = Double.parseDouble(jTextFieldCompKgTotais.getText());
            double precoKg = Double.parseDouble(jTextFieldCompPrecoKg.getText());
            double valorTotal = kgTotais * precoKg;

            jTextFieldCompTotal.setText(String.format("%.2f", valorTotal));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            jTextFieldCompTotal.setText("Valor Inválido.");
        }
    }

    private void atualizarMedia() {
        try {
            int quantidade = Integer.parseInt(jTextFieldCompQuantidade.getText());
            double mediaKg = kgTotais / quantidade;
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat formato = new DecimalFormat("#.##", symbols);
            String mediaFormatada = formato.format(mediaKg);

            jTextFieldCompMediaKg.setText("" + mediaFormatada);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            jTextFieldCompMediaKg.setText("Valor Inválido.");
        }
    }

    private void atualizarValorComissao() {
        try {
            String totalTexto = jTextFieldCompTotal.getText().trim().replace(",", ".");
            if (!jTextFieldCompTotal.getText().equals("Valor Inválido.") && !jTextFieldCompTotal.getText().isEmpty() && !jTextFieldCompPorcentagem.getText().isEmpty()) {
                valorTotal = Double.parseDouble(totalTexto);
                percentual = (Double.parseDouble(jTextFieldCompPorcentagem.getText()) / 100);
                comissao = valorTotal * percentual;
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat formato = new DecimalFormat("#.##", symbols);
                String comissaoFormatada = formato.format(comissao);

                jTextFieldCompComissao.setText("" + comissaoFormatada);
            } else {
                jTextFieldCompComissao.setText("Valor Inválido");
            }

        } catch (NumberFormatException ex) {
            jTextFieldCompComissao.setText("Valor Inválido");
            ex.printStackTrace();
        }
    }

    /**
     * Adiciona uma compra ao banco de dados. Cria um objeto ComprasAnimais,
     * chama o método Adicionar do ComprasAnimaisDao e gera um cupom em formato
     * PDF.
     *
     * @throws SQLException Exceção relacionada ao acesso ao banco de dados.
     * @throws ParseException Exceção relacionada à análise de strings.
     */
    public void adicionar() throws SQLException, ParseException {
        if ((jTextFieldCompAnimal.getText().isEmpty() || jTextFieldCompQuantidade.getText().isEmpty() || jTextFieldCompMediaKg.getText().isEmpty() || jTextFieldCompPrecoKg.getText().isEmpty() || jTextFieldCompCriador.getText().isEmpty() || jTextFieldCompTotal.getText().isEmpty() || jTextFieldCompKgTotais.getText().isEmpty() || jTextFieldCompPorcentagem.getText().isEmpty() || jTextFieldCompComissao.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
        } else {
            quantidade = Integer.parseInt(jTextFieldCompQuantidade.getText());
            mediaKg = Double.parseDouble(jTextFieldCompMediaKg.getText());
            precoKg = Double.parseDouble(jTextFieldCompPrecoKg.getText());
            valorTotal = quantidade * mediaKg * precoKg;

            ComprasAnimais compra = new ComprasAnimais(null, Integer.parseInt(jTextFieldCompAnimal.getText()), quantidade, kgTotais, mediaKg, precoKg, valorTotal, (percentual * 100), comissao, jTextFieldCompCriador.getText(), jComboCompPagador.getSelectedItem().toString(), jComboCompPagamento.getSelectedItem().toString(), operador);

            ComprasAnimaisDao comprasDao = new ComprasAnimaisDao();
            comprasDao.Adicionar(compra);

            criarCupom();

            limpaCampos();
        }
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
                float[] animalColumnWidths = {3, 3, 1, 1, 1};
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

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public void limpaCampos() {
        jTextFieldCompAnimal.setText(null);
        jTextFieldCompQuantidade.setText(null);
        jTextFieldCompMediaKg.setText(null);
        jTextFieldCompPrecoKg.setText(null);
        jTextFieldCompCriador.setText(null);
        jComboCompPagamento.setSelectedIndex(0);
        jComboCompPagador.setSelectedIndex(0);
        jTextFieldCompKgTotais.setText(null);
        jTextFieldCompPorcentagem.setText(null);
        jTextFieldCompComissao.setText(null);
        jTextFieldCompTotal.setText(null);
        ((DefaultTableModel) jTableComp.getModel()).setRowCount(0);
    }

    public void pesquisarAnimalId() {
        String sql = String.format("SELECT id as ID, sexo as Sexo, quantidade as Qtde, idade as Idade FROM animais WHERE id like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextCompBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableComp.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pesquisarAnimalIdade() {
        String sql = String.format("SELECT id as ID, sexo as Sexo, quantidade as Qtde, idade as Idade FROM animais WHERE idade like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextCompBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableComp.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pesquisarAnimalSexo() {
        String sql = String.format("SELECT id as ID, sexo as Sexo, quantidade as Qtde, idade as Idade FROM animais WHERE sexo like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextCompBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableComp.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setarCampos() {
        int setar = jTableComp.getSelectedRow();
        String valorId = jTableComp.getModel().getValueAt(setar, 0).toString();

        if (valorId != null) {
            jTextFieldCompAnimal.setText(valorId);

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

        jTextFieldCompAnimal = new javax.swing.JTextField();
        jTextFieldCompQuantidade = new javax.swing.JTextField();
        jTextFieldCompMediaKg = new javax.swing.JTextField();
        jTextFieldCompPrecoKg = new javax.swing.JTextField();
        jTextFieldCompTotal = new javax.swing.JTextField();
        jLabelCompAnimal1 = new javax.swing.JLabel();
        jTextFieldCompCriador = new javax.swing.JTextField();
        jLabelCompTotal = new javax.swing.JLabel();
        jLabelCompAnimal = new javax.swing.JLabel();
        jLabelCompQuantidade = new javax.swing.JLabel();
        jLabelCompMediaKg = new javax.swing.JLabel();
        jLabelCompPrecoKg = new javax.swing.JLabel();
        jTextCompBusca = new javax.swing.JTextField();
        jLabelCompBusca = new javax.swing.JLabel();
        jLabelCompCampos = new javax.swing.JLabel();
        jButtonCompFinalizar = new javax.swing.JButton();
        jComboCompPesquisa = new javax.swing.JComboBox<>();
        jLabelCompPagador = new javax.swing.JLabel();
        jComboCompPagador = new javax.swing.JComboBox<>();
        jLabelCompPagamento = new javax.swing.JLabel();
        jComboCompPagamento = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableComp = new javax.swing.JTable();
        jTextFieldCompPorcentagem = new javax.swing.JTextField();
        jLabelCompPorcentagem = new javax.swing.JLabel();
        jLabelCompComissao = new javax.swing.JLabel();
        jTextFieldCompComissao = new javax.swing.JTextField();
        jLabelCompKgTotais = new javax.swing.JLabel();
        jTextFieldCompKgTotais = new javax.swing.JTextField();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Compra");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        jTextFieldCompAnimal.setEditable(false);

        jTextFieldCompQuantidade.setDocument(new ValidadorNumerico());

        jTextFieldCompMediaKg.setEditable(false);

        jTextFieldCompPrecoKg.setDocument(new ValidadorNumerico());

        jTextFieldCompTotal.setEditable(false);

        jLabelCompAnimal1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompAnimal1.setText("* Criador:");

        jLabelCompTotal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelCompTotal.setText("* Total:");

        jLabelCompAnimal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompAnimal.setText("* ID Animal:");

        jLabelCompQuantidade.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompQuantidade.setText("* Quantidade:");

        jLabelCompMediaKg.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompMediaKg.setText("* Média Kg:");

        jLabelCompPrecoKg.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompPrecoKg.setText("* Preço Kg:");

        jTextCompBusca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextCompBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextCompBuscaKeyReleased(evt);
            }
        });

        jLabelCompBusca.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\busca.png"));
        jLabelCompBusca.setText(" ");

        jLabelCompCampos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelCompCampos.setText("* Campos obrigatórios");

        jButtonCompFinalizar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonCompFinalizar.setText("Finalizar");
        jButtonCompFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCompFinalizarActionPerformed(evt);
            }
        });

        jComboCompPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Idade", "Sexo" }));
        jComboCompPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboCompPesquisaActionPerformed(evt);
            }
        });

        jLabelCompPagador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompPagador.setText("* Pagador:");

        jComboCompPagador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Marcos", "Negocio", "Adiantamento" }));
        jComboCompPagador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboCompPagadorActionPerformed(evt);
            }
        });

        jLabelCompPagamento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompPagamento.setText("* Pagamento:");

        jComboCompPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dinheiro", "Cartão Débito", "Cartão Crédito", "Pix" }));
        jComboCompPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboCompPagamentoActionPerformed(evt);
            }
        });

        jTableComp = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jTableComp.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTableComp.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableComp.setFocusable(false);
        jTableComp.getTableHeader().setReorderingAllowed(false);
        jTableComp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCompMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableComp);

        jTextFieldCompPorcentagem.setDocument(new ValidadorNumerico());

        jLabelCompPorcentagem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompPorcentagem.setText("* % Comissão:");

        jLabelCompComissao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompComissao.setText("* Comissão:");

        jTextFieldCompComissao.setEditable(false);

        jLabelCompKgTotais.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCompKgTotais.setText("* Kg Totais:");

        jTextFieldCompKgTotais.setDocument(new ValidadorNumerico());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jComboCompPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextCompBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCompBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelCompCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelCompAnimal)
                                    .addComponent(jLabelCompQuantidade)
                                    .addComponent(jLabelCompKgTotais)
                                    .addComponent(jLabelCompPrecoKg)
                                    .addComponent(jLabelCompMediaKg))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldCompAnimal, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                    .addComponent(jTextFieldCompKgTotais)
                                    .addComponent(jTextFieldCompQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                    .addComponent(jTextFieldCompPrecoKg)
                                    .addComponent(jTextFieldCompMediaKg))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(295, 295, 295)
                                .addComponent(jLabelCompTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldCompTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonCompFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelCompPagador)
                                    .addComponent(jLabelCompAnimal1)
                                    .addComponent(jLabelCompPagamento)
                                    .addComponent(jLabelCompPorcentagem)
                                    .addComponent(jLabelCompComissao))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboCompPagador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldCompCriador, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboCompPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldCompPorcentagem, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldCompComissao, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCompCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextCompBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboCompPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelCompBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompAnimal1)
                            .addComponent(jTextFieldCompCriador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompPagador)
                            .addComponent(jComboCompPagador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompPagamento)
                            .addComponent(jComboCompPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompPorcentagem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCompPorcentagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldCompComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCompComissao, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCompAnimal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCompAnimal))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCompQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompKgTotais)
                            .addComponent(jTextFieldCompKgTotais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldCompPrecoKg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCompPrecoKg, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompMediaKg)
                            .addComponent(jTextFieldCompMediaKg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCompFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldCompTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCompTotal))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextCompBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextCompBuscaKeyReleased
        if (jComboCompPesquisa.getSelectedItem().toString() == "ID") {
            pesquisarAnimalId();
        } else if (jComboCompPesquisa.getSelectedItem().toString() == "Idade") {
            pesquisarAnimalIdade();
        } else if (jComboCompPesquisa.getSelectedItem().toString() == "Sexo") {
            pesquisarAnimalSexo();
        }
    }//GEN-LAST:event_jTextCompBuscaKeyReleased

    private void jButtonCompFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCompFinalizarActionPerformed
        try {
            adicionar();
        } catch (SQLException ex) {
            Logger.getLogger(TelaCompra.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TelaCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonCompFinalizarActionPerformed

    private void jComboCompPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboCompPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboCompPesquisaActionPerformed

    private void jComboCompPagadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboCompPagadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboCompPagadorActionPerformed

    private void jComboCompPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboCompPagamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboCompPagamentoActionPerformed

    private void jTableCompMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCompMouseClicked
        setarCampos();
    }//GEN-LAST:event_jTableCompMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCompFinalizar;
    private javax.swing.JComboBox<String> jComboCompPagador;
    private javax.swing.JComboBox<String> jComboCompPagamento;
    private javax.swing.JComboBox<String> jComboCompPesquisa;
    private javax.swing.JLabel jLabelCompAnimal;
    private javax.swing.JLabel jLabelCompAnimal1;
    private javax.swing.JLabel jLabelCompBusca;
    private javax.swing.JLabel jLabelCompCampos;
    private javax.swing.JLabel jLabelCompComissao;
    private javax.swing.JLabel jLabelCompKgTotais;
    private javax.swing.JLabel jLabelCompMediaKg;
    private javax.swing.JLabel jLabelCompPagador;
    private javax.swing.JLabel jLabelCompPagamento;
    private javax.swing.JLabel jLabelCompPorcentagem;
    private javax.swing.JLabel jLabelCompPrecoKg;
    private javax.swing.JLabel jLabelCompQuantidade;
    private javax.swing.JLabel jLabelCompTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableComp;
    public javax.swing.JTextField jTextCompBusca;
    private javax.swing.JTextField jTextFieldCompAnimal;
    private javax.swing.JTextField jTextFieldCompComissao;
    private javax.swing.JTextField jTextFieldCompCriador;
    private javax.swing.JTextField jTextFieldCompKgTotais;
    private javax.swing.JTextField jTextFieldCompMediaKg;
    private javax.swing.JTextField jTextFieldCompPorcentagem;
    private javax.swing.JTextField jTextFieldCompPrecoKg;
    private javax.swing.JTextField jTextFieldCompQuantidade;
    private javax.swing.JTextField jTextFieldCompTotal;
    // End of variables declaration//GEN-END:variables
}
