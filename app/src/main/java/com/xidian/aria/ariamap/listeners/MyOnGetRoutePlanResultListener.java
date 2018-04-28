package com.xidian.aria.ariamap.listeners;

import android.widget.ArrayAdapter;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xidian.aria.ariamap.overlays.MyDrivingRouteOverlay;
import com.xidian.aria.ariamap.overlays.MyWalkingRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.DrivingRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.WalkingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

public class MyOnGetRoutePlanResultListener implements OnGetRoutePlanResultListener {
    private BaiduMap map;
    private WalkingRouteResult walkingRouteResult;
    private MassTransitRouteResult massTransitRouteResult;
    private DrivingRouteResult drivingRouteResult;

    public MyOnGetRoutePlanResultListener(BaiduMap map) {
        this.map = map;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        this.walkingRouteResult = walkingRouteResult;
        WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(map);
        map.setOnMarkerClickListener(overlay);
        overlay.setData(walkingRouteResult.getRouteLines().get(0));
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
        this.massTransitRouteResult = massTransitRouteResult;

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        this.drivingRouteResult = drivingRouteResult;
        DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(map);
        map.setOnMarkerClickListener(overlay);
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

    public WalkingRouteResult getWalkingRouteResult() {
        return walkingRouteResult;
    }

    public MassTransitRouteResult getMassTransitRouteResult() {
        return massTransitRouteResult;
    }

    public DrivingRouteResult getDrivingRouteResult() {
        return drivingRouteResult;
    }
}
