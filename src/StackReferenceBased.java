/**
 * stack reference class that defines the linked stack creation
 * @param <T> type of item in stack
 *
 * @author Drew Dillman
 * @version v1.0
 */
public class StackReferenceBased<T> implements StackInterface<T> {

    /**
     * linkedlist class that will be implemented to create a stack
     * @param <E> object in each node
     */
    private static class Node<E> {
        E item;
        Node<E> next;
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private Node<T> top;

    /**
     * constructor that creates an empty stack
     */
    public StackReferenceBased() {
        top = null;
    }

    /**
     * method that determines if a stack is empty
     * @return true if stack is empty false if not
     */
    @Override
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * method that adds a new item to the top of the stack
     * @param newItem the new object being pushed
     */
    @Override
    public void push(T newItem) {
        top = new Node<>(newItem, top); // insert at top
    }

    /**
     * pops an item off of the top of the stack and sets, the 2nd to top item to the new top
     * @return the item on top of the stack when called
     * @throws StackException if stack is empty
     */
    @Override
    public T pop() throws StackException {
        if (isEmpty()) {
            throw new StackException("Stack is empty; cannot pop.");
        }
        T item = top.item;
        top = top.next;
        return item;
    }

    /**
     * peeks at the top item on the stack without removing that item
     * @return the item on the top of the stack
     * @throws StackException if stack is empty
     */
    @Override
    public T peek() throws StackException {
        if (isEmpty()) {
            throw new StackException("Stack is empty; cannot peek.");
        }
        return top.item;
    }

    /**
     * clears all items from the stack by setting top equal to null
     */
    @Override
    public void popAll() {
        top = null; // clears everything; must not crash even if already empty
    }
}