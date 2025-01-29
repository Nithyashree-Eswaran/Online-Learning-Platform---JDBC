package com.org.Service;

import com.org.Model.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CourseManagementService {
    
    List<Course> getCourses(Connection conn) throws SQLException;
    
    void enrollUserInCourse(Connection conn, int userId, int courseId) throws SQLException;
    
    int startQuizForCourse(Connection conn, int courseId) throws SQLException;
    
    void recordQuizResult(Connection conn, int userId, int quizId, int score) throws SQLException;
    
    User getTopperForCourse(Connection conn, int courseId) throws SQLException;
}
