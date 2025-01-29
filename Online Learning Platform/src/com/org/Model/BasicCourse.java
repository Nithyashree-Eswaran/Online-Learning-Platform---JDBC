package com.org.Model;

public class BasicCourse extends Course {

    public BasicCourse(int courseId, String courseName, String description, int duration) {
        super(courseId, courseName, description, "Basic", duration);
    }

    @Override
    public void displayCourseDetails() {
        System.out.println("Basic Course: " + getCourseName() + " - " + getDescription());
    }
}
