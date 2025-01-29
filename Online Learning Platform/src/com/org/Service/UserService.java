package com.org.Service;

import com.org.Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    
    public void registerUser(Connection conn, User user) throws SQLException {
        String query = "INSERT INTO Users (name, email, password, progress) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getProgress());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("User registration failed, no rows affected.");
            }
        }
    }
    
    public boolean isEmailTaken(Connection conn, String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  
            }
        }
        return false;
    }


    public void enrollUser(Connection conn, int userId, int courseId) throws SQLException {
        String query = "INSERT INTO Enrollments (user_id, course_id, completion_status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, courseId);
            stmt.setString(3, "In Progress");  

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Course enrollment failed, no rows affected.");
            }
        }
    }
    
    public void enrollUserCourse(Connection conn, int userId, int courseId) throws SQLException {
        String query = "INSERT INTO User_Course_Enrollment (user_id, course_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, courseId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User successfully enrolled in the course.");
            } else {
                System.out.println("Failed to enroll user in the course.");
            }
        }
    }
    
    public void reportProgress(Connection conn, int userId) throws SQLException {
        String query = 
            "SELECT c.course_name, e.course_id, e.completion_status, q.score " +
            "FROM Courses c " +
            "JOIN User_Course_Enrollment uce ON c.course_id = uce.course_id " +
            "JOIN Enrollments e ON uce.user_id = e.user_id AND uce.course_id = e.course_id " +
            "LEFT JOIN QuizResults q ON q.user_id = e.user_id AND q.quiz_id IN ( " +
            "    SELECT quiz_id FROM Quizzes WHERE course_id = e.course_id" +
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
                    int courseId = rs.getInt("course_id");
                    String completionStatus = rs.getString("completion_status");
                    Integer score = rs.getObject("score", Integer.class); 

                    if (score != null && score >= 10 && !"Completed".equals(completionStatus)) {
                        updateCompletionStatus(conn, userId, courseId, "Completed");
                        completionStatus = "Completed";  
                    }

                    System.out.println("Course: " + courseName);
                    System.out.println("Completion Status: " + completionStatus);
                    if (score == null) {
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

    
    private void updateCompletionStatus(Connection conn, int userId, int courseId, String status) throws SQLException {
        String updateQuery = "UPDATE Enrollments SET completion_status = ? WHERE user_id = ? AND course_id = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setString(1, status);
            updateStmt.setInt(2, userId);
            updateStmt.setInt(3, courseId);
            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Updated completion status to 'Completed' for Course ID: " + courseId);
            }
        }
    }
    
    public void handleQuizCompletion(Connection conn, int userId, int courseId, int score) throws SQLException {
        
        String insertQuizResultQuery = "INSERT INTO QuizResults (user_id, course_id, score) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuizResultQuery)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, courseId);
            stmt.setInt(3, score);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Quiz result inserted successfully for User ID: " + userId + ", Course ID: " + courseId + ", Score: " + score);
            } else {
                System.out.println("Failed to insert quiz result.");
                return;  
            }
        }

        
        if (score >= 10) {
            
            String updateStatusQuery = "UPDATE Enrollments SET completion_status = 'Completed' WHERE user_id = ? AND course_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateStatusQuery)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, courseId);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Completion status updated to 'Completed' for User ID: " + userId + ", Course ID: " + courseId);
                } else {
                    System.out.println("No records found to update completion status.");
                }
            }
        } else {
            System.out.println("Score is less than 10; no update to completion status.");
        }
    }

}
