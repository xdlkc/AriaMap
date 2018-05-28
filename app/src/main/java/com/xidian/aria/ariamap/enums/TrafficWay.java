package com.xidian.aria.ariamap.enums;


public enum TrafficWay {
    WALK("walk",1),DRIVING("driving",2),BUS("bus",3);
    private String way;
    private int wayNo;

    TrafficWay(String way, int wayNo) {
        this.way = way;
        this.wayNo = wayNo;
    }

    public String getWay() {
        return way;
    }

    public int getWayNo() {
        return wayNo;
    }

    static public TrafficWay getByWay(String way){
        for (TrafficWay trafficWay : TrafficWay.values()){
            if (trafficWay.getWay().equals(way)){
                return trafficWay;
            }
        }
        return null;
    }
}
