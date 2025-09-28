import controller.AccountController;
import controller.AuthController;
import repository.AccountRepositoryImpl;
import repository.ClientRepositoryImpl;
import repository.UserRepositoryImpl;
import security.AuthManager;
import security.Authorization;
import service.AccountService;
import util.JDBCUtil;
import view.AuthView;
import view.ConsoleView;
import view.MenuNavigator;
import view.TellerView;

import java.sql.Connection;

public class Main {
    public static void main(String[] args){
        UserRepositoryImpl userRepository = new UserRepositoryImpl();

        AuthManager authManager = new AuthManager(userRepository);
        Authorization authorization = new Authorization();

        AuthView authView = new AuthView();
        ConsoleView consoleView = new ConsoleView();

        AuthController authController = new AuthController(authManager, authorization , authView , consoleView);
        TellerView tellerView = new TellerView();
        AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();
        ClientRepositoryImpl clientRepository = new ClientRepositoryImpl();
        AccountService accountService = new AccountService(accountRepository,clientRepository);
        AccountController accountController = new AccountController(accountService);

        MenuNavigator menuNavigator = new MenuNavigator(authController,tellerView,accountController);
        menuNavigator.start();
    }
}
