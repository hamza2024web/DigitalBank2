package service;

import domain.*;
import domain.Enums.AccountCloseStatus;
import domain.Enums.AccountType;
import domain.Enums.TransactionStatus;
import domain.Enums.TransactionType;
import dto.*;
import mapper.AccountMapper;
import repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class AccountService {

    private final AccountRepositoryImpl accountRepository;
    private final ClientRepositoryImpl clientRepository;
    private final OperationRepositoryImpl operationRepository;
    private final AuditLogRepositoryImpl auditLogRepository;
    private final TransactionRepositoryImpl transactionRepository;
    private final ExchangeRateRepositoryImpl exchangeRateRepository;
    public AccountService(AccountRepositoryImpl accountRepository, ClientRepositoryImpl clientRepository,OperationRepositoryImpl operationRepository ,AuditLogRepositoryImpl auditLogRepository,TransactionRepositoryImpl transactionRepository , ExchangeRateRepositoryImpl exchangeRateRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.operationRepository = operationRepository;
        this.auditLogRepository = auditLogRepository;
        this.transactionRepository = transactionRepository;
        this.exchangeRateRepository = exchangeRateRepository;
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

        System.out.println("Searching IBAN: '" + iban + "'");

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

    public AccountDTO clientAccountDeposit(ClientAccountDepositDTO clientAccountDeposit, User teller) {
        String iban = clientAccountDeposit.getClientIban();
        String amountStr = clientAccountDeposit.getAmount();

        Account account = accountRepository.findByIban(iban).orElseThrow(() -> new RuntimeException("This Account : " + iban + " not found ."));

        if (account.getAccountType().equals(AccountCloseStatus.PENDING)){
            throw new IllegalArgumentException("Sorry , This Account has a Pending Status Contact the manager to Active this account .");
        }

        if (account.getAccountType().equals(AccountType.CREDIT)){
            throw new IllegalArgumentException("Sorry , You can't do this action in Account has Type Credit .");
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");
        }

        account.setSolde(account.getSolde().add(amount));
        accountRepository.update(account);

        OperationHistory op = new OperationHistory(
                LocalDateTime.now(),
                "ACCOUNT_DEPOSIT",
                account.getId().toString(),
                account.getId().toString(),
                "DEPOSIT THE AMOUNT: " + amount + ".",
                "SUCCESS",
                account.getSolde(),
                account.getDevise(),
                UUID.randomUUID().toString()
        );
        operationRepository.save(op);

        AuditLog log = new AuditLog(
                LocalDateTime.now(),
                "ACCOUNT_DEPOSIT",
                "DEPOSIT account (ID=" + account.getId() + ", IBAN=" + account.getIban() + ")",
                teller.getId(),
                teller.getRole(),
                true,
                null
        );
        auditLogRepository.save(log);

        return AccountMapper.toAccountDTO(account);
    }

    public AccountDTO clientAccountWithdrawal(ClientAccountWithdrawalDTO clientAccountWithdrawal, User teller) {
        String iban = clientAccountWithdrawal.getClientIban();
        String amountStr = clientAccountWithdrawal.getAmount();

        Account account = accountRepository.findByIban(iban).orElseThrow(() -> new RuntimeException("No Account Foudn by this : " + iban + " ."));

        if (account.getAccountType().equals(AccountCloseStatus.PENDING)){
            throw new IllegalArgumentException("Sorry , This Account has a Pending Status Contact the manager to Active this account .");
        }

        if (account.getAccountType().equals(AccountType.EPARGNE)){
            throw new IllegalArgumentException("Sorry , You can't do this action in Account has Type (EPARGNE -> Saving Account)");
        }

        if (account.getAccountType().equals(AccountType.CREDIT)){
            throw new IllegalArgumentException("Sorry , You can't do this action in Account has Type Credit .");
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format");
        }

        BigDecimal balance = account.getSolde();

        if (balance.compareTo(amount) < 0) {
            OperationHistory failedOp = new OperationHistory(
                    LocalDateTime.now(),
                    "ACCOUNT_WITHDRAWAL",
                    account.getId().toString(),
                    account.getId().toString(),
                    "ATTEMPTED WITHDRAWAL OF AMOUNT: " + amount + " - INSUFFICIENT FUNDS",
                    "FAILED",
                    balance,
                    account.getDevise(),
                    UUID.randomUUID().toString()
            );
            operationRepository.save(failedOp);

            AuditLog failedLog = new AuditLog(
                    LocalDateTime.now(),
                    "ACCOUNT_WITHDRAWAL",
                    "FAILED WITHDRAWAL attempt on account (ID=" + account.getId() + ", IBAN=" + account.getIban() + ")",
                    teller.getId(),
                    teller.getRole(),
                    false,
                    "Insufficient funds"
            );
            auditLogRepository.save(failedLog);

            throw new IllegalArgumentException("Insufficient balance to perform this withdrawal. Available balance: " + balance + ", requested amount: " + amount);
        }

        account.setSolde(balance.subtract(amount));
        accountRepository.update(account);

        OperationHistory op = new OperationHistory(
                LocalDateTime.now(),
                "ACCOUNT_WITHDRAWAL",
                account.getId().toString(),
                account.getId().toString(),
                "WITHDRAWAL OF AMOUNT: " + amount + ".",
                "SUCCESS",
                account.getSolde(),
                account.getDevise(),
                UUID.randomUUID().toString()
        );
        operationRepository.save(op);

        AuditLog log = new AuditLog(
                LocalDateTime.now(),
                "ACCOUNT_WITHDRAWAL",
                "SUCCESSFUL WITHDRAWAL from account (ID=" + account.getId() + ", IBAN=" + account.getIban() + ")",
                teller.getId(),
                teller.getRole(),
                true,
                null
        );
        auditLogRepository.save(log);

        return AccountMapper.toAccountDTO(account);
    }

    public AccountDTO clientTransferAccount(ClientAccountTransferDTO clientTransferAccount, User teller) {
        String sendIban = clientTransferAccount.getSendClientIban();
        String destinationIban = clientTransferAccount.getDestinationClientIban();
        String amountStr = clientTransferAccount.getAmountTransaction();

        if (sendIban.equals(destinationIban)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        Optional<Account> sendIbanAccount = accountRepository.findByIban(sendIban);
        Optional<Account> destinationIbanAccount = accountRepository.findByIban(destinationIban);

        if (sendIbanAccount.isEmpty()) {
            throw new RuntimeException("No Account found for sender IBAN: " + sendIban);
        }

        if (destinationIbanAccount.isEmpty()) {
            throw new RuntimeException("No Account found for destination IBAN: " + destinationIban);
        }

        Account sendAccount = sendIbanAccount.get();
        Account destinationAccount = destinationIbanAccount.get();

        BigDecimal transferAmount;
        try {
            transferAmount = new BigDecimal(amountStr);
            if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format: " + amountStr);
        }

        if (!sendAccount.getActive()) {
            throw new RuntimeException("Sender account (IBAN: " + sendIban + ") is not active");
        }

        if (!destinationAccount.getActive()) {
            throw new RuntimeException("Destination account (IBAN: " + destinationIban + ") is not active");
        }

        BigDecimal senderBalance = sendAccount.getSolde();

        if (senderBalance.compareTo(transferAmount) < 0) {
            OperationHistory failedOp = new OperationHistory(
                    LocalDateTime.now(),
                    "ACCOUNT_TRANSFER",
                    sendAccount.getId().toString(),
                    destinationAccount.getId().toString(),
                    "ATTEMPTED TRANSFER OF AMOUNT: " + transferAmount + " - INSUFFICIENT FUNDS",
                    "FAILED",
                    senderBalance,
                    sendAccount.getDevise(),
                    UUID.randomUUID().toString()
            );
            operationRepository.save(failedOp);

            AuditLog failedLog = new AuditLog(
                    LocalDateTime.now(),
                    "ACCOUNT_TRANSFER",
                    "FAILED TRANSFER attempt from account (ID=" + sendAccount.getId() + ") to account (ID=" + destinationAccount.getId() + ") - Insufficient funds",
                    teller.getId(),
                    teller.getRole(),
                    false,
                    "Insufficient funds for transfer of " + transferAmount
            );
            auditLogRepository.save(failedLog);

            throw new IllegalArgumentException("Insufficient balance for transfer. Available balance: " + senderBalance + ", requested amount: " + transferAmount);
        }

        boolean bothCourant = sendAccount.getAccountType() == AccountType.COURANT
                && destinationAccount.getAccountType() == AccountType.COURANT;

        boolean sameClientNonCredit = Objects.equals(sendAccount.getClient().getId(), destinationAccount.getClient().getId())
                && sendAccount.getAccountType() != AccountType.CREDIT
                && destinationAccount.getAccountType() != AccountType.CREDIT;

        if (bothCourant || sameClientNonCredit){
            if (destinationAccount.getDevise() == sendAccount.getDevise()){
                destinationAccount.setSolde(destinationAccount.getSolde().add(transferAmount));
                sendAccount.setSolde(senderBalance.subtract(transferAmount));

                accountRepository.update(destinationAccount);
                accountRepository.update(sendAccount);

                OperationHistory senderOp = new OperationHistory(
                        LocalDateTime.now(),
                        "ACCOUNT_TRANSFER_OUT",
                        sendAccount.getId().toString(),
                        destinationAccount.getId().toString(),
                        "TRANSFER OUT OF AMOUNT: " + transferAmount + " TO IBAN: " + destinationIban,
                        "SUCCESS",
                        sendAccount.getSolde(),
                        sendAccount.getDevise(),
                        UUID.randomUUID().toString()
                );
                operationRepository.save(senderOp);

                OperationHistory receiverOp = new OperationHistory(
                        LocalDateTime.now(),
                        "ACCOUNT_TRANSFER_IN",
                        sendAccount.getId().toString(),
                        destinationAccount.getId().toString(),
                        "TRANSFER IN OF AMOUNT: " + transferAmount + " FROM IBAN: " + sendIban,
                        "SUCCESS",
                        destinationAccount.getSolde(),
                        destinationAccount.getDevise(),
                        UUID.randomUUID().toString()
                );
                operationRepository.save(receiverOp);

                AuditLog senderLog = new AuditLog(
                        LocalDateTime.now(),
                        "ACCOUNT_TRANSFER",
                        "SUCCESSFUL TRANSFER OUT from account (ID=" + sendAccount.getId() + ", IBAN=" + sendAccount.getIban() + ") to IBAN: " + destinationIban,
                        teller.getId(),
                        teller.getRole(),
                        true,
                        null
                );
                auditLogRepository.save(senderLog);

                AuditLog receiverLog = new AuditLog(
                        LocalDateTime.now(),
                        "ACCOUNT_TRANSFER",
                        "SUCCESSFUL TRANSFER IN to account (ID=" + destinationAccount.getId() + ", IBAN=" + destinationAccount.getIban() + ") from IBAN: " + sendIban,
                        teller.getId(),
                        teller.getRole(),
                        true,
                        null
                );
                auditLogRepository.save(receiverLog);

                Transaction transaction = new Transaction(
                        transferAmount,
                        TransactionType.TRANSFERIN,
                        TransactionStatus.SUCCESS,
                        LocalDateTime.now(),
                        sendAccount.getDevise(),
                        "TRANSFER AN AMOUNT " + transferAmount + "TO ACCOUNT : " + sendAccount.getIban() + " .",
                        sendAccount,
                        destinationAccount
                );

                transactionRepository.save(transaction);
            } else {
                BigDecimal rate = exchangeRateRepository.findActiveByOperationAndCurrency(sendAccount.getDevise().toString(),destinationAccount.getDevise().toString());

                BigDecimal convertedAmount = transferAmount.multiply(rate);

                destinationAccount.setSolde(destinationAccount.getSolde().add(convertedAmount));
                sendAccount.setSolde(senderBalance.subtract(transferAmount));

                accountRepository.update(destinationAccount);
                accountRepository.update(sendAccount);

                OperationHistory senderOp = new OperationHistory(
                        LocalDateTime.now(),
                        "ACCOUNT_TRANSFER_OUT",
                        sendAccount.getId().toString(),
                        destinationAccount.getId().toString(),
                        "TRANSFER OUT OF " + transferAmount + " " + sendAccount.getDevise() +
                                " TO IBAN: " + destinationIban + " (Converted to " + convertedAmount + " " + destinationAccount.getDevise() + ")",
                        "SUCCESS",
                        sendAccount.getSolde(),
                        sendAccount.getDevise(),
                        UUID.randomUUID().toString()
                );
                operationRepository.save(senderOp);

                OperationHistory receiverOp = new OperationHistory(
                        LocalDateTime.now(),
                        "ACCOUNT_TRANSFER_IN",
                        sendAccount.getId().toString(),
                        destinationAccount.getId().toString(),
                        "TRANSFER IN OF " + convertedAmount + " " + destinationAccount.getDevise() +
                                " FROM IBAN: " + sendIban + " (Original amount " + transferAmount + " " + sendAccount.getDevise() + ")",
                        "SUCCESS",
                        destinationAccount.getSolde(),
                        destinationAccount.getDevise(),
                        UUID.randomUUID().toString()
                );
                operationRepository.save(receiverOp);

                AuditLog senderLog = new AuditLog(
                        LocalDateTime.now(),
                        "ACCOUNT_TRANSFER",
                        "SUCCESSFUL TRANSFER OUT (" + transferAmount + " " + sendAccount.getDevise() +
                                ") from account (ID=" + sendAccount.getId() + ", IBAN=" + sendAccount.getIban() +
                                ") to IBAN: " + destinationIban + " converted to " + convertedAmount + " " + destinationAccount.getDevise(),
                        teller.getId(),
                        teller.getRole(),
                        true,
                        null
                );
                auditLogRepository.save(senderLog);

                AuditLog receiverLog = new AuditLog(
                        LocalDateTime.now(),
                        "ACCOUNT_TRANSFER",
                        "SUCCESSFUL TRANSFER IN (" + convertedAmount + " " + destinationAccount.getDevise() +
                                ") to account (ID=" + destinationAccount.getId() + ", IBAN=" + destinationAccount.getIban() +
                                ") from IBAN: " + sendIban,
                        teller.getId(),
                        teller.getRole(),
                        true,
                        null
                );
                auditLogRepository.save(receiverLog);

                Transaction transaction = new Transaction(
                        convertedAmount,
                        TransactionType.TRANSFERIN,
                        TransactionStatus.SUCCESS,
                        LocalDateTime.now(),
                        destinationAccount.getDevise(),
                        "TRANSFER OF " + transferAmount + " " + sendAccount.getDevise() +
                                " converted to " + convertedAmount + " " + destinationAccount.getDevise() +
                                " to IBAN: " + destinationAccount.getIban(),
                        sendAccount,
                        destinationAccount
                );

                transactionRepository.save(transaction);
            }
        } else {
            throw new IllegalArgumentException("Transfer not allowed: accounts must both be COURANT or belong to the same client (and not CREDIT).");
        }

        return AccountMapper.toAccountDTO(sendAccount);
    }

    public AccountDTO clientAccountClose(ClientAccountCloseDTO clientAccountClose, User teller) {
        String iban = clientAccountClose.getClientIban();
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found with IBAN: " + iban));

        if (account.getSolde().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Please Withdraw Firstly the balance to 0 Then Your account will be Close");
        }

        account.setCloseStatus(AccountCloseStatus.PENDING);
        accountRepository.updateCloseStatus(account);

        AuditLog log = new AuditLog(
                LocalDateTime.now(),
                "ACCOUNT_CLOSE_REQUEST",
                "Teller requested closure for account with IBAN: " + iban,
                teller.getId(),
                teller.getRole(),
                true,
                null
        );
        auditLogRepository.save(log);

        return AccountMapper.toAccountDTO(account);
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
