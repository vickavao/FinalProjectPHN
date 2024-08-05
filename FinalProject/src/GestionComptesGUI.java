import javax.naming.spi.DirStateFactory.Result;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class GestionComptesGUI {
    private JTextField numeroField;
    private JTextField soldeField;
    private JRadioButton courantRadio;
    private JRadioButton epargneRadio;
    private Component frame;

    public GestionComptesGUI() {

        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ajouterCompte();
            }
        });

    }

    private void ajouterCompte() {
        String numero = numeroField.getText();
        double solde = Double.parseDouble(soldeField.getText());
        String type = courantRadio.isSelected() ? "courant" : "epargne";

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/votre_base_de_donnees", "votre_utilisateur", "votre_mot_de_passe");
            String sql = "INSERT INTO comptes (numero, solde, type) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numero);
            pstmt.setDouble(2, solde);
            pstmt.setString(3, type);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            JOptionPane.showMessageDialog(null, "Compte ajouté !");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du compte.");
        }
    }


    private void supprimerCompte(Component frame) {
        String numero = numeroField.getText();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/votre_base_de_donnees", "votre_utilisateur", "votre_mot_de_passe");
            String sql = "DELETE FROM comptes WHERE numero = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numero);
            int rowsDeleted = pstmt.executeUpdate();
            pstmt.close();
            conn.close();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(frame, "Compte supprimé !");
            } else {
                JOptionPane.showMessageDialog(frame, "Compte introuvable.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression du compte.");
        }
    }


    private void afficherInfosCompte(JTextComponent resultatArea) {
        String numero = numeroField.getText();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/votre_base_de_donnees", "votre_utilisateur", "votre_mot_de_passe");
            String sql = "SELECT * FROM comptes WHERE numero = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numero);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                //Result.setText("Numéro : " + rs.getString("numero") + "\nSolde : " + rs.getDouble("solde"));
            } else {
                resultatArea.setText("Compte introuvable.");
            }

            pstmt.close();
            conn.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la récupération des informations du compte.");
        }
    }


    private void afficherComptes(Component frame) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/votre_base_de_donnees", "votre_utilisateur", "votre_mot_de_passe");
            String sql = "SELECT * FROM comptes";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            table.setModel(buildTableModel(rs));
            pstmt.close();
            conn.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Erreur lors de la récupération des comptes.");
        }
    }


    private Object buildTableModel(ResultSet rs) {
    
        throw new UnsupportedOperationException("Unimplemented method 'buildTableModel'");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionComptesGUI());
    }
}
