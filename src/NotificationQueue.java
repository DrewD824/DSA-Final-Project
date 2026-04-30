/**
 * MECHE
 * v1.0
 *
 * NotificationQueue class for Rowan Parking Navigator.
 *
 * This queue stores users who are waiting for a parking lot to open.
 * It follows FIFO order: first user in the queue is the first user notified.
 */
public class NotificationQueue {

    private int maxQueue;
    private User[] data;
    private int front;
    private int back;

    /**
     * Creates a new notification queue.
     * One space is intentionally left empty to distinguish full from empty.
     *
     * @param maxQueue the maximum array size for the queue
     */
    public NotificationQueue(int maxQueue) {
        if (maxQueue <= 1) {
            throw new IllegalArgumentException("Queue size must be greater than 1");
        }

        this.maxQueue = maxQueue;
        data = new User[maxQueue];
        front = 0;
        back = 0;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue has no users
     */
    public boolean isEmpty() {
        return front == back;
    }

    /**
     * Checks if the queue is full.
     * Because this circular queue leaves one space empty,
     * the actual usable capacity is maxQueue - 1.
     *
     * @return true if the queue is full
     */
    public boolean isFull() {
        return (back + 1) % maxQueue == front;
    }

    /**
     * Adds a user to the back of the notification queue.
     *
     * @param newUser the user joining the waitlist
     * @throws QueueException if the queue is full
     */
    public void enqueue(User newUser) throws QueueException {
        if (newUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (!isFull()) {
            data[back] = newUser;
            back = (back + 1) % maxQueue;
        } else {
            throw new QueueException("QueueException on enqueue: Notification queue full");
        }
    }

    /**
     * Removes and returns the first user in the queue.
     *
     * @return the first user in the queue
     * @throws QueueException if the queue is empty
     */
    public User dequeue() throws QueueException {
        if (!isEmpty()) {
            User queueFront = data[front];
            data[front] = null;
            front = (front + 1) % maxQueue;
            return queueFront;
        } else {
            throw new QueueException("QueueException on dequeue: Notification queue empty");
        }
    }

    /**
     * Returns the first user without removing them.
     *
     * @return the first user in the queue
     * @throws QueueException if the queue is empty
     */
    public User peek() throws QueueException {
        if (!isEmpty()) {
            return data[front];
        } else {
            throw new QueueException("QueueException on peek: Notification queue empty");
        }
    }

    /**
     * Counts how many users are currently waiting.
     *
     * @return number of users in the queue
     */
    public int size() {
        int count = 0;
        int i = front;

        while (i != back) {
            count++;
            i = (i + 1) % maxQueue;
        }

        return count;
    }

    /**
     * Prints the users currently waiting in notification order.
     */
    public void printQueue() {
        if (isEmpty()) {
            System.out.println("No users are currently waiting.");
            return;
        }

        System.out.println("Notification queue:");

        int i = front;
        int position = 1;

        while (i != back) {
            System.out.println(position + ". " + data[i].getName());
            i = (i + 1) % maxQueue;
            position++;
        }
    }
}
