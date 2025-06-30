package controller;


import model.User;

public class UserManager {
    private User currentUser = null;

    public boolean register(String username, String password) {
        // بررسی تکراری نبودن یوزرنیم TODO

        return true;
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
