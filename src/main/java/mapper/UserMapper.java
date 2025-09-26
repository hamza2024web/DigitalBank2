package mapper;

import domain.Enums.Role;
import domain.User;
import dto.LoginRequestDTO;

public class UserMapper {
    public static LoginRequestDTO toLoginRequestDTO(User user){
        return new LoginRequestDTO(user.getEmail(), user.getPassword());
    }

    public static User toUser(LoginRequestDTO dto, String password , Role role){
        return new User(dto.getEmail(), password , role);
    }
}
