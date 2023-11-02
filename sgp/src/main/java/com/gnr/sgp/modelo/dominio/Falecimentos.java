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

/**
 * A classe Falecimentos representa informações sobre falecimentos de animais em
 * um sistema. Cada instância desta classe contém detalhes como ID do
 * falecimento, ID do animal, quantidade, observação, local e operador
 * responsável.
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
