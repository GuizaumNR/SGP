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
import com.gnr.sgp.modelo.dao.FornecedoresDao;
import com.gnr.sgp.modelo.dominio.Fornecedores;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//importando recursos da biblioteca rs2xml 
import net.proteanit.sql.DbUtils;

/**
 * A classe TelaFornecedor representa uma tela para gerenciamento de
 * fornecedores.
 *
 * @author Guilherme
 */
public class TelaFornecedor extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaFornecedor
     */
    private final Conexao conexao;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaFornecedor() {

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

    public void adicionar() {
        if ((jTextFornNome.getText().isEmpty() || jTextFornTelefone.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
        } else {

            Fornecedores fornecedor = new Fornecedores(0l, jTextFornNome.getText(), jTextFornTelefone.getText(), jTextFornEmail.getText(), jTextFornEndereco.getText());

            FornecedoresDao fornecedoresDao = new FornecedoresDao();
            fornecedoresDao.adicionar(fornecedor);

            limpaCampos();
        }
    }

    public void editar() {
        if ((jTextFornNome.getText().isEmpty() || jTextFornTelefone.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
        } else {

            Fornecedores fornecedor = new Fornecedores(0l, jTextFornNome.getText(), jTextFornTelefone.getText(), jTextFornEmail.getText(), jTextFornEndereco.getText());

            FornecedoresDao fornecedoresDao = new FornecedoresDao();
            fornecedoresDao.editar(fornecedor);

            limpaCampos();
        }
    }

    public void deletar() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar o fornecedor " + jTextFornNome.getText() + " do banco de dados?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            Fornecedores fornecedor = new Fornecedores(0l, jTextFornNome.getText(), jTextFornTelefone.getText(), jTextFornEmail.getText(), jTextFornEndereco.getText());

            FornecedoresDao fornecedoresDao = new FornecedoresDao();
            fornecedoresDao.deletar(fornecedor);

            limpaCampos();
        }
    }

    public void pesquisarFornecedorNome() {
        String sql = String.format("SELECT id as ID, nome as Nome, telefone as Telefone, endereco as Endereço, email as Email FROM fornecedores WHERE nome like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextFornBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pesquisarFornecedorId() {
        String sql = String.format("SELECT id as ID, nome as Nome, telefone as Telefone, endereco as Endereço, email as Email FROM fornecedores WHERE id like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextFornBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setarCampos() {
        int setar = jTableForn.getSelectedRow();
        String valorNome = jTableForn.getModel().getValueAt(setar, 1).toString();
        String valorEnd = jTableForn.getModel().getValueAt(setar, 2).toString();
        String valorTele = jTableForn.getModel().getValueAt(setar, 3).toString();
        String valorEmail = jTableForn.getModel().getValueAt(setar, 4).toString();
        if (valorNome != null && valorEnd != null && valorTele != null && valorEmail != null) {
            jTextFornNome.setText(valorNome);
            jTextFornEndereco.setText(valorEnd);
            jTextFornTelefone.setText(valorTele);
            jTextFornEmail.setText(valorEmail);
        }
    }

    public void limpaCampos() {
        jTextFornBusca.setText(null);
        jTextFornNome.setText(null);
        jTextFornTelefone.setText(null);
        jTextFornEmail.setText(null);
        jTextFornEndereco.setText(null);
        ((DefaultTableModel) jTableForn.getModel()).setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonFornAdicionar = new javax.swing.JButton();
        jButtonFornEditar = new javax.swing.JButton();
        jButtonFornDeletar = new javax.swing.JButton();
        jLabelFornCampos = new javax.swing.JLabel();
        jLabelFornNome = new javax.swing.JLabel();
        jTextFornNome = new javax.swing.JTextField();
        jLabelFornEndereco = new javax.swing.JLabel();
        jTextFornEndereco = new javax.swing.JTextField();
        jLabelFornTelefone = new javax.swing.JLabel();
        jTextFornTelefone = new javax.swing.JTextField();
        jLabelFornEmail = new javax.swing.JLabel();
        jTextFornEmail = new javax.swing.JTextField();
        jTextFornBusca = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableForn = new javax.swing.JTable();
        jLabelFornBusca = new javax.swing.JLabel();
        jComboFornPesquisa = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Fornecedores");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(730, 545));

        jButtonFornAdicionar.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\create.png"));
        jButtonFornAdicionar.setToolTipText("Adicionar");
        jButtonFornAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFornAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonFornAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFornAdicionarActionPerformed(evt);
            }
        });

        jButtonFornEditar.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\update.png"));
        jButtonFornEditar.setToolTipText("Editar");
        jButtonFornEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFornEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonFornEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFornEditarActionPerformed(evt);
            }
        });

        jButtonFornDeletar.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\delete.png"));
        jButtonFornDeletar.setToolTipText("Deletar");
        jButtonFornDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFornDeletar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonFornDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFornDeletarActionPerformed(evt);
            }
        });

        jLabelFornCampos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelFornCampos.setText("* Campos obrigatórios");

        jLabelFornNome.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelFornNome.setText("* Nome:");

        jTextFornNome.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFornNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFornNomeActionPerformed(evt);
            }
        });

        jLabelFornEndereco.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelFornEndereco.setText("Endereço:");

        jTextFornEndereco.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabelFornTelefone.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelFornTelefone.setText("* Telefone:");

        jTextFornTelefone.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabelFornEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelFornEmail.setText("Email:");

        jTextFornEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jTextFornBusca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextFornBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFornBuscaKeyReleased(evt);
            }
        });

        jTableForn = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jTableForn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTableForn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Telefone", "Endereço", "Email"
            }
        ));
        jTableForn.getTableHeader().setReorderingAllowed(false);
        jTableForn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFornMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableForn);

        jLabelFornBusca.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\busca.png"));
        jLabelFornBusca.setText(" ");

        jComboFornPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nome" }));
        jComboFornPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboFornPesquisaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabelFornEndereco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelFornNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabelFornTelefone)
                    .addComponent(jLabelFornEmail))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFornTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFornNome, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFornEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFornEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 50, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonFornAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(jButtonFornEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(jButtonFornDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboFornPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFornBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelFornBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelFornCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelFornCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFornBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboFornPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabelFornBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFornNome)
                    .addComponent(jTextFornNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFornEndereco)
                    .addComponent(jTextFornEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFornTelefone)
                    .addComponent(jTextFornTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFornEmail)
                    .addComponent(jTextFornEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonFornDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFornEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFornAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFornAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFornAdicionarActionPerformed
        adicionar();
    }//GEN-LAST:event_jButtonFornAdicionarActionPerformed

    private void jButtonFornEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFornEditarActionPerformed
        editar();
    }//GEN-LAST:event_jButtonFornEditarActionPerformed

    private void jButtonFornDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFornDeletarActionPerformed
        deletar();
    }//GEN-LAST:event_jButtonFornDeletarActionPerformed

    private void jTextFornNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFornNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFornNomeActionPerformed

    private void jTextFornBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFornBuscaKeyReleased
        //enquanto for digitando fazer isto
        if (jComboFornPesquisa.getSelectedItem().toString() == "Nome") {
            pesquisarFornecedorNome();
        } else if (jComboFornPesquisa.getSelectedItem().toString() == "ID") {
            pesquisarFornecedorId();
        }

    }//GEN-LAST:event_jTextFornBuscaKeyReleased

    private void jTableFornMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFornMouseClicked
        //evento para setar os campos clicando com o botao esquerdo do mouse
        setarCampos();
    }//GEN-LAST:event_jTableFornMouseClicked

    private void jComboFornPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboFornPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboFornPesquisaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonFornAdicionar;
    private javax.swing.JButton jButtonFornDeletar;
    private javax.swing.JButton jButtonFornEditar;
    private javax.swing.JComboBox<String> jComboFornPesquisa;
    private javax.swing.JLabel jLabelFornBusca;
    private javax.swing.JLabel jLabelFornCampos;
    private javax.swing.JLabel jLabelFornEmail;
    private javax.swing.JLabel jLabelFornEndereco;
    private javax.swing.JLabel jLabelFornNome;
    private javax.swing.JLabel jLabelFornTelefone;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableForn;
    public javax.swing.JTextField jTextFornBusca;
    public javax.swing.JTextField jTextFornEmail;
    public javax.swing.JTextField jTextFornEndereco;
    public javax.swing.JTextField jTextFornNome;
    public javax.swing.JTextField jTextFornTelefone;
    // End of variables declaration//GEN-END:variables
}
