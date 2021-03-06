/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.urna.views;

import br.com.urna.dao.ModuloConexao;
import br.com.urna.dao.PreencherTabela;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Washington Klebio
 */

public class ViewVisualizarCand extends javax.swing.JInternalFrame {
    
        Connection conexao = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

    /**
     * Creates new form ViewVisualizarCand
     */
    public ViewVisualizarCand() {
        initComponents();
        conexao = ModuloConexao.conector();
        preencher();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableCand = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Candidatos Cadastrados");

        jTableCand.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableCand);
        if (jTableCand.getColumnModel().getColumnCount() > 0) {
            jTableCand.getColumnModel().getColumn(0).setResizable(false);
            jTableCand.getColumnModel().getColumn(0).setPreferredWidth(150);
            jTableCand.getColumnModel().getColumn(1).setResizable(false);
            jTableCand.getColumnModel().getColumn(1).setPreferredWidth(10);
        }

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/urna/img/back.png"))); // NOI18N
        jButton1.setText("Voltar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 161, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(252, 252, 252))
            .addGroup(layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1)))
                .addGap(48, 48, 48)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ViewCadastroCandidato cadastroCand = new ViewCadastroCandidato();
        ViewPrincipal.desktop.add(cadastroCand);
        ViewPrincipal.center(cadastroCand);
        cadastroCand.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void preencher(){
        ArrayList dados = new ArrayList();
        
        String[] colunas = new String[]{"Nome", "Número", "Cargo", "Chapa"};
        
        String sql = "SELECT * FROM candidatos order by numero";
        
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.first();
            do {
                dados.add(new Object[]{rs.getString("nome"),rs.getInt("numero"),rs.getString("cargo"),rs.getString("chapa")});
            } while(rs.next());
        
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível gerar o relatório.");
        }
        
        PreencherTabela tabela = new PreencherTabela(dados, colunas);
        jTableCand.setModel(tabela);
        jTableCand.getColumnModel().getColumn(0).setPreferredWidth(250);
        jTableCand.getColumnModel().getColumn(0).setResizable(false);
        jTableCand.getColumnModel().getColumn(1).setPreferredWidth(100);
        jTableCand.getColumnModel().getColumn(1).setResizable(false);
        jTableCand.getColumnModel().getColumn(2).setPreferredWidth(190);
        jTableCand.getColumnModel().getColumn(2).setResizable(false);
        jTableCand.getColumnModel().getColumn(3).setPreferredWidth(104);
        jTableCand.getColumnModel().getColumn(3).setResizable(false);
        jTableCand.getTableHeader().setReorderingAllowed(false);
        jTableCand.setAutoResizeMode(jTableCand.AUTO_RESIZE_OFF);
        jTableCand.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableCand;
    // End of variables declaration//GEN-END:variables
}
