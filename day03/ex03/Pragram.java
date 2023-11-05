
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Synchronization on queue while adding and removing data
// When the queue is empty, the consumer has to wait until the producer adds new data to the queue
// When the queue is full, the producer has to wait until the consumer consumes data and the queue has some empty buffer

// produced data
class Message {
    private int id;
    private double data;
}

class DataQueue {
    private final Queue<Message> queue = new LinkedList<>();
    private final int maxSize;
    private final Object IS_NOT_FULL = new Object();
    private final Object IS_NOT_EMPTY = new Object();

    DataQueue(int maxSize) {
        this.maxSize = maxSize;
    }
}

class Program {

}