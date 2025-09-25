package security;

import domain.Enums.Role;
import domain.User;

public class Authorization {
    public boolean hasRole(User user , Role role){
        return user != null && user.getRole() == role;
    }
}
