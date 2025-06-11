package com.example.electricitybillestimator;

import android.content.Intent; // Make sure this import is present
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat; // Make sure this import is present

public class MainActivity extends AppCompatActivity {

    // Declare your UI variables using the type and name
    Spinner spinnerMonth; // Corresponds to @id/spinner_month in XML
    EditText editUnits;   // Corresponds to @id/et_kwh_used in XML
    EditText editRebate;  // Corresponds to @id/et_rebate_percentage in XML
    TextView txtTotalCharges; // Corresponds to @id/tv_total_charges_value in XML
    TextView txtFinalCost;    // Corresponds to @id/tv_final_cost_value in XML
    Button btnCalculate;      // Corresponds to @id/btn_calculate in XML
    Button btnViewHistory;    // Corresponds to @id/btn_view_history in XML
    Button btnAbout;          // Corresponds to @id/btn_about in XML


    String selectedMonth = "";
    private DatabaseHelper dbHelper; // Declare DatabaseHelper

    // Decimal formatter for currency display
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseHelper (assuming you have this class set up as per previous instructions)
        dbHelper = new DatabaseHelper(this);

        // Bind views - IMPORTANT: These IDs MUST EXACTLY MATCH your activity_main.xml
        spinnerMonth = findViewById(R.id.spinner_month); // Corrected ID
        editUnits = findViewById(R.id.et_kwh_used);     // Corrected ID
        editRebate = findViewById(R.id.et_rebate_percentage); // Corrected ID
        txtTotalCharges = findViewById(R.id.tv_total_charges_value); // Corrected ID
        txtFinalCost = findViewById(R.id.tv_final_cost_value);       // Corrected ID
        btnCalculate = findViewById(R.id.btn_calculate);             // Corrected ID
        btnViewHistory = findViewById(R.id.btn_view_history);        // Corrected ID
        btnAbout = findViewById(R.id.btn_about);                     // Corrected ID

        // Setup month spinner
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(adapter);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = months[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMonth = months[0]; // Default to January if nothing is selected
            }
        });

        // Calculate button click
        btnCalculate.setOnClickListener(v -> calculateAndDisplay());

        // View History button click
        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        // About button click
        btnAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void calculateAndDisplay() {
        String unitText = editUnits.getText().toString().trim();
        String rebateText = editRebate.getText().toString().trim();

        // Input validation
        if (unitText.isEmpty()) {
            editUnits.setError("Units used cannot be empty.");
            return;
        }
        if (rebateText.isEmpty()) {
            editRebate.setError("Rebate percentage cannot be empty.");
            return;
        }

        double units;
        double rebate;

        try {
            units = Double.parseDouble(unitText); // Using double for units
            if (units < 0) {
                editUnits.setError("Units used cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            editUnits.setError("Invalid units used. Please enter a number.");
            return;
        }

        try {
            rebate = Double.parseDouble(rebateText);
            if (rebate < 0 || rebate > 5) {
                editRebate.setError("Rebate must be between 0% and 5%.");
                return;
            }
        } catch (NumberFormatException e) {
            editRebate.setError("Invalid rebate. Please enter a number.");
            return;
        }

        double totalCharges = calculateCharges(units); // Pass double units
        double finalCost = totalCharges - (totalCharges * rebate / 100.0); // Use 100.0 for double division

        txtTotalCharges.setText(String.format("Total Charges: RM %s", DECIMAL_FORMAT.format(totalCharges)));
        txtFinalCost.setText(String.format("Final Cost: RM %s", DECIMAL_FORMAT.format(finalCost)));

        // Save to database
        long timestamp = System.currentTimeMillis();
        // Ensure CalculationEntry and DatabaseHelper classes exist and are correctly implemented
        CalculationEntry newEntry = new CalculationEntry(selectedMonth, units, totalCharges, rebate, finalCost, timestamp);
        long result = dbHelper.addCalculation(newEntry);

        if (result != -1) {
            Toast.makeText(this, "Calculation for " + selectedMonth + " saved!", Toast.LENGTH_SHORT).show();
            // Optionally clear inputs after successful save
            editUnits.setText("");
            editRebate.setText("");
            spinnerMonth.setSelection(0); // Reset month to January
        } else {
            Toast.makeText(this, "Failed to save calculation.", Toast.LENGTH_SHORT).show();
        }
    }

    // Corrected electricity charges calculation based on blocks
    private double calculateCharges(double units) { // Changed parameter to double
        double total = 0;
        double remainingUnits = units;

        // Block 1: First 200 kWh (1 - 200 kWh) @ 21.8 sen/kWh
        if (remainingUnits > 0) {
            double unitsInBlock = Math.min(remainingUnits, 200);
            total += unitsInBlock * 0.218;
            remainingUnits -= unitsInBlock;
        }

        // Block 2: Next 100 kWh (201 - 300 kWh) @ 33.4 sen/kWh
        if (remainingUnits > 0) {
            double unitsInBlock = Math.min(remainingUnits, 100);
            total += unitsInBlock * 0.334;
            remainingUnits -= unitsInBlock;
        }

        // Block 3: Next 300 kWh (301 - 600 kWh) @ 51.6 sen/kWh
        if (remainingUnits > 0) {
            double unitsInBlock = Math.min(remainingUnits, 300);
            total += unitsInBlock * 0.516;
            remainingUnits -= unitsInBlock;
        }

        // Block 4: Next 300 kWh (601 - 900 kWh) @ 54.6 sen/kWh
        if (remainingUnits > 0) {
            double unitsInBlock = Math.min(remainingUnits, 300);
            total += unitsInBlock * 0.546;
            remainingUnits -= unitsInBlock;
        }

        // Block 5: 901 kWh onwards @ 57.1 sen/kWh (as per our assumption, confirm with your instructor)
        if (remainingUnits > 0) {
            total += remainingUnits * 0.571;
        }

        // Round to two decimal places for currency precision
        return Math.round(total * 100.0) / 100.0;
    }
}