package database;

import controller.UserManager;
import model.Hasher;
import model.User;

import java.io.File;
import java.sql.*;

public class DatabaseManager {
    // مسیر دیتابیس - بهتره از مسیر نسبی استفاده کنیم
    private static final String DATABASE_URL = "jdbc:sqlite:Realm War/src/database/mydatabase.db";

    // Flag برای بررسی مقداردهی اولیه
    private static boolean initialized = false;

    /**
     * سازنده کلاس - ایجاد دیتابیس و جدول در اولین فراخوانی
     */
    public DatabaseManager() {
        synchronized (DatabaseManager.class) {
            if (!initialized) {
                initializeDatabase();
                initialized = true;
            }
        }
    }

    /**
     * ایجاد دیتابیس و جدول user در صورت عدم وجود
     */
    private void initializeDatabase() {
        // ایجاد پوشه database اگر وجود ندارد
        new File("database").mkdirs();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            // ایجاد جدول user با تمام فیلدهای لازم
            String sqlCreateTable = """
                CREATE TABLE IF NOT EXISTS user (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password BLOB NOT NULL,
                    wins INTEGER DEFAULT 0,
                    losses INTEGER DEFAULT 0,
                    level INTEGER DEFAULT 1,
                    score INTEGER DEFAULT 0
                );
                """;

            stmt.execute(sqlCreateTable);
        } catch (SQLException e) {
            System.err.println("❌ خطا در ایجاد دیتابیس: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    /**
     * ثبت کاربر جدید
     */
    public static void registerUser(String username, String password) {
        try {
            // هش کردن رمز عبور
            byte[] hashedPassword = Hasher.hashPassword(password);

            // درج کاربر جدید
            String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
            try (Connection conn = DriverManager.getConnection(DATABASE_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username.trim());
                pstmt.setBytes(2, hashedPassword);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("خطا در ثبت کاربر", e);
        }
    }

    /**
     * ورود کاربر به سیستم
     */
    public static User loginUser(String username, String password) throws SQLException {

        String sql = "SELECT * FROM user WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // مقایسه رمزهای هش شده
                    byte[] storedHashedPassword = rs.getBytes("password");


                    if (Hasher.verifyPassword(password, storedHashedPassword)) {
                        // ایجاد شیء کاربر با اطلاعات کامل
                        return new User(
                                username,
                                password,
                                rs.getInt("level"),
                                rs.getInt("wins"),
                                rs.getInt("losses"),
                                rs.getInt("score")
                        );
                    }
                }
                return null;
            }
        }
    }

    /**
     * بررسی وجود نام کاربری در سیستم
     */
    public static boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM user WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("خطا در بررسی نام کاربری", e);
        }
    }

    /**
     * به‌روزرسانی اطلاعات کاربر با استفاده از شیء User
     */
    public static void updateUserStats(User user) {
        String sql = """
        UPDATE user\s
        SET wins = ?,\s
            losses = ?,\s
            level = ?,\s
            score = ?\s
        WHERE username = ?
       \s""";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, user.getWins());
            pstmt.setInt(2, user.getLosses());
            pstmt.setInt(3, user.getLevel());
            pstmt.setInt(4, user.getScore());
            pstmt.setString(5, user.getUsername().trim());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("کاربری با این نام پیدا نشد");
            }

        } catch (SQLException e) {
            throw new RuntimeException("خطا در به‌روزرسانی اطلاعات کاربر", e);
        }
    }

}