package config;

import domain.Enums.Role;

import java.util.EnumSet;
import java.util.Set;

public class RoleMatrix {
    private static final Set<Role> CAN_CREATE_ACCOUNT = EnumSet.of(Role.TELLER , Role.ADMIN);

    public static boolean canCreateAccount(Role role){
        return CAN_CREATE_ACCOUNT.contains(role);
    }
}
