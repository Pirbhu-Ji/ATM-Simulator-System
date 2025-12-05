package bank.gui;

import bank.db.DatabaseConnection;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

public class Signup extends JFrame{

    JTextField accField, cardField, nameField, balanceField;
    JPasswordField pinField, confirmPinField;
    JButton createBtn, backBtn;
  
    public Signup(){
       
        setTitle("Create New Bank Account");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);

        // LOGO
        ImageIcon bankIcon = new ImageIcon("src/bank/images/signup_bank.png");
        Image logoImg = bankIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds(370, 15, 120, 120);
        add(logoLabel);

        // HEADING
        JLabel heading = new JLabel("CREATE NEW ACCOUNT");
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        heading.setForeground(Color.WHITE);
        heading.setBounds(240, 130, 450, 40);
        add(heading);

        // Account Number
        addLabel("Account Number (11 digits):", 120, 190);
        accField = addField(410, 190);

        // Card Number
        addLabel("Card Number (14 digits):", 120, 230);
        cardField = addField(410, 230);

        // Full Name
        addLabel("Account Holder Name:", 120, 270);
        nameField = addField(410, 270);

        // Balance
        addLabel("Initial Balance:", 120, 310);
        balanceField = addField(410, 310);

        // PIN
        addLabel("PIN (4 digits):", 120, 350);
        pinField = new JPasswordField();
        pinField.setBounds(410, 350, 250, 28);
        add(pinField);

        // Confirm PIN
        addLabel("Confirm PIN:", 120, 390);
        confirmPinField = new JPasswordField();
        confirmPinField.setBounds(410, 390, 250, 28);
        add(confirmPinField);

        // Buttons
        createBtn = new JButton("CREATE ACCOUNT");
        styleButton(createBtn, 260, 460);
        createBtn.addActionListener(e -> createAccount());
        add(createBtn);

        backBtn = new JButton("BACK TO LOGIN");
        styleButton(backBtn, 480, 460);
        backBtn.addActionListener(e -> goBack());
        add(backBtn);

        // Background
        ImageIcon bg = new ImageIcon("src/bank/images/signup_bg.png");
        Image bgImg = bg.getImage().getScaledInstance(950, 600, Image.SCALE_SMOOTH);
        JLabel bgLabel = new JLabel(new ImageIcon(bgImg));
        bgLabel.setBounds(0, 0, 850, 550);
        add(bgLabel);

        setVisible(true);
    
    }

    private void addLabel(String text, int x, int y){
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 230, 30);
        add(label);
    }

    private JTextField addField(int x, int y){
        JTextField field = new JTextField();
        field.setBounds(x, y, 250, 28);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        add(field);
        return field;
    }

    private void styleButton(JButton btn, int x, int y){
        btn.setBounds(x, y, 180, 35);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
    }

    void createAccount(){
        try {
            String accNo = accField.getText().trim();
            String cardNo = cardField.getText().trim();
            String name = nameField.getText().trim();
            String balStr = balanceField.getText().trim();
            String pin = new String(pinField.getPassword()).trim();
            String confirmPin = new String(confirmPinField.getPassword()).trim();

            // Required fields
            if (accNo.isEmpty() || cardNo.isEmpty() || name.isEmpty() || balStr.isEmpty() ||
                    pin.isEmpty() || confirmPin.isEmpty()){
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            // Account Number validation (11 digits)
            if (!accNo.matches("\\d{11}")){
                JOptionPane.showMessageDialog(this, "Account Number must be 11 digits!");
                return;
            }

            // Card Number validation (14 digits)
            if (!cardNo.matches("\\d{14}")){
                JOptionPane.showMessageDialog(this, "Card Number must be 14 digits!");
                return;
            }

            // Name validation
            if (!name.matches("[a-zA-Z ]+")){
                JOptionPane.showMessageDialog(this, "Name must contain only letters!");
                return;
            }

            // Balance validation
            double balance;
            try {
                balance = Double.parseDouble(balStr);
            }catch (Exception e){
                JOptionPane.showMessageDialog(this, "Balance must be a valid number!");
                return;
            }

            // PIN validation (4 digits)
            if (!pin.matches("\\d{4}")){
                JOptionPane.showMessageDialog(this, "PIN must be exactly 4 digits!");
                return;
            }

            // Confirm PIN match
            if (!pin.equals(confirmPin)){
                JOptionPane.showMessageDialog(this, "PIN and Confirm PIN must match!");
                return;
            }

            // DB connection
            DatabaseConnection db = new DatabaseConnection();

            // Check duplicate account
            PreparedStatement checkAcc = db.con.prepareStatement(
                    "SELECT * FROM accounts WHERE accno=?");
            checkAcc.setString(1, accNo);
            ResultSet rs1 = checkAcc.executeQuery();
            if (rs1.next()){
                JOptionPane.showMessageDialog(this, "Account Number already registered!");
                return;
            }

            // Check duplicate card number
            PreparedStatement checkCard = db.con.prepareStatement(
                    "SELECT * FROM accounts WHERE cardno=?");
            checkCard.setString(1, cardNo);
            ResultSet rs2 = checkCard.executeQuery();
            if (rs2.next()){
                JOptionPane.showMessageDialog(this, "Card Number already registered!");
                return;
            }

            // Insert in login table
            PreparedStatement ps1 = db.con.prepareStatement("INSERT INTO login VALUES (?, ?)");
            ps1.setString(1, cardNo);
            ps1.setString(2, pin);
            ps1.executeUpdate();

            // Insert in account table
            PreparedStatement ps2 = db.con.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?, ?, ?)");
            ps2.setString(1, accNo);
            ps2.setString(2, cardNo);
            ps2.setString(3, name);
            ps2.setDouble(4, balance);
            ps2.setString(5, pin);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account Created Successfully!");
            new Login().setVisible(true);
            dispose();

        }catch (Exception ex){
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void goBack(){
        new Login().setVisible(true);
        dispose();
        
    }
}
