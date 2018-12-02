/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.urna.views;

import static java.lang.Thread.sleep;

/**
 *
 * @author Washington Klébio
 */
public class ViewLoading extends javax.swing.JInternalFrame {

    /**
     * Creates new form ViewLoading
     */
    public ViewLoading() {
        initComponents();
        carregar();
    }
    public void carregar(){
         Thread t = new Thread(){
            @Override
            public void run(){
               for(int i = 0; i < 1000; i++){
                   try {
                       sleep(15);
                       jProgressBar1.setValue(i);
                       if(i == 100){
                            ViewFimVoto fim = new ViewFimVoto();
                            ViewPrincipal.desktop.add(fim);
                            ViewPrincipal.center(fim);
                            fim.setVisible(true);
                            fecharTela();
                       }
                   } catch (Exception e) {
                       System.out.println("Erro barra de progresso");
                   }
               } 
            }
        };
        t.start();
    }
    
    public void fecharTela(){
        this.dispose();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();

        jProgressBar1.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}