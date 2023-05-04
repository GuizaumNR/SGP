/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dao;

import com.gnr.sgp.modelo.dominio.Usuarios;

/**
 *
 * @author Guilherme
 */
public class AutenticacaoDao {

    private final UsuariosDao usuarioDao;

    public AutenticacaoDao() {
        this.usuarioDao = new UsuariosDao();
    }
    
    public Usuarios login(loginDTO login){
        
    }
}
