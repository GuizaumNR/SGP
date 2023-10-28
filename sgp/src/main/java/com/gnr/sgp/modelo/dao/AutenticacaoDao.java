/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dao;

import com.gnr.sgp.modelo.dominio.Usuarios;
import com.gnr.sgp.modelo.exception.NegocioException;
import com.gnr.sgp.view.modelo.LoginDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Guilherme
 */
public class AutenticacaoDao {

    private final UsuariosDao usuarioDao;

    public AutenticacaoDao() {
        this.usuarioDao = new UsuariosDao();
    }

    public boolean temPermissao(Usuarios usuario) {
        try {
            permissao(usuario);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public void permissao(Usuarios usuario) {
        if (!usuario.getTipo().equals("admin")) {
            throw new NegocioException("Usuário sem permissão para efetuar esta ação.");
        }
    }

    public Usuarios login(LoginDTO login) {
        Usuarios usuario = usuarioDao.buscarUsuariosLogin(login.getUsuario());

        if (usuario == null) {
            return null;
        }

        if (validarSenha(usuario.getSenha(), login.getSenha())) {
            return usuario;
        }
        return null;
    }

    private boolean validarSenha(String senhaUsuario, String senhaLogin) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.matches(senhaLogin, senhaUsuario);
    }
}
