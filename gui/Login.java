package bank.gui;

import bank.db.DatabaseConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

//import com.mysql.cj.x.protobuf.MysqlxDatatypes.Any;

public class Login extends JFrame implements ActionListener{

    JLabel label1, label2, label3;
    JTextField cardField;
    JPasswordField pinField;
    JButton signInBtn, clearBtn, signUpBtn;

    public Login(){
        setTitle("Bank Management System");
        setLayout(null);

        // BANK LOGO
        ImageIcon logoIcon = new ImageIcon("src/bank/images/bank.png");
        Image logoImg = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(logoImg));
        logo.setBounds(350, 10, 100, 100);
        add(logo);

        // CARD IMAGE
        ImageIcon cardIcon = new ImageIcon("src/bank/images/card.png");
        Image cardImg = cardIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel card = new JLabel(new ImageIcon(cardImg));
        card.setBounds(630, 330, 100, 100);
        add(card);

        // HEADING
        label1 = new JLabel("WELCOME TO ATM");
        label1.setForeground(Color.WHITE);
        label1.setFont(new Font("AvantGarde", Font.BOLD, 38));
        label1.setBackground(Color.BLACK);
        label1.setBounds(230, 125, 450, 40);
        add(label1);

        // LABELS
        label2 = new JLabel("Card No:");
        label2.setFont(new Font("Raleway", Font.BOLD, 28));
        label2.setForeground(Color.WHITE);
        label2.setBackground(Color.BLACK);
        label2.setBounds(150, 190, 375, 30);
        add(label2);

        cardField = new JTextField();
        cardField.setBounds(325, 190, 230, 30);
        cardField.setFont(new Font("Arial", Font.BOLD, 14));
        add(cardField);

        label3 = new JLabel("PIN NO:");
        label3.setFont(new Font("Raleway", Font.BOLD, 28));
        label3.setBackground(Color.BLACK);
        label3.setForeground(Color.WHITE);
        label3.setBounds(150, 250, 375, 30);
        add(label3);

        pinField = new JPasswordField();
        pinField.setBounds(325, 250, 230, 30);
        pinField.setFont(new Font("Arial", Font.BOLD, 14));
        add(pinField);

        // BUTTONS
        signInBtn = new JButton("SIGN IN");
        styleButton(signInBtn);
        signInBtn.setBounds(300, 300, 100, 30);
        add(signInBtn);

        clearBtn = new JButton("CLEAR");
        styleButton(clearBtn);
        clearBtn.setBounds(430, 300, 100, 30);
        add(clearBtn);

        signUpBtn = new JButton("SIGN UP");
        styleButton(signUpBtn);
        signUpBtn.setBounds(300, 350, 230, 30);
        add(signUpBtn);

        // BACKGROUND
        ImageIcon bgIcon = new ImageIcon("src/bank/images/backbg.png");
        Image bgImg = bgIcon.getImage().getScaledInstance(850, 480, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setBounds(0, 0, 850, 480);
        background.setOpaque(false);
        add(background);

        // ACTIONS
        signInBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        signUpBtn.addActionListener(this);

        setSize(850, 480);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void styleButton(JButton btn){
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.BLACK);
        btn.setFocusPainted(true);
    }

    public void actionPerformed(ActionEvent a){
        if(a.getSource() == clearBtn){
            cardField.setText("");
            pinField.setText("");
        }
        else if(a.getSource() == signUpBtn){
            new Signup().setVisible(true);
            dispose();
        }
        else if(a.getSource() == signInBtn){
            loginUser();
        }
    }

 void loginUser(){
    try{
        String card = cardField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if (card.isEmpty()){
            JOptionPane.showMessageDialog(this, "Card Number cannot be empty!");
            return;
        }

        if (pin.length() != 4){
            JOptionPane.showMessageDialog(this, "PIN must be exactly 4 digits!");
            return;
        }

        if (!pin.matches("\\d{4}")){   //regex works \\d Any digit (0–9) {4} Exactly 4 times
            JOptionPane.showMessageDialog(this, "PIN must contain only digits (0-9)!");
            return;
        }

        DatabaseConnection db = new DatabaseConnection();

        String sql = "SELECT * FROM login WHERE card_number=? AND pin=?";
        PreparedStatement ps = db.con.prepareStatement(sql);

        ps.setString(1, card);
        ps.setString(2, pin);

        ResultSet rs = ps.executeQuery();   //exist user row contains 

        if (rs.next()){
           // JOptionPane.showMessageDialog(this, "Login Successful!");
            new Dashboard(card).setVisible(true);
            dispose();
        }else{
            JOptionPane.showMessageDialog(this, "Invalid Card Number or PIN");
        }

       }catch (Exception e){
          e.printStackTrace();
      }
  }


}
