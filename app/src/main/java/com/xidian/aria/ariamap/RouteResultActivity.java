package com.xidian.aria.ariamap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xidian.aria.ariamap.overlayutil.DrivingRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.WalkingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

public class RouteResultActivity extends AppCompatActivity {
    TabLayout tabLayout = null;
    ViewPager viewPager = null;
    // 传递过来的数据
    SerializableBaiduMap serializableBaiduMap = null;
    MyPagerAdapter pagerAdapter = null;
    BaiduMap driveMap = null;
    BaiduMap walkMap = null;
    private String city;
    private LatLng center = null;
    // 缩放比例
    private int zoomLevel;
    // 步行线路检索结果
    private WalkingRouteResult mWalkRes = null;
    // 公交线路检索结果
    private TransitRouteResult mTransitRes = null;
    // 驾车线路检索结果
    private DrivingRouteResult mDriveRes = null;
    // 驾车路线
    DrivingRouteLine drivingRoute = null;
    // 步行路线
    WalkingRouteLine walkingRoute = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_result);
        tabLayout = findViewById(R.id.route_tab);
        viewPager = findViewById(R.id.route_view_pager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        initMap();
        initViewPager();
        initIntentData();
        initDriveRes();
        initBusRes();
        initWalkRes();
    }

    /**
     * 初始化多个地图
     */
    public void initMap(){
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(this.center)
                .zoom(this.zoomLevel)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        driveMap.setMapStatus(mMapStatusUpdate);
        walkMap.setMapStatus(mMapStatusUpdate);
    }

    public void initViewPager(){
        List<View> viewList = new ArrayList<>();
        LayoutInflater inflater = getLayoutInflater().from(this);
        MapView driveView = (MapView) inflater.inflate(R.layout.pager_drive_page,null);
        View busView = inflater.inflate(R.layout.pager_bus_page,null);
        MapView walkView = (MapView) inflater.inflate(R.layout.pager_walk_page,null);
        driveMap = driveView.getMap();
        walkMap = walkView.getMap();
        viewList.add(driveView);
        viewList.add(busView);
        viewList.add(walkView);
        pagerAdapter = new MyPagerAdapter(viewList);
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * 解析传递过来的数据
     */
    public void initIntentData(){
        Intent intent = getIntent();
        serializableBaiduMap = (SerializableBaiduMap) intent.getSerializableExtra("map");
        city = serializableBaiduMap.getCity();
        center = serializableBaiduMap.getCenter();
        zoomLevel = serializableBaiduMap.getZoomLevel();
        mDriveRes = serializableBaiduMap.getmDriveRes();
        mWalkRes = serializableBaiduMap.getmWalkRes();
        mTransitRes = serializableBaiduMap.getmTransitRes();
    }

    /**
     * 初始化驾车路线图
     */
    public void initDriveRes(){
        drivingRoute = mDriveRes.getRouteLines().get(0);
        DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(driveMap);
        driveMap.setOnMarkerClickListener(overlay);
        overlay.setData(drivingRoute);
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    /**
     * 初始化步行路线图
     */
    public void initWalkRes(){
        walkingRoute = mWalkRes.getRouteLines().get(0);
        WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(walkMap);
        overlay.setData(walkingRoute);
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    /**
     * 初始化公交路线
     */
    public void initBusRes(){
        // todo:等待公交线路布局页面完成
    }
}
