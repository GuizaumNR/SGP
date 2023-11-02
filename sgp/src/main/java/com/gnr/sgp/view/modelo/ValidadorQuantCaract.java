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

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;

/**
 * A classe ValidadorQuantCaract estende PlainDocument e é usada para limitar o
 * número de caracteres inseridos em um componente de texto.
 *
 * @author Guilherme
 */
public class ValidadorQuantCaract extends PlainDocument {

    private int limite;

    public ValidadorQuantCaract(int limite) {
        super();
        this.limite = limite;
    }

    public void insertString(int ofs, String str, AttributeSet a) throws BadLocationException {
        if ((getLength() + str.length() <= limite)) {
            super.insertString(ofs, str, a);
        }

    }
}
