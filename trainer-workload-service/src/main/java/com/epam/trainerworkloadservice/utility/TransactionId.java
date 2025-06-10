package com.epam.trainerworkloadservice.utility;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TransactionId {
    private static final ThreadLocal<String> transaction = new ThreadLocal<>();

    public void addTransactionId() {
        transaction.set(UUID.randomUUID().toString());
    }

    public void addTransactionId(String transactionId) {
        transaction.set(transactionId);
    }

    public void removeTransactionId() {
        transaction.remove();
    }

    public String getTransaction() {
        return transaction.get();
    }
}
