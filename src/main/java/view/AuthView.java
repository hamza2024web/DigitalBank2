package view;

import java.util.Scanner;

public class AuthView {
    private final Scanner scanner = new Scanner(System.in);

    public String askEmail(){
        System.out.println("Enter your email: ");
        return scanner.nextLine();
    }

    public String askPassword(){
        System.out.println("Enter your password : ");
        return scanner.nextLine();
    }

    public void showMessage(String message){
        System.out.println(message);
    }
}
