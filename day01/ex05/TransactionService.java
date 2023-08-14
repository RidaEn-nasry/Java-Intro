import java.util.UUID;

class TransactionService {
    private UsersArrayList usersList;

    public TransactionService() {
        this.usersList = new UsersArrayList();
    }

    public void addUser(User user) {
        this.usersList.addUser(user);
    }

    public User getUserById(int id) {
        return this.usersList.getUserById(id);
    }

    public double getUserBalance(int userId) {
        return this.usersList.getUserById(userId).getBalance();
    }

    public void performTransaction(int senderId, int recipientId, double amount) {
        User sender = this.usersList.getUserById(senderId);
        User recipient = this.usersList.getUserById(recipientId);
        if (sender.getBalance() < amount) {
            throw new IllegalTransactionException("Not enough money");
        }
        Transaction debit = new Transaction(sender, recipient, TransferCat.DEBIT, amount);
        sender.addTransaction(debit);
        recipient.addTransaction(debit);
    }

    public Transaction[] getUserTransactions(int userId) {
        return this.usersList.getUserById(userId).getTransactions().toArray();
    }

    public void removeTransactionByIdForUser(UUID transactionId, int userId) {
        TransactionLinkedList transactions = this.usersList.getUserById(userId).getTransactions();
        transactions.removeTransactionById(transactionId);
    }

    public Transaction[] checkValidity() {
        // what makes a transaction valid?
        TransactionLinkedList unpairedTransactions = new TransactionLinkedList();
        for (int i = 0; i < usersList.getUsersNum(); i++) {
            User curentUser = usersList.getUserByIndex(i);
            Transaction[] transactions = usersList.getUserByIndex(i).getTransactions().toArray();
            for (int j = 0; i < transactions.length; j++) {
                // check if the other user has a transaction with the same id
                // if not, add it to the unpairedTransactions list
                Transaction[] otherTransactions = null;
                if (transactions[j].getSender().getId() == curentUser.getId()) {
                    otherTransactions = transactions[j].getRecipient().getTransactions().toArray();
                } else {
                    otherTransactions = transactions[j].getSender().getTransactions().toArray();
                }
                boolean found = false;
                for (int k = 0; k < otherTransactions.length; k++) {
                    if (otherTransactions[j].getId().equals(transactions[j].getId())) {
                        found = true;
                        break;
                    }
                }
                if (found == true) {
                    unpairedTransactions.addTransaction(transactions[j]);
                }
            }
        }
        return unpairedTransactions.toArray();
    }

}