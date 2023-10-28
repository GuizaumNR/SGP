/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gnr.sgp.view.formulario;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dao.ComprasAnimaisDao;
import com.gnr.sgp.modelo.dominio.ComprasAnimais;
import com.gnr.sgp.view.modelo.ValidadorNumerico;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
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

            jTextFieldCompMediaKg.setText("" + mediaKg);
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

                jTextFieldCompComissao.setText("" + comissao);
            } else {
                jTextFieldCompComissao.setText("Valor Inválido");
            }

        } catch (NumberFormatException ex) {
            jTextFieldCompComissao.setText("Valor Inválido");
            ex.printStackTrace();
        }
    }

    public void adicionar() {
        if ((jTextFieldCompAnimal.getText().isEmpty() || jTextFieldCompQuantidade.getText().isEmpty() || jTextFieldCompMediaKg.getText().isEmpty() || jTextFieldCompPrecoKg.getText().isEmpty() || jTextFieldCompCriador.getText().isEmpty() || jTextFieldCompTotal.getText().isEmpty() || jTextFieldCompKgTotais.getText().isEmpty() || jTextFieldCompPorcentagem.getText().isEmpty() || jTextFieldCompComissao.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
        } else {
            quantidade = Integer.parseInt(jTextFieldCompQuantidade.getText());
            mediaKg = Double.parseDouble(jTextFieldCompMediaKg.getText());
            precoKg = Double.parseDouble(jTextFieldCompPrecoKg.getText());
            valorTotal = quantidade * mediaKg * precoKg;

            ComprasAnimais compra = new ComprasAnimais(null, Integer.parseInt(jTextFieldCompAnimal.getText()), quantidade, (kgTotais * 10), mediaKg, precoKg, valorTotal, (percentual * 100), comissao, jTextFieldCompCriador.getText(), jComboCompPagador.getSelectedItem().toString(), jComboCompPagamento.getSelectedItem().toString(), operador);

            ComprasAnimaisDao comprasDao = new ComprasAnimaisDao();
            comprasDao.Adicionar(compra);

            limpaCampos();
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
        String sql = String.format("SELECT id, quantidade, idade, sexo FROM animais WHERE id like ?");
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
        String sql = String.format("SELECT id, quantidade, idade, sexo FROM animais WHERE idade like ?");
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
        String sql = String.format("SELECT id, quantidade, idade, sexo FROM animais WHERE sexo like ?");
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

        jComboCompPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "id", "idade", "sexo" }));
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

        jComboCompPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dinheiro", "Cartão Débito", "Cartão Crédito", "Pix", "Permuta" }));
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
                "Id", "Sexo", "Quant", "Idade"
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompAnimal)
                            .addComponent(jTextFieldCompAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        if (jComboCompPesquisa.getSelectedItem().toString() == "id") {
            pesquisarAnimalId();
        } else if (jComboCompPesquisa.getSelectedItem().toString() == "idade") {
            pesquisarAnimalIdade();
        } else if (jComboCompPesquisa.getSelectedItem().toString() == "sexo") {
            pesquisarAnimalSexo();
        }
    }//GEN-LAST:event_jTextCompBuscaKeyReleased

    private void jButtonCompFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCompFinalizarActionPerformed
        adicionar();
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
