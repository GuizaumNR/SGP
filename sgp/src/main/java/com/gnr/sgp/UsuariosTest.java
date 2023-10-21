/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gnr.sgp;

   import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class UsuariosTest {
	public static void main(String[]args) {
		JFrame tela = new JFrame("Programa");
		final JDesktopPane deska = new JDesktopPane();
		JMenuBar barra = new JMenuBar();
		JMenu opcoes = new JMenu("Opções");
		JMenuItem abreinterna = new JMenuItem("Abrir telinha interna");
	
		Icon imagem = new ImageIcon("fundo.png");
		JLabel lab = new JLabel();
		lab.setIcon(imagem);
		double alt = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double larg = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		
		int altura = (int) alt;
		int largura = (int) larg;
		
		lab.setBounds(0,0,largura,altura);
		
		deska.add(lab);
		
		abreinterna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame interna = new JInternalFrame("Tela menor", true, true, true, true);
				interna.setBounds(10,10,400,400);
				interna.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				deska.add(interna);
				interna.setVisible(true);
			}});
		
		
		opcoes.add(abreinterna);
		barra.add(opcoes);
		
		
		tela.getContentPane().add(deska);
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tela.setExtendedState(JFrame.MAXIMIZED_BOTH);
		tela.setJMenuBar(barra);
		tela.setVisible(true);
	}
}

