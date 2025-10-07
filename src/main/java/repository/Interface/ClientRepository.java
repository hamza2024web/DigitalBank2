package repository.Interface;

import domain.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(Long clientId);
    Optional<Client> findByLastName(String lastName);
    Optional<Client> findByNomAndPrenom(String lastName , String firstName);

}
