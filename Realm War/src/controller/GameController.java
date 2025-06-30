package controller;

import view.screens.HomeScreen;

public class GameController {
    private static boolean isLogIn;
    private static UserManagement currentUser;

    public GameController() {
        setLogIn(false);
        setCurrentUser(null);
    }

    public static boolean isLoggedIn() {
        return isLogIn;
    }

    public static void setLogIn(boolean logIn) {
        isLogIn = logIn;
        updateUserButtonInHomeScreen();
    }

    private static void updateUserButtonInHomeScreen() {
        if (isLoggedIn()) {
            HomeScreen.userOrLoginButton.setText("User");
            return;
        }
        HomeScreen.userOrLoginButton.setText("Login");
    }

    public static UserManagement getCurrentUser() {
        return currentUser;
    }

        public static void setCurrentUser(UserManagement user) {
            currentUser = user;
            setLogIn(user != null);
        }

    //public static bool loginUser(String username, char[] password) {
        //TODO
    //}
}
