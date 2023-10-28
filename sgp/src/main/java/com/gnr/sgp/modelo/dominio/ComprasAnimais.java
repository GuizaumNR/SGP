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

    private Long id_compra;
    private int id_animal;
    private int quantidade;
    private double kg_totais;
    private double media_kg;
    private double preco_kg;
    private double valor_total;
    private double porce_comissao;
    private double comissao;
    private String criador;
    private String pagador;
    private String pagamento;
    private String operador;

    public ComprasAnimais() {
    }

    public ComprasAnimais(Long id_compra, int id_animal, int quantidade, double kg_totais, double media_kg, double preco_kg, double valor_total, double porce_comissao, double comissao, String criador, String pagador, String pagamento, String operador) {
        this.id_compra = id_compra;
        this.id_animal = id_animal;
        this.quantidade = quantidade;
        this.kg_totais = kg_totais;
        this.media_kg = media_kg;
        this.preco_kg = preco_kg;
        this.valor_total = valor_total;
        this.porce_comissao = porce_comissao;
        this.comissao = comissao;
        this.criador = criador;
        this.pagador = pagador;
        this.pagamento = pagamento;
        this.operador = operador;
    }

    public Long getId_compra() {
        return id_compra;
    }

    public void setId_compra(Long id_compra) {
        this.id_compra = id_compra;
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

    public double getKg_totais() {
        return kg_totais;
    }

    public void setKg_totais(double kg_totais) {
        this.kg_totais = kg_totais;
    }

    public double getMedia_kg() {
        return media_kg;
    }

    public void setMedia_kg(double media_kg) {
        this.media_kg = media_kg;
    }

    public double getPreco_kg() {
        return preco_kg;
    }

    public void setPreco_kg(double preco_kg) {
        this.preco_kg = preco_kg;
    }

    public double getValor_total() {
        return valor_total;
    }

    public void setValor_total(double valor_total) {
        this.valor_total = valor_total;
    }

    public double getPorce_comissao() {
        return porce_comissao;
    }

    public void setPorce_comissao(double porce_comissao) {
        this.porce_comissao = porce_comissao;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    public String getCriador() {
        return criador;
    }

    public void setCriador(String criador) {
        this.criador = criador;
    }

    public String getPagador() {
        return pagador;
    }

    public void setPagador(String pagador) {
        this.pagador = pagador;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

}
