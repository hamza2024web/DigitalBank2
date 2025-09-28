package repository;

import domain.Client;

import java.util.List;
import java.util.Optional;

public class ClientRepositoryImpl implements ClientRepository{

    @Override
    public void save(Client client) {

    }

    @Override
    public Optional<Client> findById(String clientId) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByFirsName(String fullName) {
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public void update(Client client) {

    }

    @Override
    public void delete(String clientId) {

    }

    @Override
    public boolean exists(String clientId) {
        return false;
    }

    @Override
    public boolean existsByFirsName(String fullName) {
        return false;
    }
}
