import java.util.UUID;

class Program {

    public static void main(String[] args) {
        TransactionService transactionService = new TransactionService();

        // UsersArrayList usersArr = new UsersArrayList();
        User[] users = new User[20];
        for (int i = 0; i < 20; i++) {
            users[i] = new User("user" + (i + 1), i + 1 * 100);
            transactionService.addUser(users[i]);
        }

        // TransactionLinkedList transactions = new TransactionLinkedList();
        // Transaction[] transactionsArr = new Transaction[20];
        for (int i = 0; i < 20; i++) {
            // get random user
            User sender = users[(int) (Math.random() * 20)];
            User receiver = users[(int) (Math.random() * 20)];
            // transactionsArr[i] = new Transaction(sender, receiver, TransferCat.DEBIT,
            // 10);
            transactionService.performTransaction(sender.getId(), receiver.getId(), 10);
        }

        // Tests for TransactionServie

        // // adding a user

        for (int i = 0; i < 20; i++) {
            transactionService.addUser(users[i]);
        }
        // printing each user balance by id
        for (int i = 0; i < 20; i++) {
            System.out.println(" -> User " + i + " balance: "
                    + transactionService.getUserBalance(users[i].getId()));
        }

        // get transactions for each user
        for (int i = 0; i < 20; i++) {
            Transaction[] userTransactions = transactionService.getUserTransactions(users[i].getId());
            for (int j = 0; j < userTransactions.length; j++) {
                System.out.println("Transaction " + j + ": " + userTransactions[j].getId());
                System.out.print("[" + userTransactions[j].getSender().getName() + " -> "
                        + userTransactions[j].getRecipient().getName() + "] : " + userTransactions[j].getAmount()
                        + " ");
                System.out.println();

            }
        }

    }
}