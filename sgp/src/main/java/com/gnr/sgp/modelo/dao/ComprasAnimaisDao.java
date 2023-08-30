/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dao;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dominio.Animais;
import com.gnr.sgp.modelo.dominio.ComprasAnimais;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Guilherme
 */
public class ComprasAnimaisDao {

    private final Conexao conexao;

    public ComprasAnimaisDao() {
        this.conexao = new ConexaoMysql();
    }

    public String Adicionar(ComprasAnimais compra) {
        String sql = "INSERT INTO compras_animais (id_animal, quantidade, media_kg, preco_kg, valor_total, criador, pagador, pagamento, local_compra, operador) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Animais animalTemp = buscarAnimaisId(compra.getId_animal());

            if (animalTemp == null) {
                JOptionPane.showMessageDialog(null, "Erro: Este ID de animal não existe no banco de dados.");
            } else {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
                pst.setInt(1, compra.getId_animal());
                pst.setInt(2, compra.getQuantidade());
                pst.setDouble(3, compra.getMedia_kg());
                pst.setDouble(4, compra.getPreco_kg());
                pst.setDouble(5, compra.getValor_total());
                pst.setString(6, compra.getCriador());
                pst.setString(7, compra.getPagador());
                pst.setString(8, compra.getPagamento());
                pst.setString(9, compra.getLocal());
                pst.setString(10, compra.getOperador());

                int resultado = pst.executeUpdate();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Compra finalizada com sucesso!");

                    String sqlUpdate = "UPDATE animais SET quantidade = quantidade + ? WHERE id = ?";
                    try {
                        PreparedStatement pstmt = conexao.obterConexao().prepareStatement(sqlUpdate);
                        pstmt.setInt(1, compra.getQuantidade());
                        pstmt.setInt(2, compra.getId_animal());

                        int resultado2 = pstmt.executeUpdate();

                        if (resultado2 > 0) {
                             JOptionPane.showMessageDialog(null, "Quantidade de animais atualizada com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Não foi possível atualizar a quantidade de animais.");
                        }
                    } catch (SQLException e) {
                         JOptionPane.showMessageDialog(null, "Erro ao atualizar a quantidade de animais: " + e.getMessage());
                    }
//                TelaFornecedor.limpaCampos(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível finalizar a compra.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar a compra.");
            e.printStackTrace();
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

    public Animais buscarAnimaisId(int id) {
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
