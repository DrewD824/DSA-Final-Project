/**
 * Represents one row in a parking lot. Tracks its capacity and how many of its
 * spots are currently occupied. A row is considered full when the occupied
 * counter equals (or exceeds) its capacity.
 */
public class ParkingRow {

    private int rowNumber;
    private int capacity;
    private int occupied;

    /**
     * Constructs a parking row with a given row number and total spot capacity.
     * The row begins empty (occupied = 0).
     * @param rowNumber the row's identifying number within its lot (1, 2, 3, ...)
     * @param capacity the total number of parking spots in the row
     */
    public ParkingRow(int rowNumber, int capacity) {
        if (rowNumber < 1) {
            throw new IllegalArgumentException("rowNumber must be at least 1");
        }
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1");
        }
        this.rowNumber = rowNumber;
        this.capacity = capacity;
        this.occupied = 0;
    }

    /**
     * Returns the row's identifying number within its lot.
     * @return rowNumber
     */
    public int getRowNumber() {
        return rowNumber;
    }

    /**
     * Returns the total number of spots in the row.
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the number of spots currently in use.
     * @return occupied
     */
    public int getOccupied() {
        return occupied;
    }

    /**
     * Returns the number of available spots remaining in the row.
     * @return capacity - occupied
     */
    public int availableSpots() {
        return capacity - occupied;
    }

    /**
     * Determines whether this row has no available spots.
     * @return true if the occupied counter equals or exceeds capacity
     */
    public boolean isFull() {
        return occupied >= capacity;
    }

    /**
     * Determines whether this row currently has no parked cars.
     * @return true if occupied is zero
     */
    public boolean isEmpty() {
        return occupied == 0;
    }

    /**
     * Records one car parking in this row by incrementing the occupied counter.
     * @throws IllegalStateException if the row is already full
     */
    public void park() {
        if (isFull()) {
            throw new IllegalStateException("Row " + rowNumber + " is already full");
        }
        occupied++;
    }

    /**
     * Records one car leaving this row by decrementing the occupied counter.
     * @throws IllegalStateException if the row is already empty
     */
    public void leave() {
        if (isEmpty()) {
            throw new IllegalStateException("Row " + rowNumber + " is already empty");
        }
        occupied--;
    }

    /**
     * Directly sets the occupied counter, bypassing the normal park/leave flow.
     * Intended for scenario setup (preseeding rows as partly or fully occupied)
     * so seed actions do not enter the action history stack later.
     * @param occupied the number of occupied spots to set; must be between 0 and capacity
     */
    public void preseed(int occupied) {
        if (occupied < 0 || occupied > capacity) {
            throw new IllegalArgumentException("occupied must be between 0 and " + capacity);
        }
        this.occupied = occupied;
    }

    /**
     * Returns a human-readable string showing the row's number, occupancy, and status.
     * @return formatted display string, e.g. "Row 7: 17/23 (open)"
     */
    public String toString() {
        String status = isFull() ? "FULL" : "open";
        return "Row " + rowNumber + ": " + occupied + "/" + capacity + " (" + status + ")";
    }
}
