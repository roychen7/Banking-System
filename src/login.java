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
    private JPasswordField passwordField;
    RegularUserBase userReg = new RegularUserBase();
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
    User user;


    public login() throws IOException {

        super("Banking System");
        setLayout(new FlowLayout());
        bAccount = new BankAccount();
        buttonList = new ArrayList<>();
        trueAccountList = new ArrayList<>();

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
                        accountList = bAccount.getAccountList(user.getUserId());
                        for (AccountModel acc : accountList) {
                            if (!(acc==null)) {
                                if (!(trueAccountList==null)) {
                                    if (!(trueAccountList.contains(acc))) {
                                        trueAccountList.add(acc);
                                    }
                                }
                            }
                        }
                        for (AccountModel acc : trueAccountList) {
                            if (!(acc==null)) {
                                JButton button = new JButton(String.valueOf(acc.accountId));
                                ArrayList<String> stringButtonList = new ArrayList<>();
                                if (!(buttonList==null)) {
                                    for (JButton buttons : buttonList) {
                                        stringButtonList.add(buttons.getActionCommand());
                                    }
                                }
                                String s = button.getActionCommand();
                                if (!(stringButtonList.contains(s))) {
                                    buttonList.add(button);
                                }
                                List<AccountModel> listAccounts = new ArrayList<>();
                                for (JButton Button : buttonList) {
                                    try {
                                        listAccounts.add(bAccount.findAccount(Integer.parseInt(Button.getActionCommand())));
                                    } catch (noAccountsFoundException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                List<AccountModel> listAccountsNotSameUserId = new ArrayList<>();
                                for (AccountModel account : listAccounts) {
                                    if (!(account.userId==user.getUserId())) {
                                        listAccountsNotSameUserId.add(account);
                                    }
                                }
                                List<Integer> accountIdList = new ArrayList<>();
                                for (AccountModel account1 : listAccountsNotSameUserId) {
                                    accountIdList.add(account1.accountId);
                                }
                                Iterator<JButton> iterator = buttonList.iterator();
                                while (iterator.hasNext()) {
                                    JButton but = iterator.next();
                                    if (accountIdList.contains(Integer.parseInt(but.getActionCommand()))) {
                                        iterator.remove();
                                    }
                                }
                            }
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

        public void createAccountSelectionFrame() {
            JPanel myPanel = new JPanel();
            JFrame myFrame = new JFrame("Account Selection");
            myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            myFrame.setSize(400, 400);
            myFrame.setLocationRelativeTo(null);
            createAccountbutton = new JButton("Create an Account");
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
            myFrame.add(myPanel);
            myFrame.setVisible(true);
        }

        private class Handler1 implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==createAccountbutton) {
                    playSound("leaguebuttonclick.wav");
                    createAccountOptionFrame();
                } else {
                    playSound("leaguebuttonclick.wav");
                    createAccountFrame(e);
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
                            "is " + String.valueOf(account.amount));
                } else if (e.getSource() == withdraw) {
                    playSound("leaguebuttonclick.wav");
                    createWithdrawWindow();
                } else if (e.getSource() == deposit) {
                    playSound("leaguebuttonclick.wav");
                    createDepositWindow();
                }
            }
        }

        public void createWithdrawWindow() {
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
                    }
                }
            }
        }
        public void createDepositWindow() {
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
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

        public void createAccountOptionFrame() {
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
            Handler2 handler = new Handler2();
            savings.addActionListener(handler);
            chequing.addActionListener(handler);
            myFrame.add(myPanel);
        }

        private class Handler2 implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==savings) {
                    try {
                        playSound("leaguebuttonclick.wav");
                        bAccount.createAccount(user.getUserId(),"Savings");
                        JOptionPane.showMessageDialog(null, "Account creation" +
                                " successful. Please re-login to see your account.");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (noAccountsFoundException e1) {
                        e1.printStackTrace();
                    }
                }
                if (e.getSource()==chequing) {
                    try {
                        playSound("leaguebuttonclick.wav");
                        bAccount.createAccount(user.getUserId(),"Chequing");
                        JOptionPane.showMessageDialog(null, "Account creation" +
                                " successful. Please re-login to see your account.");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (noAccountsFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

        public JButton createJButton(AccountModel account) {
            JButton button = new JButton(String.valueOf(account.accountId));
            return button;
        }

        public void createAccountCreationFrame() {
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

            MyHandler myHandler = new MyHandler();
            usernameField.addActionListener(myHandler);
            passwordField.addActionListener(myHandler);
        }
    }

    private class MyHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == passwordField) {
                String user = usernameField.getText();
                try {
                    if (userReg.createUser(user, e.getActionCommand())) {
                        playSound("swoosh.wav");
                        JOptionPane.showMessageDialog(null, "Your account has been created.");
                    } else {
                        playSound("errorsound.wav");
                        JOptionPane.showMessageDialog(null, "Account already exists.");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void playSound(String soundName) {
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
