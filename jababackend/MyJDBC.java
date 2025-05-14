import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyJDBC { // Use PascalCase for class names

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/todo_list";
    private static final String USER = "root";
    private static final String PASS = "U2006@july"; // Consider storing credentials securely

    public static void main(String[] args) {
        try (Connection conn = connectDB()) { // Connection in try-with-resources
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM todo_list"; // Use a String variable for SQL query
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                String todo = resultSet.getString("todo"); // Use column name instead of "coulmnLabel"
                System.out.println(todo);
            }

            System.out.println("Database connection successful!"); // Print after successful connection
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace(); // For debugging purposes
        }
    }
    private static Connection connectDB() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}