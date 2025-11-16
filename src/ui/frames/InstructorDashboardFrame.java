package ui.frames;

import model.JsonDatabaseManager;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructorDashboardFrame extends JFrame {
    private JsonDatabaseManager db;
    private Instructor instructor;

    public InstructorDashboardFrame(JsonDatabaseManager db, Instructor ins) {
        this.db = db; this.instructor = ins;
        setTitle("Instructor Dashboard - "+ins.getUsername());
        setSize(800,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new BorderLayout());
        final DefaultListModel<Course> model = new DefaultListModel<Course>();
        final JList<Course> list = new JList<Course>(model);
        list.setCellRenderer(new javax.swing.ListCellRenderer<Course>() {
            public java.awt.Component getListCellRendererComponent(JList<? extends Course> l, Course value, int index, boolean isSelected, boolean cellHasFocus) {
                return new JLabel(value.getTitle());
            }
        });

        for (Course c : db.getAllCourses()) {
            if (c.getInstructorId().equals(instructor.getUserId())) model.addElement(c);
        }

        JPanel buttons = new JPanel();
        JButton btnCreate = new JButton("Create Course");
        JButton btnEdit = new JButton("Edit Course");
        JButton btnViewStudents = new JButton("View Enrolled Students");
        JButton logoutbutton = new JButton("Logout");
        buttons.add(btnCreate);
        buttons.add(btnEdit);
        buttons.add(btnViewStudents);
        buttons.add(logoutbutton);

        p.add(new JScrollPane(list), BorderLayout.CENTER);
        p.add(buttons, BorderLayout.SOUTH);
        add(p);

        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = JOptionPane.showInputDialog(InstructorDashboardFrame.this, "Course title:");
                if (title==null || title.trim().isEmpty()) return;
                String desc = JOptionPane.showInputDialog(InstructorDashboardFrame.this, "Description:");
                Course c = new Course(title, desc==null?"":desc, instructor.getUserId());
                db.addCourse(c);
                instructor.addCourse(c.getCourseId());
                db.save();
                model.addElement(c);
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Course c = list.getSelectedValue();
                if (c==null) { JOptionPane.showMessageDialog(InstructorDashboardFrame.this, "Select a course"); return; }
                new CourseEditorDialog(InstructorDashboardFrame.this, db, c).setVisible(true);
            }
        });

        btnViewStudents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Course c = list.getSelectedValue();
                if (c==null) { JOptionPane.showMessageDialog(InstructorDashboardFrame.this, "Select a course"); return; }
                StringBuilder sb = new StringBuilder();
                for (String sid : c.getStudents()) {
                    java.util.Optional<User> uOpt = db.findById(sid);
                    if (uOpt.isPresent()) {
                        User u = uOpt.get();
                        sb.append(u.getUsername()).append(" ("+u.getEmail()+")\n");
                    }
                }
                if (sb.length()==0) sb.append("No enrolled students yet.");
                JOptionPane.showMessageDialog(InstructorDashboardFrame.this, sb.toString(), "Enrolled students", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        logoutbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                        InstructorDashboardFrame.this,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (choice == JOptionPane.YES_OPTION) {
                    InstructorDashboardFrame.this.setVisible(false);
                    new LoginFrame(db).setVisible(true);
                }
            }
        });
    }
}
