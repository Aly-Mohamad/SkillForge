package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Course {
    private String courseId;
    private String title;
    private String description;
    private String instructorId;
    private List<Lesson> lessons = new ArrayList<Lesson>();
    private List<String> students = new ArrayList<String>();

    public Course(String title, String description, String instructorId) {
        this.courseId = generatecourseid();
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
    }

    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructorId() { return instructorId; }
    public List<Lesson> getLessons() { return lessons; }
    public List<String> getStudents() { return students; }

    public void setTitle(String t) { this.title = t; }
    public void setDescription(String d) { this.description = d; }
    public void addLesson(Lesson l) { lessons.add(l); }
    public void removeLesson(String lessonId) { 
        for (int i = lessons.size()-1; i >= 0; i--) {
            if (lessons.get(i).getLessonId().equals(lessonId)) lessons.remove(i);
        }
    }
    public void enrollStudent(String userId) { if (!students.contains(userId)) students.add(userId); }

    private String generatecourseid() {
        int number = (int)(Math.random() * 10000);
        return String.format("C%04d", number);
    }
}
