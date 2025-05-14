import java.sql.*;

public class UserLogin {

    // Register a new user
    public static void registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // You can hash the password for more security
            pstmt.executeUpdate();
            System.out.println("User registered: " + username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Authenticate user login
    public static boolean loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful for user: " + username);
                return true;
            } else {
                System.out.println("Login failed. Invalid credentials.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return false;
    }
}
