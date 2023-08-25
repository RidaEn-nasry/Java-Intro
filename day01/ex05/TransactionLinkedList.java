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
        Node prev = null;
        while (node != null) {
            if (node.val.getId().equals(id)) {
                if (prev == null) {
                    head = node.next;
                } else {
                    prev.next = node.next;
                }
                len -= 1;
                return;
            }
            prev = node;
            node = node.next;
        }
        throw new TransactionNotFoundException("transaction with id: " + id + " doesn't exist");
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