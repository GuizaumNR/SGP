/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dao;
import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dominio.Usuarios;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Guilherme
 */

public class UsuariosDao {

    private final Conexao conexao;

    public UsuariosDao() {
        this.conexao = new ConexaoMysql();
    }

    public String salvar(Usuarios usuario) {
        if (usuario.getId() == 0) {
            return adicionar(usuario);
        } else {
            return editar(usuario);
        }
    }

    ;

    private String adicionar(Usuarios usuario) {
        String sql = "INSERT INTO usuarios(login, senha, tipo) VALUES(?, ?, ?)";

        Usuarios usuarioTemp = buscarUsuariosLogin(usuario.getLogin());

        if (usuarioTemp != null) {
            return String.format("Erro: esse login %s já existe no banco de dados.", usuario.getLogin());
        }

        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);

            preencherValoresPreparedStatment(preparedStatement, usuario);

            int resultado = preparedStatement.executeUpdate();

            if (resultado == 1) {
                return "Usuário adicionado com sucesso!";
            } else {
                return "Não foi possível adicionar o usuário.";
            }

        } catch (SQLException e) {
            return String.format("Error: %s", e.getMessage());
        }

    }

    private String editar(Usuarios usuario) {
        String sql = "UPDATE usuarios SET login = ?, senha = ?, tipo = ? WHERE id = ? ";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);

            preencherValoresPreparedStatment(preparedStatement, usuario);

            int resultado = preparedStatement.executeUpdate();

            if (resultado == 1) {
                return "Usuário editado com sucesso!";
            } else {
                return "Não foi possível editar o usuário.";
            }

        } catch (SQLException e) {
            return String.format("Error: %s", e.getMessage());
        }
    }

    private void preencherValoresPreparedStatment(PreparedStatement preparedStatement, Usuarios usuario) throws SQLException {

        BCryptPasswordEncoder cryp = new BCryptPasswordEncoder();
        
        preparedStatement.setString(1, usuario.getLogin());
        preparedStatement.setString(2, usuario.getSenha());
        preparedStatement.setString(3, usuario.getTipo());

        if (usuario.getId() != 0) {
            preparedStatement.setLong(6, usuario.getId());
        }
    }

    public List<Usuarios> buscarTodosUsuarios() {
        String sql = "SELECT FROM * usuarios";
        List<Usuarios> usuarios = new ArrayList<>();

        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

            while (result.next()) {
                usuarios.add(getUsuarios(result));

            }
        } catch (Exception e) {
        }

        return usuarios;
    }

    private Usuarios getUsuarios(ResultSet result) throws SQLException {
        Usuarios usuarios = new Usuarios();
        usuarios.setId(result.getLong("id"));
        usuarios.setLogin(result.getString("login"));
        usuarios.setSenha(result.getString("senha"));
        usuarios.setTipo(result.getString("tipo"));

        return usuarios;
    }

    public Usuarios buscarUsuariosId(Long id) {
        String sql = String.format("SELECT * FROM usuarios WHERE id = %s", id);
        List<Usuarios> usuarios = new ArrayList<>();

        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

            if (result.next()) {
                return getUsuarios(result);

            }
        } catch (Exception e) {
        }

        return null;
    }

    public Usuarios buscarUsuariosLogin(String login) {
        String sql = String.format("SELECT * FROM usuarios WHERE login = %s", login);
        List<Usuarios> usuarios = new ArrayList<>();

        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

            if (result.next()) {
                return getUsuarios(result);

            }
        } catch (Exception e) {
        }

        return null;
    }

}
