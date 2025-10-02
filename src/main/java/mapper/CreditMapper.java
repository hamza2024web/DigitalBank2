package mapper;

import domain.CreditRequest;
import dto.CreditReqDTO;

public class CreditMapper {

    public static CreditReqDTO toCreditReqDTO(CreditRequest creditRequest) {
        if (creditRequest == null) {
            return null;
        }

        return new CreditReqDTO(
                creditRequest.getId(),
                ClientMapper.toClientDTO(creditRequest.getClient()), // Reuse ClientMapper
                creditRequest.getMontant(),
                creditRequest.getCurrency(),
                creditRequest.getDureeMois(),
                creditRequest.getTauxAnnuel(),
                creditRequest.getDescription(),
                creditRequest.getStatus(),
                creditRequest.getRequestDate(),
                creditRequest.getRequestedBy()
        );
    }

    public static CreditRequest toCreditRequest(CreditReqDTO creditReqDTO) {
        if (creditReqDTO == null) {
            return null;
        }

        return new CreditRequest(
                creditReqDTO.getId(),
                ClientMapper.toClient(creditReqDTO.getClient()), // Reuse ClientMapper
                creditReqDTO.getMontant(),
                creditReqDTO.getCurrency(),
                creditReqDTO.getDureeMois(),
                creditReqDTO.getTauxAnnuel(),
                creditReqDTO.getDescription(),
                creditReqDTO.getStatus(),
                creditReqDTO.getRequestDate(),
                creditReqDTO.getRequestedBy()
        );
    }
}