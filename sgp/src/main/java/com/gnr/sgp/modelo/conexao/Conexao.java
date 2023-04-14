/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.conexao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Guilherme
 */
public interface Conexao {
    
    public Connection obterConexao() throws SQLException;
    
}
