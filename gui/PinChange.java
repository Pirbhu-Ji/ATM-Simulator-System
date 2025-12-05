package bank.gui;

import bank.db.DatabaseConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class PinChange extends JFrame{

    String cardNo;
    JPasswordField oldPinField, newPinField, confirmPinField;
    JButton changeBtn, backBtn;

    public PinChange(String cardNo){
        this.cardNo = cardNo;

        setTitle("PIN Change");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(null);
        setUndecorated(true);

        // BACKGROUND
        ImageIcon bgIcon = new ImageIcon("src/bank/images/atm_bg.png");
        Image bgImg = bgIcon.getImage().getScaledInstance(700, 550, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setBounds(0, 0, 700, 550);
        background.setLayout(null); 
        add(background);

        // Heading
        JLabel heading = new JLabel("CHANGE PIN");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(Color.BLACK); // for better visibility on bg
        heading.setBounds(210, 50, 300, 30);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        background.add(heading);

        // Labels and fields
        oldPinField = addLabelAndField("Enter Old PIN:", 130, 110, background);
        newPinField = addLabelAndField("Enter New PIN:", 130, 170, background);
        confirmPinField = addLabelAndField("Confirm PIN:", 130, 230, background);

        // Buttons
        changeBtn = new JButton("CHANGE");
        changeBtn.setBounds(160, 340, 120, 35);
        changeBtn.setBackground(Color.BLACK);
        changeBtn.setForeground(Color.WHITE);
        changeBtn.setFont(new Font("Arial", Font.BOLD, 18));
        changeBtn.addActionListener(e -> changePin());
        background.add(changeBtn);

        backBtn = new JButton("BACK");
        backBtn.setBounds(320, 340, 100, 35);
        backBtn.setBackground(Color.BLACK);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 20));
        backBtn.addActionListener(e -> {
            new Dashboard(cardNo).setVisible(true);
            dispose();
        });
        background.add(backBtn);

        setVisible(true);
    }

    private JPasswordField addLabelAndField(String text, int x, int y, JLabel background){
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 150, 30);
        background.add(label);

        JPasswordField field = new JPasswordField();
        field.setBounds(x + 160, y, 150, 30);
        background.add(field);

        return field;
    }

    private void changePin(){
        try {
            String oldPin = new String(oldPinField.getPassword()).trim();
            String newPin = new String(newPinField.getPassword()).trim();
            String confirmPin = new String(confirmPinField.getPassword()).trim();

            if (oldPin.isEmpty() || newPin.isEmpty() || confirmPin.isEmpty()){
                JOptionPane.showMessageDialog(this, "All fields required!");
                return;
            }

            if (!newPin.matches("\\d{4}")){
                JOptionPane.showMessageDialog(this, "PIN must be exactly 4 digits!");
                return;
            }

            if (!newPin.equals(confirmPin)){
                JOptionPane.showMessageDialog(this, "New PIN and Confirm PIN must match!");
                return;
            }

            DatabaseConnection db = new DatabaseConnection();

            // FETCH old PIN
            String check = "SELECT pin FROM login WHERE card_number=?";
            PreparedStatement ps = db.con.prepareStatement(check);
            ps.setString(1, cardNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                String dbPin = rs.getString("pin");
                if (!dbPin.equals(oldPin)){
                    JOptionPane.showMessageDialog(this, "Old PIN is incorrect!");
                    return;
                }
            } else{
                JOptionPane.showMessageDialog(this, "Card not found!");
                return;
            }

            // UPDATE PIN
            String updateLogin = "UPDATE login SET pin=? WHERE card_number=?";
            PreparedStatement ps1 = db.con.prepareStatement(updateLogin);
            ps1.setString(1, newPin);
            ps1.setString(2, cardNo);
            ps1.executeUpdate();

            String updateAcc = "UPDATE accounts SET pin=? WHERE cardno=?";
            PreparedStatement ps2 = db.con.prepareStatement(updateAcc);
            ps2.setString(1, newPin);
            ps2.setString(2, cardNo);
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "PIN Changed Successfully!");
            new Dashboard(cardNo).setVisible(true);
            dispose();

        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
