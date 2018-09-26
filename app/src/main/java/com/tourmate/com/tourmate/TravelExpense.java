package com.tourmate.com.tourmate;

public class TravelExpense {
    private String expenseID, expenseDetails, expenseDateAndTime;
    private double expenseAmount;
    private String eventID;

    public TravelExpense(String expenseID, String expenseDetails, String expenseDateAndTime, double expenseAmount, String eventID) {
        this.expenseID = expenseID;
        this.expenseDetails = expenseDetails;
        this.expenseDateAndTime = expenseDateAndTime;
        this.expenseAmount = expenseAmount;
        this.eventID = eventID;
    }

    public TravelExpense() {
    }

    public String getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(String expenseID) {
        this.expenseID = expenseID;
    }

    public String getExpenseDetails() {
        return expenseDetails;
    }

    public void setExpenseDetails(String expenseDetails) {
        this.expenseDetails = expenseDetails;
    }

    public String getExpenseDateAndTime() {
        return expenseDateAndTime;
    }

    public void setExpenseDateAndTime(String expenseDateAndTime) {
        this.expenseDateAndTime = expenseDateAndTime;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}
