package ui.frames;

import model.JsonDatabaseManager;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseEditorDialog extends JDialog {
    public CourseEditorDialog(JFrame owner, final JsonDatabaseManager db, final Course course) {
        super(owner, "Edit Course - "+course.getTitle(), true);
        setSize(700,500); setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(3,2));
        final JTextField title = new JTextField(course.getTitle());
        final JTextArea desc = new JTextArea(course.getDescription());
        top.add(new JLabel("Title:")); top.add(title);
        top.add(new JLabel("Description:")); top.add(new JScrollPane(desc));

        final DefaultListModel<Lesson> lm = new DefaultListModel<Lesson>();
        for (Lesson L : course.getLessons()) lm.addElement(L);
        final JList<Lesson> lessonsList = new JList<Lesson>(lm);
        lessonsList.setCellRenderer(new javax.swing.ListCellRenderer<Lesson>() {
            public java.awt.Component getListCellRendererComponent(JList<? extends Lesson> l, Lesson value, int index, boolean isSelected, boolean cellHasFocus) {
                return new JLabel(value.getTitle());
            }
        });

        JPanel buttons = new JPanel();
        JButton addLesson = new JButton("Add Lesson");
        JButton editLesson = new JButton("Edit Lesson");
        JButton delLesson = new JButton("Delete Lesson");
        JButton save = new JButton("Save Course");
        buttons.add(addLesson); buttons.add(editLesson); buttons.add(delLesson); buttons.add(save);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(lessonsList), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addLesson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String t = JOptionPane.showInputDialog(CourseEditorDialog.this, "Lesson title:");
                if (t==null || t.trim().isEmpty()) return;
                String c = JOptionPane.showInputDialog(CourseEditorDialog.this, "Lesson content:");
                Lesson L = new Lesson(t, c==null?"":c);
                course.addLesson(L);
                lm.addElement(L);
                db.save();
            }
        });

        editLesson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Lesson sel = lessonsList.getSelectedValue(); if (sel==null) { JOptionPane.showMessageDialog(CourseEditorDialog.this, "Select lesson"); return; }
                String t = JOptionPane.showInputDialog(CourseEditorDialog.this, "Title:", sel.getTitle());
                String c = JOptionPane.showInputDialog(CourseEditorDialog.this, "Content:", sel.getContent());
                if (t!=null) sel.setTitle(t);
                if (c!=null) sel.setContent(c);
                lessonsList.repaint();
                db.save();
            }
        });

        delLesson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Lesson sel = lessonsList.getSelectedValue(); if (sel==null) { JOptionPane.showMessageDialog(CourseEditorDialog.this, "Select"); return; }
                course.removeLesson(sel.getLessonId()); lm.removeElement(sel); db.save();
            }
        });

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                course.setTitle(title.getText());
                course.setDescription(desc.getText());
                db.updateCourse(course);
                JOptionPane.showMessageDialog(CourseEditorDialog.this, "Saved");
                dispose();
            }
        });
    }
}
