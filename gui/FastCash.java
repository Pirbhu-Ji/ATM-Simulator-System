package bank.gui;

import bank.db.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class FastCash extends JFrame{

    String card;   
    JButton b100, b500, b1000, b2000, b5000, b10000, backBtn;

    public FastCash(String card){
        this.card = card;

        setTitle("Fast Cash");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);

        ImageIcon bgIcon = new ImageIcon("src/bank/images/atm_bg.png");
        Image bgImg = bgIcon.getImage().getScaledInstance(850, 500, Image.SCALE_SMOOTH);
        JLabel bg = new JLabel(new ImageIcon(bgImg));
        bg.setBounds(0, 0, 850, 500);
        add(bg);

        JLabel heading = new JLabel("SELECT WITHDRAWAL AMOUNT");
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(Color.BLACK);
        heading.setBounds(200, 100, 500, 40);
        bg.add(heading);

        b100 = createButton("Rs 100", 170, 170, bg);
        b500 = createButton("Rs 500", 420, 170, bg);
        b1000 = createButton("Rs 1000", 170, 230, bg);
        b2000 = createButton("Rs 2000", 420, 230, bg);
        b5000 = createButton("Rs 5000", 170, 290, bg);
        b10000 = createButton("Rs 10000", 420, 290, bg);

        backBtn = new JButton("BACK");
        backBtn.setBounds(330, 360, 180, 40);
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 17));
        backBtn.addActionListener(e -> {
            new Dashboard(card).setVisible(true);
            dispose();
        });
        bg.add(backBtn);

        b100.addActionListener(e -> withdraw(100));
        b500.addActionListener(e -> withdraw(500));
        b1000.addActionListener(e -> withdraw(1000));
        b2000.addActionListener(e -> withdraw(2000));
        b5000.addActionListener(e -> withdraw(5000));
        b10000.addActionListener(e -> withdraw(10000));

        setVisible(true);
    }

    private JButton createButton(String text, int x, int y, JLabel bg){
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 180, 40);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 17));
        bg.add(btn);
        return btn;
    }

    private void withdraw(int amount){
        try {
            DatabaseConnection db = new DatabaseConnection();

            // 1. Fetch current balance (you already have card number)
            String balQuery = "SELECT balance FROM accounts WHERE cardno=?";
            PreparedStatement ps1 = db.con.prepareStatement(balQuery);
            ps1.setString(1, card);

            ResultSet rs1 = ps1.executeQuery();

            double currentBalance;
            if (rs1.next()){
                currentBalance = rs1.getDouble("balance");
            } else{
                JOptionPane.showMessageDialog(this, "Account not found!");
                return;
            }

            // Not enough balance?
            if (amount > currentBalance){
                JOptionPane.showMessageDialog(this,
                        "Insufficient Balance!\nCurrent Balance: " + currentBalance);
                return;
            }

            // NEW BALANCE
            double newBalance = currentBalance - amount;

            // 2. Update balance in accounts table
            String update = "UPDATE accounts SET balance=? WHERE cardno=?";
            PreparedStatement ps2 = db.con.prepareStatement(update);
            ps2.setDouble(1, newBalance);
            ps2.setString(2, card);
            ps2.executeUpdate();

            // 3. Insert in transaction table
            String txn = "INSERT INTO transactions (cardno, type, amount, balance_after) VALUES (?, ?, ?, ?)";
            PreparedStatement ps3 = db.con.prepareStatement(txn);
            ps3.setString(1, card);
            ps3.setString(2, "FASTCASH");
            ps3.setDouble(3, amount);
            ps3.setDouble(4, newBalance);
            ps3.executeUpdate();

            JOptionPane.showMessageDialog(this,
            "Rs " + amount + " Withdrawn Successfully!\nRemaining Balance: " + newBalance);
            
            new Dashboard(card).setVisible(true);
            dispose();

        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
