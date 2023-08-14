import java.util.Scanner;

class Menu {

    private TransactionService transactionService;

    Menu() {
        transactionService = new TransactionService();
    }

    public void run(String profile) {
        String[] prompts = {
                "1. Add a user",
                "2. View user balances",
                "3. Perform a transfer",
                "4. View all transactions for a specific user",
                "5. DEV - remove a transfer by ID",
                "6. DEV - check transfer validity",
                "7. Finish execution"
        };
        int choice = 0;
        while (choice != 7) {
            for (String prompt : prompts) {
                System.out.println(prompt);
            }
            Scanner input = new Scanner(System.in);
            if (input.hasNext()) {
                try {
                    choice = input.nextInt();
                    if (choice < 1 || choice > 7) {
                        System.out.println("Invalid Choice! Try again");
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input! Try again");
                    continue;
                }
            }
            if (choice == 1) {
                System.out.println("Enter a user name and a balance");
                String name = "";
                double balance = 0;
                try {
                    name = input.next();
                    balance = input.nextDouble();
                } catch (Exception e) {
                    System.out.println("Please enter a valid name and balance");
                    continue;
                }
                User user = new User(name, balance);
                transactionService.addUser(user);
                System.out.println("User with id = " + user.getId() + " is added");
            } else if (choice == 2) {
                System.out.println("Enter a user ID");
                int id = 0;
                try {
                    id = input.nextInt();
                } catch (Exception e) {
                    System.out.println("Please enter a valid ID");
                    continue;
                }
                try {
                    double amount = transactionService.getUserBalance(id);
                    System.out.println(transactionService.getUserById(id).getName() + " - " + (int) amount);
                } catch (UserNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else if (choice == 3) {
                System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");
                int senderId = 0;
                int recipientId = 0;
                double amount = 0;
                try {
                    senderId = input.nextInt();
                    recipientId = input.nextInt();
                    amount = input.nextDouble();
                } catch (Exception e) {
                    System.out.println("Please enter a valid sender ID, recipient ID, and transfer amount");
                    continue;
                }
                try {
                    transactionService.performTransaction(senderId, recipientId, amount);
                    System.out.println("The transfer is completed");
                } catch (UserNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (IllegalTransactionException e) {
                    System.out.println(e.getMessage());
                }
            } else if (choice == 4) {
                System.out.println("Enter a user ID");
                int id = 0;
                User user = null;
                try {
                    id = input.nextInt();
                    user = transactionService.getUserById(id);
                } catch (Exception e) {
                    System.out.println("Please enter a valid ID");
                    continue;
                }
                Transaction[] transactions = transactionService.getUserTransactions(id);
                for (Transaction transaction : transactions) {
                    if (transaction.getSender().getId() == user.getId()) {
                        System.out.println("To " + transaction.getRecipient().getName() + "(" + "id = " + transaction.getRecipient().getId() + ")" + " -" + (int) transaction.getAmount() + " id = " + transaction.getId());
                    } else {
                        System.out.println("From " + transaction.getSender().getName() + "(" + "id = " + transaction.getSender().getId() + ")" + " +" + (int) transaction.getAmount() + " id = " + transaction.getId());
                    }
                }

            }
            System.out.println("---------------------------------------------------------");
        }



    }

}