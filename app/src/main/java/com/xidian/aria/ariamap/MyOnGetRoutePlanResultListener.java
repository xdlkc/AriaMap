package com.xidian.aria.ariamap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xidian.aria.ariamap.overlays.*;
import com.xidian.aria.ariamap.overlayutil.DrivingRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.WalkingRouteOverlay;

public class MyOnGetRoutePlanResultListener implements OnGetRoutePlanResultListener {
    BaiduMap walkMap;
    BaiduMap driveMap;
    BaiduMap transitMap;
    WalkingRouteResult walkingRouteResult;
    TransitRouteResult transitRouteResult;
    DrivingRouteResult drivingRouteResult;

    public MyOnGetRoutePlanResultListener(BaiduMap walkMap, BaiduMap driveMap, BaiduMap transitMap, WalkingRouteResult walkingRouteResult, TransitRouteResult transitRouteResult, DrivingRouteResult drivingRouteResult) {
        this.walkMap = walkMap;
        this.driveMap = driveMap;
        this.transitMap = transitMap;
        this.walkingRouteResult = walkingRouteResult;
        this.transitRouteResult = transitRouteResult;
        this.drivingRouteResult = drivingRouteResult;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        this.walkingRouteResult = walkingRouteResult;
        WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(walkMap);
        walkMap.setOnMarkerClickListener(overlay);
        overlay.setData(walkingRouteResult.getRouteLines().get(0));
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        this.drivingRouteResult = drivingRouteResult;
        DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(driveMap);
        driveMap.setOnMarkerClickListener(overlay);
        overlay.setData(drivingRouteResult.getRouteLines().get(0));
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }
}
