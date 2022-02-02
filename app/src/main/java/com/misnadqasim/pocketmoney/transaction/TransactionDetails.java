package com.misnadqasim.pocketmoney.transaction;

public class TransactionDetails {

    public int id;
    public String label;
    public int amount;
    public String datetime;
    public String tag;
    public String notes;

    public TransactionDetails(int id, String label, int amount, String datetime, String tag, String notes) {
        this.id = id;
        this.label = label;
        this.datetime = datetime;
        this.amount = amount;
        this.tag = tag;
        this.notes = notes;
    }

}
