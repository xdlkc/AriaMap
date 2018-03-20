package com.xidian.aria.ariamap;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xidian.aria.ariamap.overlayutil.MassTransitRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.TransitRouteOverlay;


public class ShowMapActivity extends Activity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private RoutePlanSearch mSearch = null;
    private LocationClient mLocationClient = null;
    private MyLocationListener locationListener = null;

    //当前地图缩放级别
    private float zoomLevel;
    /**
     * 路线检索
     * @param way 交通方式
     * @param stNode 起点
     * @param enNode 终点
     */
    public void search(String way, PlanNode stNode, PlanNode enNode){
        switch (way){
            case "walk":
                mSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
                break;
            case "bus":
                mSearch.transitSearch(new TransitRoutePlanOption().from(stNode).to(enNode));
                break;
            case "driving":
                Boolean bool = mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
                break;
            case "massBus":
                mSearch.masstransitSearch(new MassTransitRoutePlanOption().from(stNode).to(enNode));
            default:
                break;
        }
    }

    /**
     * 设置中心点
     */
    public void setCenter(LatLng cenpt){
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(12)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    /**
     * 设置地图类型
     * @param order ：0普通地图，1卫星地图
     */
    public void setMapType(int order){
        switch (order){
            case 0:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_show_map);
        initMap();
        initBtn();
        initSearchTool();
        initLocationTool();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            setCenter(latLng);
            mLocationClient.stop();
        }
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        //获取地图控件引用
        mMapView = null;
//        mMapView = (MapView) findViewById(R.id.bmapView);
        // 不显示缩放比例尺
        mMapView.showZoomControls(false);
        // 不显示百度地图Logo
        mMapView.removeViewAt(1);
        //百度地图
        mBaiduMap = mMapView.getMap();
        // 改变地图状态
        MapStatus mMapStatus = new MapStatus.Builder().zoom(12).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);

    }

    /**
     * 初始化搜索工具
     */
    private void initSearchTool(){
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }
            @Override
            public void onGetTransitRouteResult(TransitRouteResult result) {
                System.out.println(result.error);
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //未找到结果
                    return;
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    result.getSuggestAddrInfo();
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {;
                    TransitRouteLine route = result.getRouteLines().get(0);
                    System.out.println(route);
                    //创建公交路线规划线路覆盖物
                    TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                    //设置公交路线规划数据
                    overlay.setData(route);
                    //将公交路线规划覆盖物添加到地图中
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //未找到结果
                    return;
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    result.getSuggestAddrInfo();
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {;
                    MassTransitRouteLine route = result.getRouteLines().get(0);
                    System.out.println(route);
                    //创建公交路线规划线路覆盖物
                    MassTransitRouteOverlay overlay = new MassTransitRouteOverlay(mBaiduMap);
                    //设置公交路线规划数据
                    overlay.setData(route);
                    //将公交路线规划覆盖物添加到地图中
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
    }

    /**
     * 初始化界面按钮控件
     */
    private void initBtn(){
//        searchBtn = (Button) findViewById(R.id.search_btn);
//        searchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = getIntent();
//                // 获取起点
//                String valueStart = intent.getStringExtra("start");
//                // 获取终点
//                String valueEnd = intent.getStringExtra("end");
//                String city = intent.getStringExtra("city");
//                final PlanNode stNode = PlanNode.withCityNameAndPlaceName(city,valueStart);
//                final PlanNode enNode = PlanNode.withCityNameAndPlaceName(city,valueEnd);
//                search("bus",stNode,enNode);
//            }
//        });
    }

    /**
     * 初始化定位
     */
    private void initLocationTool(){
        locationListener = new MyLocationListener();
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(locationListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        //开启定位
        mLocationClient.start();

    }
}
