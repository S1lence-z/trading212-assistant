package app.models;

/**
 * Enum representing the status of a document processing operation.
 */
public enum Status {
    /** Document is in queue and waiting to be processed. */
    QUEUED("Queued"),

    /** Document is currently being processed. */
    PROCESSING("Processing"),

    /** Document processing is running. */
    RUNNING("Running"),

    /** Document processing has been canceled. */
    CANCELED("Canceled"),

    /** Document processing has failed. */
    FAILED("Failed"),

    /** Document processing is completed successfully. */
    FINISHED("Finished");

    /** Display name for the status. */
    private final String displayName;

    /**
     * Constructs a Status enum with the specified display name.
     *
     * @param displayName The display name of the status.
     */
    Status(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns the display name of the status.
     *
     * @return The display name.
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Parses a string to find a matching Status enum, ignoring case.
     *
     * @param status The status string to parse.
     * @return The corresponding Status enum.
     * @throws IllegalArgumentException If the status string does not match any Status.
     */
    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Status.class.getCanonicalName() + "." + status);
    }
}
