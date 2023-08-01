/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gnr.sgp.view.formulario;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dao.ComprasAnimaisDao;
import com.gnr.sgp.modelo.dao.VendasAnimaisDao;
import com.gnr.sgp.modelo.dominio.ComprasAnimais;
import com.gnr.sgp.modelo.dominio.VendasAnimais;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    
    public TelaCompra() {
        this.conexao = new ConexaoMysql();
        initComponents();
//        componentMoved(e);
        // Adiciona o ouvinte de evento ao jTextFieldVendaQuantidade
        jTextFieldCompQuantidade.getDocument().addDocumentListener(new DocumentListener() {
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

        // Adiciona o ouvinte de evento ao jTextFieldVendaMediaKg
        jTextFieldCompMediaKg.getDocument().addDocumentListener(new DocumentListener() {
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
            double mediaKg = Double.parseDouble(jTextFieldCompMediaKg.getText());
            double precoKg = Double.parseDouble(jTextFieldCompPrecoKg.getText());
            double valorTotal = quantidade * mediaKg * precoKg;

            jTextFieldCompTotal.setText(String.format("%.2f", valorTotal));
        } catch (NumberFormatException ex) {
            jTextFieldCompTotal.setText("Valor Inválido");
        }
    }
    
    public void adicionar() {
        if ((jTextFieldCompAnimal.getText().isEmpty() || jTextFieldCompQuantidade.getText().isEmpty() || jTextFieldCompMediaKg.getText().isEmpty() || jTextFieldCompPrecoKg.getText().isEmpty()  || jTextFieldCompCriador.getText().isEmpty() || jTextFieldCompTotal.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
        } else {
            quantidade = Integer.parseInt(jTextFieldCompQuantidade.getText());
            mediaKg = Double.parseDouble(jTextFieldCompMediaKg.getText());
            precoKg = Double.parseDouble(jTextFieldCompPrecoKg.getText());
            valorTotal = quantidade * mediaKg * precoKg;

            ComprasAnimais compra = new ComprasAnimais(null, Integer.parseInt( jTextFieldCompAnimal.getText()), quantidade, mediaKg, precoKg, valorTotal, jTextFieldCompCriador.getText(), jComboCompPagamento.getSelectedItem().toString(), jTextFieldCompLocal.getText(),"Guilherme");
           
            ComprasAnimaisDao comprasDao = new ComprasAnimaisDao();
            comprasDao.Adicionar(compra);
        }
    }
    
    public void pesquisarAnimalId() {
        String sql = String.format("SELECT * FROM animais WHERE id like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextCompBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableComp.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    public void pesquisarAnimalDescricao() {
        String sql = String.format("SELECT * FROM animais WHERE descricao like ?");
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
        String sql = String.format("SELECT * FROM animais WHERE idade like ?");
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
        String sql = String.format("SELECT * FROM animais WHERE sexo like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextCompBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableComp.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
         public void setarCampo() {
        int setar = jTableComp.getSelectedRow();
        jTextFieldCompAnimal.setText(jTableComp.getModel().getValueAt(setar, 0).toString());
    }
         

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTableComp = new javax.swing.JTable();
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
        jLabelCompPagamento = new javax.swing.JLabel();
        jComboCompPagamento = new javax.swing.JComboBox<>();
        jLabelCompLocal = new javax.swing.JLabel();
        jTextFieldCompLocal = new javax.swing.JTextField();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Compra");
        setMinimumSize(new java.awt.Dimension(680, 480));

        jTableComp.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTableComp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Descrição", "Quant", "Idade", "Sexo"
            }
        ));
        jTableComp.setMinimumSize(new java.awt.Dimension(680, 480));
        jTableComp.setPreferredSize(new java.awt.Dimension(820, 420));
        jTableComp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCompMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableComp);

        jLabelCompAnimal1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelCompAnimal1.setText("* Criador:");

        jLabelCompTotal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelCompTotal.setText("* Total:");

        jLabelCompAnimal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelCompAnimal.setText("* Id:");

        jLabelCompQuantidade.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelCompQuantidade.setText("* Quantidade:");

        jLabelCompMediaKg.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelCompMediaKg.setText("* Média Kg:");

        jLabelCompPrecoKg.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
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

        jComboCompPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "id", "descricao", "idade", "sexo" }));
        jComboCompPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboCompPesquisaActionPerformed(evt);
            }
        });

        jLabelCompPagamento.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelCompPagamento.setText("* Pagamento:");

        jComboCompPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "alemao", "negocio", "adiantamento" }));
        jComboCompPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboCompPagamentoActionPerformed(evt);
            }
        });

        jLabelCompLocal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelCompLocal.setText("* Local:");

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                        .addComponent(jLabelCompCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabelCompPrecoKg)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldCompPrecoKg, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelCompQuantidade)
                                    .addComponent(jLabelCompMediaKg, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldCompMediaKg, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabelCompAnimal)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldCompAnimal)
                                    .addComponent(jTextFieldCompQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonCompFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(127, 127, 127))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelCompPagamento)
                                    .addComponent(jLabelCompAnimal1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldCompCriador, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboCompPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabelCompTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldCompTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabelCompLocal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldCompLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCompAnimal)
                    .addComponent(jTextFieldCompAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCompAnimal1)
                    .addComponent(jTextFieldCompCriador, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelCompQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldCompQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelCompPagamento)
                        .addComponent(jComboCompPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCompMediaKg)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldCompMediaKg, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompLocal)
                            .addComponent(jTextFieldCompLocal, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelCompPrecoKg, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldCompPrecoKg, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCompTotal)
                            .addComponent(jTextFieldCompTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(57, 57, 57)
                .addComponent(jButtonCompFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableCompMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCompMouseClicked
        setarCampo();
    }//GEN-LAST:event_jTableCompMouseClicked

    private void jTextCompBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextCompBuscaKeyReleased
        if(jComboCompPesquisa.getSelectedItem().toString() == "id"){
            pesquisarAnimalId();
        }else if (jComboCompPesquisa.getSelectedItem().toString() == "descricao") {
            pesquisarAnimalDescricao();
        } else if (jComboCompPesquisa.getSelectedItem().toString() == "idade") {
            pesquisarAnimalIdade();
        }
        else if (jComboCompPesquisa.getSelectedItem().toString() == "sexo") {
            pesquisarAnimalSexo();
        }
    }//GEN-LAST:event_jTextCompBuscaKeyReleased

    private void jButtonCompFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCompFinalizarActionPerformed
        adicionar();
    }//GEN-LAST:event_jButtonCompFinalizarActionPerformed

    private void jComboCompPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboCompPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboCompPesquisaActionPerformed

    private void jComboCompPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboCompPagamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboCompPagamentoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCompFinalizar;
    private javax.swing.JComboBox<String> jComboCompPagamento;
    private javax.swing.JComboBox<String> jComboCompPesquisa;
    private javax.swing.JLabel jLabelCompAnimal;
    private javax.swing.JLabel jLabelCompAnimal1;
    private javax.swing.JLabel jLabelCompBusca;
    private javax.swing.JLabel jLabelCompCampos;
    private javax.swing.JLabel jLabelCompLocal;
    private javax.swing.JLabel jLabelCompMediaKg;
    private javax.swing.JLabel jLabelCompPagamento;
    private javax.swing.JLabel jLabelCompPrecoKg;
    private javax.swing.JLabel jLabelCompQuantidade;
    private javax.swing.JLabel jLabelCompTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableComp;
    public javax.swing.JTextField jTextCompBusca;
    private javax.swing.JTextField jTextFieldCompAnimal;
    private javax.swing.JTextField jTextFieldCompCriador;
    private javax.swing.JTextField jTextFieldCompLocal;
    private javax.swing.JTextField jTextFieldCompMediaKg;
    private javax.swing.JTextField jTextFieldCompPrecoKg;
    private javax.swing.JTextField jTextFieldCompQuantidade;
    private javax.swing.JTextField jTextFieldCompTotal;
    // End of variables declaration//GEN-END:variables
}
