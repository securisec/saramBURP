package gui;

import javax.swing.*;

public class PreferencesPanel extends JPanel{
    private JTextField URLTextField;
    private JTextField usernameTextField;
    private JTextField tokenTextField;
    private JButton submitButton;
    private JPanel jpanel;


    public static void main(String[] args) {
        JFrame frame = new JFrame("PreferencesPanel");
        frame.setContentPane(new PreferencesPanel().jpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
