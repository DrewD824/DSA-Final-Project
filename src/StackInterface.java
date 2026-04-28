/**
 * stack interface that defines the methods that will be used in the stack reference class
 * @param <T> type of item in stack
 *
 * @author Drew Dillman
 * @version v1.0
 */
public interface StackInterface<T> {
    boolean isEmpty();
    void push(T newItem);
    T pop() throws StackException;
    T peek() throws StackException;
    void popAll();
}
