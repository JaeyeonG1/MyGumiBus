package com.poha.mygumibus.model;

public class Bus {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_DUAL = 1;
    public static final String NORMAL = "일반";
    public static final String DUAL = "좌석";

    private String id;
    private String name;
    private int type;
    private String startNodeName;
    private String endNodeName;
    private int startTime;
    private int endTime;

    public Bus(String id, String name, int type, String startNodeName, String endNodeName, int startTime, int endTime) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.startNodeName = startNodeName;
        this.endNodeName = endNodeName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getTypeString() {
        if(type == TYPE_NORMAL)
            return NORMAL;
        else
            return DUAL;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartNodeName() {
        return startNodeName;
    }

    public void setStartNodeName(String startNodeName) {
        this.startNodeName = startNodeName;
    }

    public String getEndNodeName() {
        return endNodeName;
    }

    public void setEndNodeName(String endNodeName) {
        this.endNodeName = endNodeName;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
