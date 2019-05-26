package ui;

import javax.swing.*;
import java.io.IOException;

public class main extends JFrame{

    public static void main(String[] args) throws IOException {
        login login = new login();
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setSize(1100, 200);
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }
}
