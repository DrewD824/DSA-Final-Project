/**
 * Builds and holds the five Rowan University parking lots used by the app.
 * Row and spot counts are sourced from an on-the-ground survey of the actual lots.
 *
 * Lots in scope:
 *   Lot A  — 10 rows, 173 spots
 *   Lot O  — 23 rows, ~568 spots (O-1 and O-2 treated as one logical lot)
 *   Lot D  —  6 rows, 207 spots
 *   Lot Y  —  4 rows,  52 spots
 *   Lot C  —  4 rows, 203 spots
 */
public class LotRepository {

    private Lot lotA;
    private Lot lotO;
    private Lot lotD;
    private Lot lotY;
    private Lot lotC;

    /**
     * Constructs the repository and seeds all five lots with their real
     * row numbers and per-row spot capacities.
     */
    public LotRepository() {
        lotA = buildLotA();
        lotO = buildLotO();
        lotD = buildLotD();
        lotY = buildLotY();
        lotC = buildLotC();
    }

    // ── lot builders ──────────────────────────────────────────────────────────

    private Lot buildLotA() {
        Lot lot = new Lot("Lot A");
        int[] caps = { 21, 6, 8, 17, 19, 19, 23, 23, 20, 17 };
        for (int i = 0; i < caps.length; i++) {
            lot.addRow(new ParkingRow(i + 1, caps[i]));
        }
        return lot;
    }

    private Lot buildLotO() {
        Lot lot = new Lot("Lot O");
        int[] caps = { 5, 25, 29, 29, 29, 29, 29, 29, 29, 29,
                       24, 6, 22, 29, 29, 31, 31, 32, 33, 34,
                       7, 10, 29 };
        for (int i = 0; i < caps.length; i++) {
            lot.addRow(new ParkingRow(i + 1, caps[i]));
        }
        return lot;
    }

    private Lot buildLotD() {
        Lot lot = new Lot("Lot D");
        int[] caps = { 8, 58, 47, 47, 36, 11 };
        for (int i = 0; i < caps.length; i++) {
            lot.addRow(new ParkingRow(i + 1, caps[i]));
        }
        return lot;
    }

    private Lot buildLotY() {
        Lot lot = new Lot("Lot Y");
        int[] caps = { 16, 13, 18, 5 };
        for (int i = 0; i < caps.length; i++) {
            lot.addRow(new ParkingRow(i + 1, caps[i]));
        }
        return lot;
    }

    private Lot buildLotC() {
        Lot lot = new Lot("Lot C");
        int[] caps = { 57, 53, 53, 40 };
        for (int i = 0; i < caps.length; i++) {
            lot.addRow(new ParkingRow(i + 1, caps[i]));
        }
        return lot;
    }

    // ── accessors ─────────────────────────────────────────────────────────────

    /**
     * Returns Lot A (10 rows, 173 spots).
     * @return Lot A
     */
    public Lot getLotA() { return lotA; }

    /**
     * Returns Lot O (23 rows, ~568 spots).
     * @return Lot O
     */
    public Lot getLotO() { return lotO; }

    /**
     * Returns Lot D (6 rows, 207 spots).
     * @return Lot D
     */
    public Lot getLotD() { return lotD; }

    /**
     * Returns Lot Y (4 rows, 52 spots).
     * @return Lot Y
     */
    public Lot getLotY() { return lotY; }

    /**
     * Returns Lot C (4 rows, 203 spots).
     * @return Lot C
     */
    public Lot getLotC() { return lotC; }

    /**
     * Returns all five lots as an array, in the order A, O, D, Y, C.
     * Useful for iterating over every lot (e.g. displaying overall availability).
     * @return array of all lots
     */
    public Lot[] getAllLots() {
        return new Lot[]{ lotA, lotO, lotD, lotY, lotC };
    }
}
