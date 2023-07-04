
package com.gnr.sgp.modelo.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexaoMysql implements Conexao {

    private static Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/dbinfox";
    private static final String USER = "root";
    private static final String PASSWORD = "guilherme2015";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
             JOptionPane.showMessageDialog(null,"Não foi possível encontrar o driver de conexão com o MySQL.");
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(null,"Não foi possível conectar ao banco de dados MySQL.");
        }
    }

    @Override
    public Connection obterConexao() throws SQLException {
        if (connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    
    public void fecharConexao() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
    }

}