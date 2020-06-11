package com.poha.mygumibus.util;

import android.util.Log;

import com.poha.mygumibus.model.ArrivalBus;
import com.poha.mygumibus.model.Bus;
import com.poha.mygumibus.model.Station;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class XmlParserService {

    private final String CERT_KEY = "?serviceKey=%2Fcfz4C2jjxQ3alVuRJ84el0cntgAJiac3C8pUv9XmD1oAWe1w8qqalKKFCZhPq3Rja13wJumXtItwgaHYvxXcg%3D%3D";
    private final String ENDPOINT = "http://openapi.tago.go.kr/openapi/service/";
    private final int NUMBER_ROW = 1000;
    private final String URL_NUMBER_ROW = "&numOfRows=" + NUMBER_ROW;

    // 구미시 도시 코드
    private final String CITY_CODE = "37050";

    // 파싱을 위한 필드 선언
    private URL url;
    private InputStream is;
    private XmlPullParserFactory factory;
    private XmlPullParser parser;

    private String tag;
    private int eventType;

    public ArrayList<ArrivalBus> getBusArrivalList(Station input){
        Log.i("XMLParserService", input.getName() + "-" + input.getCode());
        String url;

        ArrayList<Station> tmpSttn = getStationsById(input.getCode());

        if(tmpSttn.size() > 0){
            String code = tmpSttn.get(0).getCode();
            Log.i("XMLParserService", code);
            url = ENDPOINT + "ArvlInfoInqireService"+ "/" + "getSttnAcctoArvlPrearngeInfoList" + CERT_KEY + "&cityCode=" + CITY_CODE + "&nodeId=" + code + URL_NUMBER_ROW;
        }
        else
            url = ENDPOINT + "ArvlInfoInqireService"+ "/" + "getSttnAcctoArvlPrearngeInfoList" + CERT_KEY + "&cityCode=" + CITY_CODE + "&nodeId=" + input.getCode() + URL_NUMBER_ROW;

        ArrayList<ArrivalBus> busList = new ArrayList<>();

        String id = null;
        String name = null;
        int type = 0;
        String startNodeName = null;
        String endNodeName = null;
        int startTime = 0;
        int endTime = 0;
        int amntSttn = 0;
        int amntTime = 0;

        try {
            setUrlNParser(url);

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = parser.getName();

                        if (tag.equals("item")) {
                        }
                        else if (tag.equals("routeno")) {
                            parser.next();
                            name = parser.getText();
                            Log.i("노선 번호", parser.getText());
                        }
                        else if (tag.equals("routeid")) {
                            parser.next();
                            id = parser.getText();
                            Log.i("노선 ID", parser.getText());
                        }
                        else if (tag.equals("routetp")) {
                            parser.next();
                            if(parser.getText().equals("일반버스"))
                                type = 0;
                            else if(parser.getText().equals("좌석버스"))
                                type = 1;
                            Log.i("노선 타입", parser.getText());
                        }
                        else if (tag.equals("startnodenm")) {
                            parser.next();
                            startNodeName = parser.getText();
                            Log.i("출발지",parser.getText());
                        }
                        else if (tag.equals("endnodenm")) {
                            parser.next();
                            endNodeName = parser.getText();
                            Log.i("도착지",parser.getText());
                        }
                        else if (tag.equals("startvehicletime")) {
                            parser.next();
                            startTime = Integer.parseInt(parser.getText());
                            Log.i("첫차 시간",parser.getText());
                        }
                        else if (tag.equals("endvehicletime")) {
                            parser.next();
                            endTime = Integer.parseInt(parser.getText());
                            Log.i("막차 시간",parser.getText());
                        }
                        else if (tag.equals("arrprevstationcnt")) {
                            parser.next();
                            amntSttn = Integer.parseInt(parser.getText());
                            Log.i("남은 정류장 수",parser.getText());
                        }
                        else if (tag.equals("arrtime")) {
                            parser.next();
                            amntTime = Integer.parseInt(parser.getText());
                            Log.i("남은 시간",parser.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")){
                            ArrivalBus temp = new ArrivalBus(id, name, type, startNodeName, endNodeName, startTime, endTime, amntSttn, amntTime);
                            busList.add(temp); // 첫번째 검색 결과 종료.. 줄바꿈
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return busList;
    }

    public ArrayList<Station> getLocationOfBus(Bus input){
        String url = ENDPOINT + "BusLcInfoInqireService"+ "/" + "getRouteAcctoBusLcList" + CERT_KEY + "&cityCode=" + CITY_CODE + "&routeId=" + input.getId() + URL_NUMBER_ROW;
        Log.i("URL", url);

        ArrayList<Station> stationList = new ArrayList<>();

        String name = null;
        String code = null;;
        double gpsLati = 0;
        double gpsLong = 0;

        try {
            setUrlNParser(url);

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = parser.getName();

                        if (tag.equals("gpslati")) {
                            parser.next();
                            gpsLati = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("gpslong")){
                            parser.next();
                            gpsLong = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodeid")){
                            parser.next();
                            code = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodenm")){
                            parser.next();
                            name = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")){
                            Station temp = new Station(code, name, gpsLati, gpsLong);
                            stationList.add(temp); // 첫번째 검색 결과 종료.. 줄바꿈
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stationList;
    }

    public ArrayList<Station> getStationsByBus(Bus input){
        String url = ENDPOINT + "BusRouteInfoInqireService"+ "/" + "getRouteAcctoThrghSttnList" + CERT_KEY + "&cityCode=" + CITY_CODE + "&routeId=" + input.getId() + URL_NUMBER_ROW;
        Log.i("URL", url);

        ArrayList<Station> stationList = new ArrayList<>();

        String name = null;
        String code = null;;
        double gpsLati = 0;
        double gpsLong = 0;

        try {
            setUrlNParser(url);

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = parser.getName();

                        if (tag.equals("gpslati")) {
                            parser.next();
                            gpsLati = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("gpslong")){
                            parser.next();
                            gpsLong = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodeid")){
                            parser.next();
                            code = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodenm")){
                            parser.next();
                            name = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")){
                            Station temp = new Station(code, name, gpsLati, gpsLong);
                            stationList.add(temp); // 첫번째 검색 결과 종료.. 줄바꿈
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stationList;
    }

    public ArrayList<Bus> getBusByName(String inputName){
        String url = ENDPOINT + "BusRouteInfoInqireService"+ "/" + "getRouteNoList" + CERT_KEY + "&cityCode=" + CITY_CODE + "&routeNo=" + inputName + URL_NUMBER_ROW;
        Log.i("URL", url);

        ArrayList<Bus> busList = new ArrayList<>();

        String id = null;
        String name = null;
        int type = 0;
        String startNodeName = null;
        String endNodeName = null;
        int startTime = 0;
        int endTime = 0;

        try {
            setUrlNParser(url);

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = parser.getName();

                        if (tag.equals("item")) {
                        }
                        else if (tag.equals("routeno")) {
                            parser.next();
                            name = parser.getText();
                            Log.i("노선 번호", parser.getText());
                        }
                        else if (tag.equals("routeid")) {
                            parser.next();
                            id = parser.getText();
                            Log.i("노선 ID", parser.getText());
                        }
                        else if (tag.equals("routetp")) {
                            parser.next();
                            if(parser.getText().equals("일반버스"))
                                type = 0;
                            else if(parser.getText().equals("좌석버스"))
                                type = 1;
                            Log.i("노선 타입", parser.getText());
                        }
                        else if (tag.equals("startnodenm")) {
                            parser.next();
                            startNodeName = parser.getText();
                            Log.i("출발지",parser.getText());
                        }
                        else if (tag.equals("endnodenm")) {
                            parser.next();
                            endNodeName = parser.getText();
                            Log.i("도착지",parser.getText());
                        }
                        else if (tag.equals("startvehicletime")) {
                            parser.next();
                            startTime = Integer.parseInt(parser.getText());
                            Log.i("첫차 시간",parser.getText());
                        }
                        else if (tag.equals("endvehicletime")) {
                            parser.next();
                            endTime = Integer.parseInt(parser.getText());
                            Log.i("막차 시간",parser.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")){
                            Bus temp = new Bus(id, name, type, startNodeName, endNodeName, startTime, endTime);
                            busList.add(temp); // 첫번째 검색 결과 종료.. 줄바꿈
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return busList;
    }

    public ArrayList<Station> getStationsByName(String inputName){
        String url = ENDPOINT + "BusSttnInfoInqireService"+ "/" + "getSttnNoList" + CERT_KEY + "&cityCode=" + CITY_CODE + "&nodeNm=" + inputName + URL_NUMBER_ROW;
        Log.i("URL", url);

        ArrayList<Station> stationList = new ArrayList<>();

        String name = null;
        String code = null;;
        double gpsLati = 0;
        double gpsLong = 0;

        try {
            setUrlNParser(url);

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = parser.getName();

                        if (tag.equals("gpslati")) {
                            parser.next();
                            gpsLati = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("gpslong")){
                            parser.next();
                            gpsLong = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodeno")){
                            parser.next();
                            code = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodenm")){
                            parser.next();
                            name = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")){
                            Station temp = new Station(code, name, gpsLati, gpsLong);
                            stationList.add(temp); // 첫번째 검색 결과 종료.. 줄바꿈
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stationList;
    }

    public ArrayList<Station> getStationsById(String id){
        String url = ENDPOINT + "BusSttnInfoInqireService"+ "/" + "getSttnNoList" + CERT_KEY + "&cityCode=" + CITY_CODE + "&nodeNo=" + id + URL_NUMBER_ROW;
        Log.i("URL", url);

        ArrayList<Station> stationList = new ArrayList<>();

        String name = null;
        String code = null;;
        double gpsLati = 0;
        double gpsLong = 0;

        try {
            setUrlNParser(url);

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = parser.getName();

                        if (tag.equals("gpslati")) {
                            parser.next();
                            gpsLati = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("gpslong")){
                            parser.next();
                            gpsLong = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodeid")){
                            parser.next();
                            code = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodenm")){
                            parser.next();
                            name = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")){
                            Station temp = new Station(code, name, gpsLati, gpsLong);
                            stationList.add(temp); // 첫번째 검색 결과 종료.. 줄바꿈
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stationList;
    }


    public ArrayList<Station> getNearbyStations(double latitude, double longitude){
        String url = ENDPOINT + "BusSttnInfoInqireService"+ "/" + "getCrdntPrxmtSttnList" + CERT_KEY + "&gpsLati=" + latitude + "&gpsLong=" + longitude;
        Log.i("URL", url);

        ArrayList<Station> stationList = new ArrayList<>();

        String name = null;
        String code = null;;
        double gpsLati = 0;
        double gpsLong = 0;

        try {
            setUrlNParser(url);
            boolean canAdd = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = parser.getName();

                        if (tag.equals("item")) {
                            canAdd = false;
                        }
                        else if (tag.equals("citycode")) {
                            parser.next();
                            Log.i("도시 코드",parser.getText());
                            if(parser.getText().equals(CITY_CODE))
                                canAdd = true;
                        }
                        else if (tag.equals("gpslati")) {
                            parser.next();
                            gpsLati = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("gpslong")){
                            parser.next();
                            gpsLong = Double.parseDouble(parser.getText());
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodeid")){
                            parser.next();
                            code = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        else if (tag.equals("nodenm")){
                            parser.next();
                            name = parser.getText();
                            Log.i("추가", parser.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item") && canAdd){
                            Station temp = new Station(code, name, gpsLati, gpsLong);
                            stationList.add(temp); // 첫번째 검색 결과 종료.. 줄바꿈
                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stationList;
    }




    // Url, XmlPullParser 객체 생성 및 초기화
    public void setUrlNParser(String query) {
        try {
            url = new URL(query); // 문자열로 된 요청 url을 URL객체로 생성
            is = url.openStream();

            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8")); // inputStream으로부터 xml입력받기

            parser.next();
            eventType = parser.getEventType();
        } catch (Exception e) {
            Log.i("XmlParserService", "Error Set Url Parser" + e);
        }
    }

}
