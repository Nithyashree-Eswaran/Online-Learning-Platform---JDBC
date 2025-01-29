package com.org.Controller;

import com.org.Model.*;
import com.org.Service.*;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private static final String URL = "jdbc:mysql://localhost:3306/learning_platform";
    private static final String USER = "root";
    private static final String PASSWORD = "Vkookmin#7";

    
    public static Connection connectDatabase() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        
        try (Connection conn = connectDatabase(); Scanner scanner = new Scanner(System.in)) {
            System.out.println("Database connection successful....");
            if (conn == null) {
                System.out.println("Database connection failed.");
                return;
            }
            
            CourseService courseService = new CourseService();
            QuizService quizService = new QuizService();
            ProgressService progressService = new ProgressService();
            UserService userService = new UserService();

            while (true) {
            	System.out.println("\n|WELCOME TO JK Online Learning Platform|");
                System.out.println("\n1. Register User");
                System.out.println("2. List Courses");
                System.out.println("3. Enroll in Course");
                System.out.println("4. Start Quiz");
                System.out.println("5. View Top Performer");
                System.out.println("6. Report Progress");
                System.out.println("7. Exit");
                System.out.println("Enter Your Choice:");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                       
                        System.out.print("Enter Name: ");
                        scanner.nextLine();  
                        String name = scanner.nextLine(); 

                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine();

                        
                        try {
                            if (userService.isEmailTaken(conn, email)) {
                                System.out.println("Email is already registered. Please choose a different one.");
                                break;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Failed to check email availability.");
                            break;
                        }

                        System.out.print("Enter Password: ");
                        String password = scanner.nextLine();

                        String progress = "{}";  
                        User user = new User(0, name, email, password, progress);
                        try {
                            userService.registerUser(conn, user);
                            System.out.println("User registered successfully!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Failed to register the user.");
                        }
                        break;
                       
                    case 2:
                        try {
                            List<Course> courses = courseService.getCourses(conn);
                            System.out.println("=== Available Courses ===");
                            System.out.println("────────────────────────────────────────────");
                            int index = 1;
                            for (Course course : courses) {
                                System.out.printf("%d) 🆔 Course ID: %04d\n", index, course.getCourseId());
                                System.out.printf("   📚 Course Name: %s\n", course.getCourseName());
                                System.out.printf("   📝 Description: %s\n", course.getDescription());
                                System.out.printf("   📊 Level: %s\n", course.getLevel());
                                System.out.printf("   ⏳ Duration: %d hours\n", course.getDuration());
                                System.out.println("────────────────────────────────────────────");
                                index++;
                            }
                            System.out.println("===========================================");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 3:
                    	 System.out.print("Enter User ID: ");
                    	    int userId = scanner.nextInt();
                    	    System.out.print("Enter Course ID to enroll: ");
                    	    int courseId = scanner.nextInt();
                    	    try {
                    	        
                    	        userService.enrollUser(conn, userId, courseId);  
                    	        System.out.println("User enrolled in course successfully in Enrollments table.");
                    	        
                    	       
                    	        userService.enrollUserCourse(conn, userId, courseId);  
                    	        System.out.println("User enrolled in course successfully in User_Course_Enrollment table.");
                    	        
                    	    } catch (SQLException e) {
                    	        e.printStackTrace();
                    	        System.out.println("Failed to enroll the user.");
                    	    }
                    	    break;

                    case 4:
                     
                        System.out.print("Enter User ID: ");
                        int userIdForQuiz = scanner.nextInt();
                        System.out.print("Enter Course ID for quiz: ");
                        int courseIdForQuiz = scanner.nextInt();
                        try {
                            int score = quizService.startQuizForCourse(conn, courseIdForQuiz, userIdForQuiz);
                            if (score != -1) {
                                progressService.recordQuizResult(conn, userIdForQuiz, courseIdForQuiz, score);
                                System.out.println("Quiz completed. Score: " + score);
                            } else {
                                System.out.println("Quiz not found.");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 5:
                        
                        System.out.print("Enter Course ID to view top performer: ");
                        int courseIdForTopper = scanner.nextInt();
                        try {
                            User topUser = progressService.getTopperForCourse(conn, courseIdForTopper);
                            if (topUser != null) {
                                System.out.println("Top Performer: " + topUser.getName());
                            } else {
                                System.out.println("No top performer found for this course.");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 6:
                        
                        System.out.print("Enter User ID to view progress: ");
                        int userIdForProgress = scanner.nextInt();
                        try {
                            progressService.reportProgress(conn, userIdForProgress);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 7:
                        System.out.println("Good Bye...Happy Learning!!!");
                        return;

                    default:
                        System.out.println("Invalid choice... Try again!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}