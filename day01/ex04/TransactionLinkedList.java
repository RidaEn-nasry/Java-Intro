import java.util.UUID;

class TransactionLinkedList implements TransactionsList {
    private Node head;
    private Node lastAddedNode;
    private int len;

    class Node {
        Transaction val;
        Node next;

        Node(Transaction val) {
            this.val = val;
            this.next = null;
        }
    }

    public void addTransaction(Transaction transaction) {
        Node newNode = new Node(transaction);
        if (head == null) {
            head = newNode;
            lastAddedNode = newNode;
        } else {
            lastAddedNode.next = newNode;
            lastAddedNode = newNode;
        }
        len += 1;
    }

    public void removeTransactionById(UUID id) {
        Node node = head;
        while (node.next.next != null) {
            if (node.val.getId().equals(id)) {
                node.next = node.next.next;
                break;
            }
            node = node.next;
        }
        if (!node.val.getId().equals(id)) {
            throw new TransactionNotFoundException("transaction with id: " + id + " doesn't exist");
        } else {
            node.next = null;
            len -= 1;
        }
    };

    public Transaction[] toArray() {
        Transaction[] transactions = new Transaction[len];
        Node node = head;
        int i = 0;
        while (node != null) {
            transactions[i] = node.val;
            node = node.next;
            i += 1;
        }
        return transactions;
    }

}