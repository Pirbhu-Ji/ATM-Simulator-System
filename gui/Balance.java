package bank.gui;

import bank.db.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Balance extends JFrame{

    String cardNo;
    JButton backBtn;

    public Balance(String cardNo){
        this.cardNo = cardNo;

        setTitle("Balance Enquiry");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        setUndecorated(true);

        // Background
        ImageIcon bgIcon = new ImageIcon("src/bank/images/atm_bg.png");
        Image bgImg = bgIcon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setBounds(0, 0, 900, 600);
        add(background);

        JLabel title = new JLabel("CURRENT ACCOUNT BALANCE");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBounds(240, 150, 450, 40);
        background.add(title);

        try {
            DatabaseConnection db = new DatabaseConnection();

            String query = "SELECT balance FROM accounts WHERE cardno = ?";
            PreparedStatement ps = db.con.prepareStatement(query);
            ps.setString(1, cardNo);

            ResultSet rs = ps.executeQuery();

            if (!rs.isBeforeFirst()){
                System.out.println("DEBUG → No record found for card: " + cardNo);
            } else {
                System.out.println("DEBUG → Record found for card: " + cardNo);
            }

            double balance=0;
            if (rs.next()){
                balance = rs.getDouble("balance");
            }

            JLabel balanceText = new JLabel("RS. " + balance);
            balanceText.setFont(new Font("Arial", Font.BOLD, 33));
            balanceText.setForeground(Color.BLACK);
            balanceText.setBounds(330, 220, 500, 40);
            background.add(balanceText);

        } catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }

        backBtn = new JButton("BACK");
        backBtn.setBounds(350, 320, 180, 40);
        backBtn.setFont(new Font("Arial", Font.BOLD, 20));
        //backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> goBack());
        background.add(backBtn);

        setVisible(true);
    }

    private void goBack(){
        new Dashboard(cardNo).setVisible(true);
        dispose();
        
    }
}
