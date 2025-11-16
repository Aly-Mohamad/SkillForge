package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<String> enrolledCourses = new ArrayList<>();

    public Student(String username, String email, String passwordHash) {
        super("student", username, email, passwordHash);
    }

    public List<String> getEnrolledCourses() { return enrolledCourses; }

    public void enroll(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
        }
    }

}
