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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.prompt.PromptSupport;
import static projet_fe.MainEmployes.jTableClEmp;

/**
 *
 * @author intel
 */
public final class MainAdmin extends javax.swing.JFrame {

    public static String eve;
    public static String tab;
    public static int idv;
    int mouseX = 0;
    int mouseY = 0;

    static Connection con = ConnectionUtil.getConnection();

    /**
     * Creates new form main
     */
    public MainAdmin() {
        initComponents();

        RemplirTableauEmp();
        RemplirTableauClient();
        RemplirCars();
        Prompt();
        RemplirTableVoyage();
        RemplirTableauReclamation();
        RemplirCboAutocar();
        HeaderJtable();

        TableColumnModel tc = jTableRec.getColumnModel();
        tc.removeColumn(tc.getColumn(0));
        //hide column
        TableColumnModel tcm = jTable4.getColumnModel();
        tcm.removeColumn(tcm.getColumn(0));
        jButton1.setBackground(new Color(221, 255, 221));

        jLayeredPane1.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(0, 153, 0)));
        jPanelButton.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, new Color(0, 153, 0)));
        jPanelTitle.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 153, 0)));
    }

    public void Prompt() {
        PromptSupport.setPrompt("Entrer le matricule", txtMat);
        PromptSupport.setPrompt("Entrer la marque", txtMarque);
        PromptSupport.setPrompt("Entrer le modèle ", txtModele);

        // PromptSupport.setPrompt("Choisir la ville départ ", cboVD);
        // PromptSupport.setPrompt("Choidir la déstination ", txtModele);
        PromptSupport.setPrompt("Entrer l'heure départ ", txtHD);
        PromptSupport.setPrompt("Entrer la durée ", txtDuree);
        //PromptSupport.setPrompt("Entrer matricule ", txtAuto);

        PromptSupport.setPrompt("Entrer le prix ", txtPrix);

        PromptSupport.setPrompt("Entrer CIN ou Télephone", txtSerchCll);

        PromptSupport.setPrompt("Entrer CIN ou Télephone", txtSearchEmp);

        PromptSupport.setPrompt("Entrer matricule ", jTextField1);
        
         PromptSupport.setPrompt("Entrer CIN ", txtClRech);

    }

    public static void RemplirTableauEmp() {
        DefaultTableModel d = (DefaultTableModel) jTableEmp.getModel();
        try {

            PreparedStatement pstmt = con.prepareStatement("select * from employe");

            ResultSet rs1 = pstmt.executeQuery();
            d.setRowCount(0);
            while (rs1.next()) {
                d.addRow(new Object[]{rs1.getString(2), rs1.getString(3), rs1.getString(4), rs1.getString(5), rs1.getString(6), rs1.getString(7), rs1.getString(8), rs1.getString(9), rs1.getString(10)});
            }
            rs1.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RemplirTableauReclamation() {
        DefaultTableModel d = (DefaultTableModel) jTableRec.getModel();
        try {

            PreparedStatement pstmt = con.prepareStatement("select * from reclamation join client using (idcl)");

            ResultSet rs6 = pstmt.executeQuery();
            d.setRowCount(0);
            while (rs6.next()) {
                d.addRow(new Object[]{rs6.getInt("NumRec"), rs6.getString("cincl"), rs6.getString("objetrec"), rs6.getString("contenurec"), rs6.getString("etat")});
            }
            rs6.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void RemplirTableauClient() {
        DefaultTableModel d1 = (DefaultTableModel) jTableclll.getModel();

        try {
            PreparedStatement pstmt = con.prepareStatement("select * from client");

            ResultSet rs2 = pstmt.executeQuery();
            d1.setRowCount(0);
            while (rs2.next()) {
                d1.addRow(new Object[]{rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getString(5), rs2.getString(6), rs2.getString(7), rs2.getString(8)});
            }
            rs2.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void RemplirCars() {
        try {
            DefaultTableModel d = (DefaultTableModel) tableCars.getModel();

            PreparedStatement pstmt = con.prepareStatement("select * from autocar");

            ResultSet rs3 = pstmt.executeQuery();
            d.setRowCount(0);
            while (rs3.next()) {
                d.addRow(new Object[]{rs3.getString(1), rs3.getString(2), rs3.getString(3), rs3.getInt(4)});
            }
            rs3.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean verifierField() {
        String marque = txtMarque.getText();
        String modele = txtModele.getText();
        String mat = txtMat.getText();
        int nbr = (int) spnPlace.getValue();

        if (marque.trim().equals("") || modele.trim().equals("") || mat.trim().equals("") || nbr == 0) {

            JOptionPane.showMessageDialog(null, "Verifier les champs", "Champs vides", 2);
            return false;

        } else {
            return true;
        }
    }

    public boolean verifierF() {
        Calendar dateV = jdateVoy.getCalendar();
        String villeDep = (String) cboVD.getSelectedItem();
        String dest = (String) cboDest.getSelectedItem();
        String heureDep = txtHD.getText();
        String duree = txtDuree.getText();
        // String autocar = txtAuto.getText();
        String autocar = (String) cboAutocar.getSelectedItem();
        String prix = txtPrix.getText();

        if (dateV == null || villeDep.trim().equals("--choisissez une ville--") || dest.trim().equals("--choisissez une ville--") || heureDep.trim().equals("")
                || duree.trim().equals("") || autocar.trim().equals("--choisissez l'autocar--") || prix.trim().equals("")) {

            JOptionPane.showMessageDialog(null, "Verifier les champs", "Champs vides", 2);
            return false;

        } else if (villeDep == dest) {
            JOptionPane.showMessageDialog(null, "Vile départ et la déstination sont identique", "ville identique", 2);
            cboVD.setSelectedIndex(0);
            cboDest.setSelectedIndex(0);

        } else {
            return true;
        }
        return false;

    }

    public void RemplirTableVoyage() {
        try {
            DefaultTableModel d = (DefaultTableModel) jTable4.getModel();

            PreparedStatement pstmt = con.prepareStatement("select * from voyage order by datev");

            ResultSet rs4 = pstmt.executeQuery();
            d.setRowCount(0);
            while (rs4.next()) {
                idv = rs4.getInt(1);
                d.addRow(new Object[]{idv, rs4.getDate(2), rs4.getString(3), rs4.getString(4), rs4.getString(5), rs4.getString(6), rs4.getString(7), rs4.getString(8)});
            }
            rs4.close();
            pstmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void filterEmp(String query) {
        DefaultTableModel d = (DefaultTableModel) jTableEmp.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(d);
        jTableEmp.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(query));
    }

    private void filtercl(String quer) {
        DefaultTableModel d = (DefaultTableModel) jTableclll.getModel();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(d);
        jTableclll.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(quer));

    }

    public void HeaderJtable() {
        JTableHeader Theader = jTableEmp.getTableHeader();
        Theader.setBackground(Color.red);
        Theader.setForeground(new Color(0, 153, 0));
        Theader.setFont(new Font("Tahome", Font.BOLD, 14));

        JTableHeader Theade = jTableclll.getTableHeader();
        Theade.setBackground(Color.red);
        Theade.setForeground(new Color(0, 153, 0));
        Theade.setFont(new Font("Tahome", Font.BOLD, 14));

        JTableHeader Thead = jTable4.getTableHeader();
        Thead.setBackground(Color.red);
        Thead.setForeground(new Color(0, 153, 0));
        Thead.setFont(new Font("Tahome", Font.BOLD, 14));

        JTableHeader Thea = jTableRec.getTableHeader();
        Thea.setBackground(Color.red);
        Thea.setForeground(new Color(0, 153, 0));
        Thea.setFont(new Font("Tahome", Font.BOLD, 14));

        JTableHeader The = tableCars.getTableHeader();
        The.setBackground(Color.red);
        The.setForeground(new Color(0, 153, 0));
        The.setFont(new Font("Tahome", Font.BOLD, 14));

    }

    public void RemplirCboAutocar() {

        try {
            PreparedStatement pstmt = con.prepareStatement("select matricule from autocar");
            ResultSet rs10 = pstmt.executeQuery();

            while (rs10.next()) {

                cboAutocar.addItem(rs10.getString(1));
            }
            rs10.close();
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanelTitle = new javax.swing.JPanel();
        exit = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanelButton = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btnDec = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        pnl1 = new javax.swing.JPanel();
        btnSupprEmp = new javax.swing.JButton();
        btnMiseàJourE = new javax.swing.JButton();
        btnNouveauE = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableEmp = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtSearchEmp = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        pnl2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtSerchCll = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableclll = new javax.swing.JTable();
        btnUpdateC = new javax.swing.JButton();
        btnSuppC = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        btnNouveauCl2 = new javax.swing.JButton();
        pnl3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jdateVoy = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        cboVD = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cboDest = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtHD = new javax.swing.JTextField();
        txtDuree = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPrix = new javax.swing.JTextField();
        btnAjouter = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        cboAutocar = new javax.swing.JComboBox<>();
        btnAnnuler = new javax.swing.JButton();
        btnSupprimer = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        btnAjouter1 = new javax.swing.JButton();
        pnl4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableRec = new javax.swing.JTable();
        btnTraiter = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txtClRech = new javax.swing.JTextField();
        btnRechClient = new javax.swing.JButton();
        btnSupprimer1 = new javax.swing.JButton();
        btnreff = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jRadioButton1 = new javax.swing.JRadioButton();
        pnl5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableCars = new javax.swing.JTable();
        btnSuppCar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        btnNautocar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtMat = new javax.swing.JTextField();
        txtMarque = new javax.swing.JTextField();
        txtModele = new javax.swing.JTextField();
        spnPlace = new javax.swing.JSpinner();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        btnRechercheV1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanelTitle.setBackground(new java.awt.Color(0, 153, 0));
        jPanelTitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0)));
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

        exit.setBackground(new java.awt.Color(255, 255, 255));
        exit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        exit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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

        jLabel6.setFont(new java.awt.Font("Arial Black", 1, 28)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("ESPACE ADMINISTRATIF");

        javax.swing.GroupLayout jPanelTitleLayout = new javax.swing.GroupLayout(jPanelTitle);
        jPanelTitle.setLayout(jPanelTitleLayout);
        jPanelTitleLayout.setHorizontalGroup(
            jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTitleLayout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(126, 126, 126)
                .addComponent(jLabel5)
                .addGap(56, 56, 56)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exit))
        );
        jPanelTitleLayout.setVerticalGroup(
            jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelTitleLayout.createSequentialGroup()
                .addGroup(jPanelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanelTitleLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelButton.setBackground(new java.awt.Color(221, 255, 221));
        jPanelButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));

        jButton1.setBackground(new java.awt.Color(255, 255, 153));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_employee_30px.png"))); // NOI18N
        jButton1.setText("Employes");
        jButton1.setBorder(null);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.setVerifyInputWhenFocusTarget(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 153));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/user_30px.png"))); // NOI18N
        jButton2.setText("Clients");
        jButton2.setBorder(null);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusPainted(false);
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 153));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/traveler_30px.png"))); // NOI18N
        jButton3.setText("Voyages");
        jButton3.setBorder(null);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 153));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_bus_30px.png"))); // NOI18N
        jButton5.setText("Autocars");
        jButton5.setBorder(null);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.setFocusPainted(false);
        jButton5.setFocusable(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 255, 153));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_requirement_30px.png"))); // NOI18N
        jButton4.setText("Réclamations");
        jButton4.setBorder(null);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setFocusPainted(false);
        jButton4.setFocusable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        btnDec.setBackground(new java.awt.Color(255, 255, 153));
        btnDec.setFont(new java.awt.Font("Arial Black", 1, 21)); // NOI18N
        btnDec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_exit_30px.png"))); // NOI18N
        btnDec.setText("Déconnexion");
        btnDec.setBorder(null);
        btnDec.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDec.setFocusPainted(false);
        btnDec.setFocusable(false);
        btnDec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDecMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDecMouseExited(evt);
            }
        });
        btnDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDecActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelButtonLayout = new javax.swing.GroupLayout(jPanelButton);
        jPanelButton.setLayout(jPanelButtonLayout);
        jPanelButtonLayout.setHorizontalGroup(
            jPanelButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelButtonLayout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnDec, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanelButtonLayout.setVerticalGroup(
            jPanelButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelButtonLayout.createSequentialGroup()
                .addGroup(jPanelButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDec, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLayeredPane1.setBackground(new java.awt.Color(221, 255, 221));
        jLayeredPane1.setLayout(new java.awt.CardLayout());

        pnl1.setBackground(new java.awt.Color(221, 255, 221));

        btnSupprEmp.setBackground(new java.awt.Color(66, 160, 66));
        btnSupprEmp.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSupprEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_delete_user_male_30px_1.png"))); // NOI18N
        btnSupprEmp.setText("Supprimer");
        btnSupprEmp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupprEmp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupprEmpMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSupprEmpMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSupprEmpMouseExited(evt);
            }
        });
        btnSupprEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprEmpActionPerformed(evt);
            }
        });

        btnMiseàJourE.setBackground(new java.awt.Color(66, 160, 66));
        btnMiseàJourE.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnMiseàJourE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_update_30px_3.png"))); // NOI18N
        btnMiseàJourE.setText("Mise à jour");
        btnMiseàJourE.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMiseàJourE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMiseàJourEMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMiseàJourEMouseExited(evt);
            }
        });
        btnMiseàJourE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMiseàJourEActionPerformed(evt);
            }
        });

        btnNouveauE.setBackground(new java.awt.Color(66, 160, 66));
        btnNouveauE.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnNouveauE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_add_user_male_30px_1.png"))); // NOI18N
        btnNouveauE.setText("Nouveau employé");
        btnNouveauE.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNouveauE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNouveauEMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNouveauEMouseExited(evt);
            }
        });
        btnNouveauE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNouveauEActionPerformed(evt);
            }
        });

        jTableEmp.setBackground(new java.awt.Color(255, 255, 153));
        jTableEmp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
        jTableEmp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableEmp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nom", "Prénom", "Cin", "Tél", "Adresse", "Email", "Sexe", "Nom utilisateur", "Mot de passe"
            }
        ));
        jTableEmp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableEmpMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableEmpMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableEmp);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Rechercher Employes:");

        txtSearchEmp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtSearchEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchEmpActionPerformed(evt);
            }
        });
        txtSearchEmp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchEmpKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnl1Layout = new javax.swing.GroupLayout(pnl1);
        pnl1.setLayout(pnl1Layout);
        pnl1Layout.setHorizontalGroup(
            pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl1Layout.createSequentialGroup()
                .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(pnl1Layout.createSequentialGroup()
                        .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnl1Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel1)
                                .addGap(37, 37, 37)
                                .addComponent(txtSearchEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnl1Layout.createSequentialGroup()
                                .addGap(245, 245, 245)
                                .addComponent(btnNouveauE, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnMiseàJourE, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSupprEmp)))
                        .addGap(0, 212, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(271, 271, 271))
        );
        pnl1Layout.setVerticalGroup(
            pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSearchEmp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNouveauE)
                    .addComponent(btnMiseàJourE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSupprEmp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jLayeredPane1.add(pnl1, "card2");

        pnl2.setBackground(new java.awt.Color(221, 255, 221));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Rechercher client:");

        txtSerchCll.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtSerchCll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSerchCllActionPerformed(evt);
            }
        });
        txtSerchCll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSerchCllKeyReleased(evt);
            }
        });

        jTableclll.setBackground(new java.awt.Color(255, 255, 153));
        jTableclll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
        jTableclll.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableclll.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nom", "Prénom", "Cin", "Tél", "Adresse", "Email", "Sexe"
            }
        ));
        jTableclll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableclllMousePressed(evt);
            }
        });
        jTableclll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableclllKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTableclll);

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

        javax.swing.GroupLayout pnl2Layout = new javax.swing.GroupLayout(pnl2);
        pnl2.setLayout(pnl2Layout);
        pnl2Layout.setHorizontalGroup(
            pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSerchCll, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(270, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNouveauCl2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdateC, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSuppC)
                .addGap(231, 231, 231))
            .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1042, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnl2Layout.setVerticalGroup(
            pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl2Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSerchCll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 348, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateC)
                    .addComponent(btnSuppC)
                    .addComponent(btnNouveauCl2))
                .addGap(82, 82, 82))
            .addGroup(pnl2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnl2Layout.createSequentialGroup()
                    .addGap(92, 92, 92)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(173, Short.MAX_VALUE)))
        );

        jLayeredPane1.add(pnl2, "card3");

        pnl3.setBackground(new java.awt.Color(221, 255, 221));

        jTable4.setBackground(new java.awt.Color(255, 255, 153));
        jTable4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
        jTable4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "numv", "Date voyage", "Ville départ", "Destination", "Heure départ", "Durée", "Autocar", "Prix"
            }
        ));
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable4MousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Date voyage:");

        jdateVoy.setBackground(new java.awt.Color(204, 255, 204));
        jdateVoy.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));
        jdateVoy.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Ville départ:");

        cboVD.setBackground(new java.awt.Color(204, 255, 204));
        cboVD.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cboVD.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--choisissez une ville--", "Casablanca", "Marrakech", "Agadir", "El Jadida", "Tanger", "Rabat", "Tétouan", "Oujda", "Fés", "Salé", "Khouribga" }));
        cboVD.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Destination:");

        cboDest.setBackground(new java.awt.Color(204, 255, 204));
        cboDest.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cboDest.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--choisissez une ville--", "Casablanca", "Marrakech", "Agadir", "El Jadida", "Tanger", "Rabat", "Tétouan", "Oujda", "Fés", "Salé", "Khouribga" }));
        cboDest.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Heure départ:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Autocar:");

        txtHD.setBackground(new java.awt.Color(204, 255, 204));
        txtHD.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtHD.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));
        txtHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHDActionPerformed(evt);
            }
        });

        txtDuree.setBackground(new java.awt.Color(204, 255, 204));
        txtDuree.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtDuree.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));
        txtDuree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDureeActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Prix:");

        txtPrix.setBackground(new java.awt.Color(204, 255, 204));
        txtPrix.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtPrix.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));

        btnAjouter.setBackground(new java.awt.Color(66, 160, 66));
        btnAjouter.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnAjouter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_add_30px_2.png"))); // NOI18N
        btnAjouter.setText("Ajouter");
        btnAjouter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAjouter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAjouterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAjouterMouseExited(evt);
            }
        });
        btnAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjouterActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Durée:");

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        cboAutocar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cboAutocar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--choisissez l'autocar--" }));
        cboAutocar.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));

        btnAnnuler.setBackground(new java.awt.Color(66, 160, 66));
        btnAnnuler.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnAnnuler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_cancel_30px_1.png"))); // NOI18N
        btnAnnuler.setText("Annuler");
        btnAnnuler.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAnnuler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAnnulerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAnnulerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAnnulerMouseExited(evt);
            }
        });
        btnAnnuler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnnulerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(22, 22, 22)
                                .addComponent(jdateVoy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboVD, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboDest, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(35, 35, 35)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDuree)
                            .addComponent(txtHD)
                            .addComponent(txtPrix)
                            .addComponent(cboAutocar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(115, 115, 115)
                                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(207, 207, 207)
                                .addComponent(btnAjouter, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnAnnuler)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jdateVoy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(txtDuree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(cboVD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(cboDest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(txtHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(cboAutocar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtPrix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAjouter)
                    .addComponent(btnAnnuler))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        btnSupprimer.setBackground(new java.awt.Color(66, 160, 66));
        btnSupprimer.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSupprimer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_cancel_30px_1.png"))); // NOI18N
        btnSupprimer.setText("Supprimer");
        btnSupprimer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupprimer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupprimerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSupprimerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSupprimerMouseExited(evt);
            }
        });
        btnSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprimerActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel18.setText("Date voyage:");

        jDateChooser1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jDateChooser1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jDateChooser1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jDateChooser1MouseReleased(evt);
            }
        });
        jDateChooser1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateChooser1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jDateChooser1KeyTyped(evt);
            }
        });

        btnAjouter1.setBackground(new java.awt.Color(66, 160, 66));
        btnAjouter1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnAjouter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_search_30px.png"))); // NOI18N
        btnAjouter1.setText("Rechercher");
        btnAjouter1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAjouter1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAjouter1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAjouter1MouseExited(evt);
            }
        });
        btnAjouter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjouter1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl3Layout = new javax.swing.GroupLayout(pnl3);
        pnl3.setLayout(pnl3Layout);
        pnl3Layout.setHorizontalGroup(
            pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl3Layout.createSequentialGroup()
                .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl3Layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl3Layout.createSequentialGroup()
                        .addGap(431, 431, 431)
                        .addComponent(btnSupprimer))
                    .addGroup(pnl3Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 967, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnl3Layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnAjouter1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        pnl3Layout.setVerticalGroup(
            pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl3Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl3Layout.createSequentialGroup()
                        .addGap(0, 12, Short.MAX_VALUE)
                        .addGroup(pnl3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl3Layout.createSequentialGroup()
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl3Layout.createSequentialGroup()
                                .addComponent(btnAjouter1)
                                .addGap(18, 18, 18)))))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSupprimer)
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane1.add(pnl3, "card4");

        pnl4.setBackground(new java.awt.Color(221, 255, 221));

        jTableRec.setBackground(new java.awt.Color(255, 255, 153));
        jTableRec.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
        jTableRec.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTableRec.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NumRec", "Client", "Objet", "Contenu", "Etat"
            }
        ));
        jTableRec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableRecMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTableRec);

        btnTraiter.setBackground(new java.awt.Color(66, 160, 66));
        btnTraiter.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnTraiter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_checked_30px.png"))); // NOI18N
        btnTraiter.setText("Traiter");
        btnTraiter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTraiter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTraiterMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTraiterMouseExited(evt);
            }
        });
        btnTraiter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraiterActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel19.setText("Client:");

        txtClRech.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtClRech.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClRechActionPerformed(evt);
            }
        });

        btnRechClient.setBackground(new java.awt.Color(66, 160, 66));
        btnRechClient.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnRechClient.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_search_30px.png"))); // NOI18N
        btnRechClient.setText("Rechercher");
        btnRechClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRechClient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRechClientMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRechClientMouseExited(evt);
            }
        });
        btnRechClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechClientActionPerformed(evt);
            }
        });

        btnSupprimer1.setBackground(new java.awt.Color(66, 160, 66));
        btnSupprimer1.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSupprimer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_cancel_30px_1.png"))); // NOI18N
        btnSupprimer1.setText("Supprimer");
        btnSupprimer1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSupprimer1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSupprimer1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSupprimer1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSupprimer1MouseExited(evt);
            }
        });
        btnSupprimer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprimer1ActionPerformed(evt);
            }
        });

        btnreff.setBackground(new java.awt.Color(66, 160, 66));
        btnreff.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnreff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_refresh_30px_1.png"))); // NOI18N
        btnreff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnreff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnreffMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnreffMouseExited(evt);
            }
        });
        btnreff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreffActionPerformed(evt);
            }
        });

        jRadioButton1.setBackground(new java.awt.Color(221, 255, 221));
        jRadioButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRadioButton1.setText("Reclamations non traités");
        jRadioButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_unchecked_checkbox_30px.png"))); // NOI18N
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnl4Layout = new javax.swing.GroupLayout(pnl4);
        pnl4.setLayout(pnl4Layout);
        pnl4Layout.setHorizontalGroup(
            pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1042, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTraiter, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnSupprimer1)
                .addGap(36, 36, 36)
                .addComponent(btnreff)
                .addGap(312, 312, 312))
            .addGroup(pnl4Layout.createSequentialGroup()
                .addGroup(pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl4Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtClRech, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnRechClient, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(160, 160, 160)
                        .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl4Layout.createSequentialGroup()
                        .addGap(209, 209, 209)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 605, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnl4Layout.setVerticalGroup(
            pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl4Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtClRech, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRechClient)
                    .addComponent(jRadioButton1))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnreff, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTraiter)
                        .addComponent(btnSupprimer1)))
                .addGap(87, 87, 87))
        );

        jLayeredPane1.add(pnl4, "card5");

        pnl5.setBackground(new java.awt.Color(221, 255, 221));

        tableCars.setBackground(new java.awt.Color(255, 255, 153));
        tableCars.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
        tableCars.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tableCars.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Matricule", "Marque", "Modèle", "Nombre de places"
            }
        ));
        tableCars.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableCarsMousePressed(evt);
            }
        });
        jScrollPane5.setViewportView(tableCars);

        btnSuppCar.setBackground(new java.awt.Color(66, 160, 66));
        btnSuppCar.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnSuppCar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_cancel_30px_1.png"))); // NOI18N
        btnSuppCar.setText("Supprimer");
        btnSuppCar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSuppCar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSuppCarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSuppCarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSuppCarMouseExited(evt);
            }
        });
        btnSuppCar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuppCarActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));

        btnNautocar.setBackground(new java.awt.Color(66, 160, 66));
        btnNautocar.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnNautocar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_add_30px_2.png"))); // NOI18N
        btnNautocar.setText("Nouveau autocar");
        btnNautocar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNautocar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNautocarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNautocarMouseExited(evt);
            }
        });
        btnNautocar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNautocarActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("Matricule:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setText("Modèle:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel14.setText("Marque:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel16.setText("Nbr de places:");

        txtMat.setBackground(new java.awt.Color(204, 255, 204));
        txtMat.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtMat.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));
        txtMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMatActionPerformed(evt);
            }
        });

        txtMarque.setBackground(new java.awt.Color(204, 255, 204));
        txtMarque.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtMarque.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));
        txtMarque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMarqueActionPerformed(evt);
            }
        });

        txtModele.setBackground(new java.awt.Color(204, 255, 204));
        txtModele.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtModele.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));
        txtModele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModeleActionPerformed(evt);
            }
        });

        spnPlace.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        spnPlace.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        spnPlace.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 230, 64), new java.awt.Color(0, 230, 64)));
        spnPlace.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spnPlace, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMat, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtModele, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMarque, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addComponent(btnNautocar, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(spnPlace))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtMat, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMarque, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtModele, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNautocar)
                .addGap(25, 25, 25))
        );

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

        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel17.setText("Matricule:");

        javax.swing.GroupLayout pnl5Layout = new javax.swing.GroupLayout(pnl5);
        pnl5.setLayout(pnl5Layout);
        pnl5Layout.setHorizontalGroup(
            pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl5Layout.createSequentialGroup()
                .addContainerGap(101, Short.MAX_VALUE)
                .addGroup(pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnl5Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(btnRechercheV1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 872, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(89, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl5Layout.createSequentialGroup()
                        .addComponent(btnSuppCar)
                        .addGap(444, 444, 444))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnl5Layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(361, 361, 361))))
        );
        pnl5Layout.setVerticalGroup(
            pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl5Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(pnl5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRechercheV1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSuppCar)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jLayeredPane1.add(pnl5, "card6");

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
        this.setState(MainAdmin.ICONIFIED);
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
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jButton3.setBackground(new Color(221, 255, 221));
        jButton1.setBackground(new Color(255, 255, 153));
        jButton2.setBackground(new Color(255, 255, 153));
        jButton4.setBackground(new Color(255, 255, 153));
        jButton5.setBackground(new Color(255, 255, 153));
        jLayeredPane1.removeAll();
        jLayeredPane1.add(pnl3);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();

        cboAutocar.removeAllItems();
        cboAutocar.addItem("--choisissez l'autocar--");
        RemplirCboAutocar();


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jButton4.setBackground(new Color(221, 255, 221));
        jButton2.setBackground(new Color(255, 255, 153));
        jButton3.setBackground(new Color(255, 255, 153));
        jButton1.setBackground(new Color(255, 255, 153));
        jButton5.setBackground(new Color(255, 255, 153));
        jLayeredPane1.removeAll();
        jLayeredPane1.add(pnl4);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();
        try {
            PreparedStatement pstmt = con.prepareStatement("select * from client where cinCl=?");
            pstmt.setString(1, MainAdmin.tab);
            ResultSet rs5 = pstmt.executeQuery();
            while (rs5.next()) {
                //jLabel11.setText(rs5.getString("nomCl").toUpperCase() +" "+ rs5.getString("prenomCl").toUpperCase());
                txtClRech.setText(rs5.getString(4).toUpperCase());
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainEmployes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jButton5.setBackground(new Color(221, 255, 221));
        jButton2.setBackground(new Color(255, 255, 153));
        jButton3.setBackground(new Color(255, 255, 153));
        jButton4.setBackground(new Color(255, 255, 153));
        jButton1.setBackground(new Color(255, 255, 153));
        jLayeredPane1.removeAll();
        jLayeredPane1.add(pnl5);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnMiseàJourEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMiseàJourEActionPerformed
        int row = -1;
        row = jTableEmp.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé");
        } else {

            UpdateEmploye ue = new UpdateEmploye();
            ue.setVisible(true);
            ue.pack();
        }

        //this.dispose();
    }//GEN-LAST:event_btnMiseàJourEActionPerformed

    private void btnMiseàJourEMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMiseàJourEMouseExited
        btnMiseàJourE.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnMiseàJourEMouseExited

    private void btnMiseàJourEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMiseàJourEMouseEntered
        btnMiseàJourE.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnMiseàJourEMouseEntered

    private void btnSupprEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprEmpActionPerformed

        try {
            int row = -1;
            row = jTableEmp.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé");
            } else {
                int test = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer??");
                if (test == JOptionPane.YES_OPTION) {
                    PreparedStatement pstmt = con.prepareStatement("delete from employe where cinEmp=?");
                    pstmt.setString(1, eve);
                    pstmt.execute();
                    RemplirTableauEmp();
                } else {

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSupprEmpActionPerformed

    private void btnSupprEmpMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupprEmpMouseExited
        btnSupprEmp.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnSupprEmpMouseExited

    private void btnSupprEmpMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupprEmpMouseEntered
        btnSupprEmp.setBackground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnSupprEmpMouseEntered

    private void btnSupprEmpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupprEmpMouseClicked

    }//GEN-LAST:event_btnSupprEmpMouseClicked

    private void btnNouveauEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNouveauEMouseEntered
        btnNouveauE.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnNouveauEMouseEntered

    private void btnNouveauEMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNouveauEMouseExited
        btnNouveauE.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnNouveauEMouseExited

    private void btnNouveauEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNouveauEActionPerformed
        NouveauEmploye ne = new NouveauEmploye();
        ne.setVisible(true);
        ne.pack();
        //this.dispose();
    }//GEN-LAST:event_btnNouveauEActionPerformed

    private void txtSearchEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchEmpActionPerformed

    }//GEN-LAST:event_txtSearchEmpActionPerformed

    private void txtSerchCllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSerchCllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSerchCllActionPerformed

    private void btnUpdateCMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateCMouseEntered
        btnUpdateC.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnUpdateCMouseEntered

    private void btnUpdateCMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateCMouseExited
        btnUpdateC.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnUpdateCMouseExited

    private void btnUpdateCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateCActionPerformed
        int row = -1;
        row = jTableclll.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client");
        } else {

            UpdateClientA uc = new UpdateClientA();
            uc.setVisible(true);
            uc.pack();
        }
    }//GEN-LAST:event_btnUpdateCActionPerformed

    private void btnSuppCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuppCMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuppCMouseClicked

    private void btnSuppCMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuppCMouseEntered
        btnSuppC.setBackground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnSuppCMouseEntered

    private void btnSuppCMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuppCMouseExited
        btnSuppC.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnSuppCMouseExited

    private void btnSuppCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuppCActionPerformed

        try {
            int row = -1;
            row = jTableclll.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client");
            } else {
                int test = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer??");
                if (test == JOptionPane.YES_OPTION) {
                    PreparedStatement pstmt;

                    pstmt = con.prepareStatement("delete from client where cinCl=?");
                    pstmt.setString(1, tab);
                    pstmt.execute();
                    RemplirTableauClient();
                } else {

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnSuppCActionPerformed

    private void txtDureeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDureeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDureeActionPerformed

    private void btnAjouterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAjouterMouseEntered
        btnAjouter.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnAjouterMouseEntered

    private void btnAjouterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAjouterMouseExited
        btnAjouter.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnAjouterMouseExited

    private void btnAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouterActionPerformed
        Calendar dateV = jdateVoy.getCalendar();
        String villeDep = (String) cboVD.getSelectedItem();
        String dest = (String) cboDest.getSelectedItem();
        String heureDep = txtHD.getText();
        String duree = txtDuree.getText();
        // String autocar = txtAuto.getText();
        String autocar = (String) cboAutocar.getSelectedItem();
        String prix = txtPrix.getText();

        if (verifierF()) {

            try {
                PreparedStatement pstmt = con.prepareStatement("insert into voyage(datev, VilleDep, Destination, HeureDep, Duree, matricule, Prix) values (?,?,?,?,?,?,?)");
                pstmt.setDate(1, new java.sql.Date(dateV.getTimeInMillis()));
                pstmt.setString(2, villeDep);
                pstmt.setString(3, dest);
                pstmt.setString(4, heureDep);
                pstmt.setString(5, duree);
                pstmt.setString(6, autocar);
                pstmt.setString(7, prix);

                if (pstmt.executeUpdate() != 0) {
                    JOptionPane.showMessageDialog(null, " Voyage a été ajouter avec succès");
                    RemplirTableVoyage();
                    jdateVoy.setDate(null);
                    cboVD.setSelectedIndex(0);
                    cboDest.setSelectedIndex(0);
                    txtHD.setText("");
                    txtDuree.setText("");
                    cboAutocar.setSelectedIndex(0);
                    txtPrix.setText("");

                } else {
                    JOptionPane.showMessageDialog(null, "Erreur!!: Vérifiez les informations");

                }
            } catch (SQLException ex) {
                Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        validate();
    }//GEN-LAST:event_btnAjouterActionPerformed

    private void btnSupprimerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupprimerMouseClicked

    }//GEN-LAST:event_btnSupprimerMouseClicked

    private void btnSupprimerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupprimerMouseEntered
        btnSupprimer.setBackground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnSupprimerMouseEntered

    private void btnSupprimerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupprimerMouseExited
        btnSupprimer.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnSupprimerMouseExited

    private void btnSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimerActionPerformed
        DefaultTableModel d = (DefaultTableModel) jTable4.getModel();
        int row = -1;
        row = jTable4.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un voyage");
        } else {

            String voy = jTable4.getModel().getValueAt(row, 0).toString();
            try {
                int test = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer??");
                if (test == JOptionPane.YES_OPTION) {

                    PreparedStatement pstmt = con.prepareStatement("delete from voyage where numV=?");
                    pstmt.setInt(1, Integer.parseInt(voy));
                    pstmt.execute();
                    RemplirTableVoyage();
                } else {

                }
            } catch (SQLException ex) {
                Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_btnSupprimerActionPerformed

    private void jTableEmpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableEmpMouseClicked


    }//GEN-LAST:event_jTableEmpMouseClicked

    private void jTableEmpMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableEmpMousePressed
        DefaultTableModel d = (DefaultTableModel) jTableEmp.getModel();
        int row = jTableEmp.getSelectedRow();
        eve = (String) jTableEmp.getModel().getValueAt(row, 2);
    }//GEN-LAST:event_jTableEmpMousePressed

    private void jTableclllMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableclllMousePressed
        DefaultTableModel d = (DefaultTableModel) jTableclll.getModel();
        int row = jTableclll.getSelectedRow();
        tab = (String) jTableclll.getModel().getValueAt(row, 2);
    }//GEN-LAST:event_jTableclllMousePressed

    private void btnNautocarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNautocarActionPerformed
        String mat = txtMat.getText();
        String mar = txtMarque.getText();
        String mod = txtModele.getText();
        int nbr = (int) spnPlace.getValue();

        if (verifierField()) {

            try {

                PreparedStatement pstmt = con.prepareStatement("insert into autocar(matricule, marque, modele, nbrPlaces) values (?,?,?,?)");
                pstmt.setString(1, mat);
                pstmt.setString(2, mar);
                pstmt.setString(3, mod);
                pstmt.setInt(4, nbr);

                if (pstmt.executeUpdate() != 0) {
                    JOptionPane.showMessageDialog(null, " Autocar a été ajouter avec succès");
                    RemplirCars();
                    txtMarque.setText("");
                    txtMat.setText("");
                    txtModele.setText("");
                    spnPlace.setValue(0);

                } else {
                    JOptionPane.showMessageDialog(null, "Erreur!!: Vérifiez les informations");

                }
            } catch (SQLException ex) {
                Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_btnNautocarActionPerformed

    private void btnNautocarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNautocarMouseExited
        btnNautocar.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnNautocarMouseExited

    private void btnNautocarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNautocarMouseEntered
        btnNautocar.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnNautocarMouseEntered

    private void btnSuppCarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuppCarMouseClicked

    }//GEN-LAST:event_btnSuppCarMouseClicked

    private void btnSuppCarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuppCarMouseEntered
        btnSuppCar.setBackground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnSuppCarMouseEntered

    private void btnSuppCarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSuppCarMouseExited
        btnSuppCar.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnSuppCarMouseExited

    private void btnSuppCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuppCarActionPerformed
        DefaultTableModel d = (DefaultTableModel) tableCars.getModel();
        int row = -1;
        row = tableCars.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un autocar");
        } else {

            String scar = (String) tableCars.getModel().getValueAt(row, 0);

            try {
                int test = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer??");
                if (test == JOptionPane.YES_OPTION) {

                    PreparedStatement pstmt = con.prepareStatement("delete from autocar where matricule=?");
                    pstmt.setString(1, scar);
                    pstmt.execute();
                    RemplirCars();
                    jTextField1.setText("");

                } else {

                }

            } catch (SQLException ex) {
                Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_btnSuppCarActionPerformed

    private void txtMarqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMarqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMarqueActionPerformed

    private void jTable4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MousePressed

    }//GEN-LAST:event_jTable4MousePressed

    private void txtMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatActionPerformed

    private void tableCarsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCarsMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tableCarsMousePressed

    private void txtModeleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModeleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModeleActionPerformed

    private void txtSearchEmpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchEmpKeyReleased
        String query = txtSearchEmp.getText().toLowerCase();

        filterEmp(query);

    }//GEN-LAST:event_txtSearchEmpKeyReleased

    private void jTableclllKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableclllKeyReleased

    }//GEN-LAST:event_jTableclllKeyReleased

    private void txtSerchCllKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSerchCllKeyReleased
        String quer = txtSerchCll.getText().toLowerCase();

        filtercl(quer);
    }//GEN-LAST:event_txtSerchCllKeyReleased

    private void btnNouveauCl2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNouveauCl2MouseEntered
        btnNouveauCl2.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnNouveauCl2MouseEntered

    private void btnNouveauCl2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNouveauCl2MouseExited
        btnNouveauCl2.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnNouveauCl2MouseExited

    private void btnNouveauCl2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNouveauCl2ActionPerformed

        NouveauClientA n = new NouveauClientA();
        n.setVisible(true);
        n.pack();
    }//GEN-LAST:event_btnNouveauCl2ActionPerformed

    private void btnRechercheV1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRechercheV1MouseEntered
        btnRechercheV1.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnRechercheV1MouseEntered

    private void btnRechercheV1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRechercheV1MouseExited
        btnRechercheV1.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnRechercheV1MouseExited

    private void btnRechercheV1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechercheV1ActionPerformed

        try {

            DefaultTableModel d = (DefaultTableModel) tableCars.getModel();
            String req = "select * from autocar  where upper(matricule)=?";
            String matricule = jTextField1.getText();

            if (matricule.trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer matricule");
                RemplirCars();
            } else {

                PreparedStatement pstmt = con.prepareStatement(req);
                pstmt.setString(1, matricule.toUpperCase());

                ResultSet rs = pstmt.executeQuery();

                d.setRowCount(0);
                while (rs.next()) {
                    d.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4)});
                }
                pstmt.close();
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnRechercheV1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jDateChooser1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser1KeyReleased

    }//GEN-LAST:event_jDateChooser1KeyReleased

    private void jDateChooser1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser1KeyTyped

    }//GEN-LAST:event_jDateChooser1KeyTyped

    private void jDateChooser1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooser1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser1MouseReleased

    private void jDateChooser1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooser1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser1MousePressed

    private void btnAjouter1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAjouter1MouseEntered
        btnAjouter1.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnAjouter1MouseEntered

    private void btnAjouter1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAjouter1MouseExited
        btnAjouter1.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnAjouter1MouseExited

    private void btnAjouter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouter1ActionPerformed
        try {
            if (jDateChooser1.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir une date");
            } else {

                DefaultTableModel d = (DefaultTableModel) jTable4.getModel();
                String req = "select * from voyage  where datev=?";
                Calendar datev = jDateChooser1.getCalendar();

                PreparedStatement pstmt = con.prepareStatement(req);
                pstmt.setDate(1, new java.sql.Date(datev.getTimeInMillis()));

                ResultSet rs = pstmt.executeQuery();

                d.setRowCount(0);
                while (rs.next()) {
                    d.addRow(new Object[]{rs.getString(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)});
                }
                pstmt.close();
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnAjouter1ActionPerformed

    private void btnDecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDecActionPerformed
        Cnx cx = new Cnx();
        cx.setVisible(true);
        cx.pack();
        this.dispose();
    }//GEN-LAST:event_btnDecActionPerformed

    private void btnDecMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDecMouseExited
        btnDec.setBackground(new Color(255, 255, 153));
    }//GEN-LAST:event_btnDecMouseExited

    private void btnDecMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDecMouseEntered
        btnDec.setBackground(new Color(221, 255, 221));
    }//GEN-LAST:event_btnDecMouseEntered

    private void btnAnnulerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnnulerMouseClicked

    }//GEN-LAST:event_btnAnnulerMouseClicked

    private void btnAnnulerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnnulerMouseEntered
        btnAnnuler.setBackground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnAnnulerMouseEntered

    private void btnAnnulerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAnnulerMouseExited
        btnAnnuler.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnAnnulerMouseExited

    private void btnAnnulerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnnulerActionPerformed
        jdateVoy.setDate(null);
        cboVD.setSelectedIndex(0);
        cboDest.setSelectedIndex(0);
        txtDuree.setText("");
        txtHD.setText("");
        cboAutocar.setSelectedIndex(0);
        txtPrix.setText("");
    }//GEN-LAST:event_btnAnnulerActionPerformed

    private void txtHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHDActionPerformed

    private void jTableRecMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRecMousePressed

    }//GEN-LAST:event_jTableRecMousePressed

    private void btnTraiterMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTraiterMouseEntered
        btnTraiter.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnTraiterMouseEntered

    private void btnTraiterMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTraiterMouseExited
        btnTraiter.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnTraiterMouseExited

    private void btnTraiterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraiterActionPerformed
        if (jTableRec.getSelectedRow() == -1) {

            JOptionPane.showMessageDialog(this, "Veuillez selectionner la réclamation à traiter");

        } else {
            int row = jTableRec.getSelectedRow();
            int rec = (int) jTableRec.getModel().getValueAt(row, 0);

            try {
                PreparedStatement pstmt = con.prepareStatement("Update reclamation set etat='traité' where numrec =?");
                pstmt.setInt(1, rec);
                pstmt.executeUpdate();
                RemplirTableauReclamation();
                JOptionPane.showMessageDialog(this, "Réclamation traité avec succes !");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "erreur lors le traitement de la réclamation");
                System.err.println(ex);
            }
        }


    }//GEN-LAST:event_btnTraiterActionPerformed

    private void btnRechClientMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRechClientMouseEntered
         btnRechClient.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnRechClientMouseEntered

    private void btnRechClientMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRechClientMouseExited
       btnRechClient.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnRechClientMouseExited

    private void btnRechClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechClientActionPerformed
        try {

            DefaultTableModel d = (DefaultTableModel) jTableRec.getModel();
            String req = "select * from reclamation join client using(idcl) where upper(cincl)=?";
            String cin = txtClRech.getText();

            if (cin.trim().equals("")) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer  CIN");
                RemplirTableauReclamation();
            } else {

                PreparedStatement pstmt = con.prepareStatement(req);
                pstmt.setString(1, cin.toUpperCase());

                ResultSet rs = pstmt.executeQuery();

                d.setRowCount(0);
                while (rs.next()) {
                    d.addRow(new Object[]{rs.getInt("numrec"), rs.getString("cincl"), rs.getString("objetrec"), rs.getString("contenurec"), rs.getString("etat")});
                }
                pstmt.close();
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRechClientActionPerformed

    private void txtClRechActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClRechActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClRechActionPerformed

    private void btnSupprimer1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupprimer1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSupprimer1MouseClicked

    private void btnSupprimer1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupprimer1MouseEntered
         btnSupprimer1.setBackground(new Color(255, 0, 0));
    }//GEN-LAST:event_btnSupprimer1MouseEntered

    private void btnSupprimer1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSupprimer1MouseExited
          btnSupprimer1.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnSupprimer1MouseExited

    private void btnSupprimer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimer1ActionPerformed
        DefaultTableModel d = (DefaultTableModel) jTableRec.getModel();
        int row = -1;
        row = jTableRec.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réclamation");
        } else {

            String voy = jTableRec.getModel().getValueAt(row, 0).toString();
            try {
                int test = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer??");
                if (test == JOptionPane.YES_OPTION) {

                    PreparedStatement pstmt = con.prepareStatement("delete from reclamation where numrec=?");
                    pstmt.setInt(1, Integer.parseInt(voy));
                    pstmt.execute();
                    RemplirTableauReclamation();
                } else {

                }
            } catch (SQLException ex) {
                Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_btnSupprimer1ActionPerformed

    private void btnreffMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnreffMouseEntered
        btnreff.setBackground(new Color(37, 116, 169));
    }//GEN-LAST:event_btnreffMouseEntered

    private void btnreffMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnreffMouseExited
       btnreff.setBackground(new Color(66, 160, 66));
    }//GEN-LAST:event_btnreffMouseExited

    private void btnreffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreffActionPerformed
        RemplirTableauReclamation();
        txtClRech.setText("");
        jRadioButton1.setIcon(new ImageIcon("C:\\Users\\intel\\Documents\\GitHub\\MarocExpress\\src\\icons\\icons8_unchecked_checkbox_30px.png"));
    }//GEN-LAST:event_btnreffActionPerformed
public void  RemplirReclamationNTrai(){
    DefaultTableModel d = (DefaultTableModel) jTableRec.getModel();
    String req = "select * from reclamation join client using(idcl) where etat= 'non traité'";
    
        try {
            PreparedStatement pstmt = con.prepareStatement(req);
            
          ResultSet rs50=  pstmt.executeQuery();
          d.setRowCount(0);
            while(rs50.next()){
                 d.addRow(new Object[]{rs50.getInt("NumRec"), rs50.getString("cincl"), rs50.getString("objetrec"), rs50.getString("contenurec"), rs50.getString("etat")});
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    
}
    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        DefaultTableModel d = (DefaultTableModel) jTableRec.getModel();
        if(jRadioButton1.isSelected()){
            jRadioButton1.setIcon(new ImageIcon("C:\\Users\\intel\\Documents\\GitHub\\MarocExpress\\src\\icons\\icons8_checked_checkbox_30px_1.png"));
            RemplirReclamationNTrai();
           // d.setRowCount(0);
        }else{
             jRadioButton1.setIcon(new ImageIcon("C:\\Users\\intel\\Documents\\GitHub\\MarocExpress\\src\\icons\\icons8_unchecked_checkbox_30px.png"));
            RemplirTableauReclamation();
            
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(MainAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAjouter;
    private javax.swing.JButton btnAjouter1;
    private javax.swing.JButton btnAnnuler;
    private javax.swing.JButton btnDec;
    private javax.swing.JButton btnMiseàJourE;
    private javax.swing.JButton btnNautocar;
    private javax.swing.JButton btnNouveauCl2;
    private javax.swing.JButton btnNouveauE;
    private javax.swing.JButton btnRechClient;
    private javax.swing.JButton btnRechercheV1;
    private javax.swing.JButton btnSuppC;
    private javax.swing.JButton btnSuppCar;
    private javax.swing.JButton btnSupprEmp;
    private javax.swing.JButton btnSupprimer;
    private javax.swing.JButton btnSupprimer1;
    private javax.swing.JButton btnTraiter;
    private javax.swing.JButton btnUpdateC;
    private javax.swing.JButton btnreff;
    private javax.swing.JComboBox<String> cboAutocar;
    private javax.swing.JComboBox<String> cboDest;
    private javax.swing.JComboBox<String> cboVD;
    private javax.swing.JLabel exit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelButton;
    private javax.swing.JPanel jPanelTitle;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JTable jTable4;
    public static javax.swing.JTable jTableEmp;
    private javax.swing.JTable jTableRec;
    public static javax.swing.JTable jTableclll;
    private javax.swing.JTextField jTextField1;
    private com.toedter.calendar.JDateChooser jdateVoy;
    private javax.swing.JPanel pnl1;
    private javax.swing.JPanel pnl2;
    private javax.swing.JPanel pnl3;
    private javax.swing.JPanel pnl4;
    private javax.swing.JPanel pnl5;
    private javax.swing.JSpinner spnPlace;
    public static javax.swing.JTable tableCars;
    private javax.swing.JTextField txtClRech;
    private javax.swing.JTextField txtDuree;
    private javax.swing.JTextField txtHD;
    private javax.swing.JTextField txtMarque;
    private javax.swing.JTextField txtMat;
    private javax.swing.JTextField txtModele;
    private javax.swing.JTextField txtPrix;
    private javax.swing.JTextField txtSearchEmp;
    private javax.swing.JTextField txtSerchCll;
    // End of variables declaration//GEN-END:variables
}
