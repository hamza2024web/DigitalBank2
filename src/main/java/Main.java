import controller.AuthController;
import repository.UserRepositoryImpl;
import security.AuthManager;
import security.Authorization;
import util.JDBCUtil;
import view.AuthView;
import view.ConsoleView;
import view.MenuNavigator;

import java.sql.Connection;

public class Main {
    public static void main(String[] args){
        UserRepositoryImpl userRepository = new UserRepositoryImpl();

        AuthManager authManager = new AuthManager(userRepository);
        Authorization authorization = new Authorization();

        AuthView authView = new AuthView();
        ConsoleView consoleView = new ConsoleView();

        AuthController authController = new AuthController(authManager, authorization , authView , consoleView);

        MenuNavigator menuNavigator = new MenuNavigator(authController);
        menuNavigator.start();
    }
}
