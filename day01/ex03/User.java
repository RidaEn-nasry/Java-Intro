

class User {
    final private int id;
    private String name;
    private double balance;
    private TransactionLinkedList transactions;

    public User(String name, double balance) {
        if (balance < 0) {
            System.err.println("Balance cannot be negative");
            System.exit(-1);
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
}