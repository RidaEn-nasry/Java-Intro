import java.util.UUID;

class Program {

    public static void main(String[] args) {
        UsersArrayList usersArr = new UsersArrayList();
        User[] users = new User[20];
        for (int i = 0; i < 20; i++) {
            users[i] = new User("user" + (i + 1), i * 5);
            usersArr.addUser(users[i]);
        }

        TransactionLinkedList transactions = new TransactionLinkedList();
        Transaction[] transactionsArr = new Transaction[20];
        for (int i = 0; i < 20; i++) {
            // get random user
            User sender = users[(int) (Math.random() * 20)];
            User receiver = users[(int) (Math.random() * 20)];

            transactionsArr[i] = new Transaction(sender, receiver, TransferCat.DEBIT, 10);
            transactions.addTransaction(transactionsArr[i]);
        }

        // printing the transactions of the linked list
        System.out.println("Printing the transactions of the linked list");
        Transaction[] transactionsArr2 = transactions.toArray();
        for (int i = 0; i < transactionsArr2.length; i++) {
            System.out.println("Transaction " + i + ": " + transactionsArr2[i].getId());
            System.out.print("[" + transactionsArr2[i].getSender().getName() + " -> "
                    + transactionsArr2[i].getRecipient().getName() + "] : " + transactionsArr2[i].getAmount() + " ");
            System.out.println();
        }


    
        // removing a transaction by id
        UUID id = transactionsArr2[4].getId();

        try {
            transactions.removeTransactionById(id);
        } catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        }

        UUID nonExistingId = UUID.randomUUID();
        // removing a non-existing transaction by id
        try {
            transactions.removeTransactionById(nonExistingId);
        } catch (TransactionNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
    }
}