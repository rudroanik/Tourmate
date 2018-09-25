package com.tourmate.com.tourmate;

public class TravelMoment {
    private String momentID, momentDetails, imageUrl, eventID;

    public TravelMoment(String momentID, String momentDetails, String imageUrl, String eventID) {
        if (momentDetails.trim().equals("")){
            momentDetails = "No Details";
        }
        this.momentID = momentID;
        this.momentDetails = momentDetails;
        this.imageUrl = imageUrl;
        this.eventID = eventID;
    }

    public TravelMoment() {
    }

    public String getMomentID() {
        return momentID;
    }

    public void setMomentID(String momentID) {
        this.momentID = momentID;
    }

    public String getMomentDetails() {
        return momentDetails;
    }

    public void setMomentDetails(String momentDetails) {
        this.momentDetails = momentDetails;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
