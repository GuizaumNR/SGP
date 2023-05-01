package com.gnr.sgp;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dominio.Fornecedores;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class main {

    public static void main(String[] args) throws SQLException {
        
       String consultarSql = "select * from fornecedores";
       String inserirSql = "INSERT INTO fornecedores(nome, telefone, email, endereco) VALUES(?, ?, ?, ?)";
       
       Conexao conexao = new ConexaoMysql();
      
       Fornecedores fornecedor = new Fornecedores(0l, "Teste fornecedor", 00000000000, "fornecedor@gmail.com", "Rua do fornecedor, n. 1");
       
//       inserir
       PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(inserirSql);
       
       preparedStatement.setString(1, fornecedor.getNome());
       preparedStatement.setInt(2, fornecedor.getTelefone());
       preparedStatement.setString(3, fornecedor.getEmail());
       preparedStatement.setString(4, fornecedor.getEndereco());
       
      int resultadoCadastro = preparedStatement.executeUpdate();
      
      System.out.println(resultadoCadastro);
       
//       consulta
       ResultSet result = conexao.obterConexao().prepareStatement(consultarSql).executeQuery();
       
       while(result.next()){
            System.out.println("Resultado da consulta: " + result.getString("nome"));
       }

}
}
