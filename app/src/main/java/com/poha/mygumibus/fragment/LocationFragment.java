package com.poha.mygumibus.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poha.mygumibus.R;
import com.poha.mygumibus.adapter.NearStationRecyclerAdapter;
import com.poha.mygumibus.model.Station;
import com.poha.mygumibus.util.GpsService;
import com.poha.mygumibus.util.XmlParserService;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class LocationFragment extends Fragment implements View.OnClickListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;

    private XmlParserService xmlParserService;
    private GpsService gpsService;

    ArrayList<Station> nearbyStations;
    ArrayList<Double> distanceList;

    RecyclerView recyclerView;
    NearStationRecyclerAdapter adapter;

    MapView mapView;
    Button btn;

    public LocationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        xmlParserService = new XmlParserService();
        nearbyStations = new ArrayList<>();
        distanceList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        mapView = v.findViewById(R.id.map_view);
        btn = v.findViewById(R.id.button);
        recyclerView = v.findViewById(R.id.recycler_station);

        adapter = new NearStationRecyclerAdapter(nearbyStations, distanceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));

        if(!checkLocationServicesStatus()){
            btn.setEnabled(false);
            showDialogForLocationServiceSetting();
        }
        else{
            btn.setEnabled(true);
            gpsService = new GpsService(getContext());
        }

        mapView.setCurrentLocationEventListener(this);
        btn.setOnClickListener(this);

        searchNearbyStations();

        return v;
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
                btn.setEnabled(true);
                gpsService = new GpsService(getContext());
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onClick(View view) {
        searchNearbyStations();
    }

    @Override
    public void onResume() {
        super.onResume();
        searchNearbyStations();
    }

    public void searchNearbyStations(){
        btn.setEnabled(false);

        gpsService.getLocation();

        if(gpsService.isGetLocation()){
            final double lat = gpsService.getLatitude();
            final double lon = gpsService.getLongitude();
            Log.i("LocationFragment", "위도 : " + lat + " / 경도 : " + lon);
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lat, lon), 3, true);

            mapView.removeAllPOIItems();

            // 현 위치 마커 추가
            MapPOIItem markerCenter = new MapPOIItem();
            markerCenter.setMapPoint(MapPoint.mapPointWithGeoCoord(lat,lon));
            markerCenter.setMarkerType(MapPOIItem.MarkerType.BluePin);
            markerCenter.setSelectedMarkerType(MapPOIItem.MarkerType.BluePin);
            markerCenter.setItemName("내 위치");
            mapView.addPOIItem(markerCenter);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    nearbyStations = xmlParserService.getNearbyStations(lat, lon);
                    Log.i("주변 정류장", "개수 - " + nearbyStations.size());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            distanceList = new ArrayList<>();

                            for(Station s : nearbyStations){
                                MapPOIItem marker = new MapPOIItem();
                                marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
                                marker.setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
                                marker.setItemName(s.getName());
                                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(s.getLatitude(), s.getLongitude()));
                                mapView.addPOIItem(marker);

                                distanceList.add(gpsService.getDistanceByWGS80(lat, lon, s.getLatitude(), s.getLongitude()));
                                Log.i("거리", gpsService.getDistanceByWGS80(lat, lon, s.getLatitude(), s.getLongitude())+"");
                            }
                            Log.i("주변 정류장", "개수 - " + distanceList.size());
                            adapter.setList(nearbyStations, distanceList);

                            btn.setEnabled(true);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i("LocationFragment", String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, v));
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    private void onFinishReverseGeoCoding(String result) {
        Toast.makeText(getContext(), "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }
}
