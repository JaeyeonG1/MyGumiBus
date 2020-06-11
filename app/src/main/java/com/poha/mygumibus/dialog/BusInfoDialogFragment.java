package com.poha.mygumibus.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poha.mygumibus.R;
import com.poha.mygumibus.adapter.BusRecyclerAdapter;
import com.poha.mygumibus.adapter.StationRecyclerAdapter;
import com.poha.mygumibus.model.ArrivalBus;
import com.poha.mygumibus.model.Bus;
import com.poha.mygumibus.model.Station;
import com.poha.mygumibus.util.XmlParserService;

import java.util.ArrayList;

public class BusInfoDialogFragment extends DialogFragment implements View.OnClickListener, StationRecyclerAdapter.OnItemClickListener {

    public static final String TAG_BUS_DIALOG = "dialog_bus";

    Bus bus;
    ArrayList<Station> stationList;
    ArrayList<Station> busPosList;

    TextView textViewType;
    TextView textViewName;
    TextView textViewRoute;

    RecyclerView recyclerView;
    StationRecyclerAdapter stationAdapter;

    XmlParserService xmlParserService;

    public BusInfoDialogFragment(Bus bus, ArrayList<Station> stationList, ArrayList<Station> busPosList) {
        this.bus = bus;
        this.stationList = stationList;
        this.busPosList = busPosList;
        xmlParserService = new XmlParserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_info, container);

        textViewType = v.findViewById(R.id.textView_type);
        textViewName = v.findViewById(R.id.textView_name);
        textViewRoute = v.findViewById(R.id.textView_route);
        recyclerView = v.findViewById(R.id.recycler);

        textViewType.setText(bus.getTypeString());
        textViewName.setText(bus.getName());
        if(bus.getStartNodeName() != null && bus.getEndNodeName() != null)
            textViewRoute.setText(bus.getStartNodeName() + " -> " + bus.getEndNodeName());

        stationAdapter = new StationRecyclerAdapter(stationList, new ArrayList<Double>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(stationAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        stationAdapter.setBusLocation(busPosList);
        stationAdapter.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    @Override
    public void onStationItemClick(View v, int position) {
        final Station station = stationList.get(position);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<ArrivalBus> arrivalBusList = xmlParserService.getBusArrivalList(station);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StationInfoDialogFragment bd = new StationInfoDialogFragment(station, arrivalBusList);
                        bd.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_NoTitleBar_Fullscreen );
                        bd.show(getFragmentManager(), StationInfoDialogFragment.TAG_STATION_DIALOG);
                    }
                });
            }
        }).start();
    }
}
