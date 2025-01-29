package com.org.Model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {

    private int quizId;
    private int courseId;
    private String quizTitle;
    private int totalMarks;
    private List<Question> questions;

    
    public Quiz(int quizId, int courseId, String quizTitle, int totalMarks) {
        this.quizId = quizId;
        this.courseId = courseId;
        this.quizTitle = quizTitle;
        this.totalMarks = totalMarks;
        this.questions = new ArrayList<>(); 
    }

   
    public void addQuestion(Question question) {
        questions.add(question);
    }

    
    public List<Question> getQuestions() {
        return questions;
    }

    public int getQuizId() {
        return quizId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public int getTotalMarks() {
        return totalMarks;
    }


	public int startQuiz() {
		// TODO Auto-generated method stub
		return 0;
	}

 
}
