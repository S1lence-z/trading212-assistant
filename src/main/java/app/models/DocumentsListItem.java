package app.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Represents an item in a list of documents, with details including report ID,
 * date range, data inclusion options, status, and a download link.
 */
public class DocumentsListItem {

    /** Unique identifier for the document report. */
    private int reportId;

    /** Start date and time for the document's data range. */
    private String timeFrom;

    /** End date and time for the document's data range. */
    private String timeTo;

    /** Data inclusion options for this document. */
    private DataIncluded dataIncluded;

    /** Current status of the document. */
    private Status status;

    /** Download link for the document. */
    private String downloadLink;

    /**
     * Constructs a DocumentsListItem with specified attributes.
     *
     * @param id The report ID.
     * @param timeFrom The start date and time of the document's data range.
     * @param timeTo The end date and time of the document's data range.
     * @param dataIncluded The data inclusion options.
     * @param status The document status.
     * @param downloadLink The download link for the document.
     */
    public DocumentsListItem(int id, String timeFrom, String timeTo, DataIncluded dataIncluded, Status status, String downloadLink) {
        this.reportId = id;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.dataIncluded = dataIncluded;
        this.status = status;
        this.downloadLink = downloadLink;
    }

    /**
     * Constructs a DocumentsListItem from a JSON element.
     * Extracts details including report ID, date range, data inclusion options,
     * status, and download link.
     *
     * @param document The JSON element containing document details.
     */
    public DocumentsListItem(JsonElement document) {
        JsonObject documentObject = document.getAsJsonObject();
        this.reportId = documentObject.get("reportId").getAsInt();
        this.timeFrom = formatDateString(documentObject.get("timeFrom").getAsString());
        this.timeTo = formatDateString(documentObject.get("timeTo").getAsString());
        this.dataIncluded = DataIncluded.fromJsonObject(documentObject.get("dataIncluded").getAsJsonObject());
        this.status = Status.fromString(documentObject.get("status").getAsString());
        this.downloadLink = documentObject.get("downloadLink").getAsString();
    }

    /**
     * Formats a date string to a more readable format.
     *
     * @param date The date string to format.
     * @return A formatted date string in the pattern "dd. MM. yyyy HH:mm".
     */
    private String formatDateString(String date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy HH:mm");
        return zonedDateTime.format(formatter);
    }

    /**
     * Gets the report ID.
     *
     * @return The report ID.
     */
    public int getReportId() {
        return reportId;
    }

    /**
     * Gets the start date and time of the document's data range.
     *
     * @return The start date and time.
     */
    public String getTimeFrom() {
        return timeFrom;
    }

    /**
     * Gets the end date and time of the document's data range.
     *
     * @return The end date and time.
     */
    public String getTimeTo() {
        return timeTo;
    }

    /**
     * Gets the data inclusion options for this document.
     *
     * @return The data inclusion options.
     */
    public DataIncluded getDataIncluded() {
        return dataIncluded;
    }

    /**
     * Gets the current status of the document.
     *
     * @return The document's status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets the download link for the document.
     *
     * @return The download link.
     */
    public String getDownloadLink() {
        return downloadLink;
    }
}
