package com.poha.mygumibus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poha.mygumibus.R;
import com.poha.mygumibus.model.ArrivalBus;

import java.util.ArrayList;

public class ArrivalBusRecyclerAdapter extends RecyclerView.Adapter<ArrivalBusRecyclerAdapter.BusViewHolder> {

    ArrayList<ArrivalBus> busList;
    OnItemClickListener listener = null;

    public ArrivalBusRecyclerAdapter(ArrayList<ArrivalBus> busList) {
        this.busList = busList;
    }

    public class BusViewHolder extends RecyclerView.ViewHolder{
        TextView textName;
        TextView textType;
        TextView textAmount;
        TextView textTime;

        BusViewHolder(View itemView){
            super(itemView);

            textName = itemView.findViewById(R.id.textView_name);
            textType = itemView.findViewById(R.id.textView_type);
            textAmount = itemView.findViewById(R.id.textView_amount);
            textTime = itemView.findViewById(R.id.textView_time);;

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

        View view = inflater.inflate(R.layout.item_arrival_bus, parent, false);
        BusViewHolder vh = new BusViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        holder.textName.setText(busList.get(position).getName());
        holder.textAmount.setText(busList.get(position).getAmountStation()+" 정거장 전");
        holder.textTime.setText(timeFormatter(busList.get(position).getArrTime()) + "후 도착 예정");
        holder.textType.setText(busList.get(position).getTypeString());
    }

    public String timeFormatter(int time){
        int minute = time / 60;
        int second = (time - (60 * minute));

        return String.format("%02d", minute) + "분 ";// + String.format("%02d", second) + "초";
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public void setBusList(ArrayList<ArrivalBus> busList){
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
