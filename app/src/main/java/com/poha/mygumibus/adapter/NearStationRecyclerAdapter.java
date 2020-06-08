package com.poha.mygumibus.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NearStationRecyclerAdapter extends RecyclerView.Adapter{

    ArrayList<String> list;

    public NearStationRecyclerAdapter(ArrayList<String> list){
        this.list = list;
    }

    public class NearStationViewHolder extends RecyclerView.ViewHolder{

        NearStationViewHolder(View itemView){
            super(itemView);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
