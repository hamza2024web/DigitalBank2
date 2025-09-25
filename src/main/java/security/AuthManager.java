package security;

import domain.User;
import org.mindrot.jbcrypt.BCrypt;
import repository.UserRepositoryImpl;

public class AuthManager {

    private final UserRepositoryImpl userRepository;

    public AuthManager(UserRepositoryImpl userRepository){
        this.userRepository = userRepository;
    }

    public User login(String email , String password){
        User user = userRepository.findByEmail(email);
        if (user != null && checkPassword(password,user.getPassword())){
            return user;
        }
        return null;
    }

    public void logout(){

    }

    private boolean checkPassword(String plainPassword, String hashedPassword){
        return BCrypt.checkpw(plainPassword,hashedPassword);
    }
}
