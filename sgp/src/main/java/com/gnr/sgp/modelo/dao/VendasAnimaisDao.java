/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dao;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dominio.VendasAnimais;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Guilherme
 */
public class VendasAnimaisDao {

    private final Conexao conexao;

    public VendasAnimaisDao() {
        this.conexao = new ConexaoMysql();
    }

    public String Adicionar(VendasAnimais venda) {
        String sql = "INSERT INTO vendas_animais (id_animal, quantidade, media_kg, preco_kg, valor_total, comprador, vendedor, pagamento, local_venda, operador) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (verificarQuantidadeMenorZero(venda.getId_animal(), venda.getQuantidade()) && venda.getQuantidade() > 0) {
            try {
                PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
                pst.setInt(1, venda.getId_animal());
                pst.setInt(2, venda.getQuantidade());
                pst.setDouble(3, venda.getMedia_kg());
                pst.setDouble(4, venda.getPreco_peso());
                pst.setDouble(5, venda.getValor_total());
                pst.setString(6, venda.getComprador());
                pst.setString(7, venda.getVendedor());
                 pst.setString(8, venda.getPagamento());
                pst.setString(9, venda.getLocal());
                pst.setString(10, venda.getOperador());

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
                            JOptionPane.showMessageDialog(null, "Quantidade de animais atualizada com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Não foi possível atualizar a quantidade de animais.");
                        }
                                
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar a quantidade de animais: " + e.getMessage());
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível finalizar a venda.");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar a venda.");
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Quantidade de animais insuficiente para esta operação.");
        }
        return null;
    }


    public boolean verificarQuantidadeMenorZero(int id_animal, int quantidadeVenda) {
        String sql = "SELECT quantidade FROM animais WHERE id = ?";
        try {
            PreparedStatement pst = conexao.obterConexao().prepareStatement(sql);
            pst.setInt(1, id_animal);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int quantidade = rs.getInt("quantidade") - quantidadeVenda;
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
