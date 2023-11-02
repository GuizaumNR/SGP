/*
 * The MIT License
 *
 * Copyright 2023 Guilherme Rodrigues.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.gnr.sgp.modelo.dominio;

import java.util.Date;

/**
 * A classe Despesas representa informações sobre despesas em um sistema. Cada
 * instância desta classe contém detalhes como ID da despesa, descrição, valor e
 * data da despesa.
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
