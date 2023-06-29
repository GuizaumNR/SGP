/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.modelo.dominio;

import java.util.Objects;

/**
 *
 * @author Guilherme
 */
public class Usuarios {
    
    
    private Long id;
    private String login;
    private String senha;
    private String tipo;
    private String nome;

   

    public Usuarios() {
        this.tipo = "consulta"; 
    }
    
   
    public Usuarios(Long id, String login, String senha, String tipo, String nome) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
        this.nome = nome;
    }

     public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    } 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuarios other = (Usuarios) obj;
        return Objects.equals(this.id, other.id);
    }
    
    
}
