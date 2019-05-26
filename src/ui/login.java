package ui;

import Exceptions.InsufficientFundsException;
import Exceptions.NegativeAmountException;
import Exceptions.noAccountsFoundException;
import Modules.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class login extends JFrame {

    LoginClass loginInstance = new LoginClass();
    JPanel myPanel = new JPanel();
    private BankAccount bAccount;
    private JTextField username;
    private JPasswordField password;
    private JButton createAccount;
    private JTextField usernameField;
    private JButton regularUserButton;
    private JButton superUserButton;
    private JPasswordField passwordField;
    RegularUserBase userReg;
    SuperUserBase userSup;
    private List<AccountModel> accountList;
    private List<AccountModel> trueAccountList;
    private List<JButton> buttonList;
    private JButton createAccountbutton;
    private JLabel accountTypeLabel;
    private JButton savings;
    private JButton chequing;
    private AccountModel account;
    private JButton balance;
    private JButton withdraw;
    private JButton deposit;
    private JLabel withdrawLabel;
    private JTextField withdrawField;
    private JLabel depositLabel;
    private JTextField depositField;
    private JButton deleteAnAccountButton;
    private JTextField response;
    private Singleton singleton;
    User user;


    public login() throws IOException {

        super("Banking System");
        setLayout(new FlowLayout());
        bAccount = new BankAccount();
        buttonList = new ArrayList<>();
        trueAccountList = new ArrayList<>();

        userReg = new RegularUserBase();
        userSup = new SuperUserBase();

        initSingletonAccount();
        initSingleton();

        JLabel usernameLabel = new JLabel("Username: ");
        username = new JTextField(30);
        myPanel.add(usernameLabel);
        myPanel.add(username);

        password = new JPasswordField(30);
        JLabel passwordLabel = new JLabel("Password: ");
        myPanel.add(passwordLabel);
        myPanel.add(password);

        createAccount = new JButton("Create a new Account");
        myPanel.add(createAccount);
        add(myPanel);

        Handler handler = new Handler();
        username.addActionListener(handler);
        password.addActionListener(handler);
        createAccount.addActionListener(handler);
    }

    private void initSingleton() throws IOException {
        userSup.initializeSingleton();
    }
    private void initSingletonAccount() throws IOException {
        bAccount.initializeSingletonAccount();
    }

    private class Handler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String string = "";

            if (e.getSource() == username) {
            } else if (e.getSource() == password) {
                string = e.getActionCommand();
                String Username = username.getText();
                try {
                    user = loginInstance.login(Username, string);
                    if (user != null) {
                        playSound("swoosh.wav");
                        JOptionPane.showMessageDialog(null, "Login Successful.");
                        buttonList = new ArrayList<>();

                        List<AccountModel> accsWithMatchingUserId = bAccount.filterAccListByUserId(user.getUserId());
                        for (AccountModel am : accsWithMatchingUserId) {
                            JButton button = new JButton(Integer.toString(am.getAccountId()));
                            buttonList.add(button);
                        }
                        createAccountSelectionFrame();
                    } else {
                        playSound("errorsound.wav");
                        JOptionPane.showMessageDialog(null, "The username and/or password you have entered is incorrect.");
                        username.setText("");
                        password.setText("");
                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (e.getSource() == createAccount) {
                playSound("leaguebuttonclick.wav");
                createAccountCreationFrame();
            }
        }

        private void createAccountSelectionFrame() {
            JPanel myPanel = new JPanel();
            JFrame myFrame = new JFrame("Account Selection");
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            myFrame.setSize(400, 400);
            myFrame.setLocationRelativeTo(null);
            createAccountbutton = new JButton("Create a new Banking Account");
            myPanel.add(createAccountbutton);
            Handler1 handler = new Handler1();
            createAccountbutton.addActionListener(handler);
            if (!(buttonList==null)) {
                for (JButton button : buttonList) {
                    List<Component> compList = Arrays.asList(myPanel.getComponents());
                    if (!(compList.contains(button))) {
                        myPanel.add(button);
                        button.addActionListener(handler);
                    }
                }
            }
            if (user.getType().equals("Super")) {
                deleteAnAccountButton = new JButton("Delete an Account");
                myPanel.add(deleteAnAccountButton);
                deleteAnAccountButton.addActionListener(handler);
            }

            myFrame.add(myPanel);
            myFrame.setVisible(true);
        }

        private class Handler1 implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==createAccountbutton) {
                    playSound("leaguebuttonclick.wav");
                    createAccountOptionFrame();
                } else if (e.getSource() == deleteAnAccountButton) {
                    deleteAnAccountFrame();
                    return;
                }
                else {
                    playSound("leaguebuttonclick.wav");
                    createAccountFrame(e);
                }
            }
        }

        private void deleteAnAccountFrame() {
            JFrame myFrame = new JFrame("Deleting Accounts");
            JPanel myPanel = new JPanel();
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            myFrame.setSize(600, 300);
            myFrame.setLocationRelativeTo(null);
            JLabel instructions = new JLabel("Type in a regular account's username to delete");
            myPanel.add(instructions);
            response = new JTextField(30);
            myPanel.add(response);
            myFrame.setVisible(true);
            myFrame.add(myPanel);

            Handler7 handler = new Handler7();
            response.addActionListener(handler);
        }

        private class Handler7 implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == response) {
                    try {
                        if (userSup.deleteUser(response.getText())) {
                            JOptionPane.showMessageDialog(null, "User Deletion was Successful");
                        } else {
                            JOptionPane.showMessageDialog(null, "User not found");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }



        private void createAccountFrame(ActionEvent e) {
            int i = Integer.parseInt(e.getActionCommand());
            try {
                account = bAccount.findAccount(i);
            } catch (noAccountsFoundException e1) {
                JOptionPane.showMessageDialog(null, "No accounts were found with that ID");
            }
            JFrame frame = new JFrame("Account Management");
            JPanel panel = new JPanel();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400,400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            balance = new JButton("View Balance");
            withdraw = new JButton("Withdraw");
            deposit = new JButton("Deposit");
            panel.add(balance);
            panel.add(withdraw);
            panel.add(deposit);
            Handler3 handler = new Handler3();
            balance.addActionListener(handler);
            withdraw.addActionListener(handler);
            deposit.addActionListener(handler);
            frame.add(panel);
        }

        private class Handler3 implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == balance) {
                    playSound("leaguebuttonclick.wav");
                    JOptionPane.showMessageDialog(null, "Your account balance " +
                            "is " + String.valueOf(account.getAmount()));
                } else if (e.getSource() == withdraw) {
                    playSound("leaguebuttonclick.wav");
                    createWithdrawWindow();
                } else if (e.getSource() == deposit) {
                    playSound("leaguebuttonclick.wav");
                    createDepositWindow();
                }
            }
        }

        private void createWithdrawWindow() {
            JFrame frame = new JFrame();
            JPanel panel = new JPanel();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400,400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            withdrawLabel = new JLabel("Enter the amount to withdraw:");
            withdrawField = new JTextField(30);
            panel.add(withdrawLabel);
            panel.add(withdrawField);
            Handler4 handler = new Handler4();
            withdrawField.addActionListener(handler);
            frame.add(panel);
        }

        private class Handler4 implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==withdrawField) {
                    try {
                        bAccount.withdraw(Integer.parseInt(e.getActionCommand()),account);
                        JOptionPane.showMessageDialog(null, "Your money has been withdrawn.");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InsufficientFundsException e1) {
                        playSound("errorsound.wav");
                        JOptionPane.showMessageDialog(null, "You have insufficient funds.");
                    } catch (NegativeAmountException e1) {
                        playSound("errorsound.wav");
                        JOptionPane.showMessageDialog(null, "You have entered a negative amount.");
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(null, "The amount you entered is too large.");
                    }
                }
            }
        }
        private void createDepositWindow() {
            JFrame frame = new JFrame();
            JPanel panel = new JPanel();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400,400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            depositLabel = new JLabel("Enter the amount to deposit:");
            depositField = new JTextField(30);
            panel.add(depositLabel);
            panel.add(depositField);
            Handler5 handler = new Handler5();
            depositField.addActionListener(handler);
            frame.add(panel);
        }

        private class Handler5 implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == depositField) {
                    try {
                        bAccount.deposit(Integer.parseInt(e.getActionCommand()), account);
                        JOptionPane.showMessageDialog(null, "Your money has been deposited.");
                    } catch (NegativeAmountException e1) {
                        playSound("errorsound.wav");
                        JOptionPane.showMessageDialog(null, "You have entered a negative amount.");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (NumberFormatException e21) {
                        playSound("errorsound.wav");
                        JOptionPane.showMessageDialog(null, "The amount you entered is too large.");
                    }
                }
            }
        }

        private void createAccountOptionFrame() {
            JFrame myFrame = new JFrame("Account type selection");
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            myFrame.setSize(400,400);
            myFrame.setLocationRelativeTo(null);
            myFrame.setVisible(true);
            JPanel myPanel = new JPanel();
            accountTypeLabel = new JLabel("Choose an account type:");
            savings = new JButton("Savings");
            chequing = new JButton("Chequing");
            myPanel.add(accountTypeLabel);
            myPanel.add(savings);
            myPanel.add(chequing);
            Handler2 handler = new Handler2(myFrame);
            savings.addActionListener(handler);
            chequing.addActionListener(handler);
            myFrame.add(myPanel);
        }

        private class Handler2 implements ActionListener {

            JFrame myFrame;

            public Handler2(JFrame myFrame) {
                this.myFrame = myFrame;
            }

            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==savings) {
                    try {
                        playSound("leaguebuttonclick.wav");
                        bAccount.createAccount(user.getUserId(),"Savings", user);
                        JOptionPane.showMessageDialog(null, "Account creation" +
                                " successful. Please re-login to see your account.");
                        myFrame.dispose();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (noAccountsFoundException e1) {
                        e1.printStackTrace();
                    }
                }
                if (e.getSource()==chequing) {
                    try {
                        playSound("leaguebuttonclick.wav");
                        bAccount.createAccount(user.getUserId(),"Chequing", user);
                        JOptionPane.showMessageDialog(null, "Account creation" +
                                " successful. Please re-login to see your account.");
                        myFrame.dispose();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (noAccountsFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

        private void createAccountCreationFrame() {
            JFrame myFrame = new JFrame("Account Creation");
            JPanel myPanel = new JPanel();
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            myFrame.setSize(600, 300);
            myFrame.setLocationRelativeTo(null);
            JLabel usernameLabel = new JLabel("Enter your new username:");
            myPanel.add(usernameLabel);
            usernameField = new JTextField(30);
            myPanel.add(usernameField);
            JLabel passwordLabel = new JLabel("Enter your new password:");
            myPanel.add(passwordLabel);
            passwordField = new JPasswordField(30);
            myPanel.add(passwordField);
            myFrame.setVisible(true);
            myFrame.add(myPanel);

            MyHandler myHandler = new MyHandler(myFrame);
            usernameField.addActionListener(myHandler);
            passwordField.addActionListener(myHandler);
        }
    }

    private class MyHandler implements ActionListener {
        JFrame myFrame;

        public MyHandler(JFrame myFrame) {
            this.myFrame = myFrame;
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == passwordField) {
                try {
                    if (userSup.containsUserAndPass(usernameField.getText(), passwordField.getText())) {
                        JOptionPane.showMessageDialog(null, "Account already exists.");
                    } else {
                        chooseSuperOrRegular(usernameField.getText(), passwordField.getText());
                        myFrame.dispose();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void chooseSuperOrRegular(String username, String password) {
        JFrame myFrame = new JFrame("Choosing Account Type");
        JPanel myPanel = new JPanel();
        myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        myFrame.setSize(600, 300);
        myFrame.setLocationRelativeTo(null);
        regularUserButton = new JButton("Regular User");
        myPanel.add(regularUserButton);
        superUserButton = new JButton("Super User");
        myPanel.add(superUserButton);
        myFrame.setVisible(true);
        myFrame.add(myPanel);

        Handler6 handler = new Handler6(username, password, myFrame);
        regularUserButton.addActionListener(handler);
        superUserButton.addActionListener(handler);
    }

    private class Handler6 implements ActionListener {
        private String username;
        private String password;
        private JFrame myFrame;

        public Handler6(String username, String password, JFrame myFrame) {
            this.username = username;
            this.password = password;
            this.myFrame = myFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == regularUserButton) {
                try {
                    if (userReg.createUser(username, password)) {
                        JOptionPane.showMessageDialog(null, "Account creation Successful");
                        myFrame.dispose();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (e.getSource() == superUserButton) {
                try {
                    if (userSup.createUser(username, password)) {
                        JOptionPane.showMessageDialog(null, "Account creation Successful. " +
                                "You now have Super User priviliges!");
                        myFrame.dispose();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }



    private void playSound(String soundName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }
}
