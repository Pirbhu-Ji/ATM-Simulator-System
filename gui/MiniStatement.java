package bank.gui;

import bank.db.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class MiniStatement extends JFrame{

    String cardNo;
    JTextArea statementArea;
    JButton backBtn;

    public MiniStatement(String cardNo){
       this.cardNo = cardNo;

        setTitle("Mini Statement");
        setSize(750, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setUndecorated(true);

        // BACKGROUND
        ImageIcon bgIcon = new ImageIcon("src/bank/images/atm_bg.png");
        Image bgImg = bgIcon.getImage().getScaledInstance(750, 650, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setBounds(0, 0, 750, 650);
        background.setLayout(null);
        add(background);

        // Main ATM heading
        JLabel heading = new JLabel("SilverOak Bank ATM");
        heading.setFont(new Font("Courier New", Font.BOLD, 30)); 
        heading.setForeground(Color.CYAN);                        
        heading.setHorizontalAlignment(SwingConstants.CENTER);    
        heading.setBounds(12, 30, 650, 40);                        
        background.add(heading);

        // Subtitle for mini statement
        JLabel subHeading = new JLabel("Mini Statement");
        subHeading.setFont(new Font("Arial", Font.BOLD, 25));
        subHeading.setForeground(Color.WHITE);
        subHeading.setHorizontalAlignment(SwingConstants.CENTER);
        subHeading.setBounds(12, 70, 650, 30);
        background.add(subHeading);


        statementArea = new JTextArea();
        statementArea.setEditable(false);
        statementArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane pane = new JScrollPane(statementArea);
        pane.setBounds(150, 140, 460, 420);
        background.add(pane);

        backBtn = new JButton("BACK");
        backBtn.setBounds(340, 590, 120, 40);
        backBtn.setFont(new Font("Arial", Font.BOLD, 20));
        backBtn.setForeground(Color.BLACK);
        //backBtn.setBackground(Color.BLACK);
        backBtn.addActionListener(e -> {
        new Dashboard(cardNo).setVisible(true);
        dispose();
      });
       background.add(backBtn);

       loadMiniStatement();

      setVisible(true);
   }

    private void loadMiniStatement(){
    try {
        DatabaseConnection db = new DatabaseConnection();

        //FETCH ACCOUNT HOLDER NAME 
        String nameQuery = "SELECT name FROM accounts WHERE cardno=?";
        PreparedStatement psName = db.con.prepareStatement(nameQuery);
        psName.setString(1, cardNo);
        ResultSet rsName = psName.executeQuery();

        String holderName = "Unknown User";
        if (rsName.next()){
            holderName = rsName.getString("name");
        }

        //HEADER OUTPUT 
        statementArea.setText("Account Holder : " + holderName + "\n");
        statementArea.append("Card Number    : " 
            + cardNo.substring(0, 4) 
            + "XXXXXX" 
            + cardNo.substring(cardNo.length() - 4) + "\n");
        statementArea.append("--------------------------------------------------\n");

        //TRANSACTION QUERY 
        String query = "SELECT * FROM transactions WHERE cardno=? ORDER BY date_time DESC LIMIT 15";
        PreparedStatement ps = db.con.prepareStatement(query);
        ps.setString(1, cardNo);
        ResultSet rs = ps.executeQuery();

        boolean found = false;

        while (rs.next()){
            found = true;
            String type = rs.getString("type");
            double amount = rs.getDouble("amount");
            Timestamp date = rs.getTimestamp("date_time");
            double bal = rs.getDouble("balance_after");

            statementArea.append(date + " - " + type + " - Rs." + amount + " - Balance: " + bal + "\n");
        }

        if (!found){
            statementArea.append("\nNo transactions found for this card.");
        }

     } catch (Exception e){
        JOptionPane.showMessageDialog(this, "Error loading statement: " + e.getMessage());
        e.printStackTrace();
     }
  }
}