package com.poha.mygumibus.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poha.mygumibus.R;
import com.poha.mygumibus.adapter.BusRecyclerAdapter;
import com.poha.mygumibus.adapter.StationRecyclerAdapter;
import com.poha.mygumibus.dialog.BusInfoDialogFragment;
import com.poha.mygumibus.dialog.StationInfoDialogFragment;
import com.poha.mygumibus.model.ArrivalBus;
import com.poha.mygumibus.model.Bus;
import com.poha.mygumibus.model.Station;
import com.poha.mygumibus.util.XmlParserService;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener, BusRecyclerAdapter.OnItemClickListener, StationRecyclerAdapter.OnItemClickListener {

    private XmlParserService xmlParserService;

    TextView textViewNoResult;
    EditText input;
    Button searchBus;
    Button searchStation;

    RecyclerView busRecycler;
    BusRecyclerAdapter busAdapter;

    RecyclerView stationRecycler;
    StationRecyclerAdapter stationAdapter;

    ArrayList<Bus> busList;
    ArrayList<Station> stationList;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xmlParserService = new XmlParserService();

        busList = new ArrayList<>();
        stationList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        input = view.findViewById(R.id.editText);
        searchBus = view.findViewById(R.id.button_bus);
        searchStation = view.findViewById(R.id.button_station);
        textViewNoResult = view.findViewById(R.id.textView_noResult);
        busRecycler = view.findViewById(R.id.recycler_bus);
        stationRecycler = view.findViewById(R.id.recycler);

        busAdapter = new BusRecyclerAdapter(busList);
        busRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        busRecycler.setAdapter(busAdapter);
        busRecycler.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        busRecycler.setVisibility(View.INVISIBLE);

        stationAdapter = new StationRecyclerAdapter(stationList, new ArrayList<Double>());
        stationRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        stationRecycler.setAdapter(stationAdapter);
        stationRecycler.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        stationRecycler.setVisibility(View.INVISIBLE);

        searchBus.setOnClickListener(this);
        searchStation.setOnClickListener(this);
        busAdapter.setOnItemClickListener(this);
        stationAdapter.setOnItemClickListener(this);

        return view;
    }

    public void searchBus(final String input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!input.equals(""))
                    busList = xmlParserService.getBusByName(input);
                else
                    busList = new ArrayList<>();
                Log.i("버스 수",""+busList.size());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(busList.size() > 0){
                            busAdapter.setBusList(busList);
                            busRecycler.setVisibility(View.VISIBLE);
                            stationRecycler.setVisibility(View.INVISIBLE);
                            textViewNoResult.setVisibility(View.INVISIBLE);
                        }
                        else{
                            textViewNoResult.setVisibility(View.VISIBLE);
                            busRecycler.setVisibility(View.INVISIBLE);
                            stationRecycler.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

    public void searchStation(final String input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!input.equals(""))
                    stationList = xmlParserService.getStationsByName(input);
                else
                    stationList = new ArrayList<>();
                Log.i("버스 수",""+stationList.size());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(stationList.size() > 0){
                            stationAdapter.setList(stationList, new ArrayList<Double>());
                            stationRecycler.setVisibility(View.VISIBLE);
                            busRecycler.setVisibility(View.INVISIBLE);
                            textViewNoResult.setVisibility(View.INVISIBLE);
                        }
                        else{
                            textViewNoResult.setVisibility(View.VISIBLE);
                            stationRecycler.setVisibility(View.INVISIBLE);
                            busRecycler.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_bus:
                searchBus(input.getText().toString());
                break;
            case R.id.button_station:
                searchStation(input.getText().toString());
                break;
        }
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    @Override
    public void onBusItemClick(View v, int position) {
        final Bus bus = busList.get(position);

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
