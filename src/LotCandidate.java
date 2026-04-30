/**
 * A lot paired with the walking time (in minutes) from that lot to the
 * user's chosen destination building. Used as the unit of input to the
 * LotSorter — each candidate carries enough info to compute its own
 * "combined score" (walk time + availability penalty).
 *
 * Score formula:
 *   score = walkTime + AVAILABILITY_WEIGHT * (1 - availabilityRatio)
 *
 * where availabilityRatio = totalAvailableSpots / totalCapacity (0.0 to 1.0).
 * Lower scores are better. A perfectly empty lot scores exactly its walk
 * time; a totally full lot scores walkTime + AVAILABILITY_WEIGHT.
 *
 * @author Drew Dillman
 * @version v1.0
 */
public class LotCandidate {

    /**
     * Maximum extra "minutes" added to a lot's score for being completely full.
     * Tweak this to change how heavily availability is weighted versus walk time.
     */
    public static final double AVAILABILITY_WEIGHT = 5.0;

    private Lot lot;
    private int walkTime;

    /**
     * Constructs a candidate pairing a lot with its walking time to the
     * destination.
     * @param lot      the parking lot
     * @param walkTime walking time in minutes (must be at least 1)
     */
    public LotCandidate(Lot lot, int walkTime) {
        if (lot == null) {
            throw new IllegalArgumentException("lot must not be null");
        }
        if (walkTime < 1) {
            throw new IllegalArgumentException("walkTime must be at least 1");
        }
        this.lot = lot;
        this.walkTime = walkTime;
    }

    /**
     * Returns the lot for this candidate.
     * @return lot
     */
    public Lot getLot() {
        return lot;
    }

    /**
     * Returns the walking time (in minutes) to the destination.
     * @return walkTime
     */
    public int getWalkTime() {
        return walkTime;
    }

    /**
     * Computes the combined score for this candidate. Lower is better.
     * Walk time is the base cost; the availability penalty grows from 0
     * (lot is empty) up to AVAILABILITY_WEIGHT (lot is completely full).
     * @return combined score
     */
    public double getScore() {
        int capacity = lot.totalCapacity();
        if (capacity == 0) {
            return walkTime + AVAILABILITY_WEIGHT;
        }
        double availabilityRatio = (double) lot.totalAvailableSpots() / capacity;
        return walkTime + AVAILABILITY_WEIGHT * (1.0 - availabilityRatio);
    }

    /**
     * Returns a human-readable summary of this candidate including its
     * walk time, available spots, and computed score.
     * @return formatted string
     */
    public String toString() {
        return String.format(
                "%s | %d min walk | %d/%d open | score %.2f",
                lot.getName(),
                walkTime,
                lot.totalAvailableSpots(),
                lot.totalCapacity(),
                getScore()
        );
    }
}
