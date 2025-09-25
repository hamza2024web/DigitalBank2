package view;

import domain.User;
import security.AuthManager;
import security.Authorization;

import java.util.Scanner;

public class MenuNavigator {

    private final AuthManager authManager;
    private final Authorization authorization;
    private final AuthView authView;
    private final ConsoleView consoleView;
    private final Scanner scanner = new Scanner(System.in);

    private User currentUser;

    public MenuNavigator(AuthManager authManager , Authorization authorization , AuthView authView , ConsoleView consoleView){
        this.authManager = authManager;
        this.authorization = authorization;
        this.authView = authView;
        this.consoleView = consoleView;
    }

    public void start(){
        consoleView.showHeader("Welcome To Digital Bank");

        boolean running = true;
        while(running){
            consoleView.showMenu(new String[]{"login" , "Exit"});
            int choice = Integer.parseInt(scanner.nextLine());
            switch(choice){
                case 1 -> loginFlow();
                case 2 -> running = false;
                default -> consoleView.showMessage("Invalid choice!");
            }
        }
    }

    private void loginFlow(){
        String email = authView.askEmail();
        String password = authView.askPassword();
        currentUser = authManager.login(email,password);

        if (currentUser != null){
            consoleView.showMessage("Login successful! Welcome " + currentUser.getEmail());
            mainMenu();
        } else {
            consoleView.showMessage("Login failed! please check your credentials. ");
        }
    }

    private void mainMenu(){
        boolean menuOpen = true;
        while(menuOpen){
            consoleView.showMenu(new String[]{"View Profile","Logout"});
            int choice = Integer.parseInt(scanner.nextLine());
            switch(choice){
                case 1 -> consoleView.showMessage("Email : " + currentUser.getEmail() + " | Role : " + currentUser.getRole());
                case 2 -> {
                    consoleView.showMessage("Logged out successfully.");
                    currentUser = null;
                    menuOpen = false;
                }
                default -> consoleView.showMessage("Invalid Choice!");
            }
        }
    }
}
