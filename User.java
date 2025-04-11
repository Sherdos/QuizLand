import java.sql.*;

class User {
    
    static void add(String username, String secret_key){
        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                String insert = "INSERT INTO user (username, secret_key) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insert);
                pstmt.setString(1, username);    
                pstmt.setString(2, secret_key);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    static boolean checkUser(String username, String secret_key){
        String query = "SELECT * FROM user WHERE username = ? AND secret_key = ?";
        try (Connection conn = DriverManager.getConnection(Utils.dbUrl)) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, secret_key);
                ResultSet rs = pstmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("DB error: " + e.getMessage());
        }
        return false;
    }

}
