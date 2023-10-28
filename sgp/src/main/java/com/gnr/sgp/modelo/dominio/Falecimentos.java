/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dominio;

/**
 *
 * @author Guilherme
 */
public class Falecimentos {

    private long id_fale;
    private int id_animal;
    private int quantidade;
    private String observacao;
    private String local;
    private String operador;

    public Falecimentos() {
    }

    public Falecimentos(long id_fale, int id_animal, int quantidade, String observacao, String local, String operador) {
        this.id_fale = id_fale;
        this.id_animal = id_animal;
        this.quantidade = quantidade;
        this.observacao = observacao;
        this.local = local;
        this.operador = operador;
    }

    public long getId_fale() {
        return id_fale;
    }

    public void setId_nasc(long id_fale) {
        this.id_fale = id_fale;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
