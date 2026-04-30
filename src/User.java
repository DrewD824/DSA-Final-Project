/**
 * Represents a user of the Rowan Parking Navigator.
 * Currently stores only identifying info (name + user id). Used by
 * NotificationQueue to track who is waiting for a spot to open up.
 *
 * @author Drew Dillman
 * @version v1.1
 */
public class User {

    private String name;
    private String userId;

    /**
     * Constructs a user with a display name and a user id.
     * @param name   the user's display name (e.g. "Drew")
     * @param userId a unique id for the user (e.g. their Rowan banner id)
     */
    public User(String name, String userId) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name must not be null or empty");
        }
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId must not be null or empty");
        }
        this.name = name;
        this.userId = userId;
    }

    /**
     * Returns the user's display name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the user's unique id.
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Returns a human-readable summary of the user.
     * @return formatted string, e.g. "Drew (id: 916123456)"
     */
    public String toString() {
        return name + " (id: " + userId + ")";
    }
}
