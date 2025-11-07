package org.example.banking; // <-- FIX

import org.example.banking.config.AppConfig; // <-- FIX
import org.example.banking.entity.Account; // <-- FIX
import org.example.banking.exception.InsufficientFundsException; // <-- FIX
import org.example.banking.service.BankService; // <-- FIX
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

public class MainApp {

    public static void main(String[] args) {
        // 1. Initialize Spring Context
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        // 2. Get the BankService bean
        BankService bankService = context.getBean(BankService.class);

        // 3. Create initial accounts
        System.out.println("--- Creating initial accounts ---");
        Account alice = bankService.createAccount("Alice", new BigDecimal("1000.00"));
        Account bob = bankService.createAccount("Bob", new BigDecimal("500.00"));

        System.out.println("Initial state:");
        System.out.println(bankService.getAccount(alice.getId()));
        System.out.println(bankService.getAccount(bob.getId()));

        // === SCENARIO 1: Successful Transfer ===
        try {
            bankService.transferMoney(alice.getId(), bob.getId(), new BigDecimal("200.00"));
        } catch (Exception e) {
            System.err.println("Transfer failed: " + e.getMessage());
        }

        System.out.println("\nState after successful transfer:");
        System.out.println(bankService.getAccount(alice.getId())); // Should be 800
        System.out.println(bankService.getAccount(bob.getId()));   // Should be 700

        // === SCENARIO 2: Failed Transfer (Insufficient Funds) ===
        try {
            // Attempt to transfer 9000, but Alice only has 800.
            bankService.transferMoney(alice.getId(), bob.getId(), new BigDecimal("9000.00"));
        } catch (InsufficientFundsException e) {
            System.err.println("\n--- Transfer failed as expected ---");
            System.err.println("Error: " + e.getMessage());
            System.err.println("--- TRANSACTION WAS ROLLED BACK ---");
        }

        System.out.println("\nFinal state (should be unchanged from last step):");
        System.out.println(bankService.getAccount(alice.getId())); // Should STILL be 800
        System.out.println(bankService.getAccount(bob.getId()));   // Should STILL be 700

        // 5. Close context
        context.close();
    }
}