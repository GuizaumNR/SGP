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
package com.gnr.sgp.controller;

import com.gnr.sgp.TelaPrincipal;
import com.gnr.sgp.modelo.dao.AutenticacaoDao;
import com.gnr.sgp.modelo.dominio.Usuarios;
import com.gnr.sgp.modelo.exception.NegocioException;
import com.gnr.sgp.view.formulario.Login;
import com.gnr.sgp.view.modelo.LoginDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * Classe responsavel pela autenticacao do login.
 *
 * @author Guilherme
 */
public class LoginController implements ActionListener {

    private final Login login;
    private AutenticacaoDao autenticacaoDao;
    private String operador;

    public LoginController(Login login) {
        this.login = login;
        this.autenticacaoDao = new AutenticacaoDao();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String acao = ae.getActionCommand().toLowerCase();

        switch (acao) {
            case "login":
                try {
                login();

            } catch (NegocioException e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e);
            }
            break;
            case "cancelar":
                cancelar();
                break;
        }
    }

    public void login() {
        String usuario = this.login.getTxtLoginUsuario().getText();
        String senha = new String(this.login.getPassSenhaUsuario().getPassword());

        if (usuario.equals("") || senha.equals("")) {
            this.login.getLabelLoginMensagem().setText("Insira o usuário e a senha para prosseguir.");
            return;
        }

        LoginDTO loginDto = new LoginDTO(usuario, senha);

        Usuarios usuarioTemp = this.autenticacaoDao.login(loginDto);

        if (usuarioTemp != null) {
            try {
                login.dispose();

                TelaPrincipal tela = new TelaPrincipal();
                operador = usuarioTemp.getNome();
                tela.setOperador(operador);
                tela.setVisible(true);

                try {
                    this.autenticacaoDao.permissao(usuarioTemp);
                    // O código aqui será executado se o usuário tiver permissão
                    tela.setPermissao(Boolean.TRUE);

                } catch (NegocioException e) {
                    // O código aqui será executado se o usuário não tiver permissão
                    tela.setPermissao(Boolean.FALSE);

                }
            } catch (NegocioException e) {
                JOptionPane.showMessageDialog(null, "Erro: " + e);
            }
        } else {
            this.login.getLabelLoginMensagem().setText("Usuário ou senha incorretos.");
            limpaCampos();
        }

    }

    private void cancelar() {
        int confirmar = JOptionPane.showConfirmDialog(login, "Sair do sistema?", "Sair do sistema", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void limpaCampos() {
        login.txtLoginUsuario.requestFocus();
        this.login.getTxtLoginUsuario().setText("");
        this.login.getPassSenhaUsuario().setText("");
    }

}
