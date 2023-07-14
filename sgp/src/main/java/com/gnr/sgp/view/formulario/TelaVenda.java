/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.gnr.sgp.view.formulario;

import com.gnr.sgp.modelo.conexao.Conexao;
import com.gnr.sgp.modelo.conexao.ConexaoMysql;
import com.gnr.sgp.modelo.dao.FornecedoresDao;
import com.gnr.sgp.modelo.dao.VendasAnimaisDao;
import com.gnr.sgp.modelo.dominio.Fornecedores;
import com.gnr.sgp.modelo.dominio.VendasAnimais;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Guilherme
 */
public class TelaVenda extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaVenda
     */
    private final Conexao conexao;
    PreparedStatement pst = null;
    ResultSet rs = null;

    int quantidade = 0;
    double mediaKg = 0;
    double precoKg = 0;
    double valorTotal = 0;

    public TelaVenda() {
        this.conexao = new ConexaoMysql();
        initComponents();
        
        // Adiciona o ouvinte de evento ao jTextFieldVendaQuantidade
        jTextFieldVendaQuantidade.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }
        });

        // Adiciona o ouvinte de evento ao jTextFieldVendaMediaKg
        jTextFieldVendaMediaKg.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }
        });

        // Adiciona o ouvinte de evento ao jTextFieldVendaPrecoKg
        jTextFieldVendaPrecoKg.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarValorTotal();
            }
       
        });
    }
    
    private void atualizarValorTotal() {
        try {
            int quantidade = Integer.parseInt(jTextFieldVendaQuantidade.getText());
            double mediaKg = Double.parseDouble(jTextFieldVendaMediaKg.getText());
            double precoKg = Double.parseDouble(jTextFieldVendaPrecoKg.getText());
            double valorTotal = quantidade * mediaKg * precoKg;

            jTextFieldVendaTotal.setText(String.format("%.2f", valorTotal));
        } catch (NumberFormatException ex) {
            jTextFieldVendaTotal.setText("Valor Inválido");
        }
    }
    
    public void adicionar() {
        if ((jTextFieldVendaAnimal.getText().isEmpty() || jTextFieldVendaQuantidade.getText().isEmpty() || jTextFieldVendaMediaKg.getText().isEmpty() || jTextFieldVendaPrecoKg.getText().isEmpty()  || jTextFieldVendaComprador.getText().isEmpty() || jTextFieldVendaTotal.getText().isEmpty())) {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
        } else {
//            setValores();

            VendasAnimais venda = new VendasAnimais(01, Integer.parseInt(jTextFieldVendaAnimal.getText()), quantidade, mediaKg, precoKg, valorTotal, jTextFieldVendaComprador.getText());

            VendasAnimaisDao vendasDao = new VendasAnimaisDao();
            vendasDao.Adicionar(venda);
        }
    }

    public void pesquisarAnimalId() {
        String sql = String.format("SELECT * FROM animais WHERE id like ?");
        try {
            pst = conexao.obterConexao().prepareStatement(sql);
            pst.setString(1, jTextVendaBusca.getText() + "%");
            rs = pst.executeQuery();

            jTableVenda.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setarCampo() {
        int setar = jTableVenda.getSelectedRow();
        jTextFieldVendaAnimal.setText(jTableVenda.getModel().getValueAt(setar, 0).toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelVendaTotal = new javax.swing.JLabel();
        jLabelVendaAnimal = new javax.swing.JLabel();
        jLabelVendaQuantidade = new javax.swing.JLabel();
        jLabelVendaMediaKg = new javax.swing.JLabel();
        jLabelVendaPrecoKg = new javax.swing.JLabel();
        jTextVendaBusca = new javax.swing.JTextField();
        jLabelAnimBusca = new javax.swing.JLabel();
        jLabelAnimCampos = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableVenda = new javax.swing.JTable();
        jTextFieldVendaAnimal = new javax.swing.JTextField();
        jTextFieldVendaQuantidade = new javax.swing.JTextField();
        jTextFieldVendaMediaKg = new javax.swing.JTextField();
        jTextFieldVendaPrecoKg = new javax.swing.JTextField();
        jTextFieldVendaTotal = new javax.swing.JTextField();
        jLabelVendaAnimal1 = new javax.swing.JLabel();
        jTextFieldVendaComprador = new javax.swing.JTextField();
        jLabelVendaPesquisar = new javax.swing.JLabel();
        jButtonVendaFinalizar = new javax.swing.JButton();

        setBackground(new java.awt.Color(227, 234, 227));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setTitle("Vendas");
        setMinimumSize(new java.awt.Dimension(680, 480));
        setPreferredSize(new java.awt.Dimension(820, 620));

        jLabelVendaTotal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelVendaTotal.setText("* Total:");

        jLabelVendaAnimal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelVendaAnimal.setText("* ID Animal:");

        jLabelVendaQuantidade.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelVendaQuantidade.setText("* Quantidade:");

        jLabelVendaMediaKg.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelVendaMediaKg.setText("* Média Kg:");

        jLabelVendaPrecoKg.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelVendaPrecoKg.setText("* Preço Kg:");

        jTextVendaBusca.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextVendaBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextVendaBuscaKeyReleased(evt);
            }
        });

        jLabelAnimBusca.setIcon(new javax.swing.ImageIcon(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\busca.png"));
        jLabelAnimBusca.setText(" ");

        jLabelAnimCampos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelAnimCampos.setText("* Campos obrigatórios");

        jTableVenda.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTableVenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Descrição", "Quant", "Idade", "Sexo"
            }
        ));
        jTableVenda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableVendaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableVenda);

        jLabelVendaAnimal1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabelVendaAnimal1.setText("* Comprador:");

        jLabelVendaPesquisar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabelVendaPesquisar.setText("Pesquisar ID:");

        jButtonVendaFinalizar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButtonVendaFinalizar.setText("Finalizar");
        jButtonVendaFinalizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVendaFinalizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabelVendaPesquisar)
                        .addGap(18, 18, 18)
                        .addComponent(jTextVendaBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelAnimBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                        .addComponent(jLabelAnimCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabelVendaPrecoKg)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldVendaPrecoKg, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelVendaQuantidade)
                                    .addComponent(jLabelVendaMediaKg, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldVendaMediaKg, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jTextFieldVendaQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addComponent(jLabelVendaAnimal)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTextFieldVendaAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(jLabelVendaTotal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldVendaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelVendaAnimal1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldVendaComprador, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButtonVendaFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabelAnimCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelAnimBusca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextVendaBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelVendaPesquisar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelVendaAnimal)
                    .addComponent(jTextFieldVendaAnimal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelVendaAnimal1)
                    .addComponent(jTextFieldVendaComprador, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelVendaQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldVendaQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelVendaMediaKg)
                            .addComponent(jTextFieldVendaMediaKg, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelVendaTotal)
                        .addComponent(jTextFieldVendaTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelVendaPrecoKg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldVendaPrecoKg, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addComponent(jButtonVendaFinalizar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextVendaBuscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextVendaBuscaKeyReleased
        pesquisarAnimalId();
    }//GEN-LAST:event_jTextVendaBuscaKeyReleased

    private void jTableVendaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableVendaMouseClicked
        setarCampo();
    }//GEN-LAST:event_jTableVendaMouseClicked

    private void jButtonVendaFinalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVendaFinalizarActionPerformed
        adicionar();
    }//GEN-LAST:event_jButtonVendaFinalizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonVendaFinalizar;
    private javax.swing.JLabel jLabelAnimBusca;
    private javax.swing.JLabel jLabelAnimCampos;
    private javax.swing.JLabel jLabelVendaAnimal;
    private javax.swing.JLabel jLabelVendaAnimal1;
    private javax.swing.JLabel jLabelVendaMediaKg;
    private javax.swing.JLabel jLabelVendaPesquisar;
    private javax.swing.JLabel jLabelVendaPrecoKg;
    private javax.swing.JLabel jLabelVendaQuantidade;
    private javax.swing.JLabel jLabelVendaTotal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableVenda;
    private javax.swing.JTextField jTextFieldVendaAnimal;
    private javax.swing.JTextField jTextFieldVendaComprador;
    private javax.swing.JTextField jTextFieldVendaMediaKg;
    private javax.swing.JTextField jTextFieldVendaPrecoKg;
    private javax.swing.JTextField jTextFieldVendaQuantidade;
    private javax.swing.JTextField jTextFieldVendaTotal;
    public javax.swing.JTextField jTextVendaBusca;
    // End of variables declaration//GEN-END:variables
}
