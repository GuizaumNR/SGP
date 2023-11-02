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
import com.gnr.sgp.modelo.dominio.ComprasAnimais;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Classe responsavel pela ligacao com a tabela comprasAnimais no banco de
 * dados.
 *
 * @author Guilherme
 */
public class ComprasAnimaisDao {

    private final Conexao conexao;

    public ComprasAnimaisDao() {
        this.conexao = new ConexaoMysql();
    }

    public String Adicionar(ComprasAnimais compra) {
        String sql = "INSERT INTO compras_animais (id_animal, quantidade, kg_totais, media_kg, preco_kg, valor_total, criador, porce_comissao, comissao, pagador, pagamento, operador) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Animais animalTemp = buscarAnimaisId(compra.getId_animal());

            if (animalTemp == null) {
                JOptionPane.showMessageDialog(null, "Erro: Este ID de animal não existe no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
                pst.setInt(1, compra.getId_animal());
                pst.setInt(2, compra.getQuantidade());
                pst.setDouble(3, compra.getKg_totais());
                pst.setDouble(4, compra.getMedia_kg());
                pst.setDouble(5, compra.getPreco_kg());
                pst.setDouble(6, compra.getValor_total());
                pst.setString(7, compra.getCriador());
                pst.setDouble(8, compra.getPorce_comissao());
                pst.setDouble(9, compra.getComissao());
                pst.setString(10, compra.getPagador());
                pst.setString(11, compra.getPagamento());
                pst.setString(12, compra.getOperador());

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
                            JOptionPane.showMessageDialog(null, "Não foi possível atualizar a quantidade de animais.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar a quantidade de animais: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
//                TelaFornecedor.limpaCampos(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível finalizar a compra.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar a compra.", "Erro", JOptionPane.ERROR_MESSAGE);
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
