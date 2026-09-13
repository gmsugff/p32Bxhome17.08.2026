package model;

import db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperationDAO {


    public Operation insertOperation(int a, int b) throws SQLException {
        String sql = "INSERT INTO operations (a, b) VALUES (?, ?) RETURNING id, a, b, c";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, a);
            pstmt.setInt(2, b);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Operation(
                            rs.getInt("id"),
                            rs.getInt("a"),
                            rs.getInt("b"),
                            rs.getInt("c")
                    );
                }
            }
        }
        return null;
    }


    public List<Operation> getAllOperations() throws SQLException {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT id, a, b, c FROM operations ORDER BY id";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                operations.add(new Operation(
                        rs.getInt("id"),
                        rs.getInt("a"),
                        rs.getInt("b"),
                        rs.getInt("c")
                ));
            }
        }
        return operations;
    }


    public void clearHistory() throws SQLException {
        String sql = "DELETE FROM operations";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public List<Operation> filterOperations(Integer a, Integer b) throws SQLException {
        List<Operation> operations = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, a, b, c FROM operations WHERE 1=1");
        if (a != null) sql.append(" AND a = ?");
        if (b != null) sql.append(" AND b = ?");
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (a != null) pstmt.setInt(index++, a);
            if (b != null) pstmt.setInt(index, b);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    operations.add(new Operation(
                            rs.getInt("id"),
                            rs.getInt("a"),
                            rs.getInt("b"),
                            rs.getInt("c")
                    ));
                }
            }
        }
        return operations;
    }
}