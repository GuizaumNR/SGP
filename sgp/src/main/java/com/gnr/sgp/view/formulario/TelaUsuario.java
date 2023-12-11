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

/**
 * Tela responsavel pelos usuarios.
 * @author Guilherme
 */
import com.gnr.sgp.modelo.conexao.Conexao;
import java.sql.*;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dao.UsuariosDao;
import com.gnr.sgp.modelo.dominio.Usuarios;
import com.gnr.sgp.view.modelo.ValidadorQuantCaract;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaUsuario extends javax.swing.JInternalFrame {

    Conexao conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaUsuario() {

        initComponents();
        conexao = new ConexaoMysql();
        jPassUsuSenha.setDocument(new ValidadorQuantCaract(4));
        jTextUsuLogin.setDocument(new ValidadorQuantCaract(10));
        jTextUsuNome.setDocument(new ValidadorQuantCaract(10));

        setLocation(-5, -5);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                // Define a posição do componente para (0, 0)
                setLocation(-5, -5);
            }
        });
        ClassLoader classLoader = TelaUsuario.class.getClassLoader();
        String imageBusca = "busca.png";
        java.net.URL imageURLBusca = classLoader.getResource(imageBusca);

        if (imageURLBusca != null) {
            ImageIcon iconBusca = new ImageIcon(imageURLBusca);
            jLabelUsuBusca.setIcon(iconBusca);
        } else {
            System.out.println("Imagem não encontrada: " + imageBusca);
        }
        
        String imageDelete = "delete.png";
        java.net.URL imageURLDelete = classLoader.getResource(imageDelete);

        if (imageURLDelete != null) {
            ImageIcon iconDelete = new ImageIcon(imageURLDelete);
            jButtonUsuDeletar.setIcon(iconDelete);
        } else {
            System.out.println("Imagem não encontrada: " + imageDelete);
        }
        
        String imageUpdate = "update.png";
        java.net.URL imageURLUpdate = classLoader.getResource(imageUpdate);

        if (imageURLUpdate != null) {
            ImageIcon iconUpdate = new ImageIcon(imageURLUpdate);
            jButtonUsuEditar.setIcon(iconUpdate);
        } else {
            System.out.println("Imagem não encontrada: " + imageUpdate);
        }
        
        String imageCreate = "create.png";
        java.net.URL imageURLCreate = classLoader.getResource(imageCreate);

        if (imageURLCreate != null) {
            ImageIcon iconCreate = new ImageIcon(imageURLCreate);
            jButtonUsuAdicionar.setIcon(iconCreate);
        } else {
            System.out.println("Imagem não encontrada: " + imageCreate);
        }
        
    }

    private void consultar() {

        UsuariosDao usuarioDAO = new UsuariosDao();
        Usuarios usuarioEncontrado = usuarioDAO.buscarUsuariosLogin(jTextUsuLogin.getText());

        if (usuarioEncontrado != null) {
            jTextUsuNome.setText(usuarioEncontrado.getNome());
            jTextUsuLogin.setText(usuarioEncontrado.getLogin());
            jComboUsuPerfil.setSelectedItem(usuarioEncontrado.getTipo());
        } else {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado.");
            limpaCampos();
        }

    }

    private void adicionar() {
        if ((jTextUsuLogin.getText().isEmpty() || jPassUsuSenha.getText().isEmpty() || jComboUsuPerfil.getSelectedItem().toString().isEmpty() || jTextUsuNome.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        } else {

            Usuarios usuario = new Usuarios(0l, jTextUsuLogin.getText(), jPassUsuSenha.getText(), jComboUsuPerfil.getSelectedItem().toString(), jTextUsuNome.getText());

            UsuariosDao usuariosDao = new UsuariosDao();
            usuariosDao.adicionar(usuario);

            limpaCampos();
        }
    }

    private void editar() {

        if ((jTextUsuLogin.getText().isEmpty() || jPassUsuSenha.getText().isEmpty() || jComboUsuPerfil.getSelectedItem().toString().isEmpty() || jTextUsuNome.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        } else {
            Usuarios usuario = new Usuarios(Long.parseLong(jTextUsuId.getText()), jTextUsuLogin.getText(), jPassUsuSenha.getText(), jComboUsuPerfil.getSelectedItem().toString(), jTextUsuNome.getText());

            UsuariosDao usuariosDao = new UsuariosDao();
            usuariosDao.editar(usuario);

            limpaCampos();
        }
    }

    public void deletar() {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar o usuário " + jTextUsuLogin.getText() + " do banco de dados?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            Usuarios usuario = new Usuarios(Long.parseLong(jTextUsuId.getText()), jTextUsuLogin.getText(), jPassUsuSenha.getText(), jComboUsuPerfil.getSelectedItem().toString(), jTextUsuNome.getText());

            UsuariosDao usuariosDao = new UsuariosDao();
            usuariosDao.deletar(usuario);

            limpaCampos();

        }

    }

    public void pesquisarUsuarioId() {
        String sql = String.format("SELECT id as ID, nome as Nome, login as Login, tipo as Tipo FROM usuarios WHERE id like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextUsuBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pesquisarUsuarioLogin() {
        String sql = String.format("SELECT id as ID, nome as Nome, login as Login, tipo as Tipo FROM usuarios WHERE login like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextUsuBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pesquisarUsuarioNome() {
        String sql = String.format("SELECT id as ID, nome as Nome, login as Login, tipo as Tipo FROM usuarios WHERE nome like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextUsuBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pesquisarUsuarioTipo() {
        String sql = String.format("SELECT id as ID, nome as Nome, login as Login, tipo as Tipo FROM usuarios WHERE tipo like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextUsuBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setarCampos() {
        int setar = jTableForn.getSelectedRow();
        String valorId = jTableForn.getModel().getValueAt(setar, 0).toString();
        String valorLogin = jTableForn.getModel().getValueAt(setar, 1).toString();
        String valorNome = jTableForn.getModel().getValueAt(setar, 2).toString();
        String valorTipo = jTableForn.getModel().getValueAt(setar, 3).toString();
        if (valorId != null && valorLogin != null && valorNome != null) {
            jTextUsuId.setText(valorId);
            jTextUsuLogin.setText(valorLogin);
            jTextUsuNome.setText(valorNome);
            jComboUsuPerfil.setSelectedItem(valorTipo);
        }
    }

    public void limpaCampos() {
        jTextUsuId.setText(null);
        jTextUsuNome.setText(null);
        jTextUsuLogin.setText(null);
        jPassUsuSenha.setText(null);
        jComboUsuPerfil.setSelectedItem("consulta");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelUsuLogin = new javax.swing.JLabel();
        jLabelUsuPerfil = new javax.swing.JLabel();
        jTextUsuLogin = new javax.swing.JTextField();
        jLabelUsuSenha = new javax.swing.JLabel();
        jPassUsuSenha = new javax.swing.JPasswordField();
        jComboUsuPerfil = new javax.swing.JComboBox<>();
        jButtonUsuAdicionar = new javax.swing.JButton();
        jButtonUsuEditar = new javax.swing.JButton();
        jButtonUsuDeletar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableForn = new javax.swing.JTable();
        jComboUsuPesquisa = new javax.swing.JComboBox<>();
        jTextUsuBusca = new javax.swing.JTextField();
        jLabelUsuBusca = new javax.swing.JLabel();
        jLabelFornCampos = new javax.swing.JLabel();
        jTextUsuId = new javax.swing.JTextField();
        jLabelUsuId = new javax.swing.JLabel();
        jLabelUsuNome = new javax.swing.JLabel();
        jTextUsuNome = new javax.swing.JTextField();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Usuários");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        jLabelUsuLogin.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelUsuLogin.setText("* Login:");

        jLabelUsuPerfil.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelUsuPerfil.setText("Tipo:");

        jTextUsuLogin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextUsuLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUsuLoginActionPerformed(evt);
            }
        });

        jLabelUsuSenha.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelUsuSenha.setText("* Senha:");

        jPassUsuSenha.setMaximumSize(new java.awt.Dimension(64, 22));
        jPassUsuSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPassUsuSenhaActionPerformed(evt);
            }
        });

        jComboUsuPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "consulta", "admin" }));
        jComboUsuPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboUsuPerfilActionPerformed(evt);
            }
        });

        jButtonUsuAdicionar.setToolTipText("Adicionar Usuário");
        jButtonUsuAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonUsuAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonUsuAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUsuAdicionarActionPerformed(evt);
            }
        });

        jButtonUsuEditar.setToolTipText("Editar Usuário");
        jButtonUsuEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonUsuEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonUsuEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUsuEditarActionPerformed(evt);
            }
        });

        jButtonUsuDeletar.setToolTipText("Deletar Usuário");
        jButtonUsuDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonUsuDeletar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonUsuDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUsuDeletarActionPerformed(evt);
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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Login", "Nome", "Tipo"
            }
        ));
        jTableForn.getTableHeader().setReorderingAllowed(false);
        jTableForn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFornMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableForn);

        jComboUsuPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Login", "Nome", "Tipo" }));
        jComboUsuPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboUsuPesquisaActionPerformed(evt);
            }
        });

        jTextUsuBusca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextUsuBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextUsuBuscaKeyReleased(evt);
            }
        });

        jLabelUsuBusca.setText(" ");

        jLabelFornCampos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelFornCampos.setText("* Campos obrigatórios");

        jTextUsuId.setEditable(false);
        jTextUsuId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextUsuId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUsuIdActionPerformed(evt);
            }
        });

        jLabelUsuId.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelUsuId.setText("* ID:");

        jLabelUsuNome.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelUsuNome.setText("* Nome:");

        jTextUsuNome.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboUsuPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextUsuBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelUsuBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                        .addComponent(jLabelFornCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelUsuNome)
                                .addGap(18, 18, 18)
                                .addComponent(jTextUsuNome))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelUsuLogin)
                                .addGap(18, 18, 18)
                                .addComponent(jTextUsuLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelUsuId)
                                .addGap(18, 18, 18)
                                .addComponent(jTextUsuId, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelUsuPerfil)
                                .addGap(18, 18, 18)
                                .addComponent(jComboUsuPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelUsuSenha)
                                .addGap(18, 18, 18)
                                .addComponent(jPassUsuSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonUsuAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(jButtonUsuEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(jButtonUsuDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelFornCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextUsuBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboUsuPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabelUsuBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelUsuSenha)
                            .addComponent(jPassUsuSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelUsuPerfil)
                            .addComponent(jComboUsuPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextUsuId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelUsuId))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextUsuNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelUsuNome))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUsuLogin)
                    .addComponent(jTextUsuLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonUsuAdicionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonUsuEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonUsuDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jPassUsuSenha, jTextUsuLogin});

        setBounds(0, 0, 730, 545);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextUsuLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUsuLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextUsuLoginActionPerformed

    private void jPassUsuSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPassUsuSenhaActionPerformed

    }//GEN-LAST:event_jPassUsuSenhaActionPerformed

    private void jButtonUsuAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUsuAdicionarActionPerformed
        adicionar();

    }//GEN-LAST:event_jButtonUsuAdicionarActionPerformed

    private void jComboUsuPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboUsuPerfilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboUsuPerfilActionPerformed

    private void jButtonUsuEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUsuEditarActionPerformed
        editar();
    }//GEN-LAST:event_jButtonUsuEditarActionPerformed

    private void jButtonUsuDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUsuDeletarActionPerformed
        deletar();
    }//GEN-LAST:event_jButtonUsuDeletarActionPerformed

    private void jTableFornMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFornMouseClicked
        //evento para setar os campos clicando com o botao esquerdo do mouse
        setarCampos();
    }//GEN-LAST:event_jTableFornMouseClicked

    private void jComboUsuPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboUsuPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboUsuPesquisaActionPerformed

    private void jTextUsuBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUsuBuscaKeyReleased
        //enquanto for digitando fazer isto
        if (jComboUsuPesquisa.getSelectedItem().toString() == "ID") {
            pesquisarUsuarioId();
        } else if (jComboUsuPesquisa.getSelectedItem().toString() == "Login") {
            pesquisarUsuarioLogin();
        } else if (jComboUsuPesquisa.getSelectedItem().toString() == "Nome") {
            pesquisarUsuarioNome();
        } else if (jComboUsuPesquisa.getSelectedItem().toString() == "Tipo") {
            pesquisarUsuarioTipo();
        }

    }//GEN-LAST:event_jTextUsuBuscaKeyReleased

    private void jTextUsuIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUsuIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextUsuIdActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonUsuAdicionar;
    private javax.swing.JButton jButtonUsuDeletar;
    private javax.swing.JButton jButtonUsuEditar;
    public javax.swing.JComboBox<String> jComboUsuPerfil;
    private javax.swing.JComboBox<String> jComboUsuPesquisa;
    private javax.swing.JLabel jLabelFornCampos;
    private javax.swing.JLabel jLabelUsuBusca;
    private javax.swing.JLabel jLabelUsuId;
    private javax.swing.JLabel jLabelUsuLogin;
    private javax.swing.JLabel jLabelUsuNome;
    private javax.swing.JLabel jLabelUsuPerfil;
    private javax.swing.JLabel jLabelUsuSenha;
    public javax.swing.JPasswordField jPassUsuSenha;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableForn;
    public javax.swing.JTextField jTextUsuBusca;
    public javax.swing.JTextField jTextUsuId;
    public javax.swing.JTextField jTextUsuLogin;
    public javax.swing.JTextField jTextUsuNome;
    // End of variables declaration//GEN-END:variables
}
