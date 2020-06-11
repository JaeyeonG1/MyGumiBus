package com.poha.mygumibus.dialog;

import android.os.Bundle;
import android.util.Xml;
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
import com.poha.mygumibus.adapter.ArrivalBusRecyclerAdapter;
import com.poha.mygumibus.adapter.BusRecyclerAdapter;
import com.poha.mygumibus.adapter.StationRecyclerAdapter;
import com.poha.mygumibus.model.ArrivalBus;
import com.poha.mygumibus.model.Bus;
import com.poha.mygumibus.model.Station;
import com.poha.mygumibus.util.XmlParserService;

import java.util.ArrayList;

public class StationInfoDialogFragment extends DialogFragment implements View.OnClickListener, ArrivalBusRecyclerAdapter.OnItemClickListener {

    public static final String TAG_STATION_DIALOG = "dialog_bus";

    Station station;
    ArrayList<ArrivalBus> arrivalList;

    TextView textViewType;
    TextView textViewName;
    TextView textViewRoute;

    RecyclerView recyclerView;
    ArrivalBusRecyclerAdapter arrivalAdapter;

    XmlParserService xmlParserService;

    public StationInfoDialogFragment(Station station, ArrayList<ArrivalBus> arrivalList) {
        this.station = station;
        this.arrivalList = arrivalList;
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

        textViewType.setText("");
        textViewName.setText(station.getName());
        textViewRoute.setText("");

        arrivalAdapter = new ArrivalBusRecyclerAdapter(arrivalList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(arrivalAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        arrivalAdapter.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    @Override
    public void onBusItemClick(View v, int position) {
        final Bus bus = arrivalList.get(position);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Station> stations = xmlParserService.getStationsByBus(bus);
                final ArrayList<Station> finalStations = stations;
                final ArrayList<Station> busPos = xmlParserService.getLocationOfBus(bus);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BusInfoDialogFragment bd = new BusInfoDialogFragment(bus, finalStations, busPos);
                        bd.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_NoTitleBar_Fullscreen );
                        bd.show(getFragmentManager(), BusInfoDialogFragment.TAG_BUS_DIALOG);
                    }
                });
            }
        }).start();
    }
}
