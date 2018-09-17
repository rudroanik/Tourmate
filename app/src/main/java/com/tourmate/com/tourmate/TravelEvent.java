package com.tourmate.com.tourmate;

public class TravelEvent {
    private String eventID, destination, estimatedBudget, fromDate, toDate;

    public TravelEvent(String eventID, String destination, String estimatedBudget, String fromDate, String toDate) {
        this.eventID = eventID;
        this.destination = destination;
        this.estimatedBudget = estimatedBudget;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public TravelEvent() {
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(String estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
