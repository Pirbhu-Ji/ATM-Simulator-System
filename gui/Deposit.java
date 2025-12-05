package bank.gui;

import bank.db.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Deposit extends JFrame{

    String card;

    JTextField amountField;
    JButton depositBtn, backBtn;

    public Deposit(String card){
        this.card = card;

        setTitle("Deposit Money");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);

        ImageIcon bg = new ImageIcon("src/bank/images/atm_bg.png");
        Image bgImg = bg.getImage().getScaledInstance(850, 500, Image.SCALE_SMOOTH);
        JLabel bgLabel = new JLabel(new ImageIcon(bgImg));
        bgLabel.setBounds(0, 0, 850, 500);
        add(bgLabel);

        JLabel title = new JLabel("ENTER AMOUNT TO DEPOSIT");
        title.setBounds(200, 120, 400, 40);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        bgLabel.add(title);

        amountField = new JTextField();
        amountField.setBounds(260, 180, 250, 35);
        amountField.setFont(new Font("Arial", Font.PLAIN, 18));
        bgLabel.add(amountField);

        depositBtn = new JButton("DEPOSIT");
        depositBtn.setBounds(280, 260, 115, 35);
        depositBtn.setFont(new Font("Arial", Font.BOLD, 15));
        depositBtn.setBackground(Color.BLACK);
        depositBtn.setForeground(Color.WHITE);
        bgLabel.add(depositBtn);

        backBtn = new JButton("BACK");
        backBtn.setBounds(415, 260, 115, 35);
        backBtn.setFont(new Font("Arial", Font.BOLD, 15));
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        bgLabel.add(backBtn);

        depositBtn.addActionListener(e -> depositMoney());
        backBtn.addActionListener(e ->{
            new Dashboard(card).setVisible(true);
            dispose();
        });
         
        setVisible(true);
    }

    private void depositMoney(){
        String amountText = amountField.getText().trim();

        if (amountText.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter amount!");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0){
                JOptionPane.showMessageDialog(this, "Enter a valid amount!");
                return;
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "Amount must be numeric!");
            return;
        }

        try {
            DatabaseConnection db = new DatabaseConnection();

            // FIXED — correct column name cardno
            String fetch = "SELECT cardno, balance FROM accounts WHERE cardno = ?";
            PreparedStatement ps = db.con.prepareStatement(fetch);
            ps.setString(1, card);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()){
                JOptionPane.showMessageDialog(this, "Account not found!");
                return;
            }

            String cardno = rs.getString("cardno");
            double currentBalance = rs.getDouble("balance");
            double newBalance = currentBalance + amount;

            // FIXED — correct column cardno
            String update = "UPDATE accounts SET balance=? WHERE cardno=?";
            PreparedStatement ps2 = db.con.prepareStatement(update);
            ps2.setDouble(1, newBalance);
            ps2.setString(2, card);
            ps2.executeUpdate();

            // Insert into transactions table
            String txn = "INSERT INTO transactions (cardno, type, amount, balance_after) VALUES (?, 'DEPOSIT', ?, ?)";
            PreparedStatement ps3 = db.con.prepareStatement(txn);
            ps3.setString(1, cardno);
            ps3.setDouble(2, amount);
            ps3.setDouble(3, newBalance);
            ps3.executeUpdate();

            JOptionPane.showMessageDialog(this, amount + " Deposited Successfully!");
            new Dashboard(card).setVisible(true);
            dispose();

        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
