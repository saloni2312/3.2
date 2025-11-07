package org.example.banking.service; // <-- FIX

import org.example.banking.entity.Account; // <-- FIX
import java.math.BigDecimal;

public interface BankService {

    Account createAccount(String owner, BigDecimal initialBalance);

    void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount);

    Account getAccount(Long accountId);
}