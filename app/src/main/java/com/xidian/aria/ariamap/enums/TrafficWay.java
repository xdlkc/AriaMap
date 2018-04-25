package com.xidian.aria.ariamap.enums;

/**
 * Created by lkc on 18-3-21.
 */

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
        System.out.println("未查找到相应的交通方式！");
        return null;
    }
}
