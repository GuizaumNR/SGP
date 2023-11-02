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
import com.gnr.sgp.modelo.dominio.Falecimentos;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Classe responsavel pela ligacao com a tabela falecimentos no banco de dados.
 *
 * @author Guilherme
 */
public class FalecimentosDao {

    private final Conexao conexao;

    public FalecimentosDao() {
        this.conexao = new ConexaoMysql();
    }

    public String Adicionar(Falecimentos fale) {
        String sql = "INSERT INTO falecimentos (id_animal, quantidade, observacao, local_falecimento, operador) VALUES( ?, ?, ?, ?, ?)";

        if (verificarQuantidadeMenorZero(fale.getId_animal(), fale.getQuantidade()) && fale.getQuantidade() > 0) {
            try {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
                pst.setInt(1, fale.getId_animal());
                pst.setInt(2, fale.getQuantidade());
                pst.setString(3, fale.getObservacao());
                pst.setString(4, fale.getLocal());
                pst.setString(5, fale.getOperador());

                int resultado = pst.executeUpdate();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Falecimento registrado com sucesso!");

                    String sqlUpdate = "UPDATE animais SET quantidade = quantidade - ? WHERE id = ?";
                    try {
                        PreparedStatement pstmt = conexao.obterConexao().prepareStatement(sqlUpdate);
                        pstmt.setInt(1, fale.getQuantidade());
                        pstmt.setInt(2, fale.getId_animal());

                        int resultado2 = pstmt.executeUpdate();

                        if (resultado2 > 0) {
                            JOptionPane.showMessageDialog(null, "Quantidade de animais atualizada com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Não foi possível atualizar a quantidade de animais.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar a quantidade de animais: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível finalizar o falecimento.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar o falecimento.", "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Quantidade de animais insuficiente para esta operação.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public boolean verificarQuantidadeMenorZero(int id_animal, int quantidadeFalecimento) {
        String sql = "SELECT quantidade FROM animais WHERE id = ?";
        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setInt(1, id_animal);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int quantidade = rs.getInt("quantidade") - quantidadeFalecimento;
                return quantidade >= 0;
            } else {
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar quantidade de animais: " + e.getMessage());
            return false;
        }
    }

}
