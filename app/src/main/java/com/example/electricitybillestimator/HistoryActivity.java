package com.example.electricitybillestimator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView lvCalculationHistory;
    private TextView tvNoHistory;
    private DatabaseHelper dbHelper;
    private CalculationAdapter adapter;
    private List<CalculationEntry> calculationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lvCalculationHistory = findViewById(R.id.lv_calculation_history);
        tvNoHistory = findViewById(R.id.tv_no_history);
        dbHelper = new DatabaseHelper(this);

        loadCalculationHistory();

        // Set click listener for ListView items
        lvCalculationHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CalculationEntry selectedEntry = calculationList.get(position);

                // Navigate to DetailActivity and pass data
                Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                intent.putExtra("entry_id", selectedEntry.getId()); // Pass the ID
                startActivity(intent);
            }
        });
    }

    private void loadCalculationHistory() {
        calculationList = dbHelper.getAllCalculations();

        if (calculationList.isEmpty()) {
            tvNoHistory.setVisibility(View.VISIBLE);
            lvCalculationHistory.setVisibility(View.GONE);
        } else {
            tvNoHistory.setVisibility(View.GONE);
            lvCalculationHistory.setVisibility(View.VISIBLE);
            adapter = new CalculationAdapter(this, R.layout.list_item_calculation, calculationList);
            lvCalculationHistory.setAdapter(adapter);
        }
    }

    // Refresh history when returning to this activity
    @Override
    protected void onResume() {
        super.onResume();
        loadCalculationHistory();
    }
}