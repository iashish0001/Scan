package com.example.scan;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private String[] data;
    // RecyclerView recyclerView;
    public RecyclerAdapter(String[] data) {
        this.data = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = data[position];
        holder.txtTitle.setText(title);
    }


    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgIcon;
        public TextView txtTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            this.txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        }
    }
}

