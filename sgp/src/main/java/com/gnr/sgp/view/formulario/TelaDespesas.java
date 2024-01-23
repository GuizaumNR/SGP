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
package com.gnr.sgp.view.formulario;

/**
 * Tela responsavel pelos usuarios.
 * @author Guilherme
 */
import com.gnr.sgp.modelo.conexao.Conexao;
import java.sql.*;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dao.DespesasDao;
import com.gnr.sgp.modelo.dominio.Despesas;
import com.gnr.sgp.view.modelo.ValidadorNumerico;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaDespesas extends javax.swing.JInternalFrame {

    Conexao conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String operador;

    ValidadorNumerico validaNumeros = new ValidadorNumerico();
    
    public TelaDespesas() {

        initComponents();
        conexao = new ConexaoMysql();

        setLocation(-5, -5);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                // Define a posição do componente para (0, 0)
                setLocation(-5, -5);
            }
        });
        ClassLoader classLoader = TelaDespesas.class.getClassLoader();
        String imageBusca = "busca.png";
        java.net.URL imageURLBusca = classLoader.getResource(imageBusca);

        if (imageURLBusca != null) {
            ImageIcon iconBusca = new ImageIcon(imageURLBusca);
            jLabelDespesaBusca.setIcon(iconBusca);
        } else {
            System.out.println("Imagem não encontrada: " + imageBusca);
        }
        
        String imageDelete = "delete.png";
        java.net.URL imageURLDelete = classLoader.getResource(imageDelete);

        if (imageURLDelete != null) {
            ImageIcon iconDelete = new ImageIcon(imageURLDelete);
            jButtonDespesaDeletar.setIcon(iconDelete);
        } else {
            System.out.println("Imagem não encontrada: " + imageDelete);
        }
        
        String imageUpdate = "update.png";
        java.net.URL imageURLUpdate = classLoader.getResource(imageUpdate);

        if (imageURLUpdate != null) {
            ImageIcon iconUpdate = new ImageIcon(imageURLUpdate);
            jButtonDespesaEditar.setIcon(iconUpdate);
        } else {
            System.out.println("Imagem não encontrada: " + imageUpdate);
        }
        
        String imageCreate = "create.png";
        java.net.URL imageURLCreate = classLoader.getResource(imageCreate);

        if (imageURLCreate != null) {
            ImageIcon iconCreate = new ImageIcon(imageURLCreate);
            jButtonDespesaAdicionar.setIcon(iconCreate);
        } else {
            System.out.println("Imagem não encontrada: " + imageCreate);
        }
        
    }

    private void adicionar() {
        if ((jTextDespesaDescricao.getText().isEmpty() || jTextFieldDespesaValor.getText().isEmpty() || jComboDespesaPagador.getSelectedItem().toString().isEmpty() || jTextDespesaCategoria.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        } else {

            Despesas despesa = new Despesas(0l, jTextDespesaDescricao.getText(), Integer.parseInt(jTextFieldDespesaValor.getText()), jComboDespesaPagador.getSelectedItem().toString(), jTextDespesaCategoria.getText(), "", operador);

            DespesasDao despesaDao = new DespesasDao();
            despesaDao.adicionar(despesa);

            limpaCampos();
        }
    }

    private void editar() {

        if ((jTextDespesaDescricao.getText().isEmpty() || jTextFieldDespesaValor.getText().isEmpty() || jComboDespesaPagador.getSelectedItem().toString().isEmpty() || jTextDespesaCategoria.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        } else {

            Despesas despesa = new Despesas(0l, jTextDespesaDescricao.getText(), Integer.parseInt(jTextFieldDespesaValor.getText()), jComboDespesaPagador.getSelectedItem().toString(), jTextDespesaCategoria.getText(), "", operador);

            DespesasDao despesaDao = new DespesasDao();
            despesaDao.editar(despesa);

            limpaCampos();
        }
    }

    public void deletar() {
         if ((jTextDespesaDescricao.getText().isEmpty() || jTextFieldDespesaValor.getText().isEmpty() || jComboDespesaPagador.getSelectedItem().toString().isEmpty() || jTextDespesaCategoria.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
        } else {
            int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar a despesa " + jTextDespesaDescricao.getText() + " do banco de dados?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {
            Despesas despesa = new Despesas(Long.parseLong(jTextDespesaId.getText()), jTextDespesaDescricao.getText(), Integer.parseInt(jTextFieldDespesaValor.getText()), jComboDespesaPagador.getSelectedItem().toString(), jTextDespesaCategoria.getText(), "", operador);
            
            DespesasDao despesaDao = new DespesasDao();
            despesaDao.deletar(despesa);
            limpaCampos();

            }
        }
    }

    public void pesquisarDespesaId() {
        String sql = String.format("SELECT id as ID, descricao as Descrição, valor as Valor, pagador as Pagador, categoria as Categoria, data_despesa as Data, operador as Operador FROM despesas WHERE id like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextUsuBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pesquisarDespesaDescricao() {
        String sql = String.format("SELECT id as ID, descricao as Descrição, valor as Valor, pagador as Pagador, categoria as Categoria, data_despesa as Data, operador as Operador FROM despesas WHERE descricao like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextUsuBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void pesquisarDespesaCategoria() {
        String sql = String.format("SELECT id as ID, descricao as Descrição, valor as Valor, pagador as Pagador, categoria as Categoria, data_despesa as Data, operador as Operador FROM despesas WHERE categoria like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextUsuBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void pesquisarDespesaPagador() {
        String sql = String.format("SELECT id as ID, descricao as Descrição, valor as Valor, pagador as Pagador, categoria as Categoria, data_despesa as Data, operador as Operador FROM pagador like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextUsuBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableForn.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setarCampos() {
        int setar = jTableForn.getSelectedRow();
        String valorId = jTableForn.getModel().getValueAt(setar, 0).toString();
        String valorDescricao = jTableForn.getModel().getValueAt(setar, 1).toString();
        String valorValor = jTableForn.getModel().getValueAt(setar, 2).toString();
        String valorPagador = jTableForn.getModel().getValueAt(setar, 3).toString();
        String valorCategoria = jTableForn.getModel().getValueAt(setar, 4).toString();
        if (valorId != null && valorDescricao != null && valorValor != null && valorPagador != null && valorCategoria != null) {
            jTextDespesaId.setText(valorId);
            jTextDespesaDescricao.setText(valorDescricao);
            jTextFieldDespesaValor.setText(valorValor);
            jComboDespesaPagador.setSelectedItem(valorPagador);
            jTextDespesaCategoria.setText(valorCategoria);
        }
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }
    
    public void limpaCampos() {
        jTextDespesaId.setText(null);
        jTextDespesaDescricao.setText(null);
        jTextFieldDespesaValor.setText(null);
        jComboDespesaPagador.setSelectedItem("Marcos");
        jTextDespesaCategoria.setText(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelDespesaValor = new javax.swing.JLabel();
        jLabelDespesaTipo = new javax.swing.JLabel();
        jButtonDespesaAdicionar = new javax.swing.JButton();
        jButtonDespesaEditar = new javax.swing.JButton();
        jButtonDespesaDeletar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableForn = new javax.swing.JTable();
        jComboDespesaPesquisa = new javax.swing.JComboBox<>();
        jTextUsuBusca = new javax.swing.JTextField();
        jLabelDespesaBusca = new javax.swing.JLabel();
        jLabelDespesaCampos = new javax.swing.JLabel();
        jTextDespesaId = new javax.swing.JTextField();
        jLabelDespesaId = new javax.swing.JLabel();
        jLabelDespesaDescricao = new javax.swing.JLabel();
        jTextDespesaDescricao = new javax.swing.JTextField();
        jComboDespesaPagador = new javax.swing.JComboBox<>();
        jLabelDespesaPagador = new javax.swing.JLabel();
        jTextFieldDespesaValor = new javax.swing.JTextField();
        jTextDespesaCategoria = new javax.swing.JTextField();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Despesas");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(730, 545));

        jLabelDespesaValor.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelDespesaValor.setText("* Valor:");

        jLabelDespesaTipo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelDespesaTipo.setText("Categoria:");

        jButtonDespesaAdicionar.setToolTipText("Adicionar Usuário");
        jButtonDespesaAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonDespesaAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonDespesaAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDespesaAdicionarActionPerformed(evt);
            }
        });

        jButtonDespesaEditar.setToolTipText("Editar Usuário");
        jButtonDespesaEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonDespesaEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonDespesaEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDespesaEditarActionPerformed(evt);
            }
        });

        jButtonDespesaDeletar.setToolTipText("Deletar Usuário");
        jButtonDespesaDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonDespesaDeletar.setPreferredSize(new java.awt.Dimension(80, 80));
        jButtonDespesaDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDespesaDeletarActionPerformed(evt);
            }
        });

        jTableForn = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jTableForn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTableForn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Descrição", "Valor", "Pagador", "Categoria"
            }
        ));
        jTableForn.getTableHeader().setReorderingAllowed(false);
        jTableForn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableFornMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableForn);

        jComboDespesaPesquisa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Descricao", "Pagador", "Categoria" }));
        jComboDespesaPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboDespesaPesquisaActionPerformed(evt);
            }
        });

        jTextUsuBusca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextUsuBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextUsuBuscaActionPerformed(evt);
            }
        });
        jTextUsuBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextUsuBuscaKeyReleased(evt);
            }
        });

        jLabelDespesaBusca.setText(" ");

        jLabelDespesaCampos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelDespesaCampos.setText("* Campos obrigatórios");

        jTextDespesaId.setEditable(false);
        jTextDespesaId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTextDespesaId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextDespesaIdActionPerformed(evt);
            }
        });

        jLabelDespesaId.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelDespesaId.setText("* ID:");

        jLabelDespesaDescricao.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelDespesaDescricao.setText("* Descrição:");

        jTextDespesaDescricao.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jComboDespesaPagador.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Marcos", "Negocio", "Adiantamento" }));
        jComboDespesaPagador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboDespesaPagadorActionPerformed(evt);
            }
        });

        jLabelDespesaPagador.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelDespesaPagador.setText("Pagador:");

        jTextFieldDespesaValor.setDocument(new ValidadorNumerico());

        jTextDespesaCategoria.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboDespesaPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextUsuBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDespesaBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                        .addComponent(jLabelDespesaCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelDespesaDescricao)
                                .addGap(18, 18, 18)
                                .addComponent(jTextDespesaDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelDespesaValor)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldDespesaValor, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelDespesaId)
                                .addGap(18, 18, 18)
                                .addComponent(jTextDespesaId, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelDespesaPagador)
                                .addGap(18, 18, 18)
                                .addComponent(jComboDespesaPagador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelDespesaTipo)
                                .addGap(18, 18, 18)
                                .addComponent(jTextDespesaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(47, 47, 47))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonDespesaAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(jButtonDespesaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(jButtonDespesaDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelDespesaCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboDespesaPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextUsuBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelDespesaBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboDespesaPagador, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelDespesaTipo)
                            .addComponent(jTextDespesaCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextDespesaId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelDespesaPagador))
                            .addComponent(jLabelDespesaId))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextDespesaDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDespesaDescricao))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDespesaValor)
                    .addComponent(jTextFieldDespesaValor, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonDespesaAdicionar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonDespesaEditar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonDespesaDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        setBounds(0, 0, 730, 545);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDespesaAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDespesaAdicionarActionPerformed
        adicionar();

    }//GEN-LAST:event_jButtonDespesaAdicionarActionPerformed

    private void jButtonDespesaEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDespesaEditarActionPerformed
        editar();
    }//GEN-LAST:event_jButtonDespesaEditarActionPerformed

    private void jButtonDespesaDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDespesaDeletarActionPerformed
        deletar();
    }//GEN-LAST:event_jButtonDespesaDeletarActionPerformed

    private void jTableFornMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableFornMouseClicked
        //evento para setar os campos clicando com o botao esquerdo do mouse
        setarCampos();
    }//GEN-LAST:event_jTableFornMouseClicked

    private void jComboDespesaPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDespesaPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboDespesaPesquisaActionPerformed

    private void jTextUsuBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextUsuBuscaKeyReleased
        //enquanto for digitando fazer isto
        if (jComboDespesaPesquisa.getSelectedItem().toString() == "ID") {
            pesquisarDespesaId();
        } else if (jComboDespesaPesquisa.getSelectedItem().toString() == "Descricao") {
            pesquisarDespesaDescricao();
        } else if (jComboDespesaPesquisa.getSelectedItem().toString() == "Pagador") {
            pesquisarDespesaPagador();
        } else if (jComboDespesaPesquisa.getSelectedItem().toString() == "Categoria") {
            pesquisarDespesaCategoria();
        }

    }//GEN-LAST:event_jTextUsuBuscaKeyReleased

    private void jTextDespesaIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextDespesaIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextDespesaIdActionPerformed

    private void jTextUsuBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextUsuBuscaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextUsuBuscaActionPerformed

    private void jComboDespesaPagadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboDespesaPagadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboDespesaPagadorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDespesaAdicionar;
    private javax.swing.JButton jButtonDespesaDeletar;
    private javax.swing.JButton jButtonDespesaEditar;
    private javax.swing.JComboBox<String> jComboDespesaPagador;
    private javax.swing.JComboBox<String> jComboDespesaPesquisa;
    private javax.swing.JLabel jLabelDespesaBusca;
    private javax.swing.JLabel jLabelDespesaCampos;
    private javax.swing.JLabel jLabelDespesaDescricao;
    private javax.swing.JLabel jLabelDespesaId;
    private javax.swing.JLabel jLabelDespesaPagador;
    private javax.swing.JLabel jLabelDespesaTipo;
    private javax.swing.JLabel jLabelDespesaValor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableForn;
    public javax.swing.JTextField jTextDespesaCategoria;
    public javax.swing.JTextField jTextDespesaDescricao;
    public javax.swing.JTextField jTextDespesaId;
    private javax.swing.JTextField jTextFieldDespesaValor;
    public javax.swing.JTextField jTextUsuBusca;
    // End of variables declaration//GEN-END:variables
}
