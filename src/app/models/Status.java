package app.models;

public enum Status {
    QUEUED("Queued"),
    PROCESSING("Processing"),
    RUNNING("Running"),
    CANCELED("Canceled"),
    FAILED("Failed"),
    FINISHED("Finished");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.name().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Status.class.getCanonicalName() + "." + status);
    }
}
