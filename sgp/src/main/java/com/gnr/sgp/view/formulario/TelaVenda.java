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
import com.gnr.sgp.modelo.dao.VendasAnimaisDao;
import com.gnr.sgp.modelo.dominio.VendasAnimais;
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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * Classe responsavel pelas vendas.
 *
 * @author Guilherme
 */
public class TelaVenda extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaVenda
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

    public TelaVenda() {

        this.conexao = new ConexaoMysql();
        initComponents();

        // Adiciona o ouvinte de evento ao jTextFieldVendaQuantidade
        jTextFieldVendaQuantidade.getDocument().addDocumentListener(new DocumentListener() {
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
        jTextFieldVendaPrecoKg.getDocument().addDocumentListener(new DocumentListener() {
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

        // Adiciona o ouvinte de evento ao jTextFieldVendaTotal
        jTextFieldVendaTotal.getDocument().addDocumentListener(new DocumentListener() {
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
        jTextFieldVendaPorcentagem.getDocument().addDocumentListener(new DocumentListener() {
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

        jTextFieldVendaKgTotais.getDocument().addDocumentListener(new DocumentListener() {
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
        
        ClassLoader classLoader = TelaVenda.class.getClassLoader();
        String imageBusca = "busca.png";
        java.net.URL imageURLBusca = classLoader.getResource(imageBusca);

        if (imageURLBusca != null) {
            ImageIcon iconBusca = new ImageIcon(imageURLBusca);
            jLabelAnimBusca.setIcon(iconBusca);
        } else {
            System.out.println("Imagem não encontrada: " + imageBusca);
        }

    }

    private void atualizarValorTotal() {
        try {
            int quantidade = Integer.parseInt(jTextFieldVendaQuantidade.getText());
            kgTotais = Double.parseDouble(jTextFieldVendaKgTotais.getText());
            double precoKg = Double.parseDouble(jTextFieldVendaPrecoKg.getText());
            double valorTotal = kgTotais * precoKg;

            jTextFieldVendaTotal.setText(String.format("%.2f", valorTotal));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            jTextFieldVendaTotal.setText("Valor Inválido.");
        }
    }

    private void atualizarMedia() {
        try {
            int quantidade = Integer.parseInt(jTextFieldVendaQuantidade.getText());
            double mediaKg = kgTotais / quantidade;
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat formato = new DecimalFormat("#.##", symbols);
            String mediaFormatada = formato.format(mediaKg);

            jTextFieldVendaMediaKg.setText("" + mediaFormatada);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            jTextFieldVendaMediaKg.setText("Valor Inválido.");
        }
    }

    private void atualizarValorComissao() {
        try {
            String totalTexto = jTextFieldVendaTotal.getText().trim().replace(",", ".");
            if (!jTextFieldVendaTotal.getText().equals("Valor Inválido.") && !jTextFieldVendaTotal.getText().isEmpty() && !jTextFieldVendaPorcentagem.getText().isEmpty()) {
                valorTotal = Double.parseDouble(totalTexto);
                percentual = (Double.parseDouble(jTextFieldVendaPorcentagem.getText()) / 100);
                comissao = valorTotal * percentual;
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat formato = new DecimalFormat("#.##", symbols);
                String comissaoFormatada = formato.format(comissao);

                jTextFieldVendaComissao.setText("" + comissaoFormatada);
            } else {
                jTextFieldVendaComissao.setText("Valor Inválido");
            }

        } catch (NumberFormatException ex) {
            jTextFieldVendaComissao.setText("Valor Inválido");
            ex.printStackTrace();
        }
    }

    /**
     * Adiciona uma nova venda ao banco de dados e gera um cupom em PDF.
     *
     * @throws SQLException Se ocorrer um erro de SQL durante a execução.
     * @throws ParseException Se ocorrer um erro de parse durante a execução.
     */
    public void adicionar() throws SQLException, ParseException {

        quantidade = Integer.parseInt(jTextFieldVendaQuantidade.getText());
        mediaKg = Double.parseDouble(jTextFieldVendaMediaKg.getText());
        precoKg = Double.parseDouble(jTextFieldVendaPrecoKg.getText());
        valorTotal = quantidade * mediaKg * precoKg;

        VendasAnimais venda = new VendasAnimais(01, Integer.parseInt(jTextFieldVendaAnimal.getText()), quantidade, kgTotais, mediaKg, precoKg, (percentual * 100), comissao, valorTotal, jTextFieldVendaComprador.getText(), jTextFieldVendaVendedor.getText(), jComboVendaPagamento.getSelectedItem().toString(), operador);

        VendasAnimaisDao vendasDao = new VendasAnimaisDao();
        vendasDao.Adicionar(venda);

        criarCupom();

        limpaCampos();

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

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public void limpaCampos() {
        jTextFieldVendaAnimal.setText(null);
        jTextFieldVendaQuantidade.setText(null);
        jTextFieldVendaMediaKg.setText(null);
        jTextFieldVendaPrecoKg.setText(null);
        jTextFieldVendaVendedor.setText(null);
        jTextFieldVendaComprador.setText(null);
        jTextFieldVendaKgTotais.setText(null);
        jTextFieldVendaPorcentagem.setText(null);
        jTextFieldVendaComissao.setText(null);
        jTextFieldVendaTotal.setText(null);
        jComboVendaPagamento.setSelectedIndex(0);
        ((DefaultTableModel) jTableVenda.getModel()).setRowCount(0);
    }

    public void pesquisarAnimalId() {
        String sql = String.format("SELECT id as ID, sexo as Sexo, quantidade as Qtde, idade as Idade FROM animais WHERE id like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextVendaBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableVenda.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pesquisarAnimalIdade() {
        String sql = String.format("SELECT id as ID, sexo as Sexo, quantidade as Qtde, idade as Idade FROM animais WHERE idade like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextVendaBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableVenda.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pesquisarAnimalSexo() {
        String sql = String.format("SELECT id as ID, sexo as Sexo, quantidade as Qtde, idade as Idade FROM animais WHERE sexo like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextVendaBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableVenda.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setarCampos() {
        int setar = jTableVenda.getSelectedRow();
        String valorId = jTableVenda.getModel().getValueAt(setar, 0).toString();

        if (valorId != null) {
            jTextFieldVendaAnimal.setText(valorId);

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

        jLabelVendaTotal = new javax.swing.JLabel();
        jLabelVendaAnimal = new javax.swing.JLabel();
        jLabelVendaQuantidade = new javax.swing.JLabel();
        jLabelVendaMediaKg = new javax.swing.JLabel();
        jLabelVendaPrecoKg = new javax.swing.JLabel();
        jTextVendaBusca = new javax.swing.JTextField();
        jLabelAnimBusca = new javax.swing.JLabel();
        jLabelAnimCampos = new javax.swing.JLabel();
        jTextFieldVendaAnimal = new javax.swing.JTextField();
        jTextFieldVendaQuantidade = new javax.swing.JTextField();
        jTextFieldVendaMediaKg = new javax.swing.JTextField();
        jTextFieldVendaPrecoKg = new javax.swing.JTextField();
        jTextFieldVendaTotal = new javax.swing.JTextField();
        jLabelVendaComprador = new javax.swing.JLabel();
        jTextFieldVendaComprador = new javax.swing.JTextField();
        jButtonVendaFinalizar = new javax.swing.JButton();
        jComboVendaPesquisa = new javax.swing.JComboBox<>();
        jLabelVendaVendedor = new javax.swing.JLabel();
        jTextFieldVendaVendedor = new javax.swing.JTextField();
        jLabelVendaPagamento = new javax.swing.JLabel();
        jComboVendaPagamento = new javax.swing.JComboBox<>();
        jLabelVendaKgTotais = new javax.swing.JLabel();
        jTextFieldVendaKgTotais = new javax.swing.JTextField();
        jLabelVendaPorcentagem = new javax.swing.JLabel();
        jTextFieldVendaPorcentagem = new javax.swing.JTextField();
        jLabelVendaComissao = new javax.swing.JLabel();
        jTextFieldVendaComissao = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableVenda = new javax.swing.JTable();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Vendas");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        jLabelVendaTotal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelVendaTotal.setText("* Total:");

        jLabelVendaAnimal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaAnimal.setText("* ID Animal:");

        jLabelVendaQuantidade.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaQuantidade.setText("* Quantidade:");

        jLabelVendaMediaKg.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaMediaKg.setText("* Média Kg:");

        jLabelVendaPrecoKg.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaPrecoKg.setText("* Preço Kg:");

        jTextVendaBusca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextVendaBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextVendaBuscaKeyReleased(evt);
            }
        });

        jLabelAnimBusca.setText(" ");

        jLabelAnimCampos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelAnimCampos.setText("* Campos obrigatórios");

        jTextFieldVendaAnimal.setEditable(false);
        jTextFieldVendaAnimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldVendaAnimalActionPerformed(evt);
            }
        });

        jTextFieldVendaQuantidade.setDocument(new ValidadorNumerico());

        jTextFieldVendaMediaKg.setEditable(false);

        jTextFieldVendaPrecoKg.setDocument(new ValidadorNumerico());

        jTextFieldVendaTotal.setEditable(false);

        jLabelVendaComprador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaComprador.setText("* Comprador:");

        jButtonVendaFinalizar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonVendaFinalizar.setText("Finalizar");
        jButtonVendaFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVendaFinalizarActionPerformed(evt);
            }
        });

        jComboVendaPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Idade", "Sexo" }));
        jComboVendaPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboVendaPesquisaActionPerformed(evt);
            }
        });

        jLabelVendaVendedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaVendedor.setText("* Vendedor:");

        jLabelVendaPagamento.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaPagamento.setText("* Pagamento:");

        jComboVendaPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dinheiro", "Cartão Débito", "Cartão Crédito", "Pix" }));
        jComboVendaPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboVendaPagamentoActionPerformed(evt);
            }
        });

        jLabelVendaKgTotais.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaKgTotais.setText("* Kg Totais:");

        jTextFieldVendaKgTotais.setDocument(new ValidadorNumerico());

        jLabelVendaPorcentagem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaPorcentagem.setText("* % Comissão:");

        jTextFieldVendaPorcentagem.setDocument(new ValidadorNumerico());

        jLabelVendaComissao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelVendaComissao.setText("* Comissão:");

        jTextFieldVendaComissao.setEditable(false);

        jTableVenda = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jTableVenda.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTableVenda.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableVenda.setFocusable(false);
        jTableVenda.getTableHeader().setReorderingAllowed(false);
        jTableVenda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableVendaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableVenda);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelVendaAnimal)
                                    .addComponent(jLabelVendaQuantidade))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldVendaQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldVendaAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelVendaPrecoKg)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldVendaPrecoKg, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelVendaKgTotais)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldVendaKgTotais, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelVendaMediaKg)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldVendaMediaKg, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(1, 1, 1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelVendaVendedor)
                            .addComponent(jLabelVendaPagamento)
                            .addComponent(jLabelVendaComprador)
                            .addComponent(jLabelVendaPorcentagem)
                            .addComponent(jLabelVendaComissao))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldVendaComissao, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldVendaPorcentagem, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldVendaVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboVendaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldVendaComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelVendaTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldVendaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonVendaFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jComboVendaPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextVendaBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelAnimBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                        .addComponent(jLabelAnimCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelAnimCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextVendaBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelAnimBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboVendaPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVendaAnimal)
                            .addComponent(jTextFieldVendaAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldVendaQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelVendaQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVendaKgTotais, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldVendaKgTotais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVendaPrecoKg, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldVendaPrecoKg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVendaMediaKg)
                            .addComponent(jTextFieldVendaMediaKg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVendaVendedor)
                            .addComponent(jTextFieldVendaVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldVendaComprador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVendaComprador))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboVendaPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVendaPagamento))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVendaPorcentagem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldVendaPorcentagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelVendaComissao, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldVendaComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVendaFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldVendaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelVendaTotal))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonVendaFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVendaFinalizarActionPerformed
        if ((jTextFieldVendaAnimal.getText().isEmpty() || jTextFieldVendaQuantidade.getText().isEmpty() || jTextFieldVendaMediaKg.getText().isEmpty() || jTextFieldVendaPrecoKg.getText().isEmpty() || jTextFieldVendaComprador.getText().isEmpty() || jTextFieldVendaTotal.getText().isEmpty() || jTextFieldVendaVendedor.getText().isEmpty() || jTextFieldVendaKgTotais.getText().isEmpty() || jTextFieldVendaPorcentagem.getText().isEmpty() || jTextFieldVendaComissao.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
        } else {
            try {
                adicionar();
            } catch (SQLException ex) {
                Logger.getLogger(TelaVenda.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(TelaVenda.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButtonVendaFinalizarActionPerformed

    private void jComboVendaPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboVendaPagamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboVendaPagamentoActionPerformed

    private void jTextFieldVendaAnimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldVendaAnimalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldVendaAnimalActionPerformed

    private void jComboVendaPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboVendaPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboVendaPesquisaActionPerformed

    private void jTextVendaBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextVendaBuscaKeyReleased
        if (jComboVendaPesquisa.getSelectedItem().toString() == "ID") {
            pesquisarAnimalId();
        } else if (jComboVendaPesquisa.getSelectedItem().toString() == "Idade") {
            pesquisarAnimalIdade();
        } else if (jComboVendaPesquisa.getSelectedItem().toString() == "Sexo") {
            pesquisarAnimalSexo();
        }
    }//GEN-LAST:event_jTextVendaBuscaKeyReleased

    private void jTableVendaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableVendaMouseClicked
        setarCampos();
    }//GEN-LAST:event_jTableVendaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonVendaFinalizar;
    private javax.swing.JComboBox<String> jComboVendaPagamento;
    private javax.swing.JComboBox<String> jComboVendaPesquisa;
    private javax.swing.JLabel jLabelAnimBusca;
    private javax.swing.JLabel jLabelAnimCampos;
    private javax.swing.JLabel jLabelVendaAnimal;
    private javax.swing.JLabel jLabelVendaComissao;
    private javax.swing.JLabel jLabelVendaComprador;
    private javax.swing.JLabel jLabelVendaKgTotais;
    private javax.swing.JLabel jLabelVendaMediaKg;
    private javax.swing.JLabel jLabelVendaPagamento;
    private javax.swing.JLabel jLabelVendaPorcentagem;
    private javax.swing.JLabel jLabelVendaPrecoKg;
    private javax.swing.JLabel jLabelVendaQuantidade;
    private javax.swing.JLabel jLabelVendaTotal;
    private javax.swing.JLabel jLabelVendaVendedor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableVenda;
    private javax.swing.JTextField jTextFieldVendaAnimal;
    private javax.swing.JTextField jTextFieldVendaComissao;
    private javax.swing.JTextField jTextFieldVendaComprador;
    private javax.swing.JTextField jTextFieldVendaKgTotais;
    private javax.swing.JTextField jTextFieldVendaMediaKg;
    private javax.swing.JTextField jTextFieldVendaPorcentagem;
    private javax.swing.JTextField jTextFieldVendaPrecoKg;
    private javax.swing.JTextField jTextFieldVendaQuantidade;
    private javax.swing.JTextField jTextFieldVendaTotal;
    private javax.swing.JTextField jTextFieldVendaVendedor;
    public javax.swing.JTextField jTextVendaBusca;
    // End of variables declaration//GEN-END:variables
}
