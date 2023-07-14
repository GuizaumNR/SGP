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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Guilherme
 */
public class AnimaisDao {
    private final Conexao conexao;

    public AnimaisDao() {
        this.conexao = new ConexaoMysql();
    }
    
    public String adicionar(Animais animal) {
        String sql = "INSERT INTO animais(descricao, quantidade, idade, sexo) VALUES( ?, ?, ?, ?)";

        Animais animalTemp = buscarAnimaisDescricao(animal.getDescricao());
        if (animalTemp != null) {
            JOptionPane.showMessageDialog(null, "Erro: Esta descrição já existe no banco de dados.");
        }
        
        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, animal.getDescricao());
            pst.setString(2, animal.getQuantidade());
            pst.setString(3, animal.getIdade());
            pst.setString(4, animal.getSexo());
            
            int resultado = pst.executeUpdate();

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Animal adicionado com sucesso!");
//                TelaFornecedor.limpaCampos(null);
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível adicionar o animal.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao cadastrar o animal.");
            e.printStackTrace();
        }
        return null;
    }
    
    public String deletar(Animais animal){
         String sql = "DELETE FROM animais WHERE descricao = ?";
         
         Animais animalTemp = buscarAnimaisDescricao(animal.getDescricao());

        if (animalTemp == null) {
            JOptionPane.showMessageDialog(null, "Erro: Esta descrição não existe no banco de dados.", "Erro", JOptionPane.ERROR);
        }

            try {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
                pst.setString(1, animal.getDescricao());
                
                int deletado = pst.executeUpdate();
            if (deletado > 0) {
                JOptionPane.showMessageDialog(null, "Dados do animal deletados com sucesso!");

            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível deletar os dados do animal.");
            }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Erro", JOptionPane.ERROR);
            }
        return null;
    }
    
    public String editar(Animais animal) {
        String sql = "UPDATE animais SET quantidade = ?, idade = ?, sexo = ? WHERE descricao = ?";

     
        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, animal.getQuantidade());
            pst.setString(2, animal.getIdade());
            pst.setString(3, animal.getSexo());
            pst.setString(4, animal.getDescricao());

            int editado = pst.executeUpdate();
            if (editado > 0) {
                JOptionPane.showMessageDialog(null, "Dados do animal alterados com sucesso!");;

            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível alterar os dados do animal.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Erro:", JOptionPane.ERROR);
        }
        return null;

    }
    
    private Animais getAnimais(ResultSet result) throws SQLException {
        Animais animal = new Animais();
        animal.setId(result.getLong("id"));
        animal.setDescricao(result.getString("descricao"));
        animal.setQuantidade(result.getString("quantidade"));
        animal.setIdade(result.getString("idade"));
       animal.setSexo(result.getString("sexo"));
        return animal;
    }
    
    
    
     public Animais buscarAnimaisDescricao(String descricao) {
        String sql = String.format("SELECT * FROM animais WHERE descricao = '%s'", descricao);
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            if (result.next()) {
                return getAnimais(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
