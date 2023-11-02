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
package com.gnr.sgp.view.modelo;

import javax.swing.text.*;

/**
 * A classe ValidadorNumerico estende PlainDocument e é usada para validar a
 * entrada de texto, permitindo apenas dígitos numéricos ou um único ponto
 * decimal.
 *
 * @author Guilherme
 */
public class ValidadorNumerico extends PlainDocument {

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str != null) {
            // Verifica se a string contém apenas dígitos numéricos ou um único ponto decimal
            if (str.matches("[0-9]*\\.?[0-9]*")) {
                super.insertString(offs, str, a);
            }
        }
    }
}
