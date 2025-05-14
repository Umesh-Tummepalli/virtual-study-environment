package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ToDoBackend {

    // Add a task to the database
    public static void addTask(String task) {
        String sql = "INSERT INTO tasks (task) VALUES (?)";
        try (Connection conn = DBConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task);
            pstmt.executeUpdate();
            System.out.println("Task added: " + task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all tasks from the database
    public static void getTasks() {
        String sql = "SELECT * FROM tasks";
        try (Connection conn = DBConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String task = rs.getString("task");
                boolean status = rs.getBoolean("status");
                System.out.println("Task ID: " + id + ", Task: " + task + ", Status: " + (status ? "Done" : "Pending"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mark task as done
    public static void markTaskAsDone(int taskId) {
        String sql = "UPDATE tasks SET status = true WHERE id = ?";
        try (Connection conn = DBConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
            System.out.println("Task marked as done with ID: " + taskId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a task from the database
    public static void deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DBConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
            System.out.println("Task deleted with ID: " + taskId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}