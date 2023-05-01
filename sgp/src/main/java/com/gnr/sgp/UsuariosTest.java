/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp;

import com.gnr.sgp.modelo.dao.UsuariosDao;
import com.gnr.sgp.modelo.dominio.Usuarios;

/**
 *
 * @author Guilherme
 */
public class UsuariosTest {
    
    
    public static void main(String[] args) {
        Usuarios usuarioExemplo = new Usuarios(0l, "Guilherme2", "guizau2", "admin");
        
        UsuariosDao usuariosDao = new UsuariosDao();
        String mensagem = usuariosDao.salvar(usuarioExemplo);
        System.out.println(mensagem);
    }
}
