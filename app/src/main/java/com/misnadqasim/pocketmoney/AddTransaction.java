package com.misnadqasim.pocketmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.android.material.snackbar.Snackbar;
import com.misnadqasim.pocketmoney.database.dbHelper;

import java.util.Calendar;

public class AddTransaction extends AppCompatActivity {

    Button done;
    dbHelper db;

    EditText label, amount, dateView, timeView, note;
    RadioButton transaction, loan;

    Vibrator vibrator;

    boolean isKeyboardShowing = false;
    private boolean isLoan = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        int id = getIntent().getIntExtra("ID", -1);
        int z = getIntent().getIntExtra("z", 1);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        db = new dbHelper(this);

        label = findViewById(R.id.label);
        amount = findViewById(R.id.amount);
        dateView = findViewById(R.id.date);
        timeView = findViewById(R.id.time);
        note = findViewById(R.id.notes);

        transaction = findViewById(R.id.transaction);
        loan = findViewById(R.id.loan);

        done = findViewById(R.id.done);
        done.setOnClickListener(v -> {
            vibrate(22);
            if (!label.getText().toString().equals("") && !amount.getText().toString().equals("")) {
                if (isLoan) {
                    db.makeLoan(
                            label.getText().toString(),
                            Integer.parseInt(amount.getText().toString()) * z,
                            dateView.getText().toString() + " " + timeView.getText().toString(),
                            note.getText().toString());
                } else {
                    db.makeTransaction(
                            label.getText().toString(),
                            Integer.parseInt(amount.getText().toString()) * z,
                            dateView.getText().toString() + " " + timeView.getText().toString(),
                            new Tag(),
                            note.getText().toString());
                }
                finish();
            } else {
                Snackbar.make(this, done, "Label and Amount cannot be empty", Snackbar.LENGTH_LONG).show();
            }
        });

        // TODO
        // change 'label' to 'ToFrom' in addLoan,
        // edit makeLoan to add or sub to/from current Loan if 'toFrom' already exists.
        // make 'ToFrom' in addLoan dropdown box.
        // change background color according to 'in'(green) or 'out'(red)


        loan.setOnClickListener(vibrator_15);
        transaction.setOnClickListener(vibrator_15);

        loan.setOnClickListener(v -> {
            isLoan = true;
            label.setHint("To/From");
        });
        transaction.setOnClickListener(v -> {
            isLoan = false;
            label.setHint("Label");
        });

        setDateAndTimeView();

        ConstraintLayout root = findViewById(R.id.main);
        root.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            root.getWindowVisibleDisplayFrame(r);
            int screenHeight = root.getRootView().getHeight();

            // r.bottom is the position above soft keypad or device button.
            // if keypad is shown, the r.bottom is smaller than that before.
            int keypadHeight = screenHeight - r.bottom;

            Log.d("TAG", "keypadHeight = " + keypadHeight);

            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                if (!isKeyboardShowing) {
                    isKeyboardShowing = true;
                    onKeyboardVisibilityChanged(true, keypadHeight);
                }
            } else {
                // keyboard is closed
                if (isKeyboardShowing) {
                    isKeyboardShowing = false;
                    onKeyboardVisibilityChanged(false, keypadHeight);
                }
            }
        });
    }


    int doneButtonHeight;

    void onKeyboardVisibilityChanged(boolean isKeyboardShowing, int keypadHeight) {
        if (isKeyboardShowing) {
            doneButtonHeight = (int) done.getY();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            done.setY(height - keypadHeight - done.getHeight() - px2dip(this, 64));
        } else {
            done.setY(doneButtonHeight);
        }
    }


    private void setDateAndTimeView() {
        Calendar cal = Calendar.getInstance();

        showDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        showTime(cal.get(Calendar.HOUR), cal.get(Calendar.HOUR));
    }

    @SuppressLint("DefaultLocale")
    private String fTwo(int a) {
        return String.format("%02d", a);
    }

    private void vibrate(int millisecond) {
        final VibrationEffect vibrationEffect;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(millisecond, VibrationEffect.DEFAULT_AMPLITUDE);

            // it is safe to cancel other vibrations currently taking place
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect);
        }
    }

    // TODO remove vibrator_i5
    private final View.OnClickListener vibrator_15 = v -> {
        final VibrationEffect vibrationEffect;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrationEffect = VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE);

            // it is safe to cancel other vibrations currently taking place
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect);
        }
    };


    private DatePicker datePicker;

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }
        if (id == 998) {
            return new TimePickerDialog(this, timeListener,
                    Calendar.getInstance().get(Calendar.HOUR),
                    Calendar.getInstance().get(Calendar.MINUTE),
                    true);
        }
        return null;
    }

    private final TimePickerDialog.OnTimeSetListener timeListener = (view, hourOfDay, minute) -> {
        showTime(hourOfDay, minute);
    };

    private void showTime(int a, int b) {
        timeView.setText(new StringBuilder().append(fTwo(a)).append(":")
                .append(fTwo(b)));
    }

    private final DatePickerDialog.OnDateSetListener myDateListener = (DatePicker view, int year, int month, int dayOfMonth) -> {
        // TODO Auto-generated method stub
        // arg1 = year
        // arg2 = month
        // arg3 = day
        showDate(year, month + 1, dayOfMonth);
    };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(fTwo(day)).append("/")
                .append(fTwo(month)).append("/").append(year));
    }


    /**
     * dip to px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px to dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public void cancel(View view) {
        finish();
    }
}