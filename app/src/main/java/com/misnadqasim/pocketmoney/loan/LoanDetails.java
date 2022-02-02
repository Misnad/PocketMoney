package com.misnadqasim.pocketmoney.loan;

public class LoanDetails {

    public int id;
    public String toFrom;
    public int amount;
    public String datetime;
    public String notes;

    public LoanDetails(int id, String toFrom, int amount, String datetime, String notes) {
        this.id = id;
        this.toFrom = toFrom;
        this.datetime = datetime;
        this.amount = amount;
        this.notes = notes;
    }
}
