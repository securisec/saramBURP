package gui;

import burp.BurpExtender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;

public class PreferencesPanel extends JPanel {
    private JTextField urlTextField;
    private JTextField usernameTextField;
    private JTextField tokenTextField;
    private JButton submitButton;
    private JPanel jpanel;
    private JLabel urlLabel;
    private JLabel tokenLabel;
    private JLabel usernameLabel;
    private JTextField apiKeyTextField;
    private JLabel apiKeyLabel;
    private JRadioButton remoteApiRadioButton;
    private JRadioButton localhostApiRadioButton;


    public PreferencesPanel()
    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        initialize();
        readConfig();
        remoteApiRadioButton.addActionListener((e) -> {
            if (remoteApiRadioButton.isSelected()) {
                localhostApiRadioButton.setSelected(false);
                urlTextField.setText("https://saram.securisecctf.com/api/");
            }
                }
        );
        localhostApiRadioButton.addActionListener((e) -> {
                    if (localhostApiRadioButton.isSelected()) {
                        remoteApiRadioButton.setSelected(false);
                        urlTextField.setText("http://localhost:5001/api/");
                    }
                }
        );
        urlTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                remoteApiRadioButton.setSelected(false);
                localhostApiRadioButton.setSelected(false);
            }
        });

        submitButton.addActionListener((e) -> {
            if (urlTextField.getText().equals("") && !localhostApiRadioButton.isSelected() && !remoteApiRadioButton.isSelected()) {
                BurpExtender.publicCallbacks.issueAlert("Please provide URL");
            }
            else if (tokenTextField.getText().equals("")) {
                BurpExtender.publicCallbacks.issueAlert("Please provide Token");
            }
            else if (usernameTextField.getText().equals("")) {
                BurpExtender.publicCallbacks.issueAlert("Please provide Username");
            }
            else if (apiKeyTextField.getText().equals("")) {
                BurpExtender.publicCallbacks.issueAlert("Please provide apiKey");
            }
            else {
                if (localhostApiRadioButton.isSelected()){
                    BurpExtender.publicCallbacks.saveExtensionSetting("saram_url", "http://localhost:5001/api/");
                }
                else if (remoteApiRadioButton.isSelected()){
                    BurpExtender.publicCallbacks.saveExtensionSetting("saram_url", "https://saram.securisecctf.com/api/");
                    urlTextField.setText("https://saram.securisecctf.com/api/");
                }
                else {
                    BurpExtender.publicCallbacks.saveExtensionSetting("saram_url", String.valueOf(urlTextField.getText()));
                }
                BurpExtender.publicCallbacks.saveExtensionSetting("saram_token", String.valueOf(tokenTextField.getText()));
                BurpExtender.publicCallbacks.saveExtensionSetting("saram_user", String.valueOf(usernameTextField.getText()));
                BurpExtender.publicCallbacks.saveExtensionSetting("saram_api", String.valueOf(apiKeyTextField.getText()));
                //BurpExtender.publicCallbacks.issueAlert(String.valueOf(urlTextField.getText()));

                // create or overwrite .saram.conf
                JSONObject obj = new JSONObject();
                obj.put("username", String.valueOf(usernameTextField.getText()));
                obj.put("apiKey", String.valueOf(apiKeyTextField.getText()));
                /*obj.put("token", String.valueOf(tokenTextField.getText()));
                obj.put("url", String.valueOf(urlTextField.getText()));*/


                // try-with-resources statement based on post comment below :)
                try (FileWriter file = new FileWriter(System.getProperty("user.home") + "/.saram.conf")) {
                    file.write(obj.toJSONString());
                    System.out.println("Successfully Copied JSON Object to File...");
                    System.out.println("\nJSON Object: " + obj);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void initialize() {
        jpanel = new JPanel();
        jpanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        usernameTextField = new JTextField();
        usernameTextField.setText("");
        jpanel.add(usernameTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        apiKeyTextField = new JTextField();
        apiKeyTextField.setText("");
        jpanel.add(apiKeyTextField, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tokenTextField = new JTextField();
        tokenTextField.setText("");
        jpanel.add(tokenTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        submitButton = new JButton();
        submitButton.setText("Submit");
        jpanel.add(submitButton, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        urlTextField = new JTextField();
        urlTextField.setText("");
        urlTextField.setVisible(true);
        jpanel.add(urlTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        localhostApiRadioButton = new JRadioButton();
        localhostApiRadioButton.setText("http://localhost:5001/api/");
        localhostApiRadioButton.setVisible(true);
        jpanel.add(localhostApiRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        remoteApiRadioButton = new JRadioButton();
        remoteApiRadioButton.setText("https://saram.securisecctf.com/api/");
        remoteApiRadioButton.setVisible(true);
        jpanel.add(remoteApiRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));

        urlLabel = new JLabel();
        urlLabel.setText("URL");
        jpanel.add(urlLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tokenLabel = new JLabel();
        tokenLabel.setText("Token");
        jpanel.add(tokenLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameLabel = new JLabel();
        usernameLabel.setText("Username");
        jpanel.add(usernameLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        apiKeyLabel = new JLabel();
        apiKeyLabel.setText("apiKey");
        jpanel.add(apiKeyLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

    }

    private void readConfig() {
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader(
                        System.getProperty("user.home") + "/.saram.conf"));
            JSONObject jsonObject = (JSONObject) obj;

            String username = (String) jsonObject.get("username");
            String apiKey = (String) jsonObject.get("apiKey");
            /*String token = (String) jsonObject.get("token");
            String url = (String) jsonObject.get("url");

            if (url.equals("http:\\/\\/localhost:5001\\/api\\/")) {
                localhostApiRadioButton.setSelected(true);
                urlTextField.setText("http://localhost:5001/api/");
            }
            else if (url.equals("https:\\/\\/saram.securisecctf.com\\/api\\/")) {
                remoteApiRadioButton.setSelected(true);
                remoteApiRadioButton.setText("https://saram.securisecctf.com/api/");
            }
            else {
                urlTextField.setText(url);
            }*/

            usernameTextField.setText(username);
            apiKeyTextField.setText(apiKey);
            //tokenTextField.setText(token);
            //tokenTextField.setText(System.getProperty("user.home"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public JComponent getPanel() {
        return jpanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
