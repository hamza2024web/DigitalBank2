package service;

import domain.*;
import dto.ClientAccountsRequestDTO;
import dto.CreateAccountDTO;
import dto.AccountDTO;
import mapper.AccountMapper;
import repository.AccountRepositoryImpl;
import repository.AuditLogRepositoryImpl;
import repository.ClientRepositoryImpl;
import repository.OperationRepositoryImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountService {

    private final AccountRepositoryImpl accountRepository;
    private final ClientRepositoryImpl clientRepository;
    private final OperationRepositoryImpl operationRepository;
    private final AuditLogRepositoryImpl auditLogRepository;

    public AccountService(AccountRepositoryImpl accountRepository, ClientRepositoryImpl clientRepository,OperationRepositoryImpl operationRepository ,AuditLogRepositoryImpl auditLogRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.operationRepository = operationRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public AccountDTO createAccount(CreateAccountDTO createAccountDTO, User teller) {
        try {
            Client client = findOrCreateClient(createAccountDTO);

            Account account = AccountMapper.toAccount(createAccountDTO, client);

            account.setId(generateAccountId());
            account.setIban(generateIban());
            account.setDateCreation(LocalDate.now());
            account.setActive(true);

            setAccountSpecificProperties(account);

            accountRepository.save(account);

            OperationHistory op = new OperationHistory(
                    LocalDateTime.now(),
                    "ACCOUNT_CREATION",
                    account.getId().toString(),
                    account.getId().toString(),
                    "Initial account creation",
                    "SUCCESS",
                    account.getSolde(),
                    account.getDevise(),
                    UUID.randomUUID().toString()
            );

            operationRepository.save(op);

            AuditLog log = new AuditLog(
                    LocalDateTime.now(),
                    "ACCOUNT_CREATION",
                    "Created account (ID=" + account.getId() + ", IBAN=" + account.getIban() + ") for client (ID=" + client.getId() + ", Name=" + client.getNom() + " " + client.getPrenom() + ")",
                    teller.getId(),
                    teller.getRole(),
                    true,
                    null
            );

            auditLogRepository.save(log);

            return AccountMapper.toAccountDTO(account);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create account: " + e.getMessage(), e);
        }
    }

    public List<AccountDTO> clientAccountRequest(ClientAccountsRequestDTO clientAccountsRequest , User teller){
        String iban = clientAccountsRequest.getClientIban();

        Optional<Account> accountOpt = accountRepository.findByIban(iban);

        if(accountOpt.isEmpty()){
            throw new RuntimeException("No account found for IBAN: " + iban);
        }

        Account account = accountOpt.get();
        Client client = account.getClient();

        List<Account> ClientAccounts = accountRepository.findByClientId(client.getId().toString());

        AuditLog log = new AuditLog(
                LocalDateTime.now(),
                "CLIENT_ACCOUNTS_REQUEST",
                "Teller " + teller.getId() + " requested accounts for client " + client.getId(),
                teller.getId(),
                teller.getRole(),
                true,
                null
        );
        auditLogRepository.save(log);

        return ClientAccounts.stream().map(AccountMapper::toAccountDTO).toList();
    }

    private Client findOrCreateClient(CreateAccountDTO dto) {
        var existing = clientRepository.findByFirsName(dto.getFirstName());
        if (existing.isPresent()) {
            return existing.get();
        }

        BigDecimal income = new BigDecimal(dto.getMonthlyIncome());
        Client client = new Client(null, dto.getLastName(), dto.getFirstName(), income);

        clientRepository.save(client);

        var savedClient = clientRepository.findByFirsName(dto.getFirstName())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve newly created client"));

        return savedClient;
    }

    private void setAccountSpecificProperties(Account account) {
        if (account instanceof domain.CurrentAccount currentAccount) {
            if (currentAccount.getDecouvertAutorise() == null) {
                currentAccount.setDecouvertAutorise(BigDecimal.ZERO);
            }
        } else if (account instanceof domain.SavingAccount savingAccount) {
            if (savingAccount.getTauxInteret() == null) {
                savingAccount.setTauxInteret(new BigDecimal("0.03"));
            }
        }
    }

    private String generateAccountId() {
        return "ACC-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateIban() {
        // Generate proper Moroccan IBAN format
        return "MA64BMCE" + String.format("%020d", System.currentTimeMillis() % 100000000000000000L);
    }
}
