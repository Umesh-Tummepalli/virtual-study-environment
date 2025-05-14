package com.example;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TodoApplication {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/tododb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "U2006@july";
    public static void main(String[] args) throws IOException {
        // Initialize database
        initializeDatabase();
        
        // Start HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/todos", new TodoHandler());
        server.setExecutor(null);
        server.start();
        
        System.out.println("Server started on port 8080");
    }

    // Todo class
    static class Todo {
        private int id;
        private String title;
        private String description;
        private boolean completed;
        private String dueDate;

        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
        public String getDueDate() { return dueDate; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }

        // Convert Todo to JSON string
        public String toJson() {
            return String.format(
                "{\"id\":%d,\"title\":\"%s\",\"description\":\"%s\",\"completed\":%b,\"dueDate\":\"%s\"}",
                id, title, description, completed, dueDate
            );
        }

        // Parse JSON string to Todo object
        public static Todo fromJson(String json) {
            Todo todo = new Todo();
            // Remove { } from the string
            json = json.substring(1, json.length() - 1);
            // Split by commas, but not within quotes
            String[] pairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                String key = keyValue[0].replaceAll("\"", "").trim();
                String value = keyValue[1].replaceAll("\"", "").trim();
                
                switch (key) {
                    case "id":
                        todo.setId(Integer.parseInt(value));
                        break;
                    case "title":
                        todo.setTitle(value);
                        break;
                    case "description":
                        todo.setDescription(value);
                        break;
                    case "completed":
                        todo.setCompleted(Boolean.parseBoolean(value));
                        break;
                    case "dueDate":
                        todo.setDueDate(value);
                        break;
                }
            }
            return todo;
        }
    }

    // Database operations
    static class TodoDAO {
        private static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }

        public Todo create(Todo todo) throws SQLException {
            String sql = "INSERT INTO todos (title, description, completed, due_date) VALUES (?, ?, ?, ?)";
            
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setString(1, todo.getTitle());
                pstmt.setString(2, todo.getDescription());
                pstmt.setBoolean(3, todo.isCompleted());
                pstmt.setString(4, todo.getDueDate());
                
                pstmt.executeUpdate();
                
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        todo.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return todo;
        }

        public List<Todo> findAll() throws SQLException {
            List<Todo> todos = new ArrayList<>();
            String sql = "SELECT * FROM todos";
            
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    Todo todo = new Todo();
                    todo.setId(rs.getInt("id"));
                    todo.setTitle(rs.getString("title"));
                    todo.setDescription(rs.getString("description"));
                    todo.setCompleted(rs.getBoolean("completed"));
                    todo.setDueDate(rs.getString("due_date"));
                    todos.add(todo);
                }
            }
            return todos;
        }

        public Todo findById(int id) throws SQLException {
            String sql = "SELECT * FROM todos WHERE id = ?";
            
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, id);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Todo todo = new Todo();
                        todo.setId(rs.getInt("id"));
                        todo.setTitle(rs.getString("title"));
                        todo.setDescription(rs.getString("description"));
                        todo.setCompleted(rs.getBoolean("completed"));
                        todo.setDueDate(rs.getString("due_date"));
                        return todo;
                    }
                }
            }
            return null;
        }

        public void update(Todo todo) throws SQLException {
            String sql = "UPDATE todos SET title = ?, description = ?, completed = ?, due_date = ? WHERE id = ?";
            
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, todo.getTitle());
                pstmt.setString(2, todo.getDescription());
                pstmt.setBoolean(3, todo.isCompleted());
                pstmt.setString(4, todo.getDueDate());
                pstmt.setInt(5, todo.getId());
                
                pstmt.executeUpdate();
            }
        }

        public void delete(int id) throws SQLException {
            String sql = "DELETE FROM todos WHERE id = ?";
            
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        }
    }

    // Initialize database
    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
                
            String sql = "CREATE TABLE IF NOT EXISTS todos (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "title VARCHAR(255) NOT NULL," +
                        "description TEXT," +
                        "completed BOOLEAN DEFAULT FALSE," +
                        "due_date DATE)";
            
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // HTTP Handler
    static class TodoHandler implements HttpHandler {
        private final TodoDAO todoDAO = new TodoDAO();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Enable CORS
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // Handle OPTIONS request for CORS
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            try {
                switch (exchange.getRequestMethod()) {
                    case "GET":
                        handleGet(exchange);
                        break;
                    case "POST":
                        handlePost(exchange);
                        break;
                    case "PUT":
                        handlePut(exchange);
                        break;
                    case "DELETE":
                        handleDelete(exchange);
                        break;
                    default:
                        sendResponse(exchange, 405, "Method not allowed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "Internal server error: " + e.getMessage());
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException, SQLException {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            
            if (pathParts.length > 3) {
                // Get specific todo
                int id = Integer.parseInt(pathParts[3]);
                Todo todo = todoDAO.findById(id);
                if (todo != null) {
                    sendResponse(exchange, 200, todo.toJson());
                } else {
                    sendResponse(exchange, 404, "{\"error\": \"Todo not found\"}");
                }
            } else {
                // Get all todos
                List<Todo> todos = todoDAO.findAll();
                StringBuilder jsonArray = new StringBuilder("[");
                for (int i = 0; i < todos.size(); i++) {
                    jsonArray.append(todos.get(i).toJson());
                    if (i < todos.size() - 1) {
                        jsonArray.append(",");
                    }
                }
                jsonArray.append("]");
                sendResponse(exchange, 200, jsonArray.toString());
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException, SQLException {
            String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines().collect(Collectors.joining("\n"));
            
            Todo todo = Todo.fromJson(requestBody);
            Todo created = todoDAO.create(todo);
            sendResponse(exchange, 201, created.toJson());
        }

        private void handlePut(HttpExchange exchange) throws IOException, SQLException {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int id = Integer.parseInt(pathParts[3]);
            
            String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines().collect(Collectors.joining("\n"));
            
            Todo todo = Todo.fromJson(requestBody);
            todo.setId(id);
            todoDAO.update(todo);
            sendResponse(exchange, 200, todo.toJson());
        }

        private void handleDelete(HttpExchange exchange) throws IOException, SQLException {
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");
            int id = Integer.parseInt(pathParts[3]);
            
            todoDAO.delete(id);
            sendResponse(exchange, 204, "");
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            byte[] responseBytes = response.getBytes();
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}   

