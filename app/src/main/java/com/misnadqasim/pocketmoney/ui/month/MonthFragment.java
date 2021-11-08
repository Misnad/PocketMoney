package com.misnadqasim.pocketmoney.ui.month;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.misnadqasim.pocketmoney.AddTransaction;
import com.misnadqasim.pocketmoney.R;
import com.misnadqasim.pocketmoney.database.dbHelper;
import com.misnadqasim.pocketmoney.transaction.TransactionDetails;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month, container, false);


        Button n = view.findViewById(R.id.earn);
        Log.d("TAG", "onCreate: " + n);
        n.setOnClickListener(v -> startActivity(new Intent(view.getContext(), AddTransaction.class)));


        LinearLayout l = view.findViewById(R.id.transactions);

        dbHelper db = new dbHelper(view.getContext());

        ArrayList<TransactionDetails> result = db.getTransactions();
        for (int i=0; i<result.size(); i++) {
            TextView t = new TextView(view.getContext());
            t.setText(result.get(i).label +" : " + result.get(i).amount);
            t.setMinimumHeight(500);
            l.addView(t);
        }

        return view;
    }
}