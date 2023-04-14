/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dominio;

/**
 *
 * @author Guilherme
 */
public class Transacoes {
    
   private Long id;
   private String tipo_transacao;
   private int quantidade;
   private double valor_unitario;
   private Animais id_animais;
   private Fornecedores id_fornecedores;

    public Transacoes() {
    }

    public Transacoes(Long id, String tipo_transacao, int quantidade, double valor_unitario, Animais id_animais, Fornecedores id_fornecedores) {
        this.id = id;
        this.tipo_transacao = tipo_transacao;
        this.quantidade = quantidade;
        this.valor_unitario = valor_unitario;
        this.id_animais = id_animais;
        this.id_fornecedores = id_fornecedores;
    }

    public Long getId() {
        return id;
    }

    public String getTipo_transacao() {
        return tipo_transacao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValor_unitario() {
        return valor_unitario;
    }

    public Animais getId_animais() {
        return id_animais;
    }

    public Fornecedores getId_fornecedores() {
        return id_fornecedores;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipo_transacao(String tipo_transacao) {
        this.tipo_transacao = tipo_transacao;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValor_unitario(double valor_unitario) {
        this.valor_unitario = valor_unitario;
    }

    public void setId_animais(Animais id_animais) {
        this.id_animais = id_animais;
    }

    public void setId_fornecedores(Fornecedores id_fornecedores) {
        this.id_fornecedores = id_fornecedores;
    }
   
    
    
}
