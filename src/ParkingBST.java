/**
 * Binary search tree of parking lots, keyed by the walking time (in minutes)
 * from each lot to the user's chosen destination building. Used as the
 * first stage of the "find parking" pipeline: the BST gives us an ordered,
 * optionally walk-time-bounded candidate list, which the LotSorter then
 * re-ranks by combined score.
 *
 * Two recursive read operations are exposed:
 *   - inOrderCandidates()    : full in-order traversal, returns every lot
 *                              as a LotCandidate ordered by ascending walk time.
 *   - candidatesWithin(int)  : range search that uses the BST property to
 *                              prune the right subtree once a node exceeds
 *                              the walk-time limit.
 *
 * @author Drew Dillman
 * @version v1.1
 */
public class ParkingBST {

    private ParkingBSTNode root;

    /**
     * Inserts a lot into the tree using its walking time as the key.
     * Equal walk times go right (stable for repeated walk-time values).
     * @param lot      the lot to insert
     * @param walkTime walking time in minutes from this lot to the destination
     */
    public void insert(Lot lot, int walkTime) {
        root = insertRecursive(root, lot, walkTime);
    }

    /**
     * Recursive helper for insert(). Walks down the tree comparing walkTime
     * to each node's walkTime, returning the (possibly newly created) subtree.
     * @param current  current subtree root (may be null)
     * @param lot      the lot to insert
     * @param walkTime walking time key
     * @return the (possibly updated) subtree root
     */
    private ParkingBSTNode insertRecursive(ParkingBSTNode current, Lot lot, int walkTime) {
        if (current == null) {
            return new ParkingBSTNode(lot, walkTime);
        }
        if (walkTime < current.walkTime) {
            current.left = insertRecursive(current.left, lot, walkTime);
        } else {
            current.right = insertRecursive(current.right, lot, walkTime);
        }
        return current;
    }

    /**
     * Returns every lot in the tree as a LotCandidate, ordered by ascending
     * walk time. Driven by a recursive in-order traversal:
     *   visit left subtree → current node → visit right subtree
     * The BST property guarantees this yields candidates in sorted walk-time
     * order — this is effectively a tree sort by walk time.
     * @return a LinkedList of LotCandidate in walk-time order
     */
    public LinkedList inOrderCandidates() {
        LinkedList result = new LinkedList();
        inOrderCollect(root, result);
        return result;
    }

    /**
     * Recursive helper for inOrderCandidates(). Performs the standard
     * left-current-right in-order walk and appends each visited lot to the
     * accumulator list.
     * @param node current subtree root (may be null)
     * @param list accumulator that collects candidates in order
     */
    private void inOrderCollect(ParkingBSTNode node, LinkedList list) {
        if (node == null) {
            return;
        }
        inOrderCollect(node.left, list);
        list.add(new LotCandidate(node.lot, node.walkTime));
        inOrderCollect(node.right, list);
    }

    /**
     * Returns every lot whose walking time is at or below the given limit,
     * as LotCandidate objects in ascending walk-time order. Uses a recursive
     * range search that prunes intelligently:
     *   - If the current node's walk time exceeds the limit, we know its
     *     right subtree is even farther, so we only recurse left.
     *   - Otherwise we recurse left, take the current node, and recurse right.
     * This is the project's non-trivial recursion: it leverages the BST
     * invariant to skip whole subtrees rather than visiting every node.
     * @param limit maximum walk time in minutes (inclusive)
     * @return a LinkedList of LotCandidate within the limit, in walk-time order
     */
    public LinkedList candidatesWithin(int limit) {
        LinkedList result = new LinkedList();
        rangeCollect(root, limit, result);
        return result;
    }

    /**
     * Recursive helper for candidatesWithin(). Skips the right subtree of
     * any node whose walkTime already exceeds the limit.
     * @param node  current subtree root (may be null)
     * @param limit maximum walk time
     * @param list  accumulator that collects in-range candidates in order
     */
    private void rangeCollect(ParkingBSTNode node, int limit, LinkedList list) {
        if (node == null) {
            return;
        }
        if (node.walkTime > limit) {
            // Current node is already too far → right subtree is even farther.
            rangeCollect(node.left, limit, list);
        } else {
            rangeCollect(node.left, limit, list);
            list.add(new LotCandidate(node.lot, node.walkTime));
            rangeCollect(node.right, limit, list);
        }
    }
}
