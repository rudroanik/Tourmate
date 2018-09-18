package com.tourmate.com.tourmate;

public class TravelEvent {
    private String eventID, destination, fromDate, toDate;
    private double estimatedBudget;

    public TravelEvent(String eventID, String destination, double estimatedBudget, String fromDate, String toDate) {
        this.eventID = eventID;
        this.destination = destination;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.estimatedBudget = estimatedBudget;
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

    public double getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(double estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }
}
