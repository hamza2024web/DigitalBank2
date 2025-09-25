package mapper;

import domain.User;
import dto.LoginRequestDTO;

public class UserMapper {
    public static LoginRequestDTO toLoginRequestDTO(User user){
        return new LoginRequestDTO(user.getEmail(), user.getRole());
    }

    public static User toUser(LoginRequestDTO dto, String password){
        return new User(dto.getEmail(), password , dto.getRole());
    }
}
