package org.example.banking.dao; // <-- FIX

import org.example.banking.entity.Account; // <-- FIX
import org.example.banking.entity.TransactionHistory; // <-- FIX

public interface AccountDao {

    Account getAccountById(Long id);

    Account updateAccount(Account account); // <-- Changed from void

    void saveTransaction(TransactionHistory tx);
}