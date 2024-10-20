package app.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class DocumentsListItem {
    private int reportId;
    private String timeFrom;
    private String timeTo;
    private DataIncluded dataIncluded;
    private Status status;
    private String downloadLink;

    public DocumentsListItem(int id, String timeFrom, String timeTo, DataIncluded dataIncluded, Status status, String downloadLink) {
        this.reportId = id;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.dataIncluded = dataIncluded;
        this.status = status;
        this.downloadLink = downloadLink;
    }

    public DocumentsListItem(JsonElement document) {
        JsonObject documentObject = document.getAsJsonObject();
        this.reportId = documentObject.get("reportId").getAsInt();
        this.timeFrom = formatDateString(documentObject.get("timeFrom").getAsString());
        this.timeTo = formatDateString(documentObject.get("timeTo").getAsString());
        this.dataIncluded = DataIncluded.fromJsonObject(documentObject.get("dataIncluded").getAsJsonObject());
        this.status = Status.fromString(documentObject.get("status").getAsString());
        this.downloadLink = documentObject.get("downloadLink").getAsString();
    }

    private String formatDateString(String date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy HH:mm");
        return zonedDateTime.format(formatter);
    }

    public int getReportId() {
        return reportId;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public DataIncluded getDataIncluded() {
        return dataIncluded;
    }

    public Status getStatus() {
        return status;
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}
