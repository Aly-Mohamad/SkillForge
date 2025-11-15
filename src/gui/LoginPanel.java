package gui;

import Model.JsonDatabaseManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoginPanel extends JFrame {
    private JPanel Container;
    private JLabel Email;
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JLabel password;
    private JButton LoginBtn;
    private JRadioButton instructorRadioButton;
    private JRadioButton studentRadioButton;
    private JLabel signup;


    public LoginPanel(JsonDatabaseManager db) {
        setContentPane(Container);
        setTitle("Login");
        setMinimumSize(new java.awt.Dimension(350, 250));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        ButtonGroup usertypegroup = new ButtonGroup();
        usertypegroup.add(instructorRadioButton);
        usertypegroup.add(studentRadioButton);
        emailTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField.requestFocusInWindow();
            }
        });
        emailTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField.requestFocusInWindow();
            }
        });
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginBtn.doClick(); // Simulates pressing the button
            }
        });
        LoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());
                String usertype = instructorRadioButton.isSelected() ? "Instructor" : "Student";
                if(email.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(Container, "Please Enter Email and Password!");
                    return;
                }
                if(!instructorRadioButton.isSelected()&&!studentRadioButton.isSelected()){
                    JOptionPane.showMessageDialog(Container,"Please select the type of the user!");
                    return;
                }
                if(!isValidEmail(email)){
                    JOptionPane.showMessageDialog(Container, "Please enter a valid email!");
                    return;
                }
                if(isLoginValid(email, password, usertype, db)){
                    JOptionPane.showMessageDialog(Container,"Login successful!");
                    setVisible(false);
                    //next menu

                }
                else {
                    JOptionPane.showMessageDialog(Container, "Login Credentials are Incorrect!\nPlease Try Again!!");
                }
            }
        });
        signup.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signup.setForeground(Color.BLUE);
        signup.setText("<html><u>Sign Up</u></html>");
        signup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                new SignupPanel(db);
            }
        });
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(emailRegex);
    }

    public boolean isLoginValid(String email, String password, String usertype, JsonDatabaseManager db){
        try {
            String content = new String(Files.readAllBytes(Paths.get(db.getFilename())));
            JSONArray array = new JSONArray(content);

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String Email = obj.getString("email");
                String Password = obj.getString("password");
                String Usertype = obj.getString("usertype");

                if (Usertype.equalsIgnoreCase(usertype) && Email.equals(email) && db.sha256(password).equals(Password) ) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void createUIComponents() {
        emailTextField = new JTextField();
        emailTextField.setColumns(20);
    }
}