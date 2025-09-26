package mapper;

import domain.Enums.Role;
import domain.User;
import dto.LoginRequestDTO;
import dto.UserDTO;

public class UserMapper {
    public static UserDTO toUserDTO(User user){
        if (user == null) return null;
        return new UserDTO(user.getId(), user.getEmail() , user.getRole());
    }

    public static User toUser(LoginRequestDTO dto, String hashedPassword , Role role){
        return new User(dto.getEmail(), hashedPassword , role);
    }

    public static LoginRequestDTO toLoginRequestDTO(User user){
        if (user == null) return null;
        return new LoginRequestDTO(user.getEmail(),user.getMotDePasseHash());
    }
}
