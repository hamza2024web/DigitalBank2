package service;

import domain.Client;
import dto.ClientDTO;
import mapper.ClientMapper;
import repository.ClientRepositoryImpl;

public class ClientService {
    private final ClientRepositoryImpl clientRepository;

    public ClientService(ClientRepositoryImpl clientRepository){
        this.clientRepository = clientRepository;
    }

    public ClientDTO findClientByNomAndPrenom(String clientNom , String clientPrenom){
        Client client = clientRepository.findByFirsName(clientNom).orElseThrow(() -> new RuntimeException("No Client Found By this Nom : "+clientNom));

        return ClientMapper.toClientDTO(client);
    }
}
