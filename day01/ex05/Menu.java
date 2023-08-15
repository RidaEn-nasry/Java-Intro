import java.util.Scanner;
import java.util.UUID;

// handle all execeptions thrown and give input back
class Menu {

    private TransactionService transactionService;

    Menu() {
        transactionService = new TransactionService();
    }

    private void addUser(String name, int balance) {
        User user = null;
        try {
            user = new User(name, balance);
            transactionService.addUser(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("User with id = " + user.getId() + " is added");
    }

    private void viewUserBalance(int id) {
        try {
            double amount = transactionService.getUserBalance(id);
            System.out.println(transactionService.getUserById(id).getName() + " - " + (int) amount);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void performTransaction(int senderId, int recipientId, double amount) {
        try {
            transactionService.performTransaction(senderId, recipientId, amount);
            System.out.println("The transfer is completed");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IllegalTransactionException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewUserTransactions(int id, User user) {
        Transaction[] transactions = transactionService.getUserTransactions(id);
        for (Transaction transaction : transactions) {
            if (transaction.getSender().getId() == user.getId()) {
                System.out.println("To " + transaction.getRecipient().getName() + "(" + "id = "
                        + transaction.getRecipient().getId() + ")" + " -" + (int) transaction.getAmount()
                        + " id = " + transaction.getId());
            } else {
                System.out.println("From " + transaction.getSender().getName() + "(" + "id = "
                        + transaction.getSender().getId() + ")" + " " + (int) transaction.getAmount()
                        + " id = " + transaction.getId());
            }
        }
    }

    private void removeTransfer(int userId, UUID transferId) {
        // Transfer To Mike(id = 2) 150 removed
        boolean found = false;
        try {
            Transaction[] transactions = transactionService.getUserTransactions(userId);
            for (Transaction transaction : transactions) {
                if (transaction.getId().equals(transferId)) {
                    User sender = transaction.getSender();
                    User recipient = transaction.getRecipient();
                    double amount = transaction.getAmount();
                    if (sender.getId() == userId) {
                        System.out.println("Transfer To " + recipient.getName() + "(id = " + recipient.getId() + ") "
                                + (int) amount + " removed");
                    } else {
                        System.out.println("Transfer From " + sender.getName() + "(id = " + sender.getId() + ") "
                                + (int) amount + " removed");
                    }
                    transactionService.removeTransactionByIdForUser(transferId, userId);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Transfer with id = " + transferId + " not found");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void checkTransferValidity() {
        System.out.println("Check results:");
        // Mike(id = 2) has an unacknowledged transfer id =
        // 1fc852e7-914f-4bfd-913d-0313aab1ed99 from John(id = 1) for 150
        // checking the validity of all transactions
        Transaction[] transactions = transactionService.checkValidity();
        for (Transaction transaction : transactions) {
            User sender = transaction.getSender();
            User recipient = transaction.getRecipient();
            double amount = transaction.getAmount();
            System.out.println(recipient.getName() + "(id = " + recipient.getId() + ") has an unacknowledged transfer "
                    + "id = " + transaction.getId() + " from " + sender.getName() + "(id = " + sender.getId()
                    + ") for " + (int) amount);
        }
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
                addUser(name, (int) balance);

            } else if (choice == 2) {
                System.out.println("Enter a user ID");
                int id = 0;
                try {
                    id = input.nextInt();
                } catch (Exception e) {
                    System.out.println("Please enter a valid ID");
                    continue;
                }
                viewUserBalance(id);

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
                performTransaction(senderId, recipientId, amount);
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
                viewUserTransactions(id, user);

            } else if (choice == 5) {
                if (!profile.equals("dev")) {
                    System.out.println("\nThis option is only available in dev mode, run with --profile=dev\n");
                    continue;
                } else {
                    System.out.println("Enter a user ID and a transfer ID");
                    int userId = 0;
                    String transferId = "";
                    try {
                        userId = input.nextInt();
                        transferId = input.next();
                    } catch (Exception e) {
                        System.out.println("Please enter a valid user ID and transfer ID");
                        continue;
                    }
                    removeTransfer(userId, UUID.fromString(transferId));
                }
            } else if (choice == 6) {
                if (!profile.equals("dev")) {
                    System.out.println("\nThis option is only available in dev mode, run with --profile=dev\n");
                    continue;
                }
                checkTransferValidity();

            } else if (choice == 7) {
                break;
            }
            System.out.println("---------------------------------------------------------");
        }

    }

}