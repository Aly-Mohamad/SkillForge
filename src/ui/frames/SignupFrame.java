package ui.frames;

import model.JsonDatabaseManager;
import model.AuthManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignupFrame extends JFrame {
    private JsonDatabaseManager db;
    private AuthManager auth;
    public SignupFrame(JsonDatabaseManager db) {
        this.db = db; this.auth = new AuthManager(db);
        setTitle("SkillForge - Signup");
        setSize(450,260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }
    private void init() {
        JPanel p = new JPanel(new java.awt.GridLayout(6,2,5,5));
        JTextField username = new JTextField();
        JTextField email = new JTextField();
        JPasswordField pass = new JPasswordField();
        JComboBox<String> role = new JComboBox<String>(new String[]{"student","instructor"});
        JButton btnCreate = new JButton("Create account");
        JButton btnBack = new JButton("Back");

        p.add(new JLabel("Username:")); p.add(username);
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Password:")); p.add(pass);
        p.add(new JLabel("Role:")); p.add(role);
        p.add(btnCreate); p.add(btnBack);
        add(p);

        username.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                email.requestFocusInWindow();
            }
        });
        email.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pass.requestFocusInWindow();
            }
        });

        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = username.getText().trim();
                String em = email.getText().trim();
                String pw = new String(pass.getPassword());
                String r = (String)role.getSelectedItem();
                if (user.isEmpty()||em.isEmpty()||pw.isEmpty()) { JOptionPane.showMessageDialog(SignupFrame.this, "All fields required"); return; }
                if (!em.matches("^[A-Za-z0-9+_.-]+@(.+)$")) { JOptionPane.showMessageDialog(SignupFrame.this, "Invalid email"); return; }
                boolean ok;
                if ("student".equals(r)) ok = auth.signupStudent(user,em,pw);
                else ok = auth.signupInstructor(user,em,pw);
                if (!ok) JOptionPane.showMessageDialog(SignupFrame.this, "Email already registered");
                else {
                    JOptionPane.showMessageDialog(SignupFrame.this, "Account created. Please login.");
                    SignupFrame.this.setVisible(false);
                    new LoginFrame(db).setVisible(true);
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SignupFrame.this.setVisible(false); new LoginFrame(db).setVisible(true);
            }
        });
    }
}
