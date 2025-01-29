package com.org.Model;

public class QuizResult {
    private int userId;
    private int courseId;
    private int quizId;
    private int score;

 
    public QuizResult(int userId, int courseId, int quizId, int score) {
        this.userId = userId;
        this.courseId = courseId;
        this.quizId = quizId;
        this.score = score;
    }

   
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
