package br.com.urna.views;
import br.com.urna.dao.ModuloConexao;
import java.awt.Image;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
/**
 *
 * @author Washington Klébio
 */
public class ViewVoto extends javax.swing.JInternalFrame {
    String valor1 = "";
    String valor2 = "";
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    /**
     * Creates new form ViewVoto
     */
    public ViewVoto() {
        initComponents();
        conexao = ModuloConexao.conector();
        txtNumero1.setEditable(false);
        txtNumero2.setEditable(false);
        txtCargo.setEditable(false);
        txtNome.setEditable(false);
    }
    
    private void somUrna(String soundName){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } 
        catch (Exception ex) {
            System.out.println("Erro ao executar SOM!");
            ex.printStackTrace();
        }
    }
    
    private void addNumero(String numero){
        String nomeFoto = "";
        if(valor1 == ""){
            valor1 = numero;
            txtNumero1.setText(valor1);
        }else if(valor2 == ""){
            valor2 = numero;
            txtNumero2.setText(valor2);
            
            String sql = "SELECT * FROM candidatos WHERE numero=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1,valor1.replaceAll(" ","")+valor2.replaceAll(" ",""));
                rs = pst.executeQuery();
                if (rs.next()) {  
                    
                    txtNome.setText(rs.getString(2));
                    txtCargo.setText(rs.getString(4));
                    nomeFoto = rs.getString(5);
                }else{
                    lblStatus1.setText("NÚMERO ERRADO");
                    lblStatus2.setText("VOTO NULO");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,"Erro ao consultar o candidato!" + e);
            }
            String filename = "src/br/com/urna/candidatos/"+nomeFoto;
            //System.out.println(filename);
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(filename).getImage().getScaledInstance(lbFoto.getWidth(), lbFoto.getHeight(), Image.SCALE_DEFAULT));
            lbFoto.setIcon(imageIcon);            
        }
    }
    
    private void corrigir(){
        valor1 = "";
        valor2 = "";
        txtNumero1.setText("");
        txtNumero2.setText("");
        txtNome.setText("");
        txtCargo.setText("");
        lblStatus1.setText("");
        lblStatus2.setText("");
        lbFoto.setIcon(null);
    }
    
    private void votoBranco(){
        valor1 = "0";
        valor2 = "0";
        txtNumero1.setText(" 0 ");
        txtNumero2.setText(" 0 ");
        lblStatus1.setText("");
        lblStatus2.setText("VOTO EM BRANCO");
        lbFoto.setIcon(null);
    }
    
    private List listaCandidatos(){
        List<String> candidatos = new ArrayList<String>(); //Lista que vai receber todos os registros da sua query
            String resultado = null;
            String sql = "SELECT * FROM candidatos";
            try {
                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();
                while(rs.next()) { //use o while ao invés de if.. "enquanto" tiver proximo registro..
                    resultado = (rs.getString(1));
                    candidatos.add(resultado); //adiciona o usuario encontrado na lista
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return null;
            }
        return candidatos;
    }
    
    
    private boolean validarVoto(int voto){
        List candidatos = listaCandidatos();
        for(Object r : candidatos){
            int votosSql = Integer.parseInt((String) r);
            if(voto == votosSql){
                return true;
            }
        }
        return false;
    }
    private void votoEmBranco(int voto){
        String sql = "INSERT INTO votos (numero, brancos) VALUES(?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, voto);
            pst.setInt(2, 1);
            int adicionado = pst.executeUpdate();
            if(adicionado > 0){
                System.out.println("Voto em branco adicionado com sucesso");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao inserir voto em branco no banco");
        }
    }
    
    private void votoValido(int voto){
        String sql = "INSERT INTO votos (numero, validos) VALUES(?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, voto);
            pst.setInt(2, 1);
            int adicionado = pst.executeUpdate();
            if(adicionado > 0){
                System.out.println("Voto válido adicionado com sucesso");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao inserir voto válido no banco");
        }
    }
    
    private void votoNaoValido(int voto){
        String sql = "INSERT INTO votos (numero, nao_validos) VALUES(?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, voto);
            pst.setInt(2, 1);
            int adicionado = pst.executeUpdate();
            if(adicionado > 0){
                System.out.println("Voto não válido adicionado com sucesso");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Falha ao inserir voto não válido no banco");
        }
    }
    
    private void confirmarVoto(){
        String numCandidato = valor1.replaceAll(" ","")+valor2.replaceAll(" ","");
        int voto = Integer.parseInt(numCandidato);
        if(voto  <= 0){
            System.out.println("Voto nulo");
            votoEmBranco(voto);
        }else if(validarVoto(voto)){
            System.out.println("Voto válido");
            votoValido(voto);
        }else{
            System.out.println("Voto Inválido");
            votoNaoValido(voto);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn6 = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn0 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btnConfirma = new javax.swing.JButton();
        btnCorrige = new javax.swing.JButton();
        btnBranco = new javax.swing.JButton();
        btnVoltar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNumero2 = new javax.swing.JTextField();
        txtNumero1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCargo = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        lblStatus1 = new javax.swing.JLabel();
        lblStatus2 = new javax.swing.JLabel();
        lbFoto = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn6.setBackground(new java.awt.Color(0, 0, 0));
        btn6.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn6.setForeground(new java.awt.Color(255, 255, 255));
        btn6.setText("6");
        btn6.setAlignmentY(0.0F);
        btn6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn6.setMaximumSize(new java.awt.Dimension(38, 22));
        btn6.setMinimumSize(new java.awt.Dimension(38, 22));
        btn6.setPreferredSize(new java.awt.Dimension(38, 22));
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6ActionPerformed(evt);
            }
        });
        getContentPane().add(btn6, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 200, 60, 55));

        btn1.setBackground(new java.awt.Color(0, 0, 0));
        btn1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 255, 255));
        btn1.setText("1");
        btn1.setAlignmentY(0.0F);
        btn1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn1.setMaximumSize(new java.awt.Dimension(38, 22));
        btn1.setMinimumSize(new java.awt.Dimension(38, 22));
        btn1.setPreferredSize(new java.awt.Dimension(38, 22));
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });
        getContentPane().add(btn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 130, 60, 55));

        btn2.setBackground(new java.awt.Color(0, 0, 0));
        btn2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 255, 255));
        btn2.setText("2");
        btn2.setAlignmentY(0.0F);
        btn2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn2.setMaximumSize(new java.awt.Dimension(38, 22));
        btn2.setMinimumSize(new java.awt.Dimension(38, 22));
        btn2.setPreferredSize(new java.awt.Dimension(38, 22));
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });
        getContentPane().add(btn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 130, 60, 55));

        btn3.setBackground(new java.awt.Color(0, 0, 0));
        btn3.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 255, 255));
        btn3.setText("3");
        btn3.setAlignmentY(0.0F);
        btn3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn3.setMaximumSize(new java.awt.Dimension(38, 22));
        btn3.setMinimumSize(new java.awt.Dimension(38, 22));
        btn3.setPreferredSize(new java.awt.Dimension(38, 22));
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });
        getContentPane().add(btn3, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 130, 60, 55));

        btn4.setBackground(new java.awt.Color(0, 0, 0));
        btn4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn4.setForeground(new java.awt.Color(255, 255, 255));
        btn4.setText("4");
        btn4.setAlignmentY(0.0F);
        btn4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn4.setMaximumSize(new java.awt.Dimension(38, 22));
        btn4.setMinimumSize(new java.awt.Dimension(38, 22));
        btn4.setPreferredSize(new java.awt.Dimension(38, 22));
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });
        getContentPane().add(btn4, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 200, 60, 55));

        btn9.setBackground(new java.awt.Color(0, 0, 0));
        btn9.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn9.setForeground(new java.awt.Color(255, 255, 255));
        btn9.setText("9");
        btn9.setAlignmentY(0.0F);
        btn9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn9.setMaximumSize(new java.awt.Dimension(38, 22));
        btn9.setMinimumSize(new java.awt.Dimension(38, 22));
        btn9.setPreferredSize(new java.awt.Dimension(38, 22));
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9ActionPerformed(evt);
            }
        });
        getContentPane().add(btn9, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 270, 60, 55));

        btn5.setBackground(new java.awt.Color(0, 0, 0));
        btn5.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn5.setForeground(new java.awt.Color(255, 255, 255));
        btn5.setText("5");
        btn5.setAlignmentY(0.0F);
        btn5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn5.setMaximumSize(new java.awt.Dimension(38, 22));
        btn5.setMinimumSize(new java.awt.Dimension(38, 22));
        btn5.setPreferredSize(new java.awt.Dimension(38, 22));
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
            }
        });
        getContentPane().add(btn5, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 200, 60, 55));

        btn7.setBackground(new java.awt.Color(0, 0, 0));
        btn7.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn7.setForeground(new java.awt.Color(255, 255, 255));
        btn7.setText("7");
        btn7.setAlignmentY(0.0F);
        btn7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn7.setMaximumSize(new java.awt.Dimension(38, 22));
        btn7.setMinimumSize(new java.awt.Dimension(38, 22));
        btn7.setPreferredSize(new java.awt.Dimension(38, 22));
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
            }
        });
        getContentPane().add(btn7, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 270, 60, 55));

        btn0.setBackground(new java.awt.Color(0, 0, 0));
        btn0.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn0.setForeground(new java.awt.Color(255, 255, 255));
        btn0.setText("0");
        btn0.setAlignmentY(0.0F);
        btn0.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn0.setMaximumSize(new java.awt.Dimension(38, 22));
        btn0.setMinimumSize(new java.awt.Dimension(38, 22));
        btn0.setPreferredSize(new java.awt.Dimension(38, 22));
        btn0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn0ActionPerformed(evt);
            }
        });
        getContentPane().add(btn0, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 340, 60, 55));

        btn8.setBackground(new java.awt.Color(0, 0, 0));
        btn8.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        btn8.setForeground(new java.awt.Color(255, 255, 255));
        btn8.setText("8");
        btn8.setAlignmentY(0.0F);
        btn8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn8.setMaximumSize(new java.awt.Dimension(38, 22));
        btn8.setMinimumSize(new java.awt.Dimension(38, 22));
        btn8.setPreferredSize(new java.awt.Dimension(38, 22));
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8ActionPerformed(evt);
            }
        });
        getContentPane().add(btn8, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 270, 60, 55));

        btnConfirma.setBackground(new java.awt.Color(0, 255, 51));
        btnConfirma.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnConfirma.setText("CONFIRMA");
        btnConfirma.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmaActionPerformed(evt);
            }
        });
        getContentPane().add(btnConfirma, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 420, 120, 60));

        btnCorrige.setBackground(new java.awt.Color(255, 153, 0));
        btnCorrige.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCorrige.setText("CORRIGE");
        btnCorrige.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCorrige.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorrigeActionPerformed(evt);
            }
        });
        getContentPane().add(btnCorrige, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 430, 90, 50));

        btnBranco.setBackground(new java.awt.Color(255, 255, 255));
        btnBranco.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnBranco.setText("BRANCO");
        btnBranco.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBranco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrancoActionPerformed(evt);
            }
        });
        getContentPane().add(btnBranco, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 430, 90, 50));

        btnVoltar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/urna/img/back.png"))); // NOI18N
        btnVoltar.setText("Voltar");
        btnVoltar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });
        getContentPane().add(btnVoltar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("SEU VOTO PARA:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        txtNumero2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jPanel1.add(txtNumero2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, 50, 60));

        txtNumero1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtNumero1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumero1ActionPerformed(evt);
            }
        });
        jPanel1.add(txtNumero1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 50, 60));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("NÚMERO:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Cargo:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, 30));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Nome:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, -1, 30));
        jPanel1.add(txtCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, 250, 30));
        jPanel1.add(txtNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 250, 30));
        jPanel1.add(lblStatus1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 230, 20));

        lblStatus2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jPanel1.add(lblStatus2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 250, 30));

        lbFoto.setBackground(new java.awt.Color(204, 204, 204));
        lbFoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(lbFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 161, 225));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 85, 550, 410));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/urna/img/fundovoto.jpg"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        // TODO add your handling code here:
        ViewMenu menu = new ViewMenu();
        ViewPrincipal.desktop.add(menu);
        ViewPrincipal.center(menu);
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    private void btnConfirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmaActionPerformed
        // TODO add your handling code here:
        
        somUrna("src/br/com/urna/audio/confirma.wav");
        ViewLoading loading = new ViewLoading();
        ViewPrincipal.desktop.add(loading);
        ViewPrincipal.center(loading);
        loading.setVisible(true);
        confirmarVoto();
        this.dispose();
        
    }//GEN-LAST:event_btnConfirmaActionPerformed

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 1 ");
    }//GEN-LAST:event_btn1ActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 2 ");
    }//GEN-LAST:event_btn2ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 3 ");
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 4 ");
    }//GEN-LAST:event_btn4ActionPerformed

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 5 ");
    }//GEN-LAST:event_btn5ActionPerformed

    private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 6 ");
    }//GEN-LAST:event_btn6ActionPerformed

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 7 ");
    }//GEN-LAST:event_btn7ActionPerformed

    private void btn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 8 ");
    }//GEN-LAST:event_btn8ActionPerformed

    private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 9 ");
    }//GEN-LAST:event_btn9ActionPerformed

    private void btn0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn0ActionPerformed
        // TODO add your handling code here:
        somUrna("src/br/com/urna/audio/bipe.wav");
        addNumero(" 0 ");
    }//GEN-LAST:event_btn0ActionPerformed

    private void btnCorrigeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorrigeActionPerformed
        // TODO add your handling code here:
        corrigir();
    }//GEN-LAST:event_btnCorrigeActionPerformed

    private void txtNumero1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumero1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtNumero1ActionPerformed

    private void btnBrancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrancoActionPerformed
        // TODO add your handling code here:
        votoBranco();
    }//GEN-LAST:event_btnBrancoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn0;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnBranco;
    private javax.swing.JButton btnConfirma;
    private javax.swing.JButton btnCorrige;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbFoto;
    private javax.swing.JLabel lblStatus1;
    private javax.swing.JLabel lblStatus2;
    private javax.swing.JTextField txtCargo;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNumero1;
    private javax.swing.JTextField txtNumero2;
    // End of variables declaration//GEN-END:variables
}
