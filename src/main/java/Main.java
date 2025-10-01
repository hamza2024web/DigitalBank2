import controller.AccountController;
import controller.AuthController;
import domain.User;
import repository.*;
import security.AuthManager;
import security.Authorization;
import service.*;
import view.*;

public class Main {
    public static void main(String[] args) {
        // Repositories
        UserRepositoryImpl userRepository = new UserRepositoryImpl();
        AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();
        ClientRepositoryImpl clientRepository = new ClientRepositoryImpl();
        OperationRepositoryImpl operationRepository = new OperationRepositoryImpl();
        AuditLogRepositoryImpl auditLogRepository = new AuditLogRepositoryImpl();
        TransactionRepositoryImpl transactionRepsoitory = new TransactionRepositoryImpl();
        ExchangeRateRepositoryImpl exchangeRateRepository = new ExchangeRateRepositoryImpl();

        // Auth Layer
        AuthManager authManager = new AuthManager(userRepository);
        Authorization authorization = new Authorization();
        AuthView authView = new AuthView();
        ConsoleView consoleView = new ConsoleView();
        AuthController authController = new AuthController(authManager, authorization, authView, consoleView);

        // Services
        AccountService accountService = new AccountService(accountRepository, clientRepository, operationRepository, auditLogRepository, transactionRepsoitory, exchangeRateRepository);

        // Controllers
        AccountController accountController = new AccountController(accountService);

        // Views
        TellerView tellerView = new TellerView();

        // Menu Navigator
        MenuNavigator menuNavigator = new MenuNavigator(authController, tellerView, accountController);
        menuNavigator.start();
    }
}

