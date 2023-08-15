/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gnr.sgp.view.formulario;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dao.AnimaisDao;
import com.gnr.sgp.modelo.dominio.Animais;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Guilherme
 */
public class TelaAnimal extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaAnimal
     */
    private final Conexao conexao;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaAnimal() {
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
        if ((jTextAnimDescricao.getText().isEmpty() || jTextAnimQuantidade.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
        } else {

            Animais animal = new Animais(0l, jTextAnimDescricao.getText(), jTextAnimQuantidade.getText(), jComboAnimIdade.getSelectedItem().toString(), jComboAnimSexo.getSelectedItem().toString());

            AnimaisDao animaisDao = new AnimaisDao();
            animaisDao.adicionar(animal);
            
            limpaCampos();

        }
    }
    
    public void deletar() {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar " + jTextAnimDescricao.getText() + " do banco de dados?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            Animais animal = new Animais(0l, jTextAnimDescricao.getText(), jTextAnimQuantidade.getText(), jComboAnimIdade.getSelectedItem().toString(), jComboAnimSexo.getSelectedItem().toString());

            AnimaisDao animaisDao = new AnimaisDao();
            animaisDao.deletar(animal);
            
            limpaCampos();
        }

    }
    
     private void editar() {

      if ((jTextAnimDescricao.getText().isEmpty() || jTextAnimQuantidade.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        } else {
            Animais animal = new Animais(0l, jTextAnimDescricao.getText(), jTextAnimQuantidade.getText(), jComboAnimIdade.getSelectedItem().toString(), jComboAnimSexo.getSelectedItem().toString());

            AnimaisDao animaisDao = new AnimaisDao();
            animaisDao.editar(animal);
            
            limpaCampos();
        }
    }
    
    public void pesquisarAnimalId() {
        String sql = String.format("SELECT * FROM animais WHERE id like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextAnimBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableAnim.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    
    public void pesquisarAnimalDescricao() {
        String sql = String.format("SELECT * FROM animais WHERE descricao like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextAnimBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableAnim.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pesquisarAnimalIdade() {
        String sql = String.format("SELECT * FROM animais WHERE idade like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextAnimBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableAnim.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pesquisarAnimalSexo() {
        String sql = String.format("SELECT * FROM animais WHERE sexo like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextAnimBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableAnim.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setarCampos() {
        int setar = jTableAnim.getSelectedRow();
        String valorDesc = jTableAnim.getModel().getValueAt(setar, 1).toString();
        String valorQuant = jTableAnim.getModel().getValueAt(setar, 2).toString();
        String valorIdade = jTableAnim.getModel().getValueAt(setar, 3).toString();
        String valorSexo = jTableAnim.getModel().getValueAt(setar, 4).toString();
        if(valorDesc != null && valorQuant != null && valorIdade != null && valorSexo != null){
        jTextAnimDescricao.setText(valorDesc);
        jTextAnimQuantidade.setText(valorQuant);
        jComboAnimIdade.setSelectedItem(valorIdade);
        jComboAnimSexo.setSelectedItem(valorSexo);
        }
    }
    
    public void limpaCampos(){
        jTextAnimBusca.setText(null);
        jTextAnimDescricao.setText(null);
        jTextAnimQuantidade.setText(null);
        jComboAnimIdade.setSelectedItem("terneiro");
        jComboAnimSexo.setSelectedItem("boi");
        ((DefaultTableModel) jTableAnim.getModel()).setRowCount(0);
    }
        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelAnimIdade = new javax.swing.JLabel();
        jLabelAnimSexo = new javax.swing.JLabel();
        jTextAnimBusca = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableAnim = new javax.swing.JTable();
        jLabelAnimBusca = new javax.swing.JLabel();
        jComboAnimPesquisa = new javax.swing.JComboBox<>();
        jButtonAnimAdicionar = new javax.swing.JButton();
        jButtonAnimDeletar = new javax.swing.JButton();
        jLabelAnimCampos = new javax.swing.JLabel();
        jLabelAnimDescricao = new javax.swing.JLabel();
        jTextAnimDescricao = new javax.swing.JTextField();
        jLabelAnimQuantidade = new javax.swing.JLabel();
        jTextAnimQuantidade = new javax.swing.JTextField();
        jComboAnimIdade = new javax.swing.JComboBox<>();
        jComboAnimSexo = new javax.swing.JComboBox<>();
        jButtonAnimEditar = new javax.swing.JButton();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Animais");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        jLabelAnimIdade.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelAnimIdade.setText("* Idade:");

        jLabelAnimSexo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelAnimSexo.setText("* Sexo:");

        jTextAnimBusca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextAnimBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextAnimBuscaKeyReleased(evt);
            }
        });

        jTableAnim = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jTableAnim.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTableAnim.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableAnim.setFocusable(false);
        jTableAnim.getTableHeader().setReorderingAllowed(false);
        jTableAnim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableAnimMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableAnim);

        jLabelAnimBusca.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\busca.png"));
        jLabelAnimBusca.setText(" ");

        jComboAnimPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "id", "descricao", "idade", "sexo" }));
        jComboAnimPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboAnimPesquisaActionPerformed(evt);
            }
        });

        jButtonAnimAdicionar.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\create.png"));
        jButtonAnimAdicionar.setToolTipText("Adicionar Animal");
        jButtonAnimAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAnimAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonAnimAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnimAdicionarActionPerformed(evt);
            }
        });

        jButtonAnimDeletar.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\delete.png"));
        jButtonAnimDeletar.setToolTipText("Deletar Animal");
        jButtonAnimDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAnimDeletar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonAnimDeletar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButtonAnimDeletarMouseReleased(evt);
            }
        });
        jButtonAnimDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnimDeletarActionPerformed(evt);
            }
        });

        jLabelAnimCampos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelAnimCampos.setText("* Campos obrigatórios");

        jLabelAnimDescricao.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelAnimDescricao.setText("* Descrição:");

        jTextAnimDescricao.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextAnimDescricao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextAnimDescricaoActionPerformed(evt);
            }
        });

        jLabelAnimQuantidade.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelAnimQuantidade.setText("* Quantidade:");

        jTextAnimQuantidade.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jComboAnimIdade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "terneiro", "novilho", "vaca_velha" }));

        jComboAnimSexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "boi", "touro_reprodutor", "femea" }));

        jButtonAnimEditar.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\update.png"));
        jButtonAnimEditar.setToolTipText("Editar Usuário");
        jButtonAnimEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonAnimEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonAnimEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnimEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jComboAnimPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextAnimBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelAnimBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                        .addComponent(jLabelAnimCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabelAnimDescricao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextAnimDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelAnimSexo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboAnimSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelAnimQuantidade, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelAnimIdade, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboAnimIdade, 0, 115, Short.MAX_VALUE)
                            .addComponent(jTextAnimQuantidade))
                        .addGap(346, 346, 346)))
                .addContainerGap(10, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonAnimDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92)
                .addComponent(jButtonAnimEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91)
                .addComponent(jButtonAnimAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabelAnimCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelAnimBusca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboAnimPesquisa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jTextAnimBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAnimDescricao)
                    .addComponent(jTextAnimDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelAnimSexo)
                    .addComponent(jComboAnimSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAnimQuantidade)
                    .addComponent(jTextAnimQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAnimIdade)
                    .addComponent(jComboAnimIdade, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonAnimDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAnimAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAnimEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextAnimBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAnimBuscaKeyReleased
        if(jComboAnimPesquisa.getSelectedItem().toString() == "id"){
            pesquisarAnimalId();
        }else if (jComboAnimPesquisa.getSelectedItem().toString() == "descricao") {
            pesquisarAnimalDescricao();
        } else if (jComboAnimPesquisa.getSelectedItem().toString() == "idade") {
            pesquisarAnimalIdade();
        }
        else if (jComboAnimPesquisa.getSelectedItem().toString() == "sexo") {
            pesquisarAnimalSexo();
        }

    }//GEN-LAST:event_jTextAnimBuscaKeyReleased

    private void jTableAnimMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAnimMouseClicked
        setarCampos();

    }//GEN-LAST:event_jTableAnimMouseClicked

    private void jComboAnimPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboAnimPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboAnimPesquisaActionPerformed

    private void jButtonAnimAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnimAdicionarActionPerformed
        adicionar();
    }//GEN-LAST:event_jButtonAnimAdicionarActionPerformed

    private void jButtonAnimDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnimDeletarActionPerformed
        deletar();
    }//GEN-LAST:event_jButtonAnimDeletarActionPerformed

    private void jTextAnimDescricaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextAnimDescricaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextAnimDescricaoActionPerformed

    private void jButtonAnimDeletarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonAnimDeletarMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonAnimDeletarMouseReleased

    private void jButtonAnimEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnimEditarActionPerformed
        editar();
    }//GEN-LAST:event_jButtonAnimEditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnimAdicionar;
    private javax.swing.JButton jButtonAnimDeletar;
    private javax.swing.JButton jButtonAnimEditar;
    private javax.swing.JComboBox<String> jComboAnimIdade;
    private javax.swing.JComboBox<String> jComboAnimPesquisa;
    private javax.swing.JComboBox<String> jComboAnimSexo;
    private javax.swing.JLabel jLabelAnimBusca;
    private javax.swing.JLabel jLabelAnimCampos;
    private javax.swing.JLabel jLabelAnimDescricao;
    private javax.swing.JLabel jLabelAnimIdade;
    private javax.swing.JLabel jLabelAnimQuantidade;
    private javax.swing.JLabel jLabelAnimSexo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableAnim;
    public javax.swing.JTextField jTextAnimBusca;
    public javax.swing.JTextField jTextAnimDescricao;
    public javax.swing.JTextField jTextAnimQuantidade;
    // End of variables declaration//GEN-END:variables
}
