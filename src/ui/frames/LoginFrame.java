package ui.frames;

import model.AuthManager;
import model.JsonDatabaseManager;
import model.User;
import model.Student;
import model.Instructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private AuthManager auth;
    private JsonDatabaseManager db;

    public LoginFrame(JsonDatabaseManager db) {
        this.db = db; this.auth = new AuthManager(db);
        setTitle("SkillForge - Login");
        setSize(400,220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new java.awt.GridLayout(4,2,5,5));
        JTextField email = new JTextField();
        JPasswordField pass = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        JButton btnSignup = new JButton("Signup");

        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Password:")); p.add(pass);
        p.add(btnLogin); p.add(btnSignup);

        add(p);
        email.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pass.requestFocusInWindow();
            }
        });
        pass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogin.doClick();
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String em = email.getText().trim();
                String pw = new String(pass.getPassword());
                if (em.isEmpty() || pw.isEmpty()) { JOptionPane.showMessageDialog(LoginFrame.this, "Fill email and password"); return; }
                java.util.Optional<User> opt = auth.login(em,pw);
                if (opt.isPresent()) {
                    User user = opt.get();
                    JOptionPane.showMessageDialog(LoginFrame.this, "Welcome, "+user.getUsername());
                    LoginFrame.this.setVisible(false);
                    if ("student".equals(user.getRole())) {
                        Student s = (Student) user;
                        new StudentDashboardFrame(db,s).setVisible(true);
                    } else {
                        Instructor ins = (Instructor) user;
                        new InstructorDashboardFrame(db,ins).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid credentials");
                }
            }
        });

        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SignupFrame(db).setVisible(true);
                LoginFrame.this.setVisible(false);
            }
        });
    }
}
