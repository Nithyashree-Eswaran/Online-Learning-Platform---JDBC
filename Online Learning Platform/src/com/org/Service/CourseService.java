package com.org.Service;

import com.org.Model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseService implements CourseManagementService {

	 @Override
	    public List<Course> getCourses(Connection conn) throws SQLException {
	        List<Course> courses = new ArrayList<>();
	        String sql = "SELECT * FROM Courses";
	        
	        try (PreparedStatement pstmt = conn.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                int courseId = rs.getInt("course_id");
	                String courseName = rs.getString("course_name");
	                String description = rs.getString("description");
	                String level = rs.getString("level");
	                int duration = rs.getInt("duration");

	               
	                if ("Basic".equals(level)) {
	                    courses.add(new BasicCourse(courseId, courseName, description, duration));
	                } else if ("Advanced".equals(level)) {
	                    courses.add(new AdvancedCourse(courseId, courseName, description, duration, "Prerequisites here"));
	                }
	            }
	        }
	        return courses;
	    }
	    
    
    @Override
    public void enrollUserInCourse(Connection conn, int userId, int courseId) throws SQLException {
        String sql = "INSERT INTO Enrollments (user_id, course_id, enroll_date, completion_status) VALUES (?, ?, NOW(), 'In Progress')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);  
            pstmt.setInt(2, courseId); 
            pstmt.executeUpdate();     
            System.out.println("User"
            		+ " enrolled successfully.");
        }
    }

    @Override
    public int startQuizForCourse(Connection conn, int courseId) throws SQLException {
        Quiz quiz = fetchQuizByCourseId(conn, courseId);
        if (quiz == null) {
            System.out.println("No quiz available for this course.");
            return -1;
        }
        return quiz.startQuiz();
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

    @Override
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

    @Override
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
}
