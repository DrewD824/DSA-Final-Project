/**
 * Represents a single user action in the Rowan Parking Navigator.
 * Stores enough information to reverse the action (undo) and to
 * display a human-readable summary to the user.
 *
 * Supported action types:
 *   PARK  — user reported parking in a specific row of a lot
 *   LEAVE — user reported leaving a specific row of a lot
 */
public class Action {

    /** The two reversible action types supported by the app. */
    public enum Type { PARK, LEAVE }

    private final Type type;
    private final Lot lot;
    private final int rowNumber;

    /**
     * Constructs an Action recording what the user did and where.
     * @param type      PARK or LEAVE
     * @param lot       the lot the action occurred in
     * @param rowNumber the row the action occurred in
     */
    public Action(Type type, Lot lot, int rowNumber) {
        this.type      = type;
        this.lot       = lot;
        this.rowNumber = rowNumber;
    }

    /**
     * Returns the action type (PARK or LEAVE).
     * @return action type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the lot this action was performed in.
     * @return lot
     */
    public Lot getLot() {
        return lot;
    }

    /**
     * Returns the row number this action was performed in.
     * @return row number
     */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * Returns a human-readable summary of this action.
     * @return formatted string, e.g. "PARK in Lot A, Row 3"
     */
    public String toString() {
        return type + " in " + lot.getName() + ", Row " + rowNumber;
    }
}
