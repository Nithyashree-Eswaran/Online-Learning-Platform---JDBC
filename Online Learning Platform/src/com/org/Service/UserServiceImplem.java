package com.org.Service;

import com.org.Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserServiceImplem extends UserService {

    @Override
    public void registerUser(Connection conn, User user) {
        String query = "INSERT INTO Users (name, email, password, progress) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getProgress());  
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User registered successfully.");
            } else {
                System.out.println("Failed to register user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
