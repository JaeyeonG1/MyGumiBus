package com.poha.mygumibus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poha.mygumibus.R;
import com.poha.mygumibus.model.Bus;

import java.util.ArrayList;

public class BusRecyclerAdapter extends RecyclerView.Adapter<BusRecyclerAdapter.BusViewHolder> {

    ArrayList<Bus> busList;
    OnItemClickListener listener = null;

    public BusRecyclerAdapter(ArrayList<Bus> busList) {
        this.busList = busList;
    }

    public class BusViewHolder extends RecyclerView.ViewHolder{
        TextView textName;
        TextView textType;
        TextView textTime;
        TextView textStart;
        TextView textEnd;

        BusViewHolder(View itemView){
            super(itemView);

            textName = itemView.findViewById(R.id.textView_name);
            textType = itemView.findViewById(R.id.textView_type);
            textTime = itemView.findViewById(R.id.textView_time);
            textStart = itemView.findViewById(R.id.textView_startNode);
            textEnd = itemView.findViewById(R.id.textView_endNode);

            //리스너 정의
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(listener != null){
                            listener.onBusItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_bus, parent, false);
        BusViewHolder vh = new BusViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        holder.textName.setText(busList.get(position).getName());
        holder.textType.setText(busList.get(position).getTypeString());
        holder.textTime.setText(timeFormatter(busList.get(position).getStartTime()) + " - " + timeFormatter(busList.get(position).getEndTime()));
        holder.textStart.setText(busList.get(position).getStartNodeName());
        holder.textEnd.setText(busList.get(position).getEndNodeName());
    }

    public String timeFormatter(int time){
        int hour = time / 100;
        int minute = (time - (100 * hour));

        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public void setBusList(ArrayList<Bus> busList){
        this.busList = busList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onBusItemClick(View v, int position);
    }
}
