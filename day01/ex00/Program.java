
class Program {
    public static void main(String[] args) {
        User user1 = new User("user1", 100);
        User user2 = new User("user2", 200);

        System.out.println("-> " + user1.getName() + " : " + user1.getBalance());
        System.out.println("-> " + user2.getName() + " : " + user2.getBalance());

        // debit
        Transaction debit = new Transaction(user1, user2, TransferCat.DEBIT, 10.5);
        System.out.println(debit.getSender().getName() + " just sent " + debit.getAmount() + " to "
                + debit.getRecipient().getName());
        System.out.println("-> " + user1.getName() + " : " + user1.getBalance());
        System.out.println("-> " + user2.getName() + " : " + user2.getBalance());

        // credit

        Transaction credit = new Transaction(user1, user2, TransferCat.CREDIT, -10.5);
        System.out.println(credit.getSender().getName() + " just sent " + credit.getAmount() + " to "
                + credit.getRecipient().getName());
        System.out.println("-> " + user1.getName() + " : " + user1.getBalance());
        System.out.println("-> " + user2.getName() + " : " + user2.getBalance());

        // error sending debit with negative value
        // Transaction err = new Transaction(user1, user2, TransferCat.DEBIT, -200);

    }

}
