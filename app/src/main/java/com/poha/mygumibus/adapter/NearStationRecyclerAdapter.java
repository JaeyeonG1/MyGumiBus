package com.poha.mygumibus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poha.mygumibus.R;
import com.poha.mygumibus.model.Station;

import java.util.ArrayList;

public class NearStationRecyclerAdapter extends RecyclerView.Adapter<NearStationRecyclerAdapter.NearStationViewHolder>{

    ArrayList<Station> stationList;
    ArrayList<Double> distanceList;
    OnItemClickListener listener = null;

    public NearStationRecyclerAdapter(ArrayList<Station> stationList, ArrayList<Double> distanceList){
        this.stationList = stationList;
        stationList.add(new Station("Default", "표시할 정류장이 없습니다.", 0, 0));
        this.distanceList = distanceList;
        distanceList.add(0.0);
    }

    public class NearStationViewHolder extends RecyclerView.ViewHolder{
        TextView textName;
        TextView textDistance;

        NearStationViewHolder(View itemView){
            super(itemView);

            textName = itemView.findViewById(R.id.textView_name);
            textDistance = itemView.findViewById(R.id.textView_distance);
            //리스너 정의
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(listener != null){
                            listener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public NearStationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_near_station, parent, false);
        NearStationViewHolder vh = new NearStationViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NearStationViewHolder holder, int position) {
        holder.textName.setText(stationList.get(position).getName());
        holder.textDistance.setText(String.format("%.1f", distanceList.get(position)) + "m");
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    public void setList(ArrayList<Station> sttList, ArrayList<Double> distList){
        stationList = new ArrayList<>();
        distanceList = new ArrayList<>();
        this.stationList = sttList;
        this.distanceList = distList;

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
}
