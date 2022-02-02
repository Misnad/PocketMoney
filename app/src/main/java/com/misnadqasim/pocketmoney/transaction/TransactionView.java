package com.misnadqasim.pocketmoney.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.misnadqasim.pocketmoney.R;

public class TransactionView extends LinearLayout {

    public int id;
    public String label;
    public int amount;
    public long time;
    public String tag;

    private int width, height;

    public TransactionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.id = 123;
        this.label = "Hello";
        this.time = 123123;
        this.amount = 123;
        this.tag = "Hello";

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("UseCompatLoadingForDrawables")
    public TransactionView(Context context, TransactionDetails transactionDetails) {
        super(context);
        this.id = transactionDetails.id;
        this.label = transactionDetails.label;
//        this.time = transactionDetails.datetime;
        this.amount = transactionDetails.amount;
        this.tag = transactionDetails.tag;

        height = this.getHeight();
        width = this.getWidth();


        this.setPadding(20, 20, 20, 20);
        this.setBackground(getResources().getDrawable(R.drawable.transaction_bg));

//        if (this.amount > 0) {
//            Drawable bg = getResources().getDrawable(R.drawable.transaction_bg);
//            bg.setTint(Color.parseColor("#00ff00"));
//            this.setBackground(bg);
//        } else {
//            Drawable bg = getResources().getDrawable(R.drawable.transaction_bg);
//            bg.setTint(Color.parseColor("#ff0000"));
//            this.setBackground(bg);
//        }

        int color;
        if (this.amount > 0) {
            color = Color.parseColor("#99ff99");
        } else {
            color = Color.parseColor("#ff9999");
        }


        TextView mLabel = new TextView(context);
        mLabel.setText(this.label);
        mLabel.setTextColor(color);
        mLabel.setTypeface(getResources().getFont(R.font.roboto_mono_medium));
        this.addView(mLabel);

        TextView mAmount = new TextView(context);
        mAmount.setText(String.valueOf(Math.abs(this.amount)));
        mAmount.setTextColor(color);
        mLabel.setTypeface(getResources().getFont(R.font.roboto_mono_medium));
        mAmount.setLayoutDirection(LAYOUT_DIRECTION_RTL);
        this.addView(mAmount);
    }

    @Override
    public int getId() {
        return id;
    }
}
