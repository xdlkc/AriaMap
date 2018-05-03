package com.xidian.aria.ariamap.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xidian.aria.ariamap.listeners.MyOnGetRoutePlanResultListener;
import com.xidian.aria.ariamap.R;
import com.xidian.aria.ariamap.parcelables.ParcelableMapData;

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
    RoutePlanSearch search;
    MyOnGetRoutePlanResultListener onGetRoutePlanResultListener;
    public static WalkFragment newInstance(ParcelableMapData map){
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
            ParcelableMapData parcelableMapData = args.getParcelable("map_page");
            zoomLevel = parcelableMapData.getZoomLevel();
            startPoi = parcelableMapData.getStartPoi();
            endPoi = parcelableMapData.getEndPoi();
            city = parcelableMapData.getCity();
            center = parcelableMapData.getCenter();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pager_walk_page,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MapView walkMapView = Objects.requireNonNull(getView()).findViewById(R.id.walk_map_view);
        walkMapView.removeViewAt(1);
        walkMap = walkMapView.getMap();
        onGetRoutePlanResultListener = new MyOnGetRoutePlanResultListener(walkMap);
        search = RoutePlanSearch.newInstance();
        search.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);
        search.walkingSearch(new WalkingRoutePlanOption().from(PlanNode.withLocation(startPoi)).to(PlanNode.withLocation(endPoi)));

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        search.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
