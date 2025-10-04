package service;

import domain.Client;
import dto.ClientDTO;
import mapper.ClientMapper;
import repository.ClientRepositoryImpl;

import java.util.Optional;

public class ClientService {
    private final ClientRepositoryImpl clientRepository;

    public ClientService(ClientRepositoryImpl clientRepository){
        this.clientRepository = clientRepository;
    }

    public ClientDTO findByNomAndPrenom(String clientNom, String clientPrenom) {
        Optional<Client> clientOpt = clientRepository.findByNomAndPrenom(clientNom,clientPrenom);

        if (clientOpt.isPresent()) {
            return ClientMapper.toClientDTO(clientOpt.get());
        }

        return null; // Retourne null si non trouvé
    }
}
