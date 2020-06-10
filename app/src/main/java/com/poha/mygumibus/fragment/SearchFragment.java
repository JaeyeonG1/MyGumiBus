package com.poha.mygumibus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.poha.mygumibus.R;
import com.poha.mygumibus.model.Bus;
import com.poha.mygumibus.util.XmlParserService;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private XmlParserService xmlParserService;

    ArrayList<Bus> buslist;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
}
