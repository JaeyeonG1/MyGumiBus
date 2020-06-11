package com.poha.mygumibus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poha.mygumibus.R;
import com.poha.mygumibus.model.Station;

import java.util.ArrayList;

public class StationRecyclerAdapter extends RecyclerView.Adapter<StationRecyclerAdapter.StationViewHolder>{

    ArrayList<Station> stationList;
    ArrayList<Double> distanceList;
    ArrayList<Station> busLocationList;

    boolean isBusChecker;

    OnItemClickListener listener = null;

    public StationRecyclerAdapter(ArrayList<Station> stationList, ArrayList<Double> distanceList){
        this.stationList = stationList;
        this.distanceList = distanceList;
        isBusChecker = false;

        if(stationList.size() == 0){
            stationList.add(new Station("Default", "표시할 정류장이 없습니다.", 0, 0));
            distanceList.add(0.0);
        }
    }

    public class StationViewHolder extends RecyclerView.ViewHolder{
        TextView textName;
        TextView textDistanceCode;

        StationViewHolder(View itemView){
            super(itemView);

            textName = itemView.findViewById(R.id.textView_name);
            textDistanceCode = itemView.findViewById(R.id.textView_distanceOrCode);
            //리스너 정의
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(listener != null){
                            listener.onStationItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_station, parent, false);
        StationViewHolder vh = new StationViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        holder.textName.setText(stationList.get(position).getName());
        if(stationList.size() != distanceList.size()){
            if(isBusChecker){
                for(Station s : busLocationList){
                    if(s.getCode().equals(stationList.get(position).getCode())){
                        holder.textDistanceCode.setText("BUS");
                    }
                }
            }
            else{
                holder.textDistanceCode.setText(stationList.get(position).getCode());
            }
        }
        else{
            holder.textDistanceCode.setText(String.format("%.1f", distanceList.get(position)) + "m");
        }
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

    public void setBusLocation(ArrayList<Station> busLocationList){
        this.busLocationList = busLocationList;
        isBusChecker = true;

        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onStationItemClick(View v, int position);
    }
}
