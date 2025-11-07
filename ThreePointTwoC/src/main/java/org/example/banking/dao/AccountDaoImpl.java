package org.example.banking.dao; // <-- FIX

import org.example.banking.entity.Account; // <-- FIX
import org.example.banking.entity.TransactionHistory; // <-- FIX
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository // Marks this as a DAO bean and enables exception translation
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext // Injects the EntityManager
    private EntityManager entityManager;

    @Override
    public Account getAccountById(Long id) {
        return entityManager.find(Account.class, id);
    }

    @Override
    public Account updateAccount(Account account) {
        // merge() updates the entity and returns the managed instance
        return entityManager.merge(account);
    }

    @Override
    public void saveTransaction(TransactionHistory tx) {
        // persist() saves a new entity
        entityManager.persist(tx);
    }
}