/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dao;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dominio.Animais;
import com.gnr.sgp.modelo.dominio.Fornecedores;
import com.gnr.sgp.modelo.dominio.Usuarios;
import com.gnr.sgp.view.formulario.TelaFornecedor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Guilherme
 */
public class FornecedoresDao {

    private final Conexao conexao;

    public FornecedoresDao() {
        this.conexao = new ConexaoMysql();
    }

    public String adicionar(Fornecedores fornecedor) {
        String sql = "INSERT INTO fornecedores(nome, telefone, email, endereco) VALUES( ?, ?, ?, ?)";

        Fornecedores fornecedorTemp = buscarFornedoresNome(fornecedor.getNome());
        if (fornecedorTemp != null) {
            JOptionPane.showMessageDialog(null, "Erro: Este nome já existe no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }else{
        
        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, fornecedor.getNome());
            pst.setString(2, fornecedor.getTelefone());
            pst.setString(3, fornecedor.getEmail());
            pst.setString(4, fornecedor.getEndereco());
            
            int resultado = pst.executeUpdate();

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Fornecedor adicionado com sucesso!");
//                TelaFornecedor.limpaCampos(null);
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível adicionar o fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao cadastrar o fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        }
        return null;
    }
    
    public String deletar(Fornecedores fornecedor){
         String sql = "DELETE FROM fornecedores WHERE nome = ?";
         
         Fornecedores fornecedorTemp = buscarFornedoresNome(fornecedor.getNome());

        if (fornecedorTemp == null) {
            JOptionPane.showMessageDialog(null, "Erro: Este nome não existe no banco de dados.", "Erro", JOptionPane.ERROR);
        }

            try {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
                pst.setString(1, fornecedor.getNome());
                
                int deletado = pst.executeUpdate();
            if (deletado > 0) {
                JOptionPane.showMessageDialog(null, "Dados do fornecedor deletados com sucesso!");

            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível deletar os dados do fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Erro", JOptionPane.ERROR);
            }
        return null;
    }
    
    public String editar(Fornecedores fornecedor) {
        String sql = "UPDATE fornecedores SET telefone = ?, email = ?, endereco = ? WHERE nome = ?";

        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, fornecedor.getTelefone());
            pst.setString(2, fornecedor.getEmail());
            pst.setString(3, fornecedor.getEndereco());
            pst.setString(4, fornecedor.getNome());

            int editado = pst.executeUpdate();
            if (editado > 0) {
                JOptionPane.showMessageDialog(null, "Dados do fornecedor alterados com sucesso!");

            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível alterar os dados do fornecedor.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Erro:", JOptionPane.ERROR);
        }
        return null;

    }
    
    private Fornecedores getFornecedores(ResultSet result) throws SQLException {
        Fornecedores fornecedor = new Fornecedores();
        fornecedor.setId(result.getLong("id"));
        fornecedor.setNome(result.getString("nome"));
        fornecedor.setTelefone(result.getString("telefone"));
        fornecedor.setEmail(result.getString("email"));
        fornecedor.setEndereco(result.getString("endereco"));
        return fornecedor;
    }
    
    
    
     public Fornecedores buscarFornedoresNome(String nome) {
        String sql = String.format("SELECT * FROM fornecedores WHERE nome = '%s'", nome);
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            if (result.next()) {
                return getFornecedores(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
     

}
