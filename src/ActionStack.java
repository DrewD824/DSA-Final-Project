/**
 * Tracks a user's parking actions this session using a stack.
 *
 * Two main features:
 *   - undo()          — reverses the most recent action (PARK ↔ LEAVE)
 *   - peekLastParked() — returns where the user most recently parked
 *                        without modifying the stack (useful for "where did I park?")
 *
 * The LIFO property of the stack is what makes undo work correctly —
 * actions are always reversed in the opposite order they were performed.
 */
public class ActionStack {

    private final StackReferenceBased<Action> stack;

    /**
     * Constructs an empty ActionStack.
     */
    public ActionStack() {
        stack = new StackReferenceBased<>();
    }

    /**
     * Pushes a new action onto the stack, recording it as the most recent action.
     * @param action the action to record
     */
    public void push(Action action) {
        stack.push(action);
    }

    /**
     * Determines whether there are any recorded actions this session.
     * @return true if no actions have been recorded (or all have been undone)
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Undoes the most recent action by reversing its effect on the lot:
     *   - Undoing a PARK  calls leave()      on the same lot and row
     *   - Undoing a LEAVE calls parkInRow()  on the same lot and row
     * The action is removed from the stack after being reversed.
     *
     * @throws StackException        if there are no actions to undo
     * @throws IllegalStateException if the reversal itself is invalid
     *         (e.g. undoing a PARK in a row that is already empty)
     */
    public void undo() {
        if (stack.isEmpty()) {
            throw new StackException("Nothing to undo.");
        }
        Action last = stack.pop();
        if (last.getType() == Action.Type.PARK) {
            last.getLot().leave(last.getRowNumber());
        } else {
            last.getLot().parkInRow(last.getRowNumber());
        }
    }

    /**
     * Returns the most recent action without removing it, or null if no
     * actions have been recorded. Useful for inspecting the user's most
     * recent action (e.g. to check whether they are currently parked) without
     * mutating the stack or having to handle a StackException.
     * @return the top Action, or null if the stack is empty
     */
    public Action getTopAction() {
        if (stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }

    /**
     * Returns a message describing where the user most recently parked,
     * without removing anything from the stack. Returns a friendly message
     * if no PARK action exists on top of the stack (e.g. last action was a LEAVE).
     *
     * @return "You last parked in Lot X, Row Y." or a message indicating
     *         no recent park action is on record
     */
    public String peekLastParked() {
        if (stack.isEmpty()) {
            return "No parking actions recorded this session.";
        }
        Action top = stack.peek();
        if (top.getType() == Action.Type.PARK) {
            return "You last parked in " + top.getLot().getName() + ", Row " + top.getRowNumber() + ".";
        } else {
            return "Your most recent action was leaving " + top.getLot().getName() + ", Row " + top.getRowNumber() + ".";
        }
    }

    /**
     * Clears all recorded actions from the stack.
     */
    public void clear() {
        stack.popAll();
    }
}
