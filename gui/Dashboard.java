package bank.gui;

import java.awt.*;
import javax.swing.*;

public class Dashboard extends JFrame{
        String cardNo;

     public Dashboard(String cardNo){
         this.cardNo = cardNo;

        setTitle("ATM Machine Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        setUndecorated(true);

        // BACKGROUND
        ImageIcon bgIcon = new ImageIcon("src/bank/images/atm_bg.png");
        Image bgImg = bgIcon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setBounds(0, 0, 900, 600);
        add(background);

        // CENTER TITLE
        JLabel menuTitle = new JLabel("Please select your Transaction");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 26));
        menuTitle.setForeground(Color.WHITE);
        menuTitle.setBounds(190, 130, 450, 40);
        background.add(menuTitle);

        // LEFT SIDE BUTTONS 
        JButton depositBtn     = createButton("Deposit",        180, 200);
        JButton fastcashBtn    = createButton("Fast Cash",      180, 260);
        JButton pinchangeBtn   = createButton("PIN Change",     180, 320);

        // RIGHT SIDE BUTTONS
        JButton withdrawBtn    = createButton("Cash Withdrawal", 380, 200);
        JButton ministmtBtn    = createButton("Mini Statement",  380, 260);
        JButton balanceBtn     = createButton("Balance Enquiry", 380, 320);

        // BOTTOM CENTER
        JButton logoutBtn        = createButton("Logout",           280, 420);

        background.add(depositBtn);
        background.add(fastcashBtn);
        background.add(pinchangeBtn);

        background.add(withdrawBtn);
        background.add(ministmtBtn);
        background.add(balanceBtn);

        background.add(logoutBtn);

        // BUTTON FUNCTIONS
      depositBtn.addActionListener(a ->{
        new Deposit(cardNo).setVisible(true);
        dispose();
      });

      withdrawBtn.addActionListener(a ->{
         new Withdraw(cardNo).setVisible(true);
         dispose();
      });

     fastcashBtn.addActionListener(a ->{
        new FastCash(cardNo).setVisible(true);
        dispose();
      });

     ministmtBtn.addActionListener(a ->{
        new MiniStatement(cardNo).setVisible(true);
        dispose();
      });

       pinchangeBtn.addActionListener(a ->{
        new PinChange(cardNo).setVisible(true);
        dispose();
      });

      balanceBtn.addActionListener(e ->{
         new Balance(cardNo).setVisible(true);
         dispose();
     });

      logoutBtn.addActionListener(e ->{
         //cardNo = null; // clear session
          new Login().setVisible(true); // Open login window
          dispose();   
      });

        setVisible(true);
    }

    // Helper method to style buttons
    private JButton createButton(String text, int x, int y){
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 180, 40);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        return btn;
    }
}
