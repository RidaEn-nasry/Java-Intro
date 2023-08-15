
class User {
    final private int id;
    private String name;
    private double balance;
    private TransactionLinkedList transactions = new TransactionLinkedList();

    public User(String name, double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance can't be negative");
        }
        this.balance = balance;
        this.name = name;
        this.id = UserIdsGenerator.getInstance().generateId();
    };

    public int getId() {
        return this.id;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double amount) {
        this.balance += amount;
    }

    public String getName() {
        return this.name;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.addTransaction(transaction);
    }

    public TransactionLinkedList getTransactions() {
        return this.transactions;
    }

}