package ui.frames;

import model.JsonDatabaseManager;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LessonListDialog extends JDialog {
    public LessonListDialog(JFrame owner, JsonDatabaseManager db, final Student student, final Course course) {
        super(owner, "Lessons - "+course.getTitle(), true);
        setSize(600,400); setLocationRelativeTo(owner);
        final DefaultListModel<Lesson> lm = new DefaultListModel<Lesson>();
        for (Lesson L : course.getLessons()) lm.addElement(L);
        final JList<Lesson> list = new JList<Lesson>(lm);
        list.setCellRenderer(new javax.swing.ListCellRenderer<Lesson>() {
            public java.awt.Component getListCellRendererComponent(JList<? extends Lesson> l, Lesson value, int index, boolean isSelected, boolean cellHasFocus) {
                return new JLabel(value.getTitle());
            }
        });
        JButton btnOpen = new JButton("Open");
        JPanel south = new JPanel(); south.add(btnOpen);
        add(new JScrollPane(list), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        btnOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Lesson sel = list.getSelectedValue();
                if (sel==null) { JOptionPane.showMessageDialog(LessonListDialog.this, "Select a lesson"); return; }
                JTextArea ta = new JTextArea(sel.getContent()); ta.setLineWrap(true); ta.setWrapStyleWord(true);
                JOptionPane.showMessageDialog(LessonListDialog.this, new JScrollPane(ta), sel.getTitle(), JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
