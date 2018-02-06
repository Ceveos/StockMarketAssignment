package com.fafaffy.stockmarket.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fafaffy.stockmarket.Models.StockData;
import com.fafaffy.stockmarket.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by alex on 2/1/18.
 * This is a adapter used by the recycler view of the main activity. this
 * sets the logic on how to display the data to the end-user.
 */

public class StockDataRecyclerAdapter extends RecyclerView.Adapter<StockDataRecyclerAdapter.ViewHolder> {

    private List<StockData> mDataset;

    // Inner class that provides a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView open;
        public TextView high;
        public TextView low;
        public TextView close;
        public TextView volume;
        public TextView adj_closed;


        // Constructor that takes in a view and sets the textview references to the class
        public ViewHolder(View v) {
            super(v);
            date = v.findViewById(R.id.date);
            open = v.findViewById(R.id.open);
            high = v.findViewById(R.id.high);
            low = v.findViewById(R.id.low);
            close = v.findViewById(R.id.close);
            volume = v.findViewById(R.id.volume);
            adj_closed = v.findViewById(R.id.adj_closed);

        }
    }


    // Constructor for our class. Sets the dataset (list of stock data) to our list
    public StockDataRecyclerAdapter(List<StockData> myDataset)
    {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StockDataRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cell_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        StockData dataModel = mDataset.get(position);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        holder.date.setText(df.format(dataModel.Date));
        holder.open.setText(Float.toString(dataModel.Open));
        holder.high.setText(Float.toString(dataModel.High));
        holder.low.setText(Float.toString(dataModel.Low));
        holder.close.setText(Float.toString(dataModel.Close));
        holder.volume.setText(Float.toString(dataModel.Volume));
        holder.adj_closed.setText(Float.toString(dataModel.AdjClose));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
