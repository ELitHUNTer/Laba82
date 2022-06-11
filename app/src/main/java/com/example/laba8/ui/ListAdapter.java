package com.example.laba8.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laba8.R;

import java.util.ArrayList;
import java.util.List;

import Network.NetworkManager;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Item> {

    private List<String> list;

    public ListAdapter(List<String> list){
        this.list = list;
    }

    public ListAdapter(){
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ListAdapter.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ListAdapter.Item viewHolder = new ListAdapter.Item(rowView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.textView.setText(list.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("132", "132");
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Item extends RecyclerView.ViewHolder {

        TextView textView;

        public Item(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.listItem);
        }
    }

    public void update(List<String> list){
        clear();
        this.list = list;
        NetworkManager.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(list.size());
            }
        });

    }

    public void clear(){
        NetworkManager.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeRemoved(0, list.size());
            }
        });
        list.clear();
    }
}
