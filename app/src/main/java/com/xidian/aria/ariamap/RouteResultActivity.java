package com.xidian.aria.ariamap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xidian.aria.ariamap.overlays.MyDrivingRouteOverlay;
import com.xidian.aria.ariamap.overlays.MyTransitRouteOverlay;
import com.xidian.aria.ariamap.overlays.MyWalkingRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.DrivingRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.TransitRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.WalkingRouteOverlay;
import java.util.ArrayList;
import java.util.List;

public class RouteResultActivity extends AppCompatActivity {
    TabLayout tabLayout = null;
    ViewPager viewPager = null;
    // 传递过来的数据
    SerializableBaiduMap serializableBaiduMap = null;
    PagerAdapter pagerAdapter = null;
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
    private BikingRouteResult mBikeRes = null;
    private MassTransitRouteResult mMassRes = null;
    // 驾车路线
    DrivingRouteLine drivingRoute = null;
    // 步行路线
    WalkingRouteLine walkingRoute = null;
    TransitRouteLine transitRouteLine = null;
    private BaiduMap mBaiduMap = null;
    private LatLng startPoi;
    private LatLng endPoi;
    private PlanNode stNode;
    private PlanNode enNode;
    private String[] data={"路线1","路线2","路线3"};
    private Spinner spDown;
    ArrayAdapter<String> adapter;
    ListView listView;
    View driveView;
    View busView;
    View walkView;
    RoutePlanSearch search;
    MyOnGetRoutePlanResultListener onGetRoutePlanResultListener;
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
        initIntentData();
        initMap();
        initViewPager();

//        initDriveRes();
//        initBusRes(0);
//        initWalkRes();
    }

    public void initMap(){
        search = RoutePlanSearch.newInstance();
        onGetRoutePlanResultListener = new MyOnGetRoutePlanResultListener(walkMap,driveMap,null,mWalkRes,mTransitRes,mDriveRes);
        search.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);
    }
    public void initViewPager(){
        final List<View> viewList = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        driveView = inflater.inflate(R.layout.pager_drive_page,null);
        busView = inflater.inflate(R.layout.pager_bus_page,null);
        walkView = inflater.inflate(R.layout.pager_walk_page,null);
        driveMap = ((MapView) driveView.findViewById(R.id.drive_map_view)).getMap();
        walkMap = ((MapView) walkView.findViewById(R.id.walk_map_view)).getMap();
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
        viewList.add(driveView);
        viewList.add(busView);
        viewList.add(walkView);
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
                container.removeView(viewList.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return false;
            }
        };
        viewPager.setAdapter(pagerAdapter);
        spDown = busView.findViewById(R.id.spDown);
//        spDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast toast = Toast.makeText(getApplicationContext(),position,Toast.LENGTH_LONG);
//                toast.show();
//                initBusRes(position);
////                List<TransitRouteLine> transitRouteLines = mTransitRes.getRouteLines();
////                for (TransitRouteLine line : transitRouteLines){
////                    List<TransitRouteLine.TransitStep> steps = line.getAllStep();
////                    for (TransitRouteLine.TransitStep step : steps){
////                        System.out.println(step.getInstructions());
////                    }
////                    System.out.println(line.getAllStep());
////                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        adapter= new ArrayAdapter<String>(
                RouteResultActivity.this, android.R.layout.simple_list_item_1, data);
        listView = busView.findViewById(R.id.bus_list);
        listView.setAdapter(adapter);
    }

    /**
     * 解析传递过来的数据
     */
    public void initIntentData(){
        Intent intent = getIntent();
        serializableBaiduMap = intent.getParcelableExtra("map");
        city = serializableBaiduMap.getCity();
        center = serializableBaiduMap.getCenter();
        zoomLevel = serializableBaiduMap.getZoomLevel();
        mDriveRes = serializableBaiduMap.getmDriveRes();
        mWalkRes = serializableBaiduMap.getmWalkRes();
        mTransitRes = serializableBaiduMap.getmTransitRes();
        mBikeRes = serializableBaiduMap.getmBikeRes();
        mMassRes = serializableBaiduMap.getmMassRes();
        startPoi = serializableBaiduMap.getStartPoi();
        endPoi = serializableBaiduMap.getEndPoi();
        stNode = PlanNode.withLocation(startPoi);
        enNode = PlanNode.withLocation(endPoi);
    }

    /**
     * 初始化驾车路线图
     */
    public void initDriveRes(){
        search.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
    }
    /**
     * 初始化步行路线图
     */
    public void initWalkRes(){
        search.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
    }
    /**
     * 初始化公交路线
     */
   public void initBusRes(int way){
       TransitRoutePlanOption option = new TransitRoutePlanOption();
       switch (way){
           case 0:
               option.policy(TransitRoutePlanOption.TransitPolicy.EBUS_NO_SUBWAY);
               break;
           case 1:
               option.policy(TransitRoutePlanOption.TransitPolicy.EBUS_TIME_FIRST);
               break;
           case 2:
               option.policy(TransitRoutePlanOption.TransitPolicy.EBUS_TRANSFER_FIRST);
               break;
           default:
               break;
       }
       search.transitSearch(option.from(stNode).to(enNode).city(city));
    }

}
