package com.poha.mygumibus.model;

public class ArrivalBus extends Bus{

    private int amountStation;
    private int arrTime;

    public ArrivalBus(String id, String name, int type, String startNodeName, String endNodeName, int startTime, int endTime, int amountStation, int arrTime) {
        super(id, name, type, startNodeName, endNodeName, startTime, endTime);
        this.amountStation = amountStation;
        this.arrTime = arrTime;
    }

    public int getAmountStation() {
        return amountStation;
    }

    public void setAmountStation(int amountStation) {
        this.amountStation = amountStation;
    }

    public int getArrTime() {
        return arrTime;
    }

    public void setArrTime(int arrTime) {
        this.arrTime = arrTime;
    }
}
