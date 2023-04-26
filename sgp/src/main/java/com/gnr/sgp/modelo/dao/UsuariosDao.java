/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dao;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dominio.Usuarios;
import com.mysql.cj.xdevapi.PreparableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Guilherme
 */
public class UsuariosDao {
    
    private final Conexao conexao;

    public UsuariosDao(Conexao conexao) {
        this.conexao = new ConexaoMysql();
    }
    
    public String salvar(Usuarios usuario){
      if(usuario.getId() == 0){
          return adicionar(usuario);
      }else{
          return editar(usuario);
      }
    };

    private String adicionar(Usuarios usuario) {
      String sql = "INSERT INTO usuarios(login, senha) VALUES(?, ?)";
      try{
          PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
          
          preencherValoresPreparedStatment(preparedStatement, usuario);
          
          int resultado = preparedStatement.executeUpdate();
          
          if(resultado == 1){
              return "Usuário adicionado com sucesso!";
          }else{
              return "Não foi possível adicionar o usuário.";
          }
          
      }catch(SQLException e){
          return String.format("Error: %s", e.getMessage());
      }
        
    }

    private String editar(Usuarios usuario) {
        String sql = "UPDATE usuarios SET login = ?, senha = ?, tipo = ? WHERE id = ? ";
         try{
          PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
          
          preencherValoresPreparedStatment(preparedStatement, usuario);
          
          int resultado = preparedStatement.executeUpdate();
          
          if(resultado == 1){
              return "Usuário editado com sucesso!";
          }else{
              return "Não foi possível editar o usuário.";
          }
          
      }catch(SQLException e){
          return String.format("Error: %s", e.getMessage());
      }
    }

    private void preencherValoresPreparedStatment(PreparedStatement preparedStatement, Usuarios usuario) throws SQLException {
            preparedStatement.setString(1, usuario.getLogin());
            preparedStatement.setString(2, usuario.getSenha());
            preparedStatement.setBoolean(3, usuario.getTipo());
            
            if(usuario.getId() != 0){
                preparedStatement.setLong(6, usuario.getId());
            }
         }
}
