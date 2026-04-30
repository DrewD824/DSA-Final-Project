import java.util.Scanner;

/**
 * Entry point for the Rowan Parking Navigator demo.
 *
 * On launch the user picks one of three preseeded scenarios (slow morning,
 * mid-morning, lecture rush) and is then dropped into a streamlined menu
 * that exercises every data structure in the project:
 *
 *   - Linked list  : Lot's row list, traversed by Lot.findFirstAvailableRow()
 *                    and again (twice) when inspecting a lot's rows.
 *   - Tree         : ParkingBST, used inside the "find parking" pipeline to
 *                    produce candidates ordered by walk time before the sort
 *                    re-ranks them.
 *   - Recursion    : ParkingBST.inOrderCandidates() and candidatesWithin()
 *                    are both recursive (in-order traversal + range search
 *                    with subtree pruning).
 *   - Sort         : LotSorter selection sort over the BST's candidate list.
 *   - Stack        : ActionStack — park / leave / undo / "where did I park?"
 *   - Queue        : NotificationQueue — exercises enqueue (join waitlist),
 *                    isEmpty + peek + dequeue (advance time auto-parks the
 *                    front user when a spot frees), and the printQueue view.
 *                    The lecture-rush scenario pre-seeds three commuters into
 *                    the queue so FIFO order is visible from the start.
 *
 * Seed actions use ParkingRow.preseed() rather than park(), so they bypass
 * the action stack and the user's undo only ever reverses their own actions.
 * Auto-parks driven by "advance time" likewise bypass the action stack.
 *
 * @author Drew Dillman
 * @version v1.2
 */
public class Main {

    private static LotRepository      repo;
    private static WalkTimeTable      walkTimes;
    private static ActionStack        actions;
    private static NotificationQueue  waitlist;
    private static User               currentUser;
    private static Scanner            in;

    /**
     * Boots the demo: builds the world, picks a scenario, runs the menu loop.
     * @param args ignored
     */
    public static void main(String[] args) {
        in          = new Scanner(System.in);
        currentUser = new User("Drew", "916123456");

        printBanner();
        resetWorld();
        chooseScenario();
        runMenu();

        System.out.println("Goodbye!");
    }

    // ── boot helpers ──────────────────────────────────────────────────────────

    /**
     * Prints the welcome banner and project title.
     */
    private static void printBanner() {
        System.out.println("=================================================");
        System.out.println("        Rowan Parking Navigator — DSA Demo       ");
        System.out.println("=================================================");
    }

    /**
     * Rebuilds the lot repository, walk-time table, action stack, and
     * notification queue. Clears any prior session state.
     */
    private static void resetWorld() {
        repo      = new LotRepository();
        walkTimes = new WalkTimeTable(repo);
        actions   = new ActionStack();
        waitlist  = new NotificationQueue(20);
    }

    /**
     * Prompts the user to pick one of three preseeded scenarios and seeds
     * the lots accordingly.
     */
    private static void chooseScenario() {
        System.out.println("\nPick a scenario:");
        System.out.println("  1. Slow morning   (lots wide open)");
        System.out.println("  2. Mid-morning    (popular lots filling up)");
        System.out.println("  3. Lecture rush   (closest lots overflowing — Lot A is full)");
        int choice = readIntInRange("> ", 1, 3);
        switch (choice) {
            case 1: seedSlow();   System.out.println("Scenario: SLOW MORNING.");   break;
            case 2: seedMedium(); System.out.println("Scenario: MID-MORNING.");    break;
            case 3: seedBusy();   System.out.println("Scenario: LECTURE RUSH.");   break;
        }
    }

    /**
     * Slow-morning seed: every lot lightly used, parking trivial everywhere.
     */
    private static void seedSlow() {
        preseedLot(repo.getLotA(), 0.10);
        preseedLot(repo.getLotO(), 0.15);
        preseedLot(repo.getLotD(), 0.20);
        preseedLot(repo.getLotY(), 0.05);
        preseedLot(repo.getLotC(), 0.10);
    }

    /**
     * Mid-morning seed: closer lots noticeably fuller than farther ones.
     */
    private static void seedMedium() {
        preseedLot(repo.getLotA(), 0.80);
        preseedLot(repo.getLotO(), 0.60);
        preseedLot(repo.getLotD(), 0.50);
        preseedLot(repo.getLotY(), 0.30);
        preseedLot(repo.getLotC(), 0.25);
    }

    /**
     * Lecture-rush seed: Lot A is completely full so the waitlist option
     * has something meaningful to demo. Three fake commuters are also
     * pre-loaded into the notification queue so FIFO ordering is visible
     * the first time the user inspects it.
     */
    private static void seedBusy() {
        preseedLot(repo.getLotA(), 1.00);
        preseedLot(repo.getLotO(), 0.90);
        preseedLot(repo.getLotD(), 0.70);
        preseedLot(repo.getLotY(), 0.40);
        preseedLot(repo.getLotC(), 0.50);
        preseedQueue();
    }

    /**
     * Pre-populates the notification queue with three fake commuters so the
     * busy-scenario demo can show the queue working from the first menu tick.
     * Catches QueueException defensively even though the queue is empty here.
     */
    private static void preseedQueue() {
        try {
            waitlist.enqueue(new User("Alex",  "S00000001"));
            waitlist.enqueue(new User("Riley", "S00000002"));
            waitlist.enqueue(new User("Sam",   "S00000003"));
        } catch (QueueException e) {
            System.out.println("Could not pre-seed queue: " + e.getMessage());
        }
    }

    /**
     * Preseeds every row in a lot to roughly the given fill ratio (0.0–1.0).
     * Each row is filled to round(capacity * ratio) spots, capped at capacity.
     * @param lot   the lot to seed
     * @param ratio the target fill ratio
     */
    private static void preseedLot(Lot lot, double ratio) {
        for (int rowNum = 1; rowNum <= lot.rowCount(); rowNum++) {
            ParkingRow row = lot.getRow(rowNum);
            int target = (int) Math.round(row.getCapacity() * ratio);
            if (target > row.getCapacity()) {
                target = row.getCapacity();
            }
            row.preseed(target);
        }
    }

    // ── main menu ─────────────────────────────────────────────────────────────

    /**
     * Runs the main menu loop until the user quits.
     */
    private static void runMenu() {
        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("  1. Find parking near a destination");
            System.out.println("  2. Park in a lot");
            System.out.println("  3. Leave my parked spot");
            System.out.println("  4. Undo last action");
            System.out.println("  5. Where did I park?");
            System.out.println("  6. Join the waitlist for a full lot");
            System.out.println("  7. Advance time (a spot frees, queue advances)");
            System.out.println("  8. Inspect a lot's rows (open vs full)");
            System.out.println("  9. Reload scenario");
            System.out.println("  0. Quit");

            int choice = readIntInRange("> ", 0, 9);
            switch (choice) {
                case 1: handleFindParking();    break;
                case 2: handlePark();           break;
                case 3: handleLeave();          break;
                case 4: handleUndo();           break;
                case 5: handleWhereDidIPark();  break;
                case 6: handleJoinQueue();      break;
                case 7: handleAdvanceTime();    break;
                case 8: handleInspectLot();     break;
                case 9: resetWorld();
                        chooseScenario();       break;
                case 0: return;
            }
        }
    }

    // ── Find parking (BST → sort) ─────────────────────────────────────────────

    /**
     * The headline feature: chains the BST and the sorter together.
     *   1. User picks a destination building.
     *   2. User optionally caps the walking time (0 to skip).
     *   3. Build a fresh BST keyed by walk time (recursive insert).
     *   4. Recursively traverse — full in-order if no cap, range search
     *      with subtree pruning if a cap is given. The traversal returns
     *      a LinkedList of LotCandidate already ordered by walk time.
     *   5. Hand that LinkedList to LotSorter, which uses selection sort
     *      to re-rank by combined score (walk time + availability).
     *   6. Print the ranking and highlight the top recommendation.
     */
    private static void handleFindParking() {
        String dest = promptForDestination();
        int limit = readIntInRange(
                "Maximum walking minutes (0 for no limit): ", 0, 60);

        ParkingBST bst = walkTimes.buildBST(dest);

        LinkedList candidates = (limit == 0)
                ? bst.inOrderCandidates()
                : bst.candidatesWithin(limit);

        if (candidates.size() == 0) {
            System.out.println("\nNo lots within " + limit + " minute walk of " + dest + ".");
            return;
        }

        LotCandidate[] sorted = LotSorter.sortByScore(candidates);

        String header = (limit == 0)
                ? "\nLots ranked by combined score for " + dest + ":"
                : "\nLots within " + limit + " min of " + dest + ", ranked by combined score:";
        System.out.println(header);
        for (int i = 0; i < sorted.length; i++) {
            String prefix = (i == 0) ? " * " : "   ";
            System.out.println(prefix + sorted[i].toString());
        }
        System.out.println("\nRecommendation: " + sorted[0].getLot().getName() + ".");
    }

    // ── Stack features (park / leave / undo / where) ──────────────────────────

    /**
     * Asks the user to pick a lot, parks them in the first available row,
     * and pushes a PARK action onto the stack so the action can be undone.
     */
    private static void handlePark() {
        Lot lot = promptForLot();
        if (lot.isFull()) {
            System.out.println(lot.getName() + " is FULL. Try option 6 to join the waitlist.");
            return;
        }
        try {
            ParkingRow row = lot.park();
            actions.push(new Action(Action.Type.PARK, lot, row.getRowNumber()));
            System.out.println("Parked in " + lot.getName() + ", Row " + row.getRowNumber() + ".");
        } catch (IllegalStateException e) {
            System.out.println("Could not park: " + e.getMessage());
        }
    }

    /**
     * Frees the exact row the user most recently parked in by inspecting
     * the top of the action stack. If the user has not parked yet, or if
     * their most recent action was already a LEAVE, no change is made.
     */
    private static void handleLeave() {
        Action top = actions.getTopAction();
        if (top == null) {
            System.out.println("You haven't parked yet — nothing to leave.");
            return;
        }
        if (top.getType() != Action.Type.PARK) {
            System.out.println("You've already left your last spot. Park first (option 2).");
            return;
        }
        Lot lot = top.getLot();
        int rowNum = top.getRowNumber();
        try {
            lot.leave(rowNum);
            actions.push(new Action(Action.Type.LEAVE, lot, rowNum));
            System.out.println("Left " + lot.getName() + ", Row " + rowNum + ".");
        } catch (IllegalStateException e) {
            System.out.println("Could not leave: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Could not leave: " + e.getMessage());
        }
    }

    /**
     * Pops and reverses the most recent action on the stack.
     */
    private static void handleUndo() {
        if (actions.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        try {
            actions.undo();
            System.out.println("Undid last action.");
        } catch (StackException e) {
            System.out.println("Could not undo: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Could not undo: " + e.getMessage());
        }
    }

    /**
     * Peeks at the action stack to report where the user most recently parked.
     */
    private static void handleWhereDidIPark() {
        System.out.println(actions.peekLastParked());
    }

    // ── Queue feature (join + auto-print) ─────────────────────────────────────

    /**
     * Adds the current user to the notification queue if their chosen lot
     * is full, then prints the current waitlist. If the lot has spots,
     * suggests parking instead and prints nothing extra.
     */
    private static void handleJoinQueue() {
        Lot lot = promptForLot();
        if (!lot.isFull()) {
            System.out.println(lot.getName() + " has " + lot.totalAvailableSpots()
                    + " open spots — no need to wait. Use option 2 to park.");
            return;
        }
        try {
            waitlist.enqueue(currentUser);
            System.out.println(currentUser.getName() + " added to the waitlist for "
                    + lot.getName() + ".");
        } catch (QueueException e) {
            System.out.println("Could not join queue: " + e.getMessage());
            return;
        }
        System.out.println();
        waitlist.printQueue();
    }

    // ── Advance time (queue dequeue + auto-park) ──────────────────────────────

    /**
     * Advances the simulation by one tick: a spot frees somewhere on campus
     * and, if anyone is waiting in the notification queue, the front user is
     * dequeued and auto-parked in that freed spot.
     *
     * Lot selection prefers a fully-full lot so the dequeue path triggers in
     * the most narratively interesting case (someone waiting got their spot).
     * Row selection inside the chosen lot prefers a fully-full row for the
     * same reason — a row that goes FULL → open is more visible than one
     * that just decrements its counter.
     *
     * Auto-parks driven by this method bypass the action stack: this is a
     * system-driven event, not something the user did, and shouldn't be
     * undoable from the user's history.
     */
    private static void handleAdvanceTime() {
        Lot lot = findLotToAdvance();
        if (lot == null) {
            System.out.println("\nNothing to advance — no cars are parked anywhere.");
            return;
        }
        ParkingRow row = findRowToFree(lot);
        if (row == null) {
            System.out.println("\nNothing to advance — " + lot.getName() + " has no occupied rows.");
            return;
        }
        int rowNum = row.getRowNumber();
        row.leave();

        System.out.println("\n[Time advances...]");
        System.out.println("A spot freed in " + lot.getName() + ", Row " + rowNum + ".");

        if (waitlist.isEmpty()) {
            System.out.println("Notification queue is empty — no one to notify.");
        } else {
            try {
                User notified = waitlist.dequeue();
                lot.parkInRow(rowNum);
                System.out.println("Notifying " + notified.getName()
                        + " — they have parked in " + lot.getName() + ", Row " + rowNum + ".");
            } catch (QueueException e) {
                System.out.println("Could not advance queue: " + e.getMessage());
            }
        }

        System.out.println();
        waitlist.printQueue();
    }

    /**
     * Picks the most relevant lot for "advance time" to free a spot in.
     * Prefers a fully-full lot (so the dequeue path can trigger an actual
     * full → open transition). Falls back to any lot that has at least one
     * parked car, and returns null only if every lot is empty.
     * @return the chosen Lot, or null if no lot has any parked cars
     */
    private static Lot findLotToAdvance() {
        Lot[] lots = repo.getAllLots();
        for (int i = 0; i < lots.length; i++) {
            if (lots[i].isFull()) {
                return lots[i];
            }
        }
        for (int i = 0; i < lots.length; i++) {
            if (lots[i].totalAvailableSpots() < lots[i].totalCapacity()) {
                return lots[i];
            }
        }
        return null;
    }

    /**
     * Picks a row inside the given lot to free a spot in. Prefers a fully
     * full row so the lot's display visibly transitions FULL → open.
     * Falls back to any row that has at least one car.
     * @param lot the lot to pick a row from
     * @return the chosen ParkingRow, or null if every row is empty
     */
    private static ParkingRow findRowToFree(Lot lot) {
        for (int i = 1; i <= lot.rowCount(); i++) {
            ParkingRow row = lot.getRow(i);
            if (row.isFull()) {
                return row;
            }
        }
        for (int i = 1; i <= lot.rowCount(); i++) {
            ParkingRow row = lot.getRow(i);
            if (!row.isEmpty()) {
                return row;
            }
        }
        return null;
    }

    // ── Inspect feature (linked-list double-pass) ─────────────────────────────

    /**
     * Asks the user to pick a lot and prints its row-by-row status, grouped
     * into OPEN ROWS and FULL ROWS for easy visual scanning. Each grouping
     * is produced by a separate linear pass over the lot's row linked list.
     */
    private static void handleInspectLot() {
        Lot lot = promptForLot();
        System.out.println("\n" + lot.getName() + " — "
                + lot.totalAvailableSpots() + "/" + lot.totalCapacity() + " spots open");

        // First pass: rows with space remaining.
        System.out.println("\n  OPEN ROWS:");
        boolean anyOpen = false;
        for (int i = 1; i <= lot.rowCount(); i++) {
            ParkingRow row = lot.getRow(i);
            if (!row.isFull()) {
                System.out.println("    " + row.toString());
                anyOpen = true;
            }
        }
        if (!anyOpen) {
            System.out.println("    (none — every row is full)");
        }

        // Second pass: rows that are completely full.
        System.out.println("\n  FULL ROWS:");
        boolean anyFull = false;
        for (int i = 1; i <= lot.rowCount(); i++) {
            ParkingRow row = lot.getRow(i);
            if (row.isFull()) {
                System.out.println("    " + row.toString());
                anyFull = true;
            }
        }
        if (!anyFull) {
            System.out.println("    (none — every row has space)");
        }
    }

    // ── prompts ───────────────────────────────────────────────────────────────

    /**
     * Asks the user to pick a destination building from the WalkTimeTable.
     * @return the chosen building name
     */
    private static String promptForDestination() {
        String[] dests = walkTimes.getDestinations();
        System.out.println("\nPick a destination:");
        for (int i = 0; i < dests.length; i++) {
            System.out.println("  " + (i + 1) + ". " + dests[i]);
        }
        int choice = readIntInRange("> ", 1, dests.length);
        return dests[choice - 1];
    }

    /**
     * Asks the user to pick one of the five lots.
     * @return the chosen Lot
     */
    private static Lot promptForLot() {
        Lot[] lots = repo.getAllLots();
        System.out.println("\nPick a lot:");
        for (int i = 0; i < lots.length; i++) {
            System.out.println("  " + (i + 1) + ". " + lots[i].getName()
                    + "  (" + lots[i].totalAvailableSpots() + "/"
                    + lots[i].totalCapacity() + " open)");
        }
        int choice = readIntInRange("> ", 1, lots.length);
        return lots[choice - 1];
    }

    // ── input helpers ─────────────────────────────────────────────────────────

    /**
     * Reads a whole-number input from the user, re-prompting on bad input.
     * @param prompt the prompt string to display
     * @return the parsed integer
     */
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    /**
     * Reads an integer that must fall within [lo, hi], re-prompting until valid.
     * @param prompt the prompt string to display
     * @param lo     minimum acceptable value (inclusive)
     * @param hi     maximum acceptable value (inclusive)
     * @return the validated integer
     */
    private static int readIntInRange(String prompt, int lo, int hi) {
        while (true) {
            int value = readInt(prompt);
            if (value >= lo && value <= hi) {
                return value;
            }
            System.out.println("Please enter a number between " + lo + " and " + hi + ".");
        }
    }
}
