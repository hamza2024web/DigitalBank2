package repository.Interface;

import domain.User;

public interface UserRepository {
    User findById(int id);
    User findByEmail(String email);
    void save(User user);
    void update(User user);
    void delete(int id);
}
