package repository;

import domain.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    void save(Client client);
    Optional<Client> findById(String clientId);
    Optional<Client> findByFirsName(String firstName);
    List<Client> findAll();
    void update(Client client);
    void delete(String clientId);
    boolean exists(String clientId);
    boolean existsByFirsName(String firstName);
}
