package service;

import domain.Client;
import dto.ClientDTO;
import dto.CreateAccountDTO;
import mapper.ClientMapper;
import repository.ClientRepositoryImpl;

import java.math.BigDecimal;
import java.util.Optional;

public class ClientService {
    private final ClientRepositoryImpl clientRepository;

    public ClientService(ClientRepositoryImpl clientRepository){
        this.clientRepository = clientRepository;
    }

    public ClientDTO findOrCreateClient(CreateAccountDTO dto) {
        Optional<Client> existing = clientRepository.findByNomAndPrenom(dto.getLastName(), dto.getFirstName());

        if (existing.isPresent()) {
            return ClientMapper.toClientDTO(existing.get());
        }

        BigDecimal income = new BigDecimal(dto.getMonthlyIncome());
        Client newClient = new Client(null, dto.getLastName(), dto.getFirstName(), income);

        Client savedClient = clientRepository.save(newClient);

        return ClientMapper.toClientDTO(savedClient);
    }
}
