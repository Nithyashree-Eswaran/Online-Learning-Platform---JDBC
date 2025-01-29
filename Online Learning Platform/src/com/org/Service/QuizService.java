package com.org.Service;

import com.org.Model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizService {

   
    public int startQuizForCourse(Connection conn, int courseId, int userId) throws SQLException {
        Quiz quiz = fetchQuizByCourseId(conn, courseId);
        if (quiz == null) {
            System.out.println("No quiz available for this course.");
            return -1;
        }

       
        int score = 0;
        Scanner scanner = new Scanner(System.in);
        for (Question question : quiz.getQuestions()) {
            System.out.println("Question: " + question.getQuestionText());
            System.out.println("A) " + question.getOptionA());
            System.out.println("B) " + question.getOptionB());
            System.out.println("C) " + question.getOptionC());
            System.out.println("D) " + question.getOptionD());

            System.out.print("Enter your answer (A/B/C/D): ");
            String userAnswer = scanner.nextLine().toUpperCase();

            if (userAnswer.equals(question.getCorrectOption())) {
                score += 5;  
            }
        }

        System.out.println("Quiz completed. Your score: " + score);
        return score;
    }

    
    private Quiz fetchQuizByCourseId(Connection conn, int courseId) throws SQLException {
        String quizSql = "SELECT * FROM Quizzes WHERE course_id = ?";
        try (PreparedStatement quizStmt = conn.prepareStatement(quizSql)) {
            quizStmt.setInt(1, courseId);
            ResultSet quizRs = quizStmt.executeQuery();
            if (quizRs.next()) {
                int quizId = quizRs.getInt("quiz_id");
                Quiz quiz = new Quiz(quizId, courseId, quizRs.getString("quiz_title"), quizRs.getInt("total_marks"));

               
                String questionSql = "SELECT * FROM Questions WHERE quiz_id = ?";
                try (PreparedStatement questionStmt = conn.prepareStatement(questionSql)) {
                    questionStmt.setInt(1, quizId);
                    ResultSet questionRs = questionStmt.executeQuery();
                    while (questionRs.next()) {
                        quiz.addQuestion(new Question(
                            questionRs.getString("question_text"),
                            questionRs.getString("option_a"),
                            questionRs.getString("option_b"),
                            questionRs.getString("option_c"),
                            questionRs.getString("option_d"),
                            questionRs.getString("correct_option")
                        ));
                    }
                }
                return quiz;
            }
        }
        return null;
    }
}