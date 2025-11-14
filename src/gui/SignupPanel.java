package gui;

import Model.Instructor;
import Model.Student;
import Model.User;
import Model.UserDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupPanel extends JFrame{
    private JPanel Container;
    private JLabel Email;
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JLabel password;
    private JButton Create;
    private JRadioButton instructorRadioButton;
    private JRadioButton studentRadioButton;
    private JPasswordField passwordField1;
    private JLabel password2;


    public SignupPanel(UserDatabase db) {
        setContentPane(Container);
        setTitle("Sign Up");
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
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField1.requestFocusInWindow(); // Simulates pressing the button
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
                Create.doClick(); // Simulates pressing the button
            }
        });
        Create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTextField.getText();
                String password = new String(passwordField.getPassword());
                String password1 = new String(passwordField1.getPassword());
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
                if(password.equals(password1)) {
                    if (db.contains(email)){
                        JOptionPane.showMessageDialog(Container, "User already exists!");
                    }
                    else {
                        if (usertype.equals("Instructor")) {
                            db.appendUser(db.getFilename(),new Instructor(email, password));
                        }
                        else if (usertype.equals("Student")) {
                            db.appendUser(db.getFilename(),new Student(email, password));
                        }
                        JOptionPane.showMessageDialog(Container, "User added successfully!");
                        setVisible(false);
                        //open next menu
                    }
                }
                else {
                    JOptionPane.showMessageDialog(Container, "Passwords are not the same!\nPlease Try Again!!");
                }
            }
        });
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(emailRegex);
    }

    private void createUIComponents() {
        emailTextField = new JTextField();
        emailTextField.setColumns(20);
    }
}
