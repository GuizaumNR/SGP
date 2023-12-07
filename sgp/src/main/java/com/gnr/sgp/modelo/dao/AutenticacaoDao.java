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
package com.gnr.sgp.modelo.dao;

import com.gnr.sgp.modelo.dominio.Usuarios;
import com.gnr.sgp.modelo.exception.NegocioException;
import com.gnr.sgp.view.modelo.LoginDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe responsavel por validar os dados do login.
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
