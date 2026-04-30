/**
 * One node in the ParkingBST. Holds a reference to a Lot and the walking time
 * (in minutes) from that lot to the user's chosen destination building.
 * Walk time is the BST key — smaller walk times go left, larger go right.
 *
 * Fields are package-private so ParkingBST can access them directly without
 * needing getters/setters, matching the style of the linked-list Node class.
 */
public class ParkingBSTNode {

    Lot lot;
    int walkTime;
    ParkingBSTNode left;
    ParkingBSTNode right;

    /**
     * Constructs a new BST node for the given lot and walk time. Left and
     * right children are initialized to null.
     * @param lot      the parking lot stored in this node
     * @param walkTime walking time (in minutes) from this lot to the destination
     */
    public ParkingBSTNode(Lot lot, int walkTime) {
        this.lot = lot;
        this.walkTime = walkTime;
        this.left = null;
        this.right = null;
    }
}

