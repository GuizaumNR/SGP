/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
                            JOptionPane.showMessageDialog(null, "Não foi possível atualizar a quantidade de animais.");
                        }
                                
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar a quantidade de animais: " + e.getMessage());
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Não foi possível finalizar o falecimento.");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Ocorreu um erro ao finalizar o falecimento.");
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Quantidade de animais insuficiente para esta operação.");
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
