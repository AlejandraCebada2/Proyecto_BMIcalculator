package com.examples.ejemplo_navdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.examples.ejemplo_navdrawer.BMIComparison;

import java.util.List;

public class BMIComparisonAdapter extends RecyclerView.Adapter<BMIComparisonAdapter.ViewHolder> {
    private List<BMIComparison> comparisons;

    public BMIComparisonAdapter(List<BMIComparison> comparisons) {
        this.comparisons = comparisons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BMIComparison comparison = comparisons.get(position);
        holder.text1.setText(comparison.getOrganization());
        holder.text2.setText(comparison.getClassification());
    }

    @Override
    public int getItemCount() {
        return comparisons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;
        TextView text2;

        ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
