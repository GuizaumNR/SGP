/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.controller;

import com.gnr.sgp.modelo.dao.AutenticacaoDao;
import com.gnr.sgp.modelo.dominio.Usuarios;
import com.gnr.sgp.view.formulario.Login;
import com.gnr.sgp.view.modelo.LoginDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Guilherme
 */
public class LoginController implements ActionListener{
    
    private final Login login;
    private AutenticacaoDao autenticacaoDao;

    public LoginController(Login login) {
        this.login = login;
        this.autenticacaoDao = new AutenticacaoDao();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
      String acao = ae.getActionCommand().toLowerCase();
      
      switch(acao){
          case "login": login(); break;
          case "cancelar": cancelar(); break;
    }
    }

    private void login() {
        String usuario = this.login.getTxtLoginUsuario().getText();
        String senha = new String(this.login.getPassSenhaUsuario().getPassword());
        
        if(usuario.equals("") || senha.equals("")){
            this.login.getLabelLoginMensagem().setText("Insira o usuário e a senha para prosseguir.");
            return;
        }
        
        LoginDTO loginDto = new LoginDTO(usuario, senha);
        
        Usuarios usuarioTemp = this.autenticacaoDao.login(loginDto);
        
        if(usuarioTemp != null){
            JOptionPane.showConfirmDialog(null, usuarioTemp.getLogin());
        }else{
            this.login.getLabelLoginMensagem().setText("Usuário ou senha incorretos.");
            limpaCampos();
        }
    }

    private void cancelar() {
        int confirmar = JOptionPane.showConfirmDialog(login, "Sair do sistema?", "Sair do sistema", JOptionPane.YES_NO_OPTION);
        
        if(confirmar == JOptionPane.YES_OPTION){
            System.exit(0);        }
    }
    
    private void limpaCampos(){
        this.login.getTxtLoginUsuario().setText("");
        this.login.getPassSenhaUsuario().setText("");
    }
    
}
