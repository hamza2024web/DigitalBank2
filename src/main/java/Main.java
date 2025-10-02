import controller.AccountController;
import controller.AuthController;
import controller.CreditController;
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
        CreditService creditService = new CreditService();
        ClientService clientService = new ClientService(clientRepository);

        // Views
        TellerView tellerView = new TellerView();
        CreditView creditView = new CreditView();

        // Controllers
        AccountController accountController = new AccountController(accountService);
        CreditController creditController = new CreditController(creditService,clientService,accountService,creditView);


        // Menu Navigator
        MenuNavigator menuNavigator = new MenuNavigator(authController, tellerView, creditView,accountController , creditController);
        menuNavigator.start();
    }
}

