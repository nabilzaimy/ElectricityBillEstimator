package com.example.electricitybillestimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class CalculationAdapter extends ArrayAdapter<CalculationEntry> {

    private Context mContext;
    private int mResource;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");


    public CalculationAdapter(@NonNull Context context, int resource, @NonNull List<CalculationEntry> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the calculation entry for this position
        CalculationEntry entry = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            // If there's no recycled view, inflate a new one
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.tvMonth = convertView.findViewById(R.id.tv_history_month);
            holder.tvFinalCost = convertView.findViewById(R.id.tv_history_final_cost);
            convertView.setTag(holder); // Store the ViewHolder
        } else {
            // Use the recycled view and its stored ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        if (entry != null) {
            holder.tvMonth.setText("Month: " + entry.getMonth());
            holder.tvFinalCost.setText("Final Cost: RM " + DECIMAL_FORMAT.format(entry.getFinalCost()));
        }

        return convertView;
    }

    // ViewHolder pattern for efficient list scrolling
    static class ViewHolder {
        TextView tvMonth;
        TextView tvFinalCost;
    }
}