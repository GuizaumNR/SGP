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

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dominio.Animais;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Classe responsavel pela ligacao com a tabela animais no banco de dados.
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

        Animais animalTemp = buscarAnimaisId(animal.getId());
        if (animalTemp != null) {
            JOptionPane.showMessageDialog(null, "Erro: Este id já existe no banco de dados.");
        } else {

            try {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
                pst.setString(1, animal.getDescricao());
                pst.setString(2, animal.getQuantidade());
                pst.setString(3, animal.getIdade());
                pst.setString(4, animal.getSexo());

                int resultado = pst.executeUpdate();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Animal adicionado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível adicionar o animal.");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao cadastrar o animal.");
                e.printStackTrace();
            }
        }
        return null;
    }

    public String deletar(Animais animal) {
        String sql = "DELETE FROM animais WHERE id = ?";
        Animais animalTemp = buscarAnimaisId(animal.getId());

        if (animalTemp == null) {
            JOptionPane.showMessageDialog(null, "Erro: Este id não existe no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            String deleteComprasAnimaisSQL = "DELETE FROM compras_animais WHERE id_animal = ?";
            String deleteVendasAnimaisSQL = "DELETE FROM vendas_animais WHERE id_animal = ?";
            String deleteNascimentosAnimaisSQL = "DELETE FROM nascimentos WHERE id_animal = ?";
            String deleteFalecimentosAnimaisSQL = "DELETE FROM falecimentos WHERE id_animal = ?";

            PreparedStatement pstComprasAnimais = conexao.obterConexao().prepareStatement(deleteComprasAnimaisSQL);
            pstComprasAnimais.setInt(1, (int) animalTemp.getId());

            PreparedStatement pstVendasAnimais = conexao.obterConexao().prepareStatement(deleteVendasAnimaisSQL);
            pstVendasAnimais.setInt(1, (int) animalTemp.getId());

            PreparedStatement pstNascimentosAnimais = conexao.obterConexao().prepareStatement(deleteNascimentosAnimaisSQL);
            pstNascimentosAnimais.setInt(1, (int) animalTemp.getId());

            PreparedStatement pstFalecimentosAnimais = conexao.obterConexao().prepareStatement(deleteFalecimentosAnimaisSQL);
            pstFalecimentosAnimais.setInt(1, (int) animalTemp.getId());

            pstComprasAnimais.executeUpdate();
            pstVendasAnimais.executeUpdate();
            pstNascimentosAnimais.executeUpdate();
            pstFalecimentosAnimais.executeUpdate();

            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setLong(1, animal.getId());

            int deletado = pst.executeUpdate();
            if (deletado > 0) {
                JOptionPane.showMessageDialog(null, "Dados do animal deletados com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível deletar os dados do animal.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    public String editar(Animais animal) {
        String sql = "UPDATE animais SET quantidade = ?, idade = ?, sexo = ? WHERE id = ?";

        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, animal.getQuantidade());
            pst.setString(2, animal.getIdade());
            pst.setString(3, animal.getSexo());
            pst.setLong(4, animal.getId());

            int editado = pst.executeUpdate();
            if (editado > 0) {
                JOptionPane.showMessageDialog(null, "Dados do animal alterados com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível alterar os dados do animal.", "Erro", JOptionPane.ERROR_MESSAGE);
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

    public Animais buscarAnimaisId(Long id) {
        String sql = String.format("SELECT * FROM animais WHERE id = '%s'", id);
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
