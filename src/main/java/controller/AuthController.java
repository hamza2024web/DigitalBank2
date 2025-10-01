package controller;

import domain.Enums.Role;
import domain.User;
import dto.LoginRequestDTO;
import dto.UserDTO;
import mapper.UserMapper;
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

    public User login() {
        String email = authView.askEmail();
        String password = authView.askPassword();

        LoginRequestDTO loginDTO = new LoginRequestDTO(email, password);

        currentUser = authManager.login(loginDTO.getEmail(), loginDTO.getPassword());

        if (currentUser != null) {
            UserDTO userDTO = new UserDTO(currentUser.getId(), currentUser.getEmail(), currentUser.getRole());
            consoleView.showMessage("Login successful! Welcome " + userDTO.getEmail() + " | Role: " + userDTO.getRole());
            return currentUser;
        } else {
            consoleView.showMessage("Login failed! Check your credentials.");
            return null;
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
        return UserMapper.toUserDTO(currentUser);
    }
}
