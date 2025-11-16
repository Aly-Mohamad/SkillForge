package model;

import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private String lessonId;
    private String title;
    private String content;
    private List<String> resources = new ArrayList<String>();


    public Lesson(String title, String content) {
        this.lessonId = generateLessonid();
        this.title = title;
        this.content = content;
        this.resources = new ArrayList<String>();
    }

    public String getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public List<String> getResources() { return resources; }

    public void setTitle(String t) { this.title = t; }
    public void setContent(String c) { this.content = c; }
    public void addResource(String r) { resources.add(r); }

    private String generateLessonid() {
        int number = (int)(Math.random() * 10000);
        return String.format("L%04d", number);
    }
}
