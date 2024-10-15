package com.examples.ejemplo_navdrawer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BMIAdapter extends RecyclerView.Adapter<BMIAdapter.ViewHolder> {
    private List<String> bmiStringList;

    public BMIAdapter(List<String> bmiStringList) {
        this.bmiStringList = bmiStringList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bmiTextView.setText(bmiStringList.get(position));
    }

    @Override
    public int getItemCount() {
        return bmiStringList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bmiTextView;

        ViewHolder(View itemView) {
            super(itemView);
            bmiTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
