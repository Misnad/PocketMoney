package com.misnadqasim.pocketmoney.transaction;

public class TransactionDetails {

    public int id;
    public String label;
    public long time;
    public int amount;
    public String tag;

    public TransactionDetails(int id, String label, long time, int amount, String tag) {
        this.id = id;
        this.label = label;
        this.time = time;
        this.amount = amount;
        this.tag = tag;
    }
}
