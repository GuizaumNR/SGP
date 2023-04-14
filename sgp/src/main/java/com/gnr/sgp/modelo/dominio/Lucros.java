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
public class Lucros {
    
    private Long id;
    private String descricao;
    private double valor;
    private Date data_lucro;

    public Lucros() {
    }

    public Lucros(Long id, String descricao, double valor, Date data_lucro) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data_lucro = data_lucro;
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

    public Date getData_lucro() {
        return data_lucro;
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

    public void setData_lucro(Date data_lucro) {
        this.data_lucro = data_lucro;
    }
    
    
}
