/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet_fe;

import db.ConnectionUtil;
import java.awt.Color;
import java.awt.Font;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.prompt.PromptSupport;

/**
 *
 * @author intel
 */
public final class MainEmployes extends javax.swing.JFrame {

    public static String ess;
    int mouseX = 0;
    int mouseY = 0;

    static Connection con = ConnectionUtil.getConnection();
    ResultSet rs;

    /**
     * Creates new form main
     */
    public MainEmployes() {
        initComponents();
        RemplirTableauClient();
        Prompt();
        RemplirTableV();
        RemplirTableReservation();
        HeaderJtable();
  /*      
jButton1.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
jButton1.setContentAreaFilled(false);

        */
       
 jLayeredPane1.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(0,153,0)));
jPanelButton.setBorder(BorderFactory.createMatteBorder(0,1,0,1,new Color(0,153,0) ));
jPanelTitle.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0,153,0)));     

        jButton1.setBackground(new Color(221, 255, 221));

        TableColumnModel tcm = jTable4.getColumnModel();
        tcm.removeColumn(tcm.getColumn(0));

        TableColumnModel tc = jTableRes.getColumnModel();
        tc.removeColumn(tc.getColumn(0));

    }

    public void Prompt() {
        PromptSupport.setPrompt("Entrer CIN ou Télephone", jTextField1);
        PromptSupport.setPrompt("Entrer CIN ", txtclRec);
        PromptSupport.setPrompt("Entrer CIN ", txtCl);
        PromptSupport.setPrompt("Entrer Objet", txtObjet);
        PromptSupport.setPrompt("Entrer CIN ", tcinclient);
        PromptSupport.setPrompt("Entrer le contenu du réclamation", txtReclamation);
    }

    public boolean verifierF() {
        Calendar datev = jdateV.getCalendar();
        String villeDep = (String) cboVilleD.getSelectedItem();
        String dest = (String) cboDes.getSelectedItem();

        if (datev == null || villeDep.trim().equals("--choisissez une ville--") || dest.trim().equals("--choisissez une ville--")) {

            JOptionPane.showMessageDialog(null, "Verifier les champs", "Champs vides", 2);
            return false;

        } else if (villeDep == dest) {
            JOptionPane.showMessageDialog(null, "Ville départ et la déstination sont identique", "ville identique", 2);
            cboVilleD.setSelectedIndex(0);
            cboDes.setSelectedIndex(0);

        } else {
            return true;
        }
        return false;

    }

    public static void RemplirTableauClient() {

        DefaultTableModel d = (DefaultTableModel) jTableClEmp.getModel();

        try {
            PreparedStatement pstmt = con.prepareStatement("select * from client");

            ResultSet rs2;

            rs2 = pstmt.executeQuery();

            d.setRowCount(0);
            while (rs2.next()) {
                d.addRow(new Object[]{rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getString(6), rs2.getString(7), rs2.getString(8)});
            }
            rs2.close();
            pstmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void filterClient(String query) {
        DefaultTableModel d = (DefaultTableModel) jTableClEmp.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(d);
        jTableClEmp.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    public boolean verifierField() {
        String objet = txtObjet.getText();
        String reclamation = txtReclamation.getText();
        String cin = txtclRec.getText();

        if (objet.trim().equals("") || reclamation.trim().equals("") || cin.trim().equals("")) {

            JOptionPane.showMessageDialog(null, "Verifier les champs", "Champs vides", 2);
            return false;

        } else {
            return true;
        }
    }

    public void RemplirTableV() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable4.getModel();

            PreparedStatement pstmt = con.prepareStatement("select * from voyage order by datev");

            ResultSet rs8 = pstmt.executeQuery();
            d.setRowCount(0);
            while (rs8.next()) {
                if (rs8.getInt(10) == 1) {
                    d.addRow(new Object[]{rs8.getInt(1), rs8.getDate(2), rs8.getString(3), rs8.getString(4), rs8.getString(5), rs8.getString(6), rs8.getString(7), rs8.getString(8), rs8.getString(9)});
                }
            }
            rs8.close();
            pstmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RemplirTableReservation() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTableRes.getModel();

            PreparedStatement pstmt = con.prepareStatement("select * from client join reservation using(idcl) join voyage using(numv) order by dater");

            ResultSet rs13 = pstmt.executeQuery();

            d.setRowCount(0);
            while (rs13.next()) {
                d.addRow(new Object[]{rs13.getInt("idcl"), rs13.getString("nomcl"), rs13.getString("prenomCl"), rs13.getString("cincl"), rs13.getInt("numv"), rs13.getDate("dateR"), rs13.getInt("NumBillet"), rs13.getInt("Numplace")});

            }
            rs13.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void HeaderJtable(){
        JTableHeader Theader = jTable4.getTableHeader();
        Theader.setBackground(Color.red);
        Theader.setForeground(new Color(0,153,0));
        Theader.setFont(new Font("Tahome", Font.BOLD, 14));
        
        JTableHeader Theade = jTableClEmp.getTableHeader();
        Theade.setBackground(Color.red);
        Theade.setForeground(new Color(0,153,0));
        Theade.setFont(new Font("Tahome", Font.BOLD, 14));
        
         JTableHeader Thead = jTableRes.getTableHeader();
        Thead.setBackground(Color.red);
        Thead.setForeground(new Color(0,153,0));
        Thead.setFont(new Font("Tahome", Font.BOLD, 14));
        
        
           }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTitle = new javax.swing.JPanel();
        exit = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanelButton = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        pnl1 = new javax.swing.JPanel();
        pnl6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableClEmp = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btnNouveauCl2 = new javax.swing.JButton();
        btnUpdateC = new javax.swing.JButton();
        btnSuppC = new javax.swing.JButton();
        pnl2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jdateV = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        cboVilleD = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cboDes = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        btnRechercheV = new javax.swing.JButton();
        btnReserver = new javax.swing.JButton();
        txtCl = new javax.swing.JTextField();
        btnRafraichir = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        pnl3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtObjet = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtReclamation = new javax.swing.JTextArea();
        txtclRec = new javax.swing.JTextField();
        btnAjouterRec = new javax.swing.JButton();
        btnAnnulerRec = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        pnl4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableRes = new javax.swing.JTable();
        tcinclient = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnBillet1 = new javax.swing.JButton();
        btnRechercheV1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanelTitle.setBackground(new java.awt.Color(0, 153, 0));
        jPanelTitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0)));
        jPanelTitle.setFocusCycleRoot(true);
        jPanelTitle.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanelTitleMouseDragged(evt);
            }
        });
        jPanelTitle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanelTitleMousePressed(evt);
            }
        });

        exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_delete_20px_1.png"))); // NOI18N
        exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitMouseClicked(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_minus_20px_1.png"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 26)); // NOI18N

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/téléchargement_1.jpg"))); // NOI18N
        jLabel20.setOpaque(true);

        jLabel12.setFont(new java.awt.Font("Arial Black", 1, 28)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("ESPACE EMPLOYES");

        javax.swing.GroupLayout jPanelTitleLayout = new javax.swing.GroupLayout(jPanelTitle);
        jPanelTitle.setLayout(jPanelTitleLayout);
        jPanelTitleLayout.setHorizontalGroup(
            jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTitleLayout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jLabel5)
                .addGap(186, 186, 186)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(0, 0, 0)
                .addComponent(exit))
        );
        jPanelTitleLayout.setVerticalGroup(
            jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelTitleLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelTitleLayout.createSequentialGroup()
                .addGroup(jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelButton.setBackground(new java.awt.Color(221, 255, 221));
        jPanelButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0)));

        jButton1.setBackground(new java.awt.Color(255, 255, 153));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user_30px.png"))); // NOI18N
        jButton1.setText("Clients");
        jButton1.setBorder(null);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusable(false);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton1MouseExited(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 153));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/traveler_30px.png"))); // NOI18N
        jButton2.setText("Voyage");
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 153));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_exit_30px.png"))); // NOI18N
        jButton4.setText("Déconnexion");
        jButton4.setBorder(null);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setFocusable(false);
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton4MouseExited(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 153));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/reservation_30px.png"))); // NOI18N
        jButton5.setText("Réservation");
        jButton5.setBorder(null);
        jButton5.setBorderPainted(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setFocusable(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 153));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/list_30px.png"))); // NOI18N
        jButton3.setText("Réclamation");
        jButton3.setBorder(null);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelButtonLayout = new javax.swing.GroupLayout(jPanelButton);
        jPanelButton.setLayout(jPanelButtonLayout);
        jPanelButtonLayout.setHorizontalGroup(
            jPanelButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelButtonLayout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanelButtonLayout.setVerticalGroup(
            jPanelButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelButtonLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLayeredPane1.setBackground(new java.awt.Color(221, 255, 221));
        jLayeredPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0)));
        jLayeredPane1.setLayout(new java.awt.CardLayout());

        pnl1.setBackground(new java.awt.Color(221, 255, 221));

        pnl6.setBackground(new java.awt.Color(221, 255, 221));

        jTableClEmp.setBackground(new java.awt.Color(255, 255, 153));
        jTableClEmp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
        jTableClEmp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableClEmp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nom", "Prénom", "Cin", "Tél", "Adresse", "Email", "Sexe"
            }
        ));
        jTableClEmp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableClEmpMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableClEmpMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableClEmp);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Rechercher Client:");

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnl6Layout = new javax.swing.GroupLayout(pnl6);
        pnl6.setLayout(pnl6Layout);
        pnl6Layout.setHorizontalGroup(
            pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl6Layout.createSequentialGroup()
                .addGroup(pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl6Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1028, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnl6Layout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl6Layout.setVerticalGroup(
            pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnNouveauCl2.setBackground(new java.awt.Color(66, 160, 66));
        btnNouveauCl2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnNouveauCl2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_add_user_male_30px_1.png"))); // NOI18N
        btnNouveauCl2.setText("Nouveau client");
        btnNouveauCl2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNouveauCl2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNouveauCl2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNouveauCl2MouseExited(evt);
            }
        });
        btnNouveauCl2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNouveauCl2ActionPerformed(evt);
            }
        });

        btnUpdateC.setBackground(new java.awt.Color(66, 160, 66));
        btnUpdateC.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnUpdateC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_update_30px_3.png"))); // NOI18N
        btnUpdateC.setText("Mise à jour");
        btnUpdateC.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUpdateCMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUpdateCMouseExited(evt);
            }
        });
        btnUpdateC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateCActionPerformed(evt);
            }
        });

        btnSuppC.setBackground(new java.awt.Color(66, 160, 66));
        btnSuppC.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSuppC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_delete_user_male_30px_1.png"))); // NOI18N
        btnSuppC.setText("Supprimer");
        btnSuppC.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSuppC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuppCMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSuppCMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSuppCMouseExited(evt);
            }
        });
        btnSuppC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuppCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl1Layout = new javax.swing.GroupLayout(pnl1);
        pnl1.setLayout(pnl1Layout);
        pnl1Layout.setHorizontalGroup(
            pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl1Layout.createSequentialGroup()
                .addGap(249, 249, 249)
                .addComponent(btnNouveauCl2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdateC, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSuppC)
                .addContainerGap(334, Short.MAX_VALUE))
            .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl1Layout.createSequentialGroup()
                    .addContainerGap(70, Short.MAX_VALUE)
                    .addComponent(pnl6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(70, Short.MAX_VALUE)))
        );
        pnl1Layout.setVerticalGroup(
            pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl1Layout.createSequentialGroup()
                .addGap(375, 375, 375)
                .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNouveauCl2)
                    .addComponent(btnUpdateC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSuppC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(85, 85, 85))
            .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl1Layout.createSequentialGroup()
                    .addContainerGap(32, Short.MAX_VALUE)
                    .addComponent(pnl6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(131, Short.MAX_VALUE)))
        );

        jLayeredPane1.add(pnl1, "card2");

        pnl2.setBackground(new java.awt.Color(221, 255, 221));

        jTable4.setBackground(new java.awt.Color(255, 255, 153));
        jTable4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 153, 0)));
        jTable4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NumV", "Date voyage", "Ville départ", "Destination", "Heure départ", "Durée", "Autocar", "Prix", "Place disponible"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setText("Date voyage:");

        jdateV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setText("Ville départ:");

        cboVilleD.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cboVilleD.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--choisissez une ville--", "Casablanca", "Marrakech", "Agadir", "El Jadida", "Tanger", "Rabat", "Tétouan", "Oujda", "Fés", "Salé", "Khouribga" }));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel9.setText("Destination:");

        cboDes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cboDes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--choisissez une ville--", "Casablanca", "Marrakech", "Agadir", "El Jadida", "Tanger", "Rabat", "Tétouan", "Oujda", "Fés", "Salé", "Khouribga" }));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel10.setText("Client:");

        btnRechercheV.setBackground(new java.awt.Color(66, 160, 66));
        btnRechercheV.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnRechercheV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_search_30px.png"))); // NOI18N
        btnRechercheV.setText("Rechercher");
        btnRechercheV.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRechercheV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRechercheVMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRechercheVMouseExited(evt);
            }
        });
        btnRechercheV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechercheVActionPerformed(evt);
            }
        });

        btnReserver.setBackground(new java.awt.Color(66, 160, 66));
        btnReserver.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnReserver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_reserve_30px_1.png"))); // NOI18N
        btnReserver.setText("Réserver");
        btnReserver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReserver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReserverMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReserverMouseExited(evt);
            }
        });
        btnReserver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReserverActionPerformed(evt);
            }
        });

        txtCl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        btnRafraichir.setBackground(new java.awt.Color(66, 160, 66));
        btnRafraichir.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnRafraichir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_refresh_30px_1.png"))); // NOI18N
        btnRafraichir.setText("Rafraichir");
        btnRafraichir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRafraichir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRafraichirMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRafraichirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRafraichirMouseExited(evt);
            }
        });
        btnRafraichir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRafraichirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl2Layout = new javax.swing.GroupLayout(pnl2);
        pnl2.setLayout(pnl2Layout);
        pnl2Layout.setHorizontalGroup(
            pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl2Layout.createSequentialGroup()
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1091, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl2Layout.createSequentialGroup()
                        .addGap(318, 318, 318)
                        .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboDes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboVilleD, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jdateV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCl))
                        .addGap(84, 84, 84)
                        .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRechercheV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnRafraichir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnl2Layout.createSequentialGroup()
                        .addGap(464, 464, 464)
                        .addComponent(btnReserver, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl2Layout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        pnl2Layout.setVerticalGroup(
            pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtCl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRechercheV)
                    .addComponent(jLabel7)
                    .addComponent(jdateV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cboVilleD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37))
                    .addGroup(pnl2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(btnRafraichir, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(btnReserver)
                .addGap(19, 19, 19))
        );

        jLayeredPane1.add(pnl2, "card3");

        pnl3.setBackground(new java.awt.Color(221, 255, 221));

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0)));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Client:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Objet:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Contenu:");

        txtObjet.setBackground(new java.awt.Color(221, 255, 221));
        txtObjet.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtObjet.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 153, 0), new java.awt.Color(0, 153, 0)));
        txtObjet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtObjetActionPerformed(evt);
            }
        });

        txtReclamation.setBackground(new java.awt.Color(221, 255, 221));
        txtReclamation.setColumns(20);
        txtReclamation.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        txtReclamation.setRows(5);
        txtReclamation.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 153, 0), new java.awt.Color(0, 153, 0)));
        jScrollPane2.setViewportView(txtReclamation);

        txtclRec.setBackground(new java.awt.Color(221, 255, 221));
        txtclRec.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtclRec.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 153, 0), new java.awt.Color(0, 153, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtObjet)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtclRec, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtclRec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtObjet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        btnAjouterRec.setBackground(new java.awt.Color(66, 160, 66));
        btnAjouterRec.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnAjouterRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_add_30px_2.png"))); // NOI18N
        btnAjouterRec.setText("Ajouter");
        btnAjouterRec.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAjouterRec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAjouterRecMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAjouterRecMouseExited(evt);
            }
        });
        btnAjouterRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjouterRecActionPerformed(evt);
            }
        });

        btnAnnulerRec.setBackground(new java.awt.Color(66, 160, 66));
        btnAnnulerRec.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnAnnulerRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_cancel_30px_1.png"))); // NOI18N
        btnAnnulerRec.setText("Annuler");
        btnAnnulerRec.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnnulerRec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAnnulerRecMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAnnulerRecMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAnnulerRecMouseExited(evt);
            }
        });
        btnAnnulerRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnulerRecActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl3Layout = new javax.swing.GroupLayout(pnl3);
        pnl3.setLayout(pnl3Layout);
        pnl3Layout.setHorizontalGroup(
            pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl3Layout.createSequentialGroup()
                .addContainerGap(373, Short.MAX_VALUE)
                .addComponent(btnAjouterRec, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAnnulerRec, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(484, 484, 484))
            .addGroup(pnl3Layout.createSequentialGroup()
                .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl3Layout.createSequentialGroup()
                        .addGap(411, 411, 411)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl3Layout.createSequentialGroup()
                        .addGap(204, 204, 204)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl3Layout.setVerticalGroup(
            pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl3Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAjouterRec)
                    .addComponent(btnAnnulerRec))
                .addGap(78, 78, 78))
        );

        jLayeredPane1.add(pnl3, "card4");

        pnl4.setBackground(new java.awt.Color(221, 255, 221));

        jTableRes.setBackground(new java.awt.Color(255, 255, 153));
        jTableRes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 153, 0)));
        jTableRes.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableRes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Idcl", "Nom", "Prénom", "Cin", "Numv", "Date Réservation", "Num billet", "Num place"
            }
        ));
        jScrollPane3.setViewportView(jTableRes);

        tcinclient.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tcinclient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tcinclientActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setText("Cin Client:");

        btnBillet1.setBackground(new java.awt.Color(66, 160, 66));
        btnBillet1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnBillet1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_ticket_30px.png"))); // NOI18N
        btnBillet1.setText("Billet");
        btnBillet1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBillet1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBillet1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBillet1MouseExited(evt);
            }
        });
        btnBillet1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillet1ActionPerformed(evt);
            }
        });

        btnRechercheV1.setBackground(new java.awt.Color(66, 160, 66));
        btnRechercheV1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnRechercheV1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_search_30px.png"))); // NOI18N
        btnRechercheV1.setText("Rechercher");
        btnRechercheV1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRechercheV1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRechercheV1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRechercheV1MouseExited(evt);
            }
        });
        btnRechercheV1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechercheV1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl4Layout = new javax.swing.GroupLayout(pnl4);
        pnl4.setLayout(pnl4Layout);
        pnl4Layout.setHorizontalGroup(
            pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl4Layout.createSequentialGroup()
                .addGroup(pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl4Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tcinclient, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(btnRechercheV1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl4Layout.createSequentialGroup()
                        .addGap(491, 491, 491)
                        .addComponent(btnBillet1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl4Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1089, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl4Layout.createSequentialGroup()
                        .addGap(272, 272, 272)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        pnl4Layout.setVerticalGroup(
            pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tcinclient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(btnRechercheV1))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBillet1)
                .addGap(65, 65, 65))
        );

        jLayeredPane1.add(pnl4, "card5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLayeredPane1))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void exitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.setState(MainEmployes.ICONIFIED);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jPanelTitleMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelTitleMouseDragged
        this.setLocation(this.getX() + evt.getX() - mouseX, this.getY() + evt.getY() - mouseY);
    }//GEN-LAST:event_jPanelTitleMouseDragged

    private void jPanelTitleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelTitleMousePressed
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_jPanelTitleMousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.setBackground(new Color(221, 255, 221));
        jButton2.setBackground(new Color(255, 255, 153));
        jButton3.setBackground(new Color(255, 255, 153));
        jButton4.setBackground(new Color(255, 255, 153));
        jButton5.setBackground(new Color(255, 255, 153));
        jLayeredPane1.removeAll();
        jLayeredPane1.add(pnl1);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jButton2.setBackground(new Color(221, 255, 221));
        jButton1.setBackground(new Color(255, 255, 153));
        jButton3.setBackground(new Color(255, 255, 153));
        jButton4.setBackground(new Color(255, 255, 153));
        jButton5.setBackground(new Color(255, 255, 153));
        jLayeredPane1.removeAll();
        jLayeredPane1.add(pnl2);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();

        try {
            PreparedStatement pstmt = con.prepareStatement("select * from client where cinCl=?");
            pstmt.setString(1, MainEmployes.ess);
            ResultSet rs5 = pstmt.executeQuery();
            while (rs5.next()) {
                //jLabel11.setText(rs5.getString("nomCl").toUpperCase() +" "+ rs5.getString("prenomCl").toUpperCase());
                txtCl.setText(rs5.getString(4).toUpperCase());
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jButton3.setBackground(new Color(221, 255, 221));
        jButton1.setBackground(new Color(255, 255, 153));
        jButton5.setBackground(new Color(255, 255, 153));
        jButton4.setBackground(new Color(255, 255, 153));
        jButton2.setBackground(new Color(255, 255, 153));
        jLayeredPane1.removeAll();
        jLayeredPane1.add(pnl3);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();

        try {
            PreparedStatement pstmt = con.prepareStatement("select * from client where cinCl=?");
            pstmt.setString(1, MainEmployes.ess);
            ResultSet rs5 = pstmt.executeQuery();
            while (rs5.next()) {
                //jLabel11.setText(rs5.getString("nomCl").toUpperCase() +" "+ rs5.getString("prenomCl").toUpperCase());
                txtclRec.setText(rs5.getString(4).toUpperCase());
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        Cnx cx = new Cnx();
        cx.setVisible(true);
        cx.pack();
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnSuppCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuppCMouseClicked

    }//GEN-LAST:event_btnSuppCMouseClicked

    private void btnSuppCMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuppCMouseEntered
        btnSuppC.setBackground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnSuppCMouseEntered

    private void btnSuppCMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuppCMouseExited
        btnSuppC.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnSuppCMouseExited

    private void btnSuppCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuppCActionPerformed
        try {
            DefaultTableModel d = (DefaultTableModel) jTableClEmp.getModel();

            int row = -1;
            row = jTableClEmp.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client");
            } else {

                String supp = (String) jTableClEmp.getModel().getValueAt(row, 2);

                int test = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer??");
                if (test == JOptionPane.YES_OPTION) {

                    PreparedStatement pstmt = con.prepareStatement("delete from client where cinCl=?");
                    pstmt.setString(1, supp);
                    pstmt.execute();
                    RemplirTableauClient();
                } else {

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnSuppCActionPerformed

    private void btnUpdateCMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateCMouseEntered
        btnUpdateC.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnUpdateCMouseEntered

    private void btnUpdateCMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateCMouseExited
        btnUpdateC.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnUpdateCMouseExited

    private void btnUpdateCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateCActionPerformed
        int row = -1;
        row = jTableClEmp.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client");
        } else {

            UpdateClientE uc = new UpdateClientE();
            uc.setVisible(true);
            uc.pack();
        }
    }//GEN-LAST:event_btnUpdateCActionPerformed

    private void btnNouveauCl2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNouveauCl2MouseEntered
        btnNouveauCl2.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnNouveauCl2MouseEntered

    private void btnNouveauCl2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNouveauCl2MouseExited
        btnNouveauCl2.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnNouveauCl2MouseExited

    private void btnNouveauCl2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNouveauCl2ActionPerformed

        NouveauClient nc = new NouveauClient();
        nc.setVisible(true);
        nc.pack();

    }//GEN-LAST:event_btnNouveauCl2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void txtObjetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtObjetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtObjetActionPerformed

    private void btnAjouterRecMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAjouterRecMouseEntered
        btnAjouterRec.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnAjouterRecMouseEntered

    private void btnAjouterRecMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAjouterRecMouseExited
        btnAjouterRec.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnAjouterRecMouseExited

    private void btnAjouterRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouterRecActionPerformed

        try {
            if (verifierField()) {

                PreparedStatement pstmt1 = con.prepareStatement("select idcl from client where upper(cincl)=?");
                pstmt1.setString(1, txtclRec.getText());
                rs = pstmt1.executeQuery();
                PreparedStatement pstmt2 = con.prepareStatement("insert into reclamation(ObjetRec, ContenuRec, idcl) values(?,?,?)");
                pstmt2.setString(1, txtObjet.getText());
                pstmt2.setString(2, txtReclamation.getText());
                if (rs.next()) {
                    pstmt2.setInt(3, rs.getInt(1));
                }
                if (pstmt2.executeUpdate() != 0) {
                    JOptionPane.showMessageDialog(null, " Réclamation a été ajouter avec succès");
                    txtclRec.setText("");
                    txtObjet.setText("");
                    txtReclamation.setText("");
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnAjouterRecActionPerformed

    private void btnRechercheVMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRechercheVMouseEntered
        btnRechercheV.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnRechercheVMouseEntered

    private void btnRechercheVMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRechercheVMouseExited
        btnRechercheV.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnRechercheVMouseExited

    private void btnRechercheVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechercheVActionPerformed
        DefaultTableModel d = (DefaultTableModel) jTable4.getModel();
        String req = "select * from voyage  where datev=? and VilleDep=? and Destination=?";
        Calendar datev = jdateV.getCalendar();

        String villedep = cboVilleD.getSelectedItem().toString();

        String destin = cboDes.getSelectedItem().toString();
        if (verifierF()) {
            try {
                PreparedStatement pstmt = con.prepareStatement(req);
                pstmt.setDate(1, new java.sql.Date(datev.getTimeInMillis()));
                pstmt.setString(2, villedep);
                pstmt.setString(3, destin);

                rs = pstmt.executeQuery();

                d.setRowCount(0);
                while (rs.next()) {
                    d.addRow(new Object[]{rs.getInt(1), rs.getDate("datev"), rs.getString("VilleDep"), rs.getString("Destination"), rs.getString("HeureDep"), rs.getString("Duree"), rs.getString("matricule"), rs.getString("prix"),rs.getInt("nbrpd")});
                }
                pstmt.close();
                rs.close();

            } catch (SQLException ex) {
                Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_btnRechercheVActionPerformed

    private void btnReserverMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReserverMouseEntered
        btnReserver.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnReserverMouseEntered

    private void btnReserverMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReserverMouseExited
        btnReserver.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnReserverMouseExited

    private void btnReserverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReserverActionPerformed
         String cin = null;
        try {
            PreparedStatement pstmt = con.prepareStatement("select upper(cinCl) from client where upper(cincl)=upper(?)");
            pstmt.setString(1, txtCl.getText());
            ResultSet rss = pstmt.executeQuery();

            if (rss.next()) {
                cin = rss.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            if (txtCl.getText().trim().equals(cin)) {

                PreparedStatement pstmt1 = con.prepareStatement("select idcl from client where upper(cincl)=?");
                pstmt1.setString(1, txtCl.getText());
                rs = pstmt1.executeQuery();

                DefaultTableModel d = (DefaultTableModel) jTable4.getModel();
                int row = -1;
                row = jTable4.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un voyage");

                } else {

                    int voy = (int) jTable4.getModel().getValueAt(row, 0);
                    PreparedStatement pstmt2 = con.prepareStatement("insert into reservation(idcl,numv) values(?,?) ");
                    int idcl = 0;
                    if (rs.next()) {
                        idcl = rs.getInt(1);
                        pstmt2.setInt(1, rs.getInt(1));
                    }
                    pstmt2.setInt(2, (voy));

                    if (pstmt2.executeUpdate() != 0) {
                        JOptionPane.showMessageDialog(null, "Votre voyage a été reserver avec succée");
                        txtCl.setText("");
                        jdateV.setDate(null);
                        cboVilleD.setSelectedItem(0);
                        cboDes.setSelectedIndex(0);
                        RemplirTableV();
                        
                       PreparedStatement pstmt3 = con.prepareStatement("select max(NumBillet) from reservation where idcl=? and numv=?");
                        pstmt3.setInt(1, idcl);
                        pstmt3.setInt(2, voy);
              
                       ResultSet rs1 = pstmt3.executeQuery();
                       int numb=0;
                         if (rs1.next()) {
                        numb = rs1.getInt(1);
                        
                    }

                        Map parametre = new HashMap();
                        parametre.put("pIDcl", idcl);
                        parametre.put("pNumv", voy);
                        parametre.put("pNumB", numb);

                        try {
                            JasperReport jr = JasperCompileManager.compileReport("C:\\Users\\intel\\JaspersoftWorkspace\\MyReports\\BilletM.jrxml");
                            JasperPrint jp = JasperFillManager.fillReport(jr, parametre, con);
                            JasperViewer.viewReport(jp, false);
                        } catch (JRException ex) {
                            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }

            } else {
                if (txtCl.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Veuillez choisir un client");

                } else {
                    JOptionPane.showMessageDialog(null, "client introuvable");
                }
            }
            RemplirTableV();
        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, "Autocar plein");
        }
    }//GEN-LAST:event_btnReserverActionPerformed

    private void jTableClEmpMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableClEmpMousePressed
        DefaultTableModel d = (DefaultTableModel) jTableClEmp.getModel();
        int row = jTableClEmp.getSelectedRow();
        ess = (String) jTableClEmp.getModel().getValueAt(row, 2);
    }//GEN-LAST:event_jTableClEmpMousePressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String query = jTextField1.getText().toLowerCase();

        filterClient(query);
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTableClEmpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableClEmpMouseClicked

    }//GEN-LAST:event_jTableClEmpMouseClicked

    private void btnAnnulerRecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnnulerRecMouseClicked

    }//GEN-LAST:event_btnAnnulerRecMouseClicked

    private void btnAnnulerRecMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnnulerRecMouseEntered
        btnAnnulerRec.setBackground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnAnnulerRecMouseEntered

    private void btnAnnulerRecMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnnulerRecMouseExited
        btnAnnulerRec.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnAnnulerRecMouseExited

    private void btnAnnulerRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnulerRecActionPerformed
        txtclRec.setText("");
        txtObjet.setText("");
        txtReclamation.setText("");

        //System.out.println(txtNU.getText());
    }//GEN-LAST:event_btnAnnulerRecActionPerformed

    private void btnRafraichirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRafraichirMouseClicked

    }//GEN-LAST:event_btnRafraichirMouseClicked

    private void btnRafraichirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRafraichirMouseEntered
        btnRafraichir.setBackground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnRafraichirMouseEntered

    private void btnRafraichirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRafraichirMouseExited
        btnRafraichir.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnRafraichirMouseExited

    private void btnRafraichirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRafraichirActionPerformed
        txtCl.setText("");
        jdateV.setDate(null);
         cboVilleD.setSelectedIndex(0);
        cboDes.setSelectedIndex(0);
        RemplirTableV();


    }//GEN-LAST:event_btnRafraichirActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jButton5.setBackground(new Color(221, 255, 221));
        jButton1.setBackground(new Color(255, 255, 153));
        jButton3.setBackground(new Color(255, 255, 153));
        jButton4.setBackground(new Color(255, 255, 153));
        jButton2.setBackground(new Color(255, 255, 153));
        jLayeredPane1.removeAll();
        jLayeredPane1.add(pnl4);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();

        try {

            PreparedStatement pstmt = con.prepareStatement("select * from client where cinCl=?");
            pstmt.setString(1, MainEmployes.ess);
            ResultSet rs5 = pstmt.executeQuery();
            while (rs5.next()) {
                //jLabel11.setText(rs5.getString("nomCl").toUpperCase() +" "+ rs5.getString("prenomCl").toUpperCase());
                tcinclient.setText(rs5.getString(4).toUpperCase());

            }
        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }

        RemplirTableReservation();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnBillet1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillet1MouseEntered
        btnBillet1.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnBillet1MouseEntered

    private void btnBillet1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBillet1MouseExited
        btnBillet1.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnBillet1MouseExited

    private void btnBillet1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillet1ActionPerformed
        DefaultTableModel d = (DefaultTableModel) jTableRes.getModel();
        int row = -1;
        row = jTableRes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une réservation");

        } else {

            int voy = (int) jTableRes.getModel().getValueAt(row, 0);

            int row2 = -1;
            row2 = jTableRes.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner une réservation");

            } else {

                int voy2 = (int) jTableRes.getModel().getValueAt(row2, 4);
                
                
                int row3 = -1;
            row3 = jTableRes.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Veuillez sélectionner une réservation");

            } else {

                int voy3 = (int) jTableRes.getModel().getValueAt(row3, 6);
                

                Map parametre = new HashMap();

                parametre.put("pIDcl", voy);

                parametre.put("pNumv", voy2);
                
                parametre.put("pNumB", voy3);
                
                try {
                    JasperReport jr = JasperCompileManager.compileReport("C:\\Users\\intel\\JaspersoftWorkspace\\MyReports\\BilletM.jrxml");
                    JasperPrint jp = JasperFillManager.fillReport(jr, parametre, con);
                    JasperViewer.viewReport(jp, false);
                } catch (JRException ex) {
                    Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            }
        }
       // RemplirTableReservation();
    }//GEN-LAST:event_btnBillet1ActionPerformed

    private void tcinclientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tcinclientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tcinclientActionPerformed

    private void btnRechercheV1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRechercheV1MouseEntered
        btnRechercheV1.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnRechercheV1MouseEntered

    private void btnRechercheV1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRechercheV1MouseExited
        btnRechercheV1.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnRechercheV1MouseExited

    private void btnRechercheV1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechercheV1ActionPerformed
        DefaultTableModel d = (DefaultTableModel) jTableRes.getModel();
        String req = "select * from client join reservation using(idcl) join voyage using(numv) where upper(cincl)=? order by dater";
        String cin = tcinclient.getText();
        if (cin.trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer cin");
        } else {

            try {
                PreparedStatement pstmt = con.prepareStatement(req);
                pstmt.setString(1, cin.toUpperCase());

                rs = pstmt.executeQuery();

                d.setRowCount(0);
                while (rs.next()) {
                    d.addRow(new Object[]{rs.getInt("idcl"), rs.getString("nomcl"), rs.getString("prenomCl"), rs.getString("cincl"), rs.getInt("numv"), rs.getDate("dateR"), rs.getInt("NumBillet"), rs.getInt("numplace")});
                }
                pstmt.close();
                rs.close();

            } catch (SQLException ex) {
                Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnRechercheV1ActionPerformed

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered

    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseExited

    }//GEN-LAST:event_jButton1MouseExited

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
//               jButton1.setBackground(new Color(221,255,221))   ; 

    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseExited
        jButton4.setBackground(new Color(255, 255, 153));
    }//GEN-LAST:event_jButton4MouseExited

    private void jButton4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseEntered

        jButton4.setBackground(new Color(221, 255, 221));
    }//GEN-LAST:event_jButton4MouseEntered

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
            java.util.logging.Logger.getLogger(MainEmployes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainEmployes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainEmployes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainEmployes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainEmployes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAjouterRec;
    private javax.swing.JButton btnAnnulerRec;
    private javax.swing.JButton btnBillet1;
    private javax.swing.JButton btnNouveauCl2;
    private javax.swing.JButton btnRafraichir;
    private javax.swing.JButton btnRechercheV;
    private javax.swing.JButton btnRechercheV1;
    private javax.swing.JButton btnReserver;
    private javax.swing.JButton btnSuppC;
    private javax.swing.JButton btnUpdateC;
    private javax.swing.JComboBox<String> cboDes;
    private javax.swing.JComboBox<String> cboVilleD;
    private javax.swing.JLabel exit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelButton;
    private javax.swing.JPanel jPanelTitle;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable4;
    public static javax.swing.JTable jTableClEmp;
    private javax.swing.JTable jTableRes;
    private javax.swing.JTextField jTextField1;
    private com.toedter.calendar.JDateChooser jdateV;
    private javax.swing.JPanel pnl1;
    private javax.swing.JPanel pnl2;
    private javax.swing.JPanel pnl3;
    private javax.swing.JPanel pnl4;
    private javax.swing.JPanel pnl6;
    private javax.swing.JTextField tcinclient;
    private javax.swing.JTextField txtCl;
    private javax.swing.JTextField txtObjet;
    private javax.swing.JTextArea txtReclamation;
    private javax.swing.JTextField txtclRec;
    // End of variables declaration//GEN-END:variables
}
