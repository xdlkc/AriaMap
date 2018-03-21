package com.xidian.aria.ariamap;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.xidian.aria.ariamap.enums.TrafficWay;
import com.xidian.aria.ariamap.overlayutil.MassTransitRouteOverlay;
import com.xidian.aria.ariamap.overlayutil.TransitRouteOverlay;


public class ShowMapActivity extends Activity {
    // 地图显示组件
    private MapView mMapView = null;
    // 地图
    private BaiduMap mBaiduMap = null;
    private RoutePlanSearch mSearch = null;
    private LocationClient mLocationClient = null;
    private MyLocationListener locationListener = null;
    // 目标
    private TextInputEditText stTxt = null;
    // 侧边栏按钮
    private ImageButton userBtn = null;
    // 卫星图按钮
    private FloatingActionButton satelliteBtn = null;
    private int mapType = BaiduMap.MAP_TYPE_NORMAL;
    // 热力图按钮
    private FloatingActionButton heatBtn = null;
    // 交通图按钮
    private FloatingActionButton trafficBtn = null;

    private Button plusBtn = null;
    private Button minusBtn = null;
    private Button nearBtn = null;
    private Button navBtn = null;
    private Button wayBtn = null;
    // 中心点
    private LatLng centerPoint = null;
    // 城市
    private String city = null;
    //当前地图缩放级别
    private float zoomLevel;
    private AutoCompleteTextView autoCompleteTextView=null;
    private SuggestionSearch mSuggestionSearch = null;
    ArrayAdapter<String> sugAdapter;
    /**
     * 路线检索
     * @param way 交通方式
     * @param stNode 起点
     * @param enNode 终点
     */
    public void search(TrafficWay way, PlanNode stNode, PlanNode enNode){
        switch (way){
            case WALK:
                mSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
                break;
            case BUS:
                mSearch.transitSearch(new TransitRoutePlanOption().from(stNode).to(enNode));
                break;
            case DRIVING:
                mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
                break;
            default:
                break;
        }
    }

    /**
     * 设置中心点
     */
    public void setCenter(LatLng cenPoi,String city){
        this.centerPoint = cenPoi;
        this.city = city;
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(this.centerPoint)
                .zoom(this.zoomLevel)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        OverlayOptions option = new MarkerOptions()
                .position(this.centerPoint)
                .icon(bitmap);
        mBaiduMap.addOverlay(option);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_show_map);
//        stTxt = (TextInputEditText) findViewById(R.id.st_txt);
        userBtn = (ImageButton) findViewById(R.id.user_btn);
        satelliteBtn = (FloatingActionButton) findViewById(R.id.satellite_btn);
        minusBtn = (Button) findViewById(R.id.minus_btn);
        plusBtn = (Button) findViewById(R.id.plus_btn);
        heatBtn = (FloatingActionButton) findViewById(R.id.heat_btn);
        trafficBtn = (FloatingActionButton) findViewById(R.id.traffic_btn);
        nearBtn = (Button) findViewById(R.id.near_btn);
        navBtn = (Button) findViewById(R.id.nav_btn);
        wayBtn = (Button) findViewById(R.id.way_btn);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.endAutoTw);
        mSuggestionSearch = SuggestionSearch.newInstance();
        sugAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(sugAdapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(charSequence.toString()).city(city));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        initMap();
        initComponents();
        initSearchTool();
        initLocationTool();
        initSearchComplete();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuggestionSearch.destroy();
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
            setCenter(latLng,location.getCity());
            mLocationClient.stop();
        }
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        // 不显示缩放比例尺
        mMapView.showZoomControls(false);
        // 不显示百度地图Logo
        mMapView.removeViewAt(1);
        zoomLevel = 14;
        //百度地图
        mBaiduMap = mMapView.getMap();
        // 改变地图状态
        MapStatus mMapStatus = new MapStatus.Builder().zoom(this.zoomLevel).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
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
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //未找到结果
                    return;
                }
                TransitRouteLine route = result.getRouteLines().get(0);
                //创建公交路线规划线路覆盖物
                TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                //设置公交路线规划数据
                overlay.setData(route);
                //将公交路线规划覆盖物添加到地图中
                overlay.addToMap();
                overlay.zoomToSpan();
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
    private void initComponents(){

//        stTxt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String key = editable.toString();
//
//                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(key).city(city));
//            }
//        });

        satelliteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapType == BaiduMap.MAP_TYPE_NORMAL){
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    mapType = BaiduMap.MAP_TYPE_SATELLITE;
                }else {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    mapType = BaiduMap.MAP_TYPE_NORMAL;
                }
            }
        });

        heatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.setBaiduHeatMapEnabled(!mBaiduMap.isBaiduHeatMapEnabled());
            }
        });

        trafficBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean bool = mBaiduMap.isTrafficEnabled();
                mBaiduMap.setTrafficEnabled(!bool);
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zoomLevel < 20){
                    zoomLevel += 1;
                }
                MapStatus mMapStatus = new MapStatus.Builder().zoom(zoomLevel).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(mMapStatusUpdate);
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (zoomLevel > 0){
                    zoomLevel -= 1;
                }
                MapStatus mMapStatus = new MapStatus.Builder().zoom(zoomLevel).build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(mMapStatusUpdate);
            }
        });

        wayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endStr = stTxt.getText().toString();
                if (endStr.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"请输入目标位置！",Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    PlanNode stNode = PlanNode.withLocation(centerPoint);
                    PlanNode enNode = PlanNode.withCityNameAndPlaceName(city,endStr);
                    search(TrafficWay.getByWay("bus"),stNode,enNode);
                }
            }
        });

    }

    /**
     * 初始化定位
     */
    private void initLocationTool(){
        locationListener = new MyLocationListener();
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(locationListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
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

    public void initSearchComplete(){
        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    return ;
                    //未找到相关结果
                }
                sugAdapter.clear();
                for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
                    if (info.key != null)
                        sugAdapter.add(info.key);
                }
                sugAdapter.notifyDataSetChanged();
                //获取在线建议检索结果
            }
        };
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
    }
}
