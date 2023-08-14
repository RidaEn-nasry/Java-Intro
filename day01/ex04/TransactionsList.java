

import java.util.UUID;

interface TransactionsList {
    abstract public void addTransaction(Transaction transaction);

    abstract public void removeTransactionById(UUID id);

    Transaction[] toArray();
}