/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dominio;

/**
 *
 * @author Guilherme
 */
public class VendasAnimais {
 
    private long id;
    private int id_animal;
    private int quantidade;
    private double media_kg;
    private double preco_peso;
    private double valor_total;
    private String comprador;
    private String vendedor;
    private String local;
    private String operador;

     public VendasAnimais() {
         
    }

    public VendasAnimais(long id, int id_animal, int quantidade, double media_kg, double preco_peso, double valor_total, String comprador, String vendedor, String local, String operador) {
        this.id = id;
        this.id_animal = id_animal;
        this.quantidade = quantidade;
        this.media_kg = media_kg;
        this.preco_peso = preco_peso;
        this.valor_total = valor_total;
        this.comprador = comprador;
        this.vendedor = vendedor;
        this.local = local;
        this.operador = operador;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getId_animal() {
        return id_animal;
    }

    public void setId_animal(int id_animal) {
        this.id_animal = id_animal;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getMedia_kg() {
        return media_kg;
    }

    public void setMedia_kg(double media_kg) {
        this.media_kg = media_kg;
    }

    public double getPreco_peso() {
        return preco_peso;
    }

    public void setPreco_peso(double preco_peso) {
        this.preco_peso = preco_peso;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    
    
}
