package bank.gui;

import bank.db.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Withdraw extends JFrame{

    String card;  
    JTextField amountField;
    JButton withdrawBtn, backBtn;

    public Withdraw(String card) {
        this.card = card;

        setTitle("Withdraw Cash");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);

        ImageIcon bgIcon = new ImageIcon("src/bank/images/atm_bg.png");
        Image bgImg = bgIcon.getImage().getScaledInstance(850, 500, Image.SCALE_SMOOTH);
        JLabel bg = new JLabel(new ImageIcon(bgImg));
        bg.setBounds(0, 0, 850, 500);
        add(bg);

        JLabel heading = new JLabel("CASH WITHDRAWAL");
        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setForeground(Color.BLACK);
        heading.setBounds(240, 100, 400, 40);
        bg.add(heading);

        JLabel label = new JLabel("Enter Amount:");
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setForeground(Color.BLACK);
        label.setBounds(180, 200, 200, 30);
        bg.add(label);

        amountField = new JTextField();
        amountField.setBounds(355, 200, 200, 35);
        amountField.setFont(new Font("Arial", Font.PLAIN, 18));
        bg.add(amountField);

        withdrawBtn = new JButton("WITHDRAW");
        withdrawBtn.setBounds(250, 300, 150, 40);
        withdrawBtn.setBackground(Color.BLACK);
        withdrawBtn.setForeground(Color.WHITE);
        withdrawBtn.setFont(new Font("Arial", Font.BOLD, 18));
        withdrawBtn.addActionListener(e -> withdrawMoney());
        bg.add(withdrawBtn);

        backBtn = new JButton("BACK");
        backBtn.setBounds(440, 300, 150, 40);
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.addActionListener(e ->{
            new Dashboard(card).setVisible(true);
            dispose();
        });
        bg.add(backBtn);

        setVisible(true);
    }

    private void withdrawMoney(){
        try {
            String amountText = amountField.getText();

            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter an amount!");
                return;
            }

            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0");
                return;
            }

            DatabaseConnection db = new DatabaseConnection();

            // 1. Get balance from accounts table using card_number
            String balQuery = "SELECT balance FROM accounts WHERE cardno=?";
            PreparedStatement ps2 = db.con.prepareStatement(balQuery);
            ps2.setString(1, card);

            ResultSet rs2 = ps2.executeQuery();

            double currentBalance;
            if (rs2.next()) {
                currentBalance = rs2.getDouble("balance");
            } else {
                JOptionPane.showMessageDialog(this, "Account not found!");
                return;
            }

            // Check balance
            if (amount > currentBalance) {
                JOptionPane.showMessageDialog(this,
                        "Insufficient Balance! Current balance: " + currentBalance);
                return;
            }

            double newBalance = currentBalance - amount;

            // 2. Update balance
            String updateQuery = "UPDATE accounts SET balance=? WHERE cardno=?";
            PreparedStatement ps3 = db.con.prepareStatement(updateQuery);
            ps3.setDouble(1, newBalance);
            ps3.setString(2, card);
            ps3.executeUpdate();

            // 3. Insert transaction
            String transQuery =
                    "INSERT INTO transactions (cardno, type, amount, balance_after) VALUES (?, 'WITHDRAW', ?, ?)";
            PreparedStatement ps4 = db.con.prepareStatement(transQuery);
            ps4.setString(1, card);
            ps4.setDouble(2, amount);
            ps4.setDouble(3, newBalance);
            ps4.executeUpdate();

            JOptionPane.showMessageDialog(this,
                    "Withdrawal Successful!\nNew Balance: " + newBalance);

            new Dashboard(card).setVisible(true);
            dispose();

        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
