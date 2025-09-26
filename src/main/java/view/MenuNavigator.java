package view;

import controller.AuthController;
import domain.User;
import dto.UserDTO;
import security.AuthManager;
import security.Authorization;

import java.util.Scanner;

public class MenuNavigator {
    private final AuthController authController;
    private final Scanner scanner = new Scanner(System.in);

    public MenuNavigator(AuthController authController){
        this.authController = authController;
    }

    public void start(){
        while (true){
            System.out.println("\n===== MAIN MANU ====");
            System.out.println("1. Login");
            System.out.println("2. Logout");
            System.out.println("3. Exit");
            System.out.println("Choice : ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1 -> {
                    authController.login();
                    UserDTO userDTO = authController.getCurrentUserDTO();
                    if (userDTO != null){
                        switch (userDTO.getRole()){
                            case ADMIN  -> showAdminMenu();
                            case MANAGER -> showManagerMenu();
                            case TELLER -> showTellerMenu();
                        }
                    }
                }
                case 2 -> authController.logout();
                case 3 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice .");
            }
        }
    }

    private void showAdminMenu(){
        System.out.println("=== ADMIN MENU ===");
        System.out.println("1. Manage Users");
        System.out.println("2. View Reports");
    }

    private void showManagerMenu(){
        System.out.println("=== MANAGER MENU ====");
        System.out.println("1. Approve Transaction");
        System.out.println("2. View Teller Performance");
    }

    private void showTellerMenu(){
        System.out.println("=== TELLER MENU ===");
        System.out.println("1. Process Deposit");
        System.out.println("2. Process Withdrawal");
    }
}
