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
import com.gnr.sgp.modelo.dominio.Nascimentos;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Classe responsavel pela ligacao com a tabela nascimentos no banco de dados.
 *
 * @author Guilherme
 */
public class NascimentosDao {

    private final Conexao conexao;

    public NascimentosDao() {
        this.conexao = new ConexaoMysql();
    }

    public String Adicionar(Nascimentos nasc) {
        String sql = "INSERT INTO nascimentos (id_animal, quantidade, observacao, local_nasc, operador) VALUES( ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setInt(1, nasc.getId_animal());
            pst.setInt(2, nasc.getQuantidade());
            pst.setString(3, nasc.getObservacao());
            pst.setString(4, nasc.getLocal());
            pst.setString(5, nasc.getOperador());

            int resultado = pst.executeUpdate();

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Nascimento registrado com sucesso!");

                String sqlUpdate = "UPDATE animais SET quantidade = quantidade + ? WHERE id = ?";
                try {
                    PreparedStatement pstmt = conexao.obterConexao().prepareStatement(sqlUpdate);
                    pstmt.setInt(1, nasc.getQuantidade());
                    pstmt.setInt(2, nasc.getId_animal());

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
                JOptionPane.showMessageDialog(null, "Não foi possível registrar o nascimento.", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao registrar o nascimento.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return null;
    }

}
