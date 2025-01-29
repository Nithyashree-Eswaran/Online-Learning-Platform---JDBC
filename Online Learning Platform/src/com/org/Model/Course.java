package com.org.Model;

public abstract class Course {
    private int courseId;
    private String courseName;
    private String description;
    private String level;
    private int duration;

    public Course(int courseId, String courseName, String description, String level, int duration) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.level = level;
        this.duration = duration;
    }

    public int getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getDescription() { return description; }
    public String getLevel() { return level; }
    public int getDuration() { return duration; }

    public abstract void displayCourseDetails();
}
