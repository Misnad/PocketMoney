package com.misnadqasim.pocketmoney.ui.month;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.misnadqasim.pocketmoney.AddTransaction;
import com.misnadqasim.pocketmoney.MainActivity;
import com.misnadqasim.pocketmoney.R;
import com.misnadqasim.pocketmoney.activity.SettingsActivity;
import com.misnadqasim.pocketmoney.database.dbHelper;
import com.misnadqasim.pocketmoney.transaction.TransactionDetails;
import com.misnadqasim.pocketmoney.transaction.TransactionView;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthFragment newInstance(String param1, String param2) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    dbHelper db;
    View view;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_month, container, false);
        db = new dbHelper(view.getContext());

        refreshView();

        setMonthChanger();
        setSettingsButton();

        return view;
    }

    private void setSettingsButton() {
        view.findViewById(R.id.settings).setOnClickListener(v -> startActivity(new Intent(requireActivity(), SettingsActivity.class)));
    }

    private void setMonthChanger() {
        // TODO replace with month picker (date picker)
        ImageButton menu = view.findViewById(R.id.month_changer);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {
                        "JANUARY",
                        "FEBRUARY",
                        "MARCH",
                        "APRIL",
                        "MAY",
                        "JUNE",
                        "JULY",
                        "AUGUST",
                        "SEPTEMBER",
                        "OCTOBER",
                        "NOVEMBER",
                        "DECEMBER"};

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                builder.setTitle("Choose month:");
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {


                    }

                });

                AlertDialog alert = builder.create();

                alert.show();
                //do your stuff here
            }
        });
    }

    Intent longPressIntent;
    int vId;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, 0, "Edit");
        menu.add(0, 1, 0, "Delete");
        longPressIntent = new Intent(getContext(), AddTransaction.class);
        longPressIntent.putExtra("ID", v.getId());
        vId = v.getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                startActivity(longPressIntent);
                break;
            case 1:
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                dialog.setMessage("Deleting a transaction cannot be undone!");
                dialog.setTitle("Are you sure?");
                dialog.setPositiveButton("DELETE",
                        (dialog12, which) -> {
                            db.deleteTransaction(vId);
                            refreshView();
                        }
                );
                dialog.setNegativeButton(android.R.string.cancel, (dialog1, which) -> {
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                break;
        }
        return true;
    }


    @SuppressLint("SetTextI18n")
    public void setBalances() {
        TextView balance = view.findViewById(R.id.balance);
        balance.setText(db.getTransactionSum() + " ₹");
        TextView totalIn = view.findViewById(R.id.total_in);
        totalIn.setText(db.getTransactionIns() + " ₹");
        TextView totalOut = view.findViewById(R.id.total_out);
        totalOut.setText(Math.abs(db.getTransactionOuts()) + " ₹");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTransactions() {
        LinearLayout l = view.findViewById(R.id.transactions);
        l.removeAllViews();

        ArrayList<TransactionDetails> result = db.getTransactions();
        for (int i = 0; i < result.size(); i++) {
            TransactionView tv = new TransactionView(view.getContext(), result.get(i));
            registerForContextMenu(tv);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 5, 0, 5);
            tv.setLayoutParams(params);
            l.addView(tv);
        }
    }

    private void setEarnSpendButtons() {
        Button earn = view.findViewById(R.id.earn);
        Button spend = view.findViewById(R.id.spend);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), AddTransaction.class);
                if (v == earn) {
                    intent.putExtra("z", 1);
                } else {
                    intent.putExtra("z", -1);
                }
                startActivity(intent);
//                MainActivity.overridePendingTransition(R.anim.hold, R.anim.fade_in);
            }
        };
        earn.setOnClickListener(listener);
        spend.setOnClickListener(listener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshView() {
        setBalances();
        setTransactions();
        setEarnSpendButtons();
    }
}