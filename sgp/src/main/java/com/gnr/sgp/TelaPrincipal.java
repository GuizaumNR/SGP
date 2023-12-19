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
package com.gnr.sgp;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.view.formulario.TelaAnimal;
import com.gnr.sgp.view.formulario.TelaCompra;
import com.gnr.sgp.view.formulario.TelaConsultaAnimal;
import com.gnr.sgp.view.formulario.TelaConsultaUsuario;
import com.gnr.sgp.view.formulario.TelaFalecimento;
import com.gnr.sgp.view.formulario.TelaFornecedor;
import com.gnr.sgp.view.formulario.TelaNascimento;
import com.gnr.sgp.view.formulario.TelaRelatorioCompra;
import com.gnr.sgp.view.formulario.TelaRelatorioEstoque;
import com.gnr.sgp.view.formulario.TelaRelatorioFalecimento;
import com.gnr.sgp.view.formulario.TelaRelatorioNascimento;
import com.gnr.sgp.view.formulario.TelaRelatorioVenda;
import com.gnr.sgp.view.formulario.TelaUsuario;
import com.gnr.sgp.view.formulario.TelaVenda;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Tela responsavel por gerenciar todas as telas.
 *
 * @author Guilherme
 */
public class TelaPrincipal extends javax.swing.JFrame {

    public static final String VERSION = "1.0.0";

    Date dataSistema = new Date();
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    Calendar now = Calendar.getInstance();

    TelaUsuario telaUsuario = new TelaUsuario();
    TelaFornecedor telaFornecedor = new TelaFornecedor();
    TelaAnimal telaAnimal = new TelaAnimal();
    TelaVenda telaVenda = new TelaVenda();
    TelaCompra telaCompra = new TelaCompra();
    TelaRelatorioVenda telaRelVenda = new TelaRelatorioVenda();
    TelaRelatorioCompra telaRelCompra = new TelaRelatorioCompra();
    TelaNascimento telaNasc = new TelaNascimento();
    TelaFalecimento telaFale = new TelaFalecimento();
    TelaRelatorioNascimento telaRelNasc = new TelaRelatorioNascimento();
    TelaRelatorioFalecimento telaRelFale = new TelaRelatorioFalecimento();
    TelaRelatorioEstoque telaRelEst = new TelaRelatorioEstoque();
    TelaConsultaAnimal telaConAni = new TelaConsultaAnimal();
    TelaConsultaUsuario telaConUsu = new TelaConsultaUsuario();

    Conexao conexao;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenWidth = (int) screenSize.getWidth();
    int screenHeight = (int) screenSize.getHeight();

    public TelaPrincipal() {

        this.conexao = new ConexaoMysql();

        setUndecorated(true);
        initComponents();
        setSize(screenWidth, screenHeight);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        requestFocus();
        labelVersao.setText(" Versão: " + VERSION);
        LabelAviso.setText("");
        labelSuporte.setText("Suporte: Guilherme - (53) 99912-8134");
        menu();
        atualizarDataHora();
        Timer timer = new Timer(1000, e -> atualizarDataHora());
        timer.start();

        addWindowFocusListener(new WindowFocusListener() {
            public void windowGainedFocus(WindowEvent e) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }

            public void windowLostFocus(WindowEvent e) {

            }
        });

        jMenuRelatorioManejo.setVisible(false);
        jMenuAjuda.setVisible(false);
        jMenuOpcoes.setVisible(false);
        jMenuConsultaManejo.setVisible(false);
        jMenuCadastroManejo.setVisible(false);
        jMenuCadastroFornecedor.setVisible(false);
        jMenuConsultaFornecedores.setVisible(false);

        jButtonSair.setFocusable(false);
        jButtonMinimizar.setFocusable(false);

        jDesktop.add(telaFornecedor);
        jDesktop.add(telaUsuario);
        jDesktop.add(telaAnimal);
        jDesktop.add(telaVenda);
        jDesktop.add(telaCompra);
        jDesktop.add(telaRelVenda);
        jDesktop.add(telaRelCompra);
        jDesktop.add(telaNasc);
        jDesktop.add(telaFale);
        jDesktop.add(telaRelNasc);
        jDesktop.add(telaRelFale);
        jDesktop.add(telaRelEst);
        jDesktop.add(telaConAni);
        jDesktop.add(telaConUsu);

        int largura = jDesktop.getWidth() - 2;
        int altura = jDesktop.getHeight() - 2;

        telaFornecedor.setSize(largura, altura);
        telaUsuario.setSize(largura, altura);
        telaAnimal.setSize(largura, altura);
        telaVenda.setSize(largura, altura);
        telaCompra.setSize(largura, altura);
        telaRelVenda.setSize(largura, altura);
        telaRelCompra.setSize(largura, altura);
        telaNasc.setSize(largura, altura);
        telaFale.setSize(largura, altura);
        telaRelNasc.setSize(largura, altura);
        telaRelFale.setSize(largura, altura);
        telaRelEst.setSize(largura, altura);
        telaConAni.setSize(largura, altura);
        telaConUsu.setSize(largura, altura);

    }

    public void setOperador(String operador) {
        labelOperador.setText("Operador: " + operador);
        telaCompra.setOperador(operador);
        telaVenda.setOperador(operador);
        telaNasc.setOperador(operador);
        telaFale.setOperador(operador);
    }

    public void setPermissao(Boolean permissao) {
        if (permissao) {

        } else {
            jMenuCadastro.setEnabled(false);
            jMenuCaixa.setEnabled(false);
            jMenuMovEstoque.setEnabled(false);
            jMenuRelatorio.setEnabled(false);
        }
    }

    public void menu() {

        jPanelTop.setLayout(new BorderLayout());

        jPanelTop.add(jPanelBotoes, BorderLayout.NORTH);
        jPanelTop.add(jMenuBar, BorderLayout.SOUTH);

    }

    private void atualizarDataHora() {

        now = Calendar.getInstance();
        labelDataEHora.setText(formato.format(dataSistema) + " " + String.format("%1$tH:%1$tM:%1$tS", now));
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar = new javax.swing.JMenuBar();
        jMenuCaixa = new javax.swing.JMenu();
        jMenuCaixaVenda = new javax.swing.JMenuItem();
        jMenuCaixaCompra = new javax.swing.JMenuItem();
        jMenuCadastro = new javax.swing.JMenu();
        jMenuCadastroAnimal = new javax.swing.JMenuItem();
        jMenuCadastroFornecedor = new javax.swing.JMenuItem();
        jMenuCadastroUsuario = new javax.swing.JMenuItem();
        jMenuCadastroManejo = new javax.swing.JMenuItem();
        jMenuConsulta = new javax.swing.JMenu();
        jMenuConsultaAnimais = new javax.swing.JMenuItem();
        jMenuConsultaFornecedores = new javax.swing.JMenuItem();
        jMenuConsultaUsuarios = new javax.swing.JMenuItem();
        jMenuConsultaManejo = new javax.swing.JMenuItem();
        jMenuMovEstoque = new javax.swing.JMenu();
        jMenuMovEstoqueNasc = new javax.swing.JMenuItem();
        jMenuMovEstoqueFale = new javax.swing.JMenuItem();
        jMenuRelatorio = new javax.swing.JMenu();
        jMenuRelatorioVendas = new javax.swing.JMenuItem();
        jMenuRelatorioCompras = new javax.swing.JMenuItem();
        jMenuRelatorioManejo = new javax.swing.JMenuItem();
        jMenuRelatorioNascimento = new javax.swing.JMenuItem();
        jMenuRelatorioFalecimento = new javax.swing.JMenuItem();
        jMenuItemEstoque = new javax.swing.JMenuItem();
        jMenuAjuda = new javax.swing.JMenu();
        jMenuAjudaSobre = new javax.swing.JMenuItem();
        jMenuOpcoes = new javax.swing.JMenu();
        painelTelaPrincipal = new javax.swing.JPanel();
        jPanelTop = new javax.swing.JPanel();
        jPanelBotoes = new javax.swing.JPanel();
        jButtonMinimizar = new javax.swing.JButton();
        jButtonSair = new javax.swing.JButton();
        jPanelDown = new javax.swing.JPanel();
        labelDataEHora = new javax.swing.JLabel();
        labelOperador = new javax.swing.JLabel();
        labelVersao = new javax.swing.JLabel();
        labelSuporte = new javax.swing.JLabel();
        jDesktop = new javax.swing.JDesktopPane();
        LabelAviso = new javax.swing.JLabel();

        jMenuBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jMenuBar.setBorderPainted(false);
        jMenuBar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenuBar.setInheritsPopupMenu(true);
        jMenuBar.setPreferredSize(new java.awt.Dimension(450, 50));

        jMenuCaixa.setText(" Caixa");
        jMenuCaixa.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jMenuCaixaVenda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuCaixaVenda.setText("Venda");
        jMenuCaixaVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCaixaVendaActionPerformed(evt);
            }
        });
        jMenuCaixa.add(jMenuCaixaVenda);

        jMenuCaixaCompra.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuCaixaCompra.setText("Compra");
        jMenuCaixaCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCaixaCompraActionPerformed(evt);
            }
        });
        jMenuCaixa.add(jMenuCaixaCompra);

        jMenuBar.add(jMenuCaixa);

        jMenuCadastro.setText(" Cadastro");
        jMenuCadastro.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jMenuCadastro.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                jMenuCadastroAncestorMoved(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jMenuCadastroAnimal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuCadastroAnimal.setText("Animais");
        jMenuCadastroAnimal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCadastroAnimalActionPerformed(evt);
            }
        });
        jMenuCadastro.add(jMenuCadastroAnimal);

        jMenuCadastroFornecedor.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuCadastroFornecedor.setText("Fornecedores");
        jMenuCadastroFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCadastroFornecedorActionPerformed(evt);
            }
        });
        jMenuCadastro.add(jMenuCadastroFornecedor);

        jMenuCadastroUsuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuCadastroUsuario.setText("Usuários");
        jMenuCadastroUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCadastroUsuarioActionPerformed(evt);
            }
        });
        jMenuCadastro.add(jMenuCadastroUsuario);

        jMenuCadastroManejo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuCadastroManejo.setText("Manejo");
        jMenuCadastroManejo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCadastroManejoActionPerformed(evt);
            }
        });
        jMenuCadastro.add(jMenuCadastroManejo);

        jMenuBar.add(jMenuCadastro);

        jMenuConsulta.setText("Consulta");
        jMenuConsulta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jMenuConsultaAnimais.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuConsultaAnimais.setText("Consulta Animais");
        jMenuConsultaAnimais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuConsultaAnimaisActionPerformed(evt);
            }
        });
        jMenuConsulta.add(jMenuConsultaAnimais);

        jMenuConsultaFornecedores.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuConsultaFornecedores.setText("Consulta Fornecedores");
        jMenuConsulta.add(jMenuConsultaFornecedores);

        jMenuConsultaUsuarios.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuConsultaUsuarios.setText("Consulta Usuários");
        jMenuConsultaUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuConsultaUsuariosActionPerformed(evt);
            }
        });
        jMenuConsulta.add(jMenuConsultaUsuarios);

        jMenuConsultaManejo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuConsultaManejo.setText("Consulta de Manejo");
        jMenuConsultaManejo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuConsultaManejoActionPerformed(evt);
            }
        });
        jMenuConsulta.add(jMenuConsultaManejo);

        jMenuBar.add(jMenuConsulta);

        jMenuMovEstoque.setText("Mov. Estoque");
        jMenuMovEstoque.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jMenuMovEstoqueNasc.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuMovEstoqueNasc.setText("Nascimento");
        jMenuMovEstoqueNasc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuMovEstoqueNascActionPerformed(evt);
            }
        });
        jMenuMovEstoque.add(jMenuMovEstoqueNasc);

        jMenuMovEstoqueFale.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuMovEstoqueFale.setText("Falecimento");
        jMenuMovEstoqueFale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuMovEstoqueFaleActionPerformed(evt);
            }
        });
        jMenuMovEstoque.add(jMenuMovEstoqueFale);

        jMenuBar.add(jMenuMovEstoque);

        jMenuRelatorio.setText("Relatório");
        jMenuRelatorio.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jMenuRelatorioVendas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuRelatorioVendas.setText("Relatório de Vendas");
        jMenuRelatorioVendas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuRelatorioVendasActionPerformed(evt);
            }
        });
        jMenuRelatorio.add(jMenuRelatorioVendas);

        jMenuRelatorioCompras.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuRelatorioCompras.setText("Relatório de Compras");
        jMenuRelatorioCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuRelatorioComprasActionPerformed(evt);
            }
        });
        jMenuRelatorio.add(jMenuRelatorioCompras);

        jMenuRelatorioManejo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuRelatorioManejo.setText("Relatório de Manejos");
        jMenuRelatorioManejo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuRelatorioManejoActionPerformed(evt);
            }
        });
        jMenuRelatorio.add(jMenuRelatorioManejo);

        jMenuRelatorioNascimento.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuRelatorioNascimento.setText("Relatório de Nascimentos");
        jMenuRelatorioNascimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuRelatorioNascimentoActionPerformed(evt);
            }
        });
        jMenuRelatorio.add(jMenuRelatorioNascimento);

        jMenuRelatorioFalecimento.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuRelatorioFalecimento.setText("Relatório de Falecimentos");
        jMenuRelatorioFalecimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuRelatorioFalecimentoActionPerformed(evt);
            }
        });
        jMenuRelatorio.add(jMenuRelatorioFalecimento);

        jMenuItemEstoque.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuItemEstoque.setText("Relatório de Estoque");
        jMenuItemEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEstoqueActionPerformed(evt);
            }
        });
        jMenuRelatorio.add(jMenuItemEstoque);

        jMenuBar.add(jMenuRelatorio);

        jMenuAjuda.setText("Ajuda");
        jMenuAjuda.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jMenuAjudaSobre.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuAjudaSobre.setText("Sobre");
        jMenuAjudaSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAjudaSobreActionPerformed(evt);
            }
        });
        jMenuAjuda.add(jMenuAjudaSobre);

        jMenuBar.add(jMenuAjuda);

        jMenuOpcoes.setText("Opções");
        jMenuOpcoes.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jMenuBar.add(jMenuOpcoes);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        painelTelaPrincipal.setBackground(new java.awt.Color(237, 249, 237));
        painelTelaPrincipal.setPreferredSize(new java.awt.Dimension(1080, 720));

        jPanelTop.setBackground(new java.awt.Color(198, 222, 198));

        jPanelBotoes.setBackground(new java.awt.Color(198, 222, 198));

        jButtonMinimizar.setBackground(new java.awt.Color(153, 153, 153));
        jButtonMinimizar.setFont(new java.awt.Font("Courier New", 1, 48)); // NOI18N
        jButtonMinimizar.setText("-");
        jButtonMinimizar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonMinimizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMinimizarActionPerformed(evt);
            }
        });

        jButtonSair.setBackground(new java.awt.Color(255, 51, 51));
        jButtonSair.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        jButtonSair.setText("X");
        jButtonSair.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButtonSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelBotoesLayout = new javax.swing.GroupLayout(jPanelBotoes);
        jPanelBotoes.setLayout(jPanelBotoesLayout);
        jPanelBotoesLayout.setHorizontalGroup(
            jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBotoesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonMinimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSair, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelBotoesLayout.setVerticalGroup(
            jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSair, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonMinimizar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelTopLayout = new javax.swing.GroupLayout(jPanelTop);
        jPanelTop.setLayout(jPanelTopLayout);
        jPanelTopLayout.setHorizontalGroup(
            jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelBotoes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelTopLayout.setVerticalGroup(
            jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTopLayout.createSequentialGroup()
                .addComponent(jPanelBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jPanelDown.setBackground(new java.awt.Color(198, 222, 198));

        labelDataEHora.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        labelOperador.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        labelVersao.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        labelSuporte.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanelDownLayout = new javax.swing.GroupLayout(jPanelDown);
        jPanelDown.setLayout(jPanelDownLayout);
        jPanelDownLayout.setHorizontalGroup(
            jPanelDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDownLayout.createSequentialGroup()
                .addComponent(labelVersao, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addGap(18, 19, Short.MAX_VALUE)
                .addComponent(labelSuporte, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(labelOperador, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addGap(18, 19, Short.MAX_VALUE)
                .addComponent(labelDataEHora, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelDownLayout.setVerticalGroup(
            jPanelDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanelDownLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelDownLayout.createSequentialGroup()
                        .addGroup(jPanelDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelOperador, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelDataEHora, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelDownLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanelDownLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelSuporte, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelVersao, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jDesktop.setBackground(new java.awt.Color(237, 249, 237));
        if (screenWidth <= 1366) {
            jDesktop.setPreferredSize(new java.awt.Dimension(820, 620));
        }
        jDesktop.setMinimumSize(new java.awt.Dimension(640, 480));
        jDesktop.setPreferredSize(new java.awt.Dimension(730, 545));

        javax.swing.GroupLayout jDesktopLayout = new javax.swing.GroupLayout(jDesktop);
        jDesktop.setLayout(jDesktopLayout);
        jDesktopLayout.setHorizontalGroup(
            jDesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jDesktopLayout.setVerticalGroup(
            jDesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 545, Short.MAX_VALUE)
        );

        LabelAviso.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout painelTelaPrincipalLayout = new javax.swing.GroupLayout(painelTelaPrincipal);
        painelTelaPrincipal.setLayout(painelTelaPrincipalLayout);
        painelTelaPrincipalLayout.setHorizontalGroup(
            painelTelaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(painelTelaPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelTelaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelAviso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDesktop, javax.swing.GroupLayout.DEFAULT_SIZE, 1068, Short.MAX_VALUE))
                .addContainerGap())
        );
        painelTelaPrincipalLayout.setVerticalGroup(
            painelTelaPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelTelaPrincipalLayout.createSequentialGroup()
                .addComponent(jPanelTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelAviso, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDesktop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelTelaPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelTelaPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuCadastroAnimalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCadastroAnimalActionPerformed
        if (!telaUsuario.isVisible() && !telaFornecedor.isVisible() && !telaVenda.isVisible() && !telaCompra.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {
            telaAnimal.setVisible(true);
        } else {
            telaUsuario.setVisible(false);
            telaFornecedor.setVisible(false);
            telaVenda.setVisible(false);
            telaCompra.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaAnimal.setVisible(true);
        }
    }//GEN-LAST:event_jMenuCadastroAnimalActionPerformed

    private void jMenuCadastroFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCadastroFornecedorActionPerformed

        if (!telaUsuario.isVisible() && !telaAnimal.isVisible() && !telaVenda.isVisible() && !telaCompra.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {
            telaFornecedor.setVisible(true);

        } else {
            telaUsuario.setVisible(false);
            telaAnimal.setVisible(false);
            telaVenda.setVisible(false);
            telaCompra.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaFornecedor.setVisible(true);

        }
    }//GEN-LAST:event_jMenuCadastroFornecedorActionPerformed

    private void jMenuCadastroAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jMenuCadastroAncestorMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuCadastroAncestorMoved

    private void jMenuConsultaAnimaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuConsultaAnimaisActionPerformed
        if (!telaUsuario.isVisible() && !telaAnimal.isVisible() && !telaVenda.isVisible() && !telaCompra.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaFornecedor.isVisible() && !telaConUsu.isVisible()  && !telaRelEst.isVisible()) {
            telaConAni.setVisible(true);

        } else {
            telaUsuario.setVisible(false);
            telaAnimal.setVisible(false);
            telaVenda.setVisible(false);
            telaCompra.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaFornecedor.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaConAni.setVisible(true);

        }

    }//GEN-LAST:event_jMenuConsultaAnimaisActionPerformed

    private void jMenuConsultaUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuConsultaUsuariosActionPerformed
        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaVenda.isVisible() && !telaCompra.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaUsuario.isVisible() && !telaRelEst.isVisible()) {
            telaConUsu.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaVenda.setVisible(false);
            telaCompra.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaUsuario.setVisible(false);
            telaRelEst.setVisible(false);
            telaConUsu.setVisible(true);

        }
    }//GEN-LAST:event_jMenuConsultaUsuariosActionPerformed

    private void jMenuAjudaSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAjudaSobreActionPerformed

    }//GEN-LAST:event_jMenuAjudaSobreActionPerformed

    private void jButtonSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSairActionPerformed
        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair?", "Sair do sistema", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jButtonSairActionPerformed

    private void jButtonMinimizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMinimizarActionPerformed
        setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_jButtonMinimizarActionPerformed

    private void jMenuCadastroUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCadastroUsuarioActionPerformed
        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaVenda.isVisible() && !telaCompra.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {
            telaUsuario.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaVenda.setVisible(false);
            telaCompra.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaUsuario.setVisible(true);

        }
    }//GEN-LAST:event_jMenuCadastroUsuarioActionPerformed

    private void jMenuCadastroManejoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCadastroManejoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuCadastroManejoActionPerformed

    private void jMenuRelatorioManejoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuRelatorioManejoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuRelatorioManejoActionPerformed

    private void jMenuConsultaManejoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuConsultaManejoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuConsultaManejoActionPerformed

    private void jMenuRelatorioComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuRelatorioComprasActionPerformed

        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaUsuario.isVisible() && !telaVenda.isVisible() && !telaRelVenda.isVisible() && !telaCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {
            telaRelCompra.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaUsuario.setVisible(false);
            telaVenda.setVisible(false);
            telaRelVenda.setVisible(false);
            telaCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaRelCompra.setVisible(true);
        }

    }//GEN-LAST:event_jMenuRelatorioComprasActionPerformed

    private void jMenuRelatorioVendasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuRelatorioVendasActionPerformed
        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaUsuario.isVisible() && !telaCompra.isVisible() && !telaVenda.isVisible() && !telaRelCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible()  && !telaRelEst.isVisible()) {
            telaRelVenda.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaUsuario.setVisible(false);
            telaCompra.setVisible(false);
            telaVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaRelVenda.setVisible(true);

        }
    }//GEN-LAST:event_jMenuRelatorioVendasActionPerformed

    private void jMenuCaixaVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCaixaVendaActionPerformed
        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaUsuario.isVisible() && !telaCompra.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {
            telaVenda.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaUsuario.setVisible(false);
            telaCompra.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaVenda.setVisible(true);

        }
    }//GEN-LAST:event_jMenuCaixaVendaActionPerformed

    private void jMenuCaixaCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCaixaCompraActionPerformed
        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaUsuario.isVisible() && !telaVenda.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {

            telaCompra.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaUsuario.setVisible(false);
            telaVenda.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaCompra.setVisible(true);
        }
    }//GEN-LAST:event_jMenuCaixaCompraActionPerformed

    private void jMenuMovEstoqueNascActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuMovEstoqueNascActionPerformed

        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaUsuario.isVisible() && !telaVenda.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaCompra.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {

            telaNasc.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaUsuario.setVisible(false);
            telaVenda.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaCompra.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaNasc.setVisible(true);
        }

    }//GEN-LAST:event_jMenuMovEstoqueNascActionPerformed

    private void jMenuMovEstoqueFaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuMovEstoqueFaleActionPerformed
        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaUsuario.isVisible() && !telaVenda.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaCompra.isVisible() && !telaNasc.isVisible() && !telaRelNasc.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {

            telaFale.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaUsuario.setVisible(false);
            telaVenda.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaRelNasc.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaFale.setVisible(true);
        }
    }//GEN-LAST:event_jMenuMovEstoqueFaleActionPerformed

    private void jMenuRelatorioNascimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuRelatorioNascimentoActionPerformed
        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaUsuario.isVisible() && !telaVenda.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelFale.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {

            telaRelNasc.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaUsuario.setVisible(false);
            telaVenda.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelFale.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaRelNasc.setVisible(true);
        }
    }//GEN-LAST:event_jMenuRelatorioNascimentoActionPerformed

    private void jMenuRelatorioFalecimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuRelatorioFalecimentoActionPerformed
        if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaUsuario.isVisible() && !telaVenda.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelEst.isVisible()) {

            telaRelFale.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaUsuario.setVisible(false);
            telaVenda.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);
            telaRelEst.setVisible(false);
            telaRelFale.setVisible(true);
        }
    }//GEN-LAST:event_jMenuRelatorioFalecimentoActionPerformed

    private void jMenuItemEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEstoqueActionPerformed
       if (!telaFornecedor.isVisible() && !telaAnimal.isVisible() && !telaUsuario.isVisible() && !telaVenda.isVisible() && !telaRelVenda.isVisible() && !telaRelCompra.isVisible() && !telaCompra.isVisible() && !telaNasc.isVisible() && !telaFale.isVisible() && !telaRelNasc.isVisible() && !telaConAni.isVisible() && !telaConUsu.isVisible() && !telaRelFale.isVisible()) {

            telaRelEst.setVisible(true);

        } else {
            telaFornecedor.setVisible(false);
            telaAnimal.setVisible(false);
            telaUsuario.setVisible(false);
            telaVenda.setVisible(false);
            telaRelVenda.setVisible(false);
            telaRelCompra.setVisible(false);
            telaCompra.setVisible(false);
            telaNasc.setVisible(false);
            telaFale.setVisible(false);
            telaRelNasc.setVisible(false);
            telaConAni.setVisible(false);
            telaConUsu.setVisible(false);     
            telaRelFale.setVisible(false);
            telaRelEst.setVisible(true);
        }
        
    }//GEN-LAST:event_jMenuItemEstoqueActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelAviso;
    private javax.swing.JButton jButtonMinimizar;
    private javax.swing.JButton jButtonSair;
    private javax.swing.JDesktopPane jDesktop;
    private javax.swing.JMenu jMenuAjuda;
    private javax.swing.JMenuItem jMenuAjudaSobre;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuCadastro;
    private javax.swing.JMenuItem jMenuCadastroAnimal;
    private javax.swing.JMenuItem jMenuCadastroFornecedor;
    private javax.swing.JMenuItem jMenuCadastroManejo;
    private javax.swing.JMenuItem jMenuCadastroUsuario;
    private javax.swing.JMenu jMenuCaixa;
    private javax.swing.JMenuItem jMenuCaixaCompra;
    private javax.swing.JMenuItem jMenuCaixaVenda;
    private javax.swing.JMenu jMenuConsulta;
    private javax.swing.JMenuItem jMenuConsultaAnimais;
    private javax.swing.JMenuItem jMenuConsultaFornecedores;
    private javax.swing.JMenuItem jMenuConsultaManejo;
    private javax.swing.JMenuItem jMenuConsultaUsuarios;
    private javax.swing.JMenuItem jMenuItemEstoque;
    private javax.swing.JMenu jMenuMovEstoque;
    private javax.swing.JMenuItem jMenuMovEstoqueFale;
    private javax.swing.JMenuItem jMenuMovEstoqueNasc;
    private javax.swing.JMenu jMenuOpcoes;
    private javax.swing.JMenu jMenuRelatorio;
    private javax.swing.JMenuItem jMenuRelatorioCompras;
    private javax.swing.JMenuItem jMenuRelatorioFalecimento;
    private javax.swing.JMenuItem jMenuRelatorioManejo;
    private javax.swing.JMenuItem jMenuRelatorioNascimento;
    private javax.swing.JMenuItem jMenuRelatorioVendas;
    private javax.swing.JPanel jPanelBotoes;
    private javax.swing.JPanel jPanelDown;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JLabel labelDataEHora;
    private javax.swing.JLabel labelOperador;
    private javax.swing.JLabel labelSuporte;
    private javax.swing.JLabel labelVersao;
    private javax.swing.JPanel painelTelaPrincipal;
    // End of variables declaration//GEN-END:variables

}
