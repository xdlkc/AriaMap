package com.xidian.aria.ariamap.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xidian.aria.ariamap.listeners.MyOnGetRoutePlanResultListener;
import com.xidian.aria.ariamap.R;
import com.xidian.aria.ariamap.SerializableBaiduMap;

import java.util.Objects;

public class WalkFragment extends Fragment {
    BaiduMap walkMap = null;
    private WalkingRouteResult walkingRouteResult = null;
    private int zoomLevel;
    private LatLng  startPoi;
    private LatLng endPoi;
    // 城市
    private String city;
    // 中心点
    private LatLng center;
    MyOnGetRoutePlanResultListener onGetRoutePlanResultListener;
    public static WalkFragment newInstance(SerializableBaiduMap map){
        WalkFragment fragment = new WalkFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("map_page",map);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            SerializableBaiduMap serializableBaiduMap = args.getParcelable("map_page");
            zoomLevel = serializableBaiduMap.getZoomLevel();
            startPoi = serializableBaiduMap.getStartPoi();
            endPoi = serializableBaiduMap.getEndPoi();
            city = serializableBaiduMap.getCity();
            center = serializableBaiduMap.getCenter();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pager_walk_page,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        MapView walkMapView = Objects.requireNonNull(getView()).findViewById(R.id.walk_map_view);
        walkMapView.removeViewAt(1);
        walkMap = walkMapView.getMap();
        onGetRoutePlanResultListener = new MyOnGetRoutePlanResultListener(walkMap);
        RoutePlanSearch search = RoutePlanSearch.newInstance();
        search.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);
        search.walkingSearch(new WalkingRoutePlanOption().from(PlanNode.withLocation(startPoi)).to(PlanNode.withLocation(endPoi)));
    }
}
