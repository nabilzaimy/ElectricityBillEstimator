package com.example.electricitybillestimator; // MAKE SURE THIS PACKAGE NAME MATCHES YOURS

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.text.DecimalFormat; // For formatting numbers
import java.util.Objects; // For Objects.requireNonNull if you're using API 19+

public class DetailActivity extends AppCompatActivity {

    private TextView tvDetailMonth, tvDetailUnitsUsed, tvDetailTotalCharges,
            tvDetailRebate, tvDetailFinalCost;
    private DatabaseHelper dbHelper; // Will be red until DatabaseHelper.java is created/correct

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // Links to your activity_detail.xml

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Bind TextViews from the layout
        tvDetailMonth = findViewById(R.id.tv_detail_month);
        tvDetailUnitsUsed = findViewById(R.id.tv_detail_units_used);
        tvDetailTotalCharges = findViewById(R.id.tv_detail_total_charges);
        tvDetailRebate = findViewById(R.id.tv_detail_rebate);
        tvDetailFinalCost = findViewById(R.id.tv_detail_final_cost);

        // Get the ID of the calculation entry passed from HistoryActivity
        long entryId = getIntent().getLongExtra("entry_id", -1);

        if (entryId != -1) {
            // Retrieve the CalculationEntry from the database using its ID
            CalculationEntry entry = dbHelper.getCalculationById(entryId); // Will be red until getCalculationById is implemented in DatabaseHelper

            if (entry != null) {
                // Populate the TextViews with the retrieved data
                tvDetailMonth.setText("Month: " + entry.getMonth());
                tvDetailUnitsUsed.setText("Units Used: " + DECIMAL_FORMAT.format(entry.getUnitsUsed()) + " kWh");
                tvDetailTotalCharges.setText("Total Charges: RM " + DECIMAL_FORMAT.format(entry.getTotalCharges()));
                tvDetailRebate.setText("Rebate: " + DECIMAL_FORMAT.format(entry.getRebatePercentage()) + " %");
                tvDetailFinalCost.setText("Final Cost: RM " + DECIMAL_FORMAT.format(entry.getFinalCost()));
            } else {
                // Handle case where entry is not found (e.g., show a Toast)
                tvDetailMonth.setText("Error: Calculation not found.");
            }
        } else {
            // Handle case where no ID was passed
            tvDetailMonth.setText("Error: No calculation ID provided.");
        }

        // Optional: Set a title for the activity in the action bar
        Objects.requireNonNull(getSupportActionBar()).setTitle("Calculation Details");
    }
}