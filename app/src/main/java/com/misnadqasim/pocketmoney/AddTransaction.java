package com.misnadqasim.pocketmoney;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.misnadqasim.pocketmoney.database.dbHelper;

public class AddTransaction extends AppCompatActivity {

    Button done;
    dbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        db = new dbHelper(this);

        done = findViewById(R.id.done);
        done.setOnClickListener(v -> {
            db.doTransaction("asdf", 123, 123, "tag");
            finish();
        });
    }
}