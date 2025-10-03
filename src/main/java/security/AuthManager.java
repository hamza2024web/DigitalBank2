package security;

import domain.User;
import org.mindrot.jbcrypt.BCrypt;
import repository.Interface.UserRepository;

public class AuthManager {

    private final UserRepository userRepository;

    public AuthManager(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User login(String email , String plainPassword){
        User user = userRepository.findByEmail(email);

        if (user != null && BCrypt.checkpw(plainPassword, user.getMotDePasseHash())){
            return user;
        }
        return null;
    }
}
