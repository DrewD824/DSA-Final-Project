/**
 * custom stack exception class that forwards a method thrown at the specifc error
 *
 * @Author Drew Dillman
 * @version v1.0
 */
public class StackException extends RuntimeException {
    public StackException(String message) {
        super(message);
    }
}
