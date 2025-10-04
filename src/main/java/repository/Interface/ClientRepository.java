package repository.Interface;

import domain.Client;
import dto.ClientDTO;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    void save(Client client);
    Optional<Client> findById(Long clientId);
    Optional<Client> findByFirsName(String firstName);
    Optional<Client> findByNomAndPrenom(String lastName , String firstName);
    List<Client> findAll();
    void update(Client client);
    void delete(String clientId);
    boolean exists(String clientId);
    boolean existsByFirsName(String firstName);
}
