package controller;

import database.DatabaseManager;
import model.User;
import view.screens.HomeScreen;

import java.sql.SQLException;

public class UserManager {
    private static User currentUser = null;
    private static boolean isUserLoggedIn = false;

    /**
     * ثبت نام کاربر جدید
     * @param errors StringBuilder برای جمع‌آوری خطاها
     * @param username نام کاربری
     * @param password رمز عبور
     * @param confirmPassword تکرار رمز عبور
     * @return true اگر ثبت نام موفقیت‌آمیز باشد، false در غیر این صورت
     */
    public static User register(StringBuilder errors, String username, String password, String confirmPassword) {
        errors.setLength(0); // پاک کردن خطاهای قبلی
        boolean isValid = validateRegistration(errors, username, password, confirmPassword);
        User user;

        if (isValid) {
            try {
                DatabaseManager.registerUser(username, password);
                user = login(username, password);
                setCurrentUser(user);
                return user;
            } catch (Exception e) {
                errors.append("•Registration error: ").append(e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * ورود کاربر به سیستم
     * @param username نام کاربری
     * @param password رمز عبور
     * @return true اگر ورود موفقیت‌آمیز باشد، false در غیر این صورت
     */
    public static User login(String username, String password) {
        try {
            User user = DatabaseManager.loginUser(username, password);
            setCurrentUser(user);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * خروج کاربر از سیستم
     */
    public static void logout() {
        setCurrentUser(null);
    }

    /**
     * بررسی وضعیت ورود کاربر
     * @return true اگر کاربر وارد شده باشد، false در غیر این صورت
     */
    public static boolean isLoggedIn() {
        return isUserLoggedIn && currentUser != null;
    }

    /**
     * دریافت کاربر جاری
     * @return شیء User جاری
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * تنظیم کاربر جاری
     * @param user شیء User
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
        setLogIn(user != null);
    }

    // ================= متدهای خصوصی =================

    /**
     * اعتبارسنجی اطلاعات ثبت نام
     */
    private static boolean validateRegistration(StringBuilder errors, String username, String password, String confirmPassword) {
        boolean isValid = true;

        if (username == null || username.trim().isEmpty()) {
            errors.append("• Username cannot be empty<br>");
            isValid = false;
        } else if (username.length() > 12) {
            errors.append("• Username must be at most 12 characters long<br>");
            isValid = false;
        }

        if (password == null || password.isEmpty()) {
            errors.append("• Password cannot be empty<br>");
            isValid = false;
        }/* else if (password.length() < 8) {
            errors.append("• Password must be at least 8 characters long<br>");
            isValid = false;
        }*/

        assert password != null;
        if (!password.equals(confirmPassword)) {
            errors.append("• Password and confirmation do not match<br>");
            isValid = false;
        }

        return isValid;
    }


    /**
     * به‌روزرسانی وضعیت ورود کاربر
     * @param logIn true برای ورود، false برای خروج
     */
    private static void setLogIn(boolean logIn) {
        isUserLoggedIn = logIn;
        updateUserButtonInHomeScreen();
    }

    /**
     * به‌روزرسانی دکمه کاربر/ورود در صفحه اصلی
     */
    private static void updateUserButtonInHomeScreen() {
        if (HomeScreen.userOrLoginButton != null) {
            HomeScreen.userOrLoginButton.setText(isLoggedIn() ? "پنل کاربری" : "ورود");
        }
    }
}