/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dominio;

/**
 *
 * @author Guilherme
 */
public class Animais {
    private long id;
    private String descricao;
    private String raca;
    private String idade;
    private String sexo;
    private Fornecedores id_fornecedores;

    public Animais() {
    }

    public Animais(long id, String descricao, String raca, String idade, String sexo, Fornecedores id_fornecedores) {
        this.id = id;
        this.descricao = descricao;
        this.raca = raca;
        this.idade = idade;
        this.sexo = sexo;
        this.id_fornecedores = id_fornecedores;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Fornecedores getId_fornecedores() {
        return id_fornecedores;
    }

    public void setId_fornecedores(Fornecedores id_fornecedores) {
        this.id_fornecedores = id_fornecedores;
    }
    
}
