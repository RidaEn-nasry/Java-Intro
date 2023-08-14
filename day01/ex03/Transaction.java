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
        // generate id later
        if ((transferCat == TransferCat.DEBIT && amount < 0)
        || (transferCat == TransferCat.CREDIT && amount > 0)) {
            System.err.println("IllegalArgument");
            System.exit(-1);
        }
        this.id = UUID.randomUUID();
        this.transferCat = transferCat;
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        sender.setBalance(amount * -1);
        recipient.setBalance(amount);
    }

    public User getSender() {
        return this.sender;
    }

    public User getRecipient() {
        return this.recipient;
    }

    public TransferCat getTransferCat() {
        return this.transferCat;
    }

    public UUID getId() {
        return this.id;
    }

    public double getAmount() {
        return this.amount;
    }

}