package com.org.Model;

public class AdvancedCourse extends Course {
    private String prerequisites;

    public AdvancedCourse(int courseId, String courseName, String description, int duration, String prerequisites) {
        super(courseId, courseName, description, "Advanced", duration);
        this.prerequisites = prerequisites;
    }

    public String getPrerequisites() { return prerequisites; }

    @Override
    public void displayCourseDetails() {
        System.out.println("Advanced Course: " + getCourseName() + " - " + getDescription() + 
                           ". Prerequisites: " + prerequisites);
    }
}
