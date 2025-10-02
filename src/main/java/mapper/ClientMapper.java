package mapper;

import domain.Client;
import dto.ClientDTO;

public class ClientMapper {
    public static ClientDTO toClientDTO(Client client) {
        if (client == null) {
            return null;
        }

        return new ClientDTO(
                client.getId(),
                client.getNom(),
                client.getPrenom(),
                client.getRevenueMensuel()
        );
    }

    public static Client toClient(ClientDTO clientDTO) {
        if (clientDTO == null) {
            return null;
        }

        return new Client(
                clientDTO.getId(),
                clientDTO.getNom(),
                clientDTO.getPrenom(),
                clientDTO.getRevenueMensuel()
        );
    }
}