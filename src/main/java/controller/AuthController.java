package controller;

import domain.Enums.Role;
import domain.User;
import dto.LoginRequestDTO;
import dto.UserDTO;
import security.AuthManager;
import security.Authorization;
import view.AuthView;
import view.ConsoleView;

public class AuthController {
    private final AuthManager authManager;
    private final Authorization authorization;
    private final AuthView authView;
    private ConsoleView consoleView;

    private User currentUser;

    public AuthController(AuthManager auhManager , Authorization authorization , AuthView authView , ConsoleView consoleView){
        this.authManager = auhManager;
        this.authorization = authorization;
        this.authView = authView;
        this.consoleView = consoleView;
    }

    public void login(){
        String email = authView.askEmail();
        String password = authView.askPassword();

        LoginRequestDTO loginDTO = new LoginRequestDTO(email,null);

        currentUser = authManager.login(loginDTO.getEmail(),password);

        if (currentUser != null){
            UserDTO userDTO = new UserDTO(currentUser.getId(),currentUser.getEmail(),currentUser.getRole());
            consoleView.showMessage("Login successful! Welcome " + userDTO.getEmail() + " | Role : " + userDTO.getRole());
        } else {
            consoleView.showMessage("Login failed! Check your credentials.");
        }
    }

    public boolean checkAccess(Role role){
        if (currentUser != null && authorization.hasRole(currentUser,role)){
            return true;
        }
        consoleView.showMessage("Access denied! you do not have permission. ");
        return false;
    }

    public void logout(){
        currentUser = null;
        consoleView.showMessage("Logged out successfully");
    }

    public UserDTO getCurrentUserDTO(){
        if (currentUser != null){
            return new UserDTO(currentUser.getId(),currentUser.getEmail(),currentUser.getRole());
        }
        return null;
    }
}
