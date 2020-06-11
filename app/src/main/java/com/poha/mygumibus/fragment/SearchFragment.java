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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poha.mygumibus.R;
import com.poha.mygumibus.adapter.BusSearchRecyclerAdapter;
import com.poha.mygumibus.adapter.StationRecyclerAdapter;
import com.poha.mygumibus.model.Bus;
import com.poha.mygumibus.model.Station;
import com.poha.mygumibus.util.XmlParserService;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private XmlParserService xmlParserService;

    TextView textViewNoResult;
    EditText input;
    Button searchBus;
    Button searchStation;

    RecyclerView busRecycler;
    BusSearchRecyclerAdapter busAdapter;

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
        stationRecycler = view.findViewById(R.id.recycler_station);

        busAdapter = new BusSearchRecyclerAdapter(busList);
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
}
