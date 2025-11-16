package ui.frames;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentDashboardFrame extends JFrame {
    private JsonDatabaseManager db;
    private Student student;

    public StudentDashboardFrame(JsonDatabaseManager db, Student s) {
        this.db = db;
        this.student = s;
        setTitle("Student Dashboard - " + s.getUsername());
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Models for enrolled and available courses
        DefaultListModel<Course> enrolledModel = new DefaultListModel<>();
        DefaultListModel<Course> availableModel = new DefaultListModel<>();

        // Split courses
        for (Course c : db.getAllCourses()) {
            if (student.getCourses().contains(c.getCourseId())) {
                enrolledModel.addElement(c);
            } else {
                availableModel.addElement(c);
            }
        }

        // Enrolled courses list
        JList<Course> enrolledList = new JList<>(enrolledModel);
        enrolledList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrolledList.setBorder(BorderFactory.createTitledBorder("Enrolled Courses"));
        enrolledList.setCellRenderer(courseRenderer());

        // Available courses list
        JList<Course> availableList = new JList<>(availableModel);
        availableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableList.setBorder(BorderFactory.createTitledBorder("Available Courses"));
        availableList.setCellRenderer(courseRenderer());

        // Center panel with two lists side by side
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.add(new JScrollPane(enrolledList));
        centerPanel.add(new JScrollPane(availableList));

        // Buttons
        JButton btnEnroll = new JButton("➕ Enroll");
        JButton btnView = new JButton("📖 View Lessons");
        JButton logoutButton = new JButton("🚪 Logout");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(btnEnroll);
        buttonPanel.add(btnView);
        buttonPanel.add(logoutButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // Enroll button logic
        btnEnroll.addActionListener(e -> {
            Course selected = availableList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select a course to enroll.");
                return;
            }
            selected.enrollStudent(student.getUserId());
            student.enroll(selected.getCourseId());
            db.updateCourse(selected);
            db.save();
            availableModel.removeElement(selected);
            enrolledModel.addElement(selected);
            JOptionPane.showMessageDialog(this, "Enrolled in " + selected.getTitle());
        });

        // View lessons button logic
        btnView.addActionListener(e -> {
            Course selected = enrolledList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Select an enrolled course to view lessons.");
                return;
            }
            new LessonListDialog(this, db, student, selected).setVisible(true);
        });

        // Logout button logic
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                this.setVisible(false);
                new LoginFrame(db).setVisible(true);
            }
        });
    }

    private ListCellRenderer<Course> courseRenderer() {
        return (list, value, index, isSelected, cellHasFocus) -> {
            String display = "📘 " + value.getTitle() + " — " + value.getDescription();
            JLabel label = new JLabel(display);
            label.setFont(new Font("SansSerif", Font.PLAIN, 13));
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            label.setOpaque(true);
            label.setBackground(isSelected ? new Color(220, 240, 255) : Color.WHITE);
            label.setForeground(isSelected ? Color.BLACK : new Color(50, 50, 50));
            return label;
        };
    }
}