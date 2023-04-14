/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dominio;

/**
 *
 * @author Guilherme
 */
public class ComprasAnimais {
    
    private Animais id_animal;
    private int quantidade;
    private Fornecedores id_fornecedor;
    private double valor_peso;
    private double valor_total;

    public ComprasAnimais() {
    }

    public ComprasAnimais( Animais id_animal, int quantidade, Fornecedores id_fornecedor, double valor_peso, double valor_total) {
        this.id_animal = id_animal;
        this.quantidade = quantidade;
        this.id_fornecedor = id_fornecedor;
        this.valor_peso = valor_peso;
        this.valor_total = valor_total;
    }

    public Animais getId_animal() {
        return id_animal;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Fornecedores getId_fornecedor() {
        return id_fornecedor;
    }

    public double getValor_peso() {
        return valor_peso;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setId_animal(Animais id_animal) {
        this.id_animal = id_animal;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setId_fornecedor(Fornecedores id_fornecedor) {
        this.id_fornecedor = id_fornecedor;
    }

    public void setValor_peso(double valor_peso) {
        this.valor_peso = valor_peso;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }
    
    
}
