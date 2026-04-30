/**
 * Stores estimated walking times (in minutes) from each parking lot to each
 * supported destination building on Rowan's campus. Numbers are based on
 * Drew's on-the-ground survey, not Google Maps — they reflect the actual
 * pedestrian routes a commuter would take.
 *
 * Data layout:
 *   times[buildingIndex][lotIndex] = walk time in minutes
 * where buildingIndex matches the order of DESTINATIONS and lotIndex matches
 * the order returned by LotRepository.getAllLots() (A, O, D, Y, C).
 *
 * Supported destinations:
 *   - Business Hall
 *   - Engineering Hall
 *   - Bunce Hall
 *   - Robinson Hall
 *   - Wilson Hall
 *
 * @author Drew Dillman
 * @version v1.0
 */
public class WalkTimeTable {

    /** Canonical list of destination buildings, in the order used by the times grid. */
    private static final String[] DESTINATIONS = {
            "Business Hall",
            "Engineering Hall",
            "Bunce Hall",
            "Robinson Hall",
            "Wilson Hall"
    };

    /**
     * Walk-time grid. Rows are destinations (in DESTINATIONS order), columns
     * are lots (in LotRepository.getAllLots() order: A, O, D, Y, C).
     */
    private static final int[][] TIMES = {
            //  A   O   D   Y   C
            {   1,  6,  5,  8, 10 }, // Business Hall
            {   2,  4,  3,  9,  8 }, // Engineering Hall
            {   3,  5,  7,  6, 12 }, // Bunce Hall
            {   3,  4,  5,  7,  9 }, // Robinson Hall
            {   2,  3,  4,  8,  8 }  // Wilson Hall
    };

    private final LotRepository repo;

    /**
     * Constructs the walk-time table backed by the given lot repository.
     * The repository's lot order (A, O, D, Y, C) must match the columns
     * of the TIMES grid.
     * @param repo the lot repository providing the lots for lookups
     */
    public WalkTimeTable(LotRepository repo) {
        if (repo == null) {
            throw new IllegalArgumentException("repo must not be null");
        }
        this.repo = repo;
    }

    /**
     * Returns the list of destination buildings supported by this table.
     * The returned array is a defensive copy; callers may freely modify it.
     * @return supported destination names
     */
    public String[] getDestinations() {
        String[] copy = new String[DESTINATIONS.length];
        for (int i = 0; i < DESTINATIONS.length; i++) {
            copy[i] = DESTINATIONS[i];
        }
        return copy;
    }

    /**
     * Determines whether the given building name is a supported destination.
     * Comparison is case-insensitive.
     * @param building the building name to check
     * @return true if the building is in DESTINATIONS
     */
    public boolean hasDestination(String building) {
        return findDestinationIndex(building) != -1;
    }

    /**
     * Returns the walking time (in minutes) from the given lot to the given
     * destination building.
     * @param building the destination building name (case-insensitive)
     * @param lot      the parking lot
     * @return walk time in minutes
     * @throws IllegalArgumentException if the building or lot is not recognized
     */
    public int getWalkTime(String building, Lot lot) {
        if (lot == null) {
            throw new IllegalArgumentException("lot must not be null");
        }
        int buildingIndex = findDestinationIndex(building);
        if (buildingIndex == -1) {
            throw new IllegalArgumentException("Unknown destination: " + building);
        }
        int lotIndex = findLotIndex(lot);
        if (lotIndex == -1) {
            throw new IllegalArgumentException("Unknown lot: " + lot.getName());
        }
        return TIMES[buildingIndex][lotIndex];
    }

    /**
     * Builds a fresh ParkingBST keyed by walk time, where every lot in the
     * repository is inserted with its walking time to the given destination.
     * Useful as a one-shot setup for a destination-based recommendation.
     * @param building the destination building name (case-insensitive)
     * @return a populated ParkingBST
     * @throws IllegalArgumentException if the building is not recognized
     */
    public ParkingBST buildBST(String building) {
        if (!hasDestination(building)) {
            throw new IllegalArgumentException("Unknown destination: " + building);
        }
        ParkingBST bst = new ParkingBST();
        Lot[] lots = repo.getAllLots();
        for (int i = 0; i < lots.length; i++) {
            int walkTime = getWalkTime(building, lots[i]);
            bst.insert(lots[i], walkTime);
        }
        return bst;
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    /**
     * Looks up a destination's index in DESTINATIONS by name (case-insensitive).
     * @param building the building name to look up
     * @return the index, or -1 if not found
     */
    private int findDestinationIndex(String building) {
        if (building == null) {
            return -1;
        }
        for (int i = 0; i < DESTINATIONS.length; i++) {
            if (DESTINATIONS[i].equalsIgnoreCase(building)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Looks up a lot's column index by comparing its name to the lots in
     * the repository's canonical ordering (A, O, D, Y, C).
     * @param lot the lot to look up
     * @return the index, or -1 if not found
     */
    private int findLotIndex(Lot lot) {
        Lot[] lots = repo.getAllLots();
        for (int i = 0; i < lots.length; i++) {
            if (lots[i].getName().equals(lot.getName())) {
                return i;
            }
        }
        return -1;
    }
}
