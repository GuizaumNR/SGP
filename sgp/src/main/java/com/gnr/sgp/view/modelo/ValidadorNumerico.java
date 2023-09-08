/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp.view.modelo;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 *
 * @author Guilherme
 */
public class ValidadorNumerico extends PlainDocument{
    
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
       if (str != null) {
            // Verifica se a string contém apenas dígitos numéricos ou um único ponto decimal
            if (str.matches("[0-9]*\\.?[0-9]*")) {
                super.insertString(offs, str, a);
            }
        }
    }
}
