/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.gnr.sgp;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author Guilherme
 */
public class main {

    public static void main(String[] args) throws SQLException {
        
        String sql = "select * from animais";
        
       Conexao conexao = new ConexaoMysql();
       
       ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
       
       while(result.next()){
    System.out.println("Resultado da consulta: " + result.getString("descricao"));
}

    }
}
