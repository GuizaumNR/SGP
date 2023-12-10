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
import com.gnr.sgp.modelo.dominio.Usuarios;
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
public class UsuariosDao {

    private final Conexao conexao;
    TelaUsuario telaUsu = new TelaUsuario();

    public UsuariosDao() {
        this.conexao = new ConexaoMysql();
    };

    public String adicionar(Usuarios usuario) {
        String sql = "INSERT INTO usuarios(login, senha, tipo, nome) VALUES(?, ?, ?, ?)";

        Usuarios usuarioTemp = buscarUsuariosLogin(usuario.getLogin());

        if (usuarioTemp != null) {
            JOptionPane.showMessageDialog(null, "Erro: Este login já existe no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {

            try {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);

                preencherValoresPreparedStatment(pst, usuario);

                int resultado = pst.executeUpdate();

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível adicionar o usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao cadastrar o usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        return null;
    }

    public String editar(Usuarios usuario) {
        String sql = "UPDATE usuarios SET senha = ?, tipo = ?, nome = ? WHERE login = ?";

        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, usuario.getSenha());
            pst.setString(2, usuario.getTipo());
            pst.setString(3, usuario.getNome());
            pst.setString(4, usuario.getLogin());

            int editado = pst.executeUpdate();
            if (editado > 0) {
                JOptionPane.showMessageDialog(null, "Dados do usuário alterados com sucesso!");

            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível alterar os dados do usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Erro:", JOptionPane.ERROR);
        }
        return null;

    }

    public String deletar(Usuarios usuario) {
        String sql = "DELETE FROM usuarios WHERE login = ?";

        Usuarios usuarioTemp = buscarUsuariosLogin(usuario.getLogin());
 JOptionPane.showMessageDialog(null,"Login " + usuario.getLogin());
       
        if (usuarioTemp == null) {
            JOptionPane.showMessageDialog(null, "Erro: Este login não existe no banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, usuario.getLogin());

            int deletado = pst.executeUpdate();
            if (deletado > 0) {
                JOptionPane.showMessageDialog(null, "Dados do usuário deletados com sucesso!");

            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível deletar os dados do usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Erro:", JOptionPane.ERROR);
        }
        return null;
    }

    private void preencherValoresPreparedStatment(PreparedStatement preparedStatement, Usuarios usuario) throws SQLException {

        preparedStatement.setString(1, usuario.getLogin());
        preparedStatement.setString(2, usuario.getSenha());
        preparedStatement.setString(3, usuario.getTipo());
        preparedStatement.setString(4, usuario.getNome());

        if (usuario.getId() != 0) {
            preparedStatement.setLong(6, usuario.getId());
        }
    }

    private Usuarios getUsuarios(ResultSet result) throws SQLException {
        Usuarios usuarios = new Usuarios();
        usuarios.setId(result.getLong("id"));
        usuarios.setLogin(result.getString("login"));
        usuarios.setSenha(result.getString("senha"));
        usuarios.setTipo(result.getString("tipo"));
        usuarios.setNome(result.getString("nome"));
        return usuarios;
    }

    public Usuarios buscarUsuariosLogin(String login) {
        String sql = String.format("SELECT * FROM usuarios WHERE login = '%s'", login);
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            if (result.next()) {
                return getUsuarios(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
