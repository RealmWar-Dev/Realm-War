package controller;


import model.User;

import java.util.Arrays;

public class UserManager {
    private User currentUser = null;

    public boolean register(StringBuilder errors , String username, String password , String confirmPassword) {
        boolean result = true;
        if (!isValidUserName(username)) {
            errors.append("• Invalid username<br>");
            result = false;
        }

        if (!isUserNameAvailable(username)) {
            errors.append("• username is token <br>");
            result = false;
        }

        if (!isValidUserPassword(password)) {
            errors.append("• Password required<br>");
            result = false;
        }
        if (!password.equals(confirmPassword)) {
            errors.append("• Passwords don't match<br>");
            result = false;
        }

        return result;
    }

    private boolean isUserNameAvailable(String username) {
        //TODO
        return true;
    }

    private boolean isValidUserPassword(String password) {
        return !password.isEmpty() && password.length() <= 8;
    }

    private boolean isValidUserName(String username) {
        return !username.isEmpty() && username.length() <= 12;
    }

    public boolean login(String username, String password) {
        //TODO
        return true;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}
