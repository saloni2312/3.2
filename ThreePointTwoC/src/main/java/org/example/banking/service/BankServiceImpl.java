package org.example.banking.service; // <-- FIX

import org.example.banking.dao.AccountDao; // <-- FIX
import org.example.banking.entity.Account; // <-- FIX
import org.example.banking.entity.TransactionHistory; // <-- FIX
import org.example.banking.exception.InsufficientFundsException; // <-- FIX
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Spring's annotation

import java.math.BigDecimal;

@Service // Marks this as a Service (business logic) bean
public class BankServiceImpl implements BankService {

    @Autowired
    private AccountDao accountDao;

    @Override
    // We make account creation transactional as well
    @Transactional
    public Account createAccount(String owner, BigDecimal initialBalance) {
        Account account = new Account(owner, initialBalance);

        // Call the updated DAO method and return the managed instance.
        // This ensures the returned object has the auto-generated ID.
        return accountDao.updateAccount(account);
    }

    @Override
    public Account getAccount(Long accountId) {
        // This method doesn't modify data, so it can be non-transactional
        // or @Transactional(readOnly = true) for optimization.
        return accountDao.getAccountById(accountId);
    }

    /**
     * This entire method runs inside a single database transaction.
     * If *any* part of this method fails (e.g., an exception is thrown),
     * Spring will automatically roll back all database changes.
     */
    @Override
    @Transactional
    public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        System.out.println("\n--- Attempting transfer of " + amount + " from " + fromAccountId + " to " + toAccountId + " ---");

        // 1. Get both accounts
        Account fromAccount = accountDao.getAccountById(fromAccountId);
        Account toAccount = accountDao.getAccountById(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Invalid account ID.");
        }

        // 2. Check for sufficient funds
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            // This exception will trigger a ROLLBACK
            throw new InsufficientFundsException("Insufficient funds in account " + fromAccountId);
        }

        // 3. Perform the debit
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        System.out.println("Debited " + amount + " from " + fromAccount.getOwnerName());

        // 4. Perform the credit
        toAccount.setBalance(toAccount.getBalance().add(amount));
        System.out.println("Credited " + amount + " to " + toAccount.getOwnerName());

        // 5. Save the updated accounts
        accountDao.updateAccount(fromAccount);
        accountDao.updateAccount(toAccount);

        // 6. Log the transaction
        TransactionHistory txLog = new TransactionHistory(fromAccountId, toAccountId, amount);
        accountDao.saveTransaction(txLog);

        System.out.println("--- Transfer successful! ---");
    }
}