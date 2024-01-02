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

import com.gnr.sgp.controller.LoginController;
import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dao.UsuariosDao;
import com.gnr.sgp.modelo.dominio.Usuarios;
import com.gnr.sgp.view.modelo.ValidadorQuantCaract;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 * A classe Login representa a interface de login da aplicação.
 *
 * @author Guilherme
 */
public class Login extends javax.swing.JFrame {

    private final LoginController loginController;
    private final String copy = "Copyright ©2023 Guilherme Rodrigues - Todos os direitos reservados.";

    Conexao conexao;

    ValidadorQuantCaract validaQuant = new ValidadorQuantCaract(4);

    private URL url;

    /**
     * Creates new form Login
     */
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = (int) screenSize.getWidth();
    int screenHeight = (int) screenSize.getHeight();

    public Login() {
        this.conexao = new ConexaoMysql();
        initComponents();
        setLocationRelativeTo(null);
        requestFocus();
        setSize(screenWidth, screenHeight);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }

            public void windowLostFocus(WindowEvent e) {

            }
        });

        Usuarios usuarioExemplo = new Usuarios(0l, "admin", "1234", "admin", "admin");
        Usuarios usuarioAdmin = buscarUsuariosLogin(usuarioExemplo.getLogin());
        if (usuarioAdmin != null) {
        } else {
            UsuariosDao usuariosDao = new UsuariosDao();
            usuariosDao.adicionar(usuarioExemplo);
        }

        this.loginController = new LoginController(this);
        eventos();
        LabelLoginCopy.setText(copy);

        jButtonLoginLogin.setFocusable(false);
        jButtonLoginCancelar.setFocusable(false);

        if (txtLoginUsuario != null) {
            txtLoginUsuario.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    passLoginSenha.requestFocus();
                }
            });
        }
        passLoginSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginLogin.requestFocus();
                loginController.login();
            }
        });

        ClassLoader classLoader = Login.class.getClassLoader();
        String imagePath = "logo.png";
        java.net.URL imageURL = classLoader.getResource(imagePath);

        if (imageURL != null) {
            ImageIcon icon = new ImageIcon(imageURL);
            jLabelLogo.setIcon(icon);
        } else {
            System.out.println("Imagem não encontrada: " + imagePath);
        }

    }
    

    private void eventos() {
        jButtonLoginLogin.addActionListener(loginController);
        jButtonLoginCancelar.addActionListener(loginController);
    }

    public Usuarios buscarUsuariosLogin(String login) {
        String sql = String.format("SELECT * FROM usuarios WHERE login = '%s'", login);
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            if (result.next()) {
                return new Usuarios();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel = new javax.swing.JPanel();
        labelLoginMensagem = new javax.swing.JLabel();
        jLabelLoginLogin = new javax.swing.JLabel();
        jLabelLoginSenha = new javax.swing.JLabel();
        txtLoginUsuario = new javax.swing.JTextField();
        passLoginSenha = new javax.swing.JPasswordField();
        jButtonLoginLogin = new javax.swing.JButton();
        jButtonLoginCancelar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        LabelLoginCopy = new javax.swing.JLabel();
        jLabelLogo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel.setBackground(new java.awt.Color(198, 222, 198));

        labelLoginMensagem.setBackground(new java.awt.Color(255, 255, 255));
        labelLoginMensagem.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        labelLoginMensagem.setForeground(new java.awt.Color(51, 51, 51));
        labelLoginMensagem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelLoginMensagem.setText(" ");

        jLabelLoginLogin.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelLoginLogin.setForeground(new java.awt.Color(51, 51, 51));
        jLabelLoginLogin.setText("Usuário:");

        jLabelLoginSenha.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabelLoginSenha.setForeground(new java.awt.Color(51, 51, 51));
        jLabelLoginSenha.setText("Senha:");

        txtLoginUsuario.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtLoginUsuario.setBorder(null);
        txtLoginUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoginUsuarioActionPerformed(evt);
            }
        });

        passLoginSenha.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        passLoginSenha.setBorder(null);
        passLoginSenha.setDocument(validaQuant);
        passLoginSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passLoginSenhaActionPerformed(evt);
            }
        });

        jButtonLoginLogin.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButtonLoginLogin.setForeground(new java.awt.Color(51, 51, 51));
        jButtonLoginLogin.setText("Login");
        jButtonLoginLogin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jButtonLoginCancelar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButtonLoginCancelar.setForeground(new java.awt.Color(51, 51, 51));
        jButtonLoginCancelar.setText("Cancelar");
        jButtonLoginCancelar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonLoginCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginCancelarActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(102, 102, 102));
        jSeparator1.setForeground(new java.awt.Color(51, 51, 51));

        jSeparator2.setBackground(new java.awt.Color(102, 102, 102));
        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        LabelLoginCopy.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LabelLoginCopy.setForeground(new java.awt.Color(51, 51, 51));
        LabelLoginCopy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabelLogo.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\logo.png"));

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabelLoginCopy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelLoginMensagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 813, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonLoginLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelLoginSenha)
                            .addComponent(jLabelLoginLogin)
                            .addComponent(passLoginSenha)
                            .addComponent(txtLoginUsuario)
                            .addComponent(jButtonLoginCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addComponent(labelLoginMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelLoginLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLoginUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelLoginSenha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passLoginSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLoginCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonLoginLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(LabelLoginCopy, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLoginCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginCancelarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonLoginCancelarActionPerformed

    private void passLoginSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passLoginSenhaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passLoginSenhaActionPerformed

    private void txtLoginUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoginUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoginUsuarioActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelLoginCopy;
    private javax.swing.JButton jButtonLoginCancelar;
    private javax.swing.JButton jButtonLoginLogin;
    private javax.swing.JLabel jLabelLoginLogin;
    private javax.swing.JLabel jLabelLoginSenha;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JPanel jPanel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labelLoginMensagem;
    private javax.swing.JPasswordField passLoginSenha;
    public javax.swing.JTextField txtLoginUsuario;
    // End of variables declaration//GEN-END:variables

    public JTextField getTxtLoginUsuario() {
        return txtLoginUsuario;
    }

    public JPasswordField getPassSenhaUsuario() {
        return passLoginSenha;
    }

    public JButton getBotaoLogin() {
        return jButtonLoginLogin;
    }

    public JButton getBotaoLoginCancelar() {
        return jButtonLoginCancelar;
    }

    public JLabel getLabelLoginMensagem() {
        return labelLoginMensagem;
    }

}
