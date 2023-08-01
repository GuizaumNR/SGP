/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dao;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dominio.VendasAnimais;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Guilherme
 */
public class VendasAnimaisDao {

    private final Conexao conexao;
    String operador = "Guilherme";

    public VendasAnimaisDao() {
        this.conexao = new ConexaoMysql();
    }

    public String Adicionar(VendasAnimais venda) {
        String sql = "INSERT INTO compras_animais (id_animal, quantidade, media_kg, preco_kg, valor_total, comprador, vendedor, local_venda, operador) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setInt(1, venda.getId_animal());
            pst.setInt(2, venda.getQuantidade());
            pst.setDouble(3, venda.getMedia_kg());
            pst.setDouble(4, venda.getPreco_peso());
            pst.setDouble(5, venda.getValor_total());
            pst.setString(6, venda.getComprador());
            pst.setString(7, venda.getVendedor());
            pst.setString(8, venda.getLocal());
            pst.setString(9, venda.getOperador());

            int resultado = pst.executeUpdate();

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Venda finalizada com sucesso!");

                String sqlUpdate = "UPDATE animais SET quantidade = quantidade - ? WHERE id = ?";
                try {
                    PreparedStatement pstmt = conexao.obterConexao().prepareStatement(sqlUpdate);
                    pstmt.setInt(1, venda.getQuantidade());
                    pstmt.setInt(2, venda.getId_animal());

                    int resultado2 = pstmt.executeUpdate();

                    if (resultado2 > 0) {
                        System.out.println("Quantidade de animais atualizada com sucesso!");
                    } else {
                        System.out.println("Não foi possível atualizar a quantidade de animais.");
                    }
                } catch (SQLException e) {
                    System.out.println("Erro ao atualizar a quantidade de animais: " + e.getMessage());
                }
//                TelaFornecedor.limpaCampos(null);
            } else {
                JOptionPane.showMessageDialog(null, "Não foi possível finalizar a venda.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar a venda.");
            e.printStackTrace();
        }

        return null;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }
}
