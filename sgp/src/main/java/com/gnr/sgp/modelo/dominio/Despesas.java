/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dominio;

import java.util.Date;

/**
 *
 * @author Guilherme
 */
public class Despesas {
      private Long id;
    private String descricao;
    private double valor;
    private Date data_despesa;

    public Despesas() {
    }

    public Despesas(Long id, String descricao, double valor, Date data_despesa) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data_despesa = data_despesa;
    }

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public Date getData_despesa() {
        return data_despesa;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setData_despesa(Date data_despesa) {
        this.data_despesa = data_despesa;
    }
    
    
}
