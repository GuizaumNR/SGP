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
import com.gnr.sgp.modelo.dominio.Despesas;
import com.gnr.sgp.view.formulario.TelaUsuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 * Classe responsavel pela ligacao com a tabela usuarios no banco de dados.
 *
 * @author Guilherme
 */
public class DespesasDao {

    private final Conexao conexao;
    TelaUsuario telaUsu = new TelaUsuario();

    public DespesasDao() {
        this.conexao = new ConexaoMysql();
    };

    public String adicionar(Despesas despesa) {
        String sql = "INSERT INTO despesas(descricao, valor, pagador, categoria, operador) VALUES(?, ?, ?, ?, ?)";

            try {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);

                preencherValoresPreparedStatment(pst, despesa);

                int resultado = pst.executeUpdate();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Despesa adicionada com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível adicionar a despesa.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao cadastrar a despesa.", "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        return null;

    }

    public String editar(Despesas despesa) {
        String sql = "UPDATE despesas SET descricao = ?, valor = ?, pagador = ?, categoria = ? WHERE id = ?";

        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, despesa.getDescricao());
            pst.setDouble(2, despesa.getValor());
            pst.setString(3, despesa.getPagador());
            pst.setString(4, despesa.getCategoria());
            pst.setLong(5, despesa.getId());

            int editado = pst.executeUpdate();
            if (editado > 0) {
                JOptionPane.showMessageDialog(null, "Dados da despesa alterados com sucesso!");

            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível alterar os dados da despesa.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Erro:", JOptionPane.ERROR);
        }
        return null;

    }

    public String deletar(Despesas despesa) {
        String sql = "DELETE FROM despesa WHERE id = ?";
        Despesas despesaTemp = pesquisarDespesaId(despesa.getId());

        if (despesaTemp == null) {
            JOptionPane.showMessageDialog(null, "Erro: Este id não existe no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setLong(1, despesa.getId());

            int deletado = pst.executeUpdate();
            if (deletado > 0) {
                JOptionPane.showMessageDialog(null, "Dados da despesa deletados com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível deletar os dados da despesa.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    

    private void preencherValoresPreparedStatment(PreparedStatement preparedStatement, Despesas despesa) throws SQLException {

        preparedStatement.setString(1, despesa.getDescricao());
        preparedStatement.setDouble(2, despesa.getValor());
        preparedStatement.setString(3, despesa.getPagador());
        preparedStatement.setString(4, despesa.getCategoria());
        preparedStatement.setString(5, despesa.getOperador());
    }

    private Despesas getDespesa(ResultSet result) throws SQLException {
        Despesas despesa = new Despesas();
        despesa.setId(result.getLong("id"));
        despesa.setDescricao(result.getString("descricao"));
        despesa.setValor(result.getLong("valor"));
        despesa.setPagador(result.getString("pagador"));
        despesa.setCategoria(result.getString("categoria"));
        despesa.setData_despesa(result.getString("data_despesa"));
        despesa.setOperador(result.getString("operador"));
        return despesa;
    }

    public Despesas buscarDespesaDescricao(String descricao) {
        String sql = String.format("SELECT * FROM despesas WHERE descricao = '%s'", descricao);
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            if (result.next()) {
                return getDespesa(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Despesas pesquisarDespesaId(Long id) {
        String sql = String.format("SELECT * FROM despesas WHERE id = '%s'", id);
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            if (result.next()) {
                return getDespesa(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
