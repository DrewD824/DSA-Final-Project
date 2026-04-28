/**
 * Represents one parking lot on campus. A lot owns a linked list of ParkingRow
 * objects and exposes lot-level operations for finding available rows, parking,
 * leaving, and reporting state. The linked list provides storage; this class
 * provides the parking-specific semantics on top of it.
 */
public class Lot {

    private String name;
    private LinkedList rows;

    /**
     * Constructs a lot with a given name and an empty row list.
     * @param name the display name of the lot (e.g., "Lot A", "Lot O")
     */
    public Lot(String name) {
        this.name = name;
        this.rows = new LinkedList();
    }

    /**
     * Returns the lot's display name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a row to the end of this lot's row list.
     * @param row the ParkingRow to add
     */
    public void addRow(ParkingRow row) {
        rows.add(row);
    }

    /**
     * Returns the total number of rows in this lot.
     * @return row count
     */
    public int rowCount() {
        return rows.size();
    }

    /**
     * Looks up a row by its row number (the row's identity within the lot).
     * Note: a row's number is not necessarily its position in the list, so
     * this performs a linear search rather than a direct index lookup.
     * @param rowNumber the row number to find
     * @return the matching ParkingRow, or null if no row has that number
     */
    public ParkingRow getRow(int rowNumber) {
        for (int i = 1; i <= rows.size(); i++) {
            ParkingRow row = (ParkingRow) rows.get(i);
            if (row.getRowNumber() == rowNumber) {
                return row;
            }
        }
        return null;
    }

    /**
     * Walks the row list and returns the first row that still has at least one
     * open spot. This is the project's linear search through the linked list.
     * @return the first open ParkingRow, or null if every row is full
     */
    public ParkingRow findFirstAvailableRow() {
        for (int i = 1; i <= rows.size(); i++) {
            ParkingRow row = (ParkingRow) rows.get(i);
            if (!row.isFull()) {
                return row;
            }
        }
        return null;
    }

    /**
     * Determines whether every row in the lot is full.
     * @return true if no row has any available spots
     */
    public boolean isFull() {
        return findFirstAvailableRow() == null;
    }

    /**
     * Returns the sum of available spots across every row in the lot.
     * @return total open spots in the lot
     */
    public int totalAvailableSpots() {
        int total = 0;
        for (int i = 1; i <= rows.size(); i++) {
            ParkingRow row = (ParkingRow) rows.get(i);
            total += row.availableSpots();
        }
        return total;
    }

    /**
     * Returns the sum of all spots (occupied or not) across every row in the lot.
     * @return total capacity of the lot
     */
    public int totalCapacity() {
        int total = 0;
        for (int i = 1; i <= rows.size(); i++) {
            ParkingRow row = (ParkingRow) rows.get(i);
            total += row.getCapacity();
        }
        return total;
    }

    /**
     * Parks one car in this lot by finding the first available row and
     * incrementing its occupied counter.
     * @return the ParkingRow the car was assigned to
     * @throws IllegalStateException if the lot has no open rows
     */
    public ParkingRow park() {
        ParkingRow row = findFirstAvailableRow();
        if (row == null) {
            throw new IllegalStateException(name + " is full");
        }
        row.park();
        return row;
    }

    /**
     * Parks one car in a user-specified row of this lot.
     * @param rowNumber the row the user reports parking in
     * @return the ParkingRow that was updated
     * @throws IllegalArgumentException if no row has that number
     * @throws IllegalStateException if the chosen row is already full
     */
    public ParkingRow parkInRow(int rowNumber) {
        ParkingRow row = getRow(rowNumber);
        if (row == null) {
            throw new IllegalArgumentException("No row " + rowNumber + " in " + name);
        }
        row.park();
        return row;
    }

    /**
     * Frees one spot in a specific row of this lot (used when a user leaves).
     * @param rowNumber the row the user is leaving from
     * @throws IllegalArgumentException if no row has that number
     */
    public void leave(int rowNumber) {
        ParkingRow row = getRow(rowNumber);
        if (row == null) {
            throw new IllegalArgumentException("No row " + rowNumber + " in " + name);
        }
        row.leave();
    }

    /**
     * Returns a multi-line summary of the lot showing its name and every row's status.
     * @return formatted display string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(totalAvailableSpots())
                .append("/").append(totalCapacity()).append(" spots open)\n");
        for (int i = 1; i <= rows.size(); i++) {
            ParkingRow row = (ParkingRow) rows.get(i);
            sb.append("  ").append(row.toString()).append("\n");
        }
        return sb.toString();
    }
}
