package com.org.Service;

import com.org.Model.User;
import java.sql.*;

public class ProgressService {

    
    public void recordQuizResult(Connection conn, int userId, int quizId, int score) throws SQLException {
        String sql = "INSERT INTO QuizResults (user_id, quiz_id, score, attempt_date) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, quizId);
            pstmt.setInt(3, score);
            pstmt.executeUpdate();
            System.out.println("Quiz result recorded successfully.");
        }
    }

    
    public User getTopperForCourse(Connection conn, int courseId) throws SQLException {
        String sql = "SELECT user_id, SUM(score) as totalScore FROM QuizResults " +
                     "JOIN Quizzes ON QuizResults.quiz_id = Quizzes.quiz_id " +
                     "WHERE course_id = ? GROUP BY user_id ORDER BY totalScore DESC LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int topUserId = rs.getInt("user_id");
                return fetchUserById(conn, topUserId);
            }
        }
        return null;
    }

    private User fetchUserById(Connection conn, int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("progress"));
            }
        }
        return null;
    }
    public void reportProgress(Connection conn, int userId) throws SQLException {
        
        String query = 
            "SELECT c.course_name, e.completion_status, q.score " +
            "FROM Courses c " +
            "JOIN User_Course_Enrollment uce ON c.course_id = uce.course_id " +
            "JOIN Enrollments e ON uce.user_id = e.user_id AND uce.course_id = e.course_id " +
            "LEFT JOIN QuizResults q ON q.user_id = e.user_id AND q.quiz_id = ( " +
            "    SELECT quiz_id FROM Quizzes WHERE course_id = e.course_id LIMIT 1" +
            ") " +  
            "WHERE uce.user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);  
            ResultSet rs = stmt.executeQuery();

            
            if (rs.next()) {
                System.out.println("Progress Report for User ID: " + userId);
                System.out.println("--------------------------------------------");
                do {
                   
                    String courseName = rs.getString("course_name");
                    String completionStatus = rs.getString("completion_status");
                    Integer score = rs.getInt("score");

                    System.out.println("Course: " + courseName);
                    System.out.println("Completion Status: " + completionStatus);
                    if (rs.wasNull()) {
                        
                        System.out.println("Quiz Score: Not Attempted");
                    } else {
                        
                        System.out.println("Quiz Score: " + score);
                    }
                    System.out.println("--------------------------------------------");
                } while (rs.next());
            } else {
                System.out.println("No courses found for User ID: " + userId);
            }
        }
    }

   
    public void generateProgressReport(Connection conn, int userId) throws SQLException {
        String sql = "SELECT quiz_id, SUM(score) as totalScore FROM QuizResults " +
                     "WHERE user_id = ? GROUP BY quiz_id ORDER BY totalScore DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Progress Report for User ID: " + userId);
            while (rs.next()) {
                int quizId = rs.getInt("quiz_id");
                int totalScore = rs.getInt("totalScore");
                System.out.println("Quiz ID: " + quizId + " | Total Score: " + totalScore);
            }
        }
    }
}