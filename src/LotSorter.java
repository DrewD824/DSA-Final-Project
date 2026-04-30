/**
 * Hand-rolled sorter that ranks LotCandidate objects from best to worst
 * by their combined score (walk time + availability penalty). Lower
 * scores rank first.
 *
 * Algorithm: selection sort.
 *   For each position i from 0 to n-1:
 *     1. Scan the unsorted portion (i..n-1) for the candidate with the
 *        smallest score.
 *     2. Swap that candidate into position i.
 * After n-1 passes the array is fully sorted in ascending score order.
 *
 * This sort is implemented from scratch to satisfy the project's
 * "self-implemented sorting" requirement — no java.util sorting calls.
 *
 * @author Drew Dillman
 * @version v1.0
 */
public class LotSorter {

    /**
     * Sorts the given array of candidates in place using selection sort.
     * After this call, candidates[0] holds the lowest-score (best) lot
     * and candidates[n-1] holds the highest-score (worst) lot.
     * @param candidates the candidates to sort (modified in place)
     * @throws IllegalArgumentException if candidates is null
     */
    public static void sortByScore(LotCandidate[] candidates) {
        if (candidates == null) {
            throw new IllegalArgumentException("candidates must not be null");
        }

        int n = candidates.length;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            // Find the smallest-score candidate in the unsorted portion.
            for (int j = i + 1; j < n; j++) {
                if (candidates[j].getScore() < candidates[minIndex].getScore()) {
                    minIndex = j;
                }
            }

            // Swap the found minimum into position i (skip if already in place).
            if (minIndex != i) {
                LotCandidate temp = candidates[i];
                candidates[i] = candidates[minIndex];
                candidates[minIndex] = temp;
            }
        }
    }

    /**
     * Convenience overload that accepts the candidates as a LinkedList,
     * sorts them, and returns a new sorted array. The original list is
     * left unchanged.
     * @param candidates the LinkedList of LotCandidate objects to sort
     * @return a new LotCandidate[] sorted in ascending score order
     * @throws IllegalArgumentException if candidates is null or contains
     *         non-LotCandidate elements
     */
    public static LotCandidate[] sortByScore(LinkedList candidates) {
        if (candidates == null) {
            throw new IllegalArgumentException("candidates must not be null");
        }

        LotCandidate[] arr = new LotCandidate[candidates.size()];
        for (int i = 1; i <= candidates.size(); i++) {
            Object item = candidates.get(i);
            if (!(item instanceof LotCandidate)) {
                throw new IllegalArgumentException(
                        "candidates must contain only LotCandidate objects");
            }
            arr[i - 1] = (LotCandidate) item;
        }

        sortByScore(arr);
        return arr;
    }
}
