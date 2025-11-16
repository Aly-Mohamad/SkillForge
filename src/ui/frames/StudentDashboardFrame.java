package ui.frames;

import model.JsonDatabaseManager;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentDashboardFrame extends JFrame {
    private JsonDatabaseManager db;
    private Student student;

    public StudentDashboardFrame(JsonDatabaseManager db, Student s) {
        this.db = db; this.student = s;
        setTitle("Student Dashboard - "+s.getUsername());
        setSize(700,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new BorderLayout());
        final DefaultListModel<Course> model = new DefaultListModel<Course>();
        final JList<Course> list = new JList<Course>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new javax.swing.ListCellRenderer<Course>() {
            public java.awt.Component getListCellRendererComponent(JList<? extends Course> l, Course value, int index, boolean isSelected, boolean cellHasFocus) {
                return new JLabel(value.getTitle() + " - " + value.getDescription());
            }
        });

        List<Course> courses = db.getAllCourses();
        for (Course c : courses) model.addElement(c);

        JButton btnEnroll = new JButton("Enroll");
        JButton btnView = new JButton("View Lessons");
        JPanel south = new JPanel(); south.add(btnEnroll); south.add(btnView);
        JButton logoutbutton = new JButton("Logout");
        south.add(logoutbutton);

        p.add(new JScrollPane(list), BorderLayout.CENTER);
        p.add(south, BorderLayout.SOUTH);
        add(p);

        btnEnroll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Course c = list.getSelectedValue();
                if (c==null) { JOptionPane.showMessageDialog(StudentDashboardFrame.this, "Select a course"); return; }
                c.enrollStudent(student.getUserId());
                student.enroll(c.getCourseId());
                db.updateCourse(c);
                db.save();
                JOptionPane.showMessageDialog(StudentDashboardFrame.this, "Enrolled in "+c.getTitle());
            }
        });

        btnView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Course c = list.getSelectedValue();
                if (c==null) { JOptionPane.showMessageDialog(StudentDashboardFrame.this, "Select a course"); return; }
                new LessonListDialog(StudentDashboardFrame.this, db, student, c).setVisible(true);
            }
        });

        logoutbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        StudentDashboardFrame.this,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (choice == JOptionPane.YES_OPTION) {
                    StudentDashboardFrame.this.setVisible(false);
                    new LoginFrame(db).setVisible(true);
                }
            }
        });
    }
}
