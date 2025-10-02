package mapper;

import domain.Client;
import dto.ClientDTO;

public class ClientMapper {
    public static ClientDTO toClientDTO (Client client){
        if (client == null){
            return null;
        }

        return new ClientDTO(

        );
    }
}
