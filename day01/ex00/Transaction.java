import java.util.UUID;

enum TransferCat {
    DEBIT, CREDIT
}

class Transaction {

    private UUID id;
    private User sender;
    private User recipient;
    private TransferCat transferCat;
    private double amount;

    public Transaction(User sender, User recipient, TransferCat transferCat, double amount) {
        // generate id
        if ((transferCat == TransferCat.DEBIT && amount < 0)
                || (transferCat == TransferCat.CREDIT && amount > 0)) {
                // do something
        }
        sender.setBalance(amount);
        recipient.setBalance(amount);
    }


    public User getSender(){
        return this.sender;
    }
    
}