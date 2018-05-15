
package com.xidian.aria.ariamap;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.google.gson.Gson;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xidian.aria.ariamap.calwalk.activity.MainActivity;
import com.xidian.aria.ariamap.navs.BusSearchActivity;
import com.xidian.aria.ariamap.navs.FaceLoginActivity;
import com.xidian.aria.ariamap.navs.HistoryTodayActivity;
import com.xidian.aria.ariamap.navs.SubwayActivity;
import com.xidian.aria.ariamap.navs.WeatherActivity;
import com.xidian.aria.ariamap.parcelables.ParcelableMapData;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图首页
 */
public class ShowMapActivity extends Activity implements BaiduMap.OnMapClickListener,
        NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,
        OnGetSuggestionResultListener{
    // 地图显示组件
    private MapView mMapView = null;
    // 地图
    private BaiduMap mBaiduMap = null;
    // 定位器
    private LocationClient mLocationClient = null;
    // 定位监听
    private MyLocationListener locationListener = null;
    // 侧边栏按钮
    private ImageButton userBtn = null;
    // 语音输入按钮
    private ImageButton voiceBtn;
    // 卫星图按钮
    private FloatingActionButton satelliteBtn = null;
    // 地图类型
    private int mapType = BaiduMap.MAP_TYPE_NORMAL;
    // 热力图按钮
    private FloatingActionButton heatBtn = null;
    // 交通图按钮
    private FloatingActionButton trafficBtn = null;
    // 放大按钮
    private Button plusBtn = null;
    // 缩小按钮
    private Button minusBtn = null;
    // 周边按钮 todo:未完成
    private Button nearBtn = null;
    // 导航按钮 todo:进行中
    private Button navBtn = null;
    // 路线按钮
    private Button wayBtn = null;
    // 中心点
    private LatLng centerPoint = null;
    // 城市
    private String city = null;
    //当前地图缩放级别
    private int zoomLevel = 14;
    // 地点自动补全
    private AutoCompleteTextView enAutoTw =null;
    // 建议搜索地点的字符串
    private List<String> sugStrList;
    // 建议搜索地点对应的坐标
    private List<LatLng> sugPoiList;
    private SuggestionSearch mSuggestionSearch = null;
    // 侧边栏
    private NavigationView navigationView = null;
    // 起点坐标
    private LatLng startPoi;
    private LatLng endPoi;
    // 语音
    private InitListener initListener;
    // 录音框
    private RecognizerDialogListener recognizerDialogListener;
    private StringBuilder speechBuilder;
    // 除了地图，其他控件是否展示
    private boolean isVisible = true;
    private RelativeLayout mapLayout;
    private BitmapDescriptor startDesc;
    private Overlay startOverlay = null;
    private BitmapDescriptor endDesc;
    private Overlay endOverlay = null;
    private ArrayList<String > inputSugData;
    private ArrayAdapter<String > arrayAdapter;
    private BikeNavigateHelper mNaviHelper;
    private WalkNavigateHelper mWNaviHelper;
    private BikeNaviLaunchParam param;
    private WalkNaviLaunchParam walkParam;
    private OnGetSuggestionResultListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPermission();
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_show_map);
        mMapView = (MapView) findViewById(R.id.bmapView);
        userBtn = (ImageButton) findViewById(R.id.user_btn);
        voiceBtn = findViewById(R.id.voice_btn);
        satelliteBtn = (FloatingActionButton) findViewById(R.id.satellite_btn);
        minusBtn = (Button) findViewById(R.id.minus_btn);
        plusBtn = (Button) findViewById(R.id.plus_btn);
        heatBtn = (FloatingActionButton) findViewById(R.id.heat_btn);
        trafficBtn = (FloatingActionButton) findViewById(R.id.traffic_btn);
        nearBtn = (Button) findViewById(R.id.near_btn);
        navBtn = (Button) findViewById(R.id.nav_btn);
        wayBtn = (Button) findViewById(R.id.way_btn);
        enAutoTw = (AutoCompleteTextView) findViewById(R.id.endAutoTw);
        mSuggestionSearch = SuggestionSearch.newInstance();
        navigationView = (NavigationView) findViewById(R.id.user_nav);
        mapLayout = findViewById(R.id.map_layout);
        startDesc = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        endDesc = BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        speechBuilder = new StringBuilder();
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

    /**
     * 地图单击事件,单击后隐藏其他控件
     * @param point
     */
    @Override
    public void onMapClick(LatLng point) {
        AnimationUtils.showAndHiddenAnimation(navigationView, isVisible, 1000);
        AnimationUtils.showAndHiddenAnimation(mapLayout,isVisible,1000);
        isVisible = !isVisible;
    }

    /**
     * 地图上的点的单击事件
     * @param poi
     * @return
     */
    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        mBaiduMap.hideInfoWindow();
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.input,null);
        TextView textView = relativeLayout.findViewById(R.id.poi_name_tv);
        textView.setText(poi.getName());

        final LatLng inputPoi = poi.getPosition();
        ImageButton setStartBtn = relativeLayout.findViewById(R.id.set_start);
        setStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartPoi(inputPoi);
                mBaiduMap.hideInfoWindow();
            }
        });
        ImageButton setEndBtn = relativeLayout.findViewById(R.id.set_end);
        setEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndPoi(inputPoi);
                mBaiduMap.hideInfoWindow();
            }
        });
        InfoWindow mInfoWindow = new InfoWindow(relativeLayout, inputPoi, -47);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
        return true;
    }
    public void setStartPoi(LatLng poi){
        OverlayOptions options = new MarkerOptions().position(poi).icon(startDesc);
        if (startOverlay != null){
            startOverlay.remove();
        }
        startOverlay = mBaiduMap.addOverlay(options);
        startPoi = poi;
    }
    public void setEndPoi(LatLng poi){
        OverlayOptions options = new MarkerOptions().position(poi).icon(endDesc);
        if (endOverlay != null){
            endOverlay.remove();
        }
        endOverlay = mBaiduMap.addOverlay(options);
        endPoi = poi;
    }

    /**
     * 获取权限
     */
    public void getPermission(){
        String [] permissions = new String[]{Manifest.permission.READ_SYNC_SETTINGS,Manifest.permission.WRITE_SETTINGS
                ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                ,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE
                ,Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.READ_PHONE_STATE
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET
                ,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA};
        requestPermissions(permissions,1);
    }

    /**
     * 获取权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                initAfterPermission();

                break;
            default:
                finish();
                break;
        }
    }

    public void initAfterPermission(){
        initMap();
        initComponents();
        initLocationTool();
        initSearchComplete();
    }

    /**
     * 设置中心点，城市，缩放比例
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
        OverlayOptions option = new MarkerOptions()
                .position(this.centerPoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka));
        mBaiduMap.addOverlay(option);
        startPoi = centerPoint;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.face_login:
                intent = new Intent(getApplicationContext(),FaceLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.bus_search_item:
                intent = new Intent(getApplicationContext(),BusSearchActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
                break;
            case R.id.subway:
                intent = new Intent(getApplicationContext(),SubwayActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
                break;
            case R.id.weather:
                intent = new Intent(getApplicationContext(), WeatherActivity.class);
                intent.putExtra("city",city);
                startActivity(intent);
                break;
            case R.id.history_today:
                intent = new Intent(getApplicationContext(), HistoryTodayActivity.class);
                startActivity(intent);
                break;
            case R.id.step_item:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        MapStatus mMapStatus;
        MapStatusUpdate mMapStatusUpdate;
        Intent intent;
        switch (v.getId()){
            case R.id.satellite_btn:
                if (mapType == BaiduMap.MAP_TYPE_NORMAL){
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    mapType = BaiduMap.MAP_TYPE_SATELLITE;
                }else {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    mapType = BaiduMap.MAP_TYPE_NORMAL;
                }
                break;
            case R.id.heat_btn:
                mBaiduMap.setBaiduHeatMapEnabled(!mBaiduMap.isBaiduHeatMapEnabled());
                break;
            case R.id.traffic_btn:
                Boolean bool = mBaiduMap.isTrafficEnabled();
                mBaiduMap.setTrafficEnabled(!bool);
                break;
            case R.id.plus_btn:
                if (zoomLevel < 20){
                    zoomLevel += 1;
                }
                mMapStatus = new MapStatus.Builder().zoom(zoomLevel).build();
                mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(mMapStatusUpdate);
                break;
            case R.id.minus_btn:
                if (zoomLevel > 1 ){
                    zoomLevel -= 1;
                }
                mMapStatus = new MapStatus.Builder().zoom(zoomLevel).build();
                mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                mBaiduMap.setMapStatus(mMapStatusUpdate);
                break;
            case R.id.way_btn:
                if (endPoi == null){
                    Toast toast = Toast.makeText(getApplicationContext(),"请输入目标位置！",Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                intent = new Intent(getApplicationContext(),RouteResultActivity.class);
                ParcelableMapData parcelableMapData = new ParcelableMapData(mBaiduMap,city,centerPoint,zoomLevel,startPoi,endPoi);
                intent.putExtra("map", parcelableMapData);
                startActivity(intent);
                break;
            case R.id.nav_btn:
                if (endPoi == null){
                    Toast.makeText(getApplicationContext(),"未确定目标地点!",Toast.LENGTH_SHORT).show();
                }
                initMapGuide();
                break;
            case R.id.near_btn:
                intent = new Intent(getApplicationContext(),NearBuildingActivity.class);
                intent.putExtra("center",centerPoint);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return ;
            //未找到相关结果
        }
        sugStrList = new ArrayList<>();
        sugPoiList = new ArrayList<>();
//        arrayAdapter.clear();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                sugStrList.add(info.key);
                sugPoiList.add(info.pt);
                arrayAdapter.add(info.key);
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }

    /**
     * 定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            setCenter(latLng,location.getCity());
            mLocationClient.stop();
        }
    }

    /**
     * 初始化地图属性
     */
    private void initMap() {
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
        mBaiduMap.setOnMapClickListener(ShowMapActivity.this);
    }

    /**
     * 初始化界面控件及事件响应
     */
    private void initComponents(){
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5ae49d2d");
        //登录
        View navView = navigationView.inflateHeaderView(R.layout.left_head_layout);
        ImageView user_pic = (ImageView) navView.findViewById(R.id.land);
        ImageButton imageButton = navView.findViewById(R.id.nav_set_btn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
//                startActivity(intent);
            }
        });
        user_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),dengluActivity.class);
                startActivity(intent);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSpeechRecognizer();
            }
        });
        satelliteBtn.setOnClickListener(this);
        heatBtn.setOnClickListener(this);
        trafficBtn.setOnClickListener(this);
        plusBtn.setOnClickListener(this);
        minusBtn.setOnClickListener(this);
        //路线规划
        wayBtn.setOnClickListener(this);
        //导航
        navBtn.setOnClickListener(this);
        initListener = new InitListener() {
            @Override
            public void onInit(int i) {

            }
        };
        // 语音识别返回
        recognizerDialogListener = new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                Gson gson = new Gson();
                SpeechDo speechDo = gson.fromJson(recognizerResult.getResultString(),SpeechDo.class);
                for (SpeechDo.SpeechV2 speechV2 : speechDo.ws){
                    for (SpeechDo.SpeechV3 speechV3 : speechV2.cw){
                        speechBuilder.append(speechV3.w);
                    }
                }
                if (speechDo.ls){
                    enAutoTw.setText(speechBuilder.toString());
                }
            }

            @Override
            public void onError(SpeechError speechError) {

            }
        };
        nearBtn.setOnClickListener(this);
    }

    /**
     * 初始化定位
     */
    private void initLocationTool(){
        locationListener = new MyLocationListener();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(locationListener);
        LocationClientOption option = new LocationClientOption();
        // 设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        // 设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        int span = 1000;
        // 即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        // 设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        // 设置是否使用gps
        option.setOpenGps(true);
        // 设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        // 设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        // 设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        // 定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        // 设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        // 设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
        //开启定位
        mLocationClient.start();
    }

    /**
     * 初始化地点提示部分
     */
    public void initSearchComplete(){
        inputSugData = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item, inputSugData);
        enAutoTw.setAdapter(arrayAdapter);
        // 设置提示开始长度
        enAutoTw.setThreshold(1);
        enAutoTw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 0) {
                    return;
                }
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(editable.toString()).city(city));
            }
        });
        enAutoTw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                endPoi = sugPoiList.get(position);
                setEndPoi(endPoi);
            }
        });
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }

    /**
     * 初始化语音识别
     */
    public void loadSpeechRecognizer() {
        speechBuilder = new StringBuilder();
        ///1.创建 RecognizerDialog 对象
        RecognizerDialog mDialog = new RecognizerDialog(this, initListener);
        //若要将 RecognizerDialog 用于语义理解，必须添加以下参数设置，设置之后 onResult 回调返回将是语义理解的结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "3.0");
        mDialog.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mDialog.setParameter(SpeechConstant. ACCENT, "mandarin" );
        //3.设置回调接口
        mDialog.setListener( recognizerDialogListener );
        //4.显示 dialog，接收语音输入
        mDialog.show();
    }

    private void initMapGuide(){
        try {
            mNaviHelper = BikeNavigateHelper.getInstance();
            mWNaviHelper = WalkNavigateHelper.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        param = new BikeNaviLaunchParam().stPt(centerPoint).endPt(endPoi);
        walkParam = new WalkNaviLaunchParam().stPt(centerPoint).endPt(endPoi);
        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowMapActivity.this);
        //    设置Title的图标
//        builder.setIcon(R.drawable.ic_launcher);
        //    设置Title的内容
        builder.setTitle("选择导航方式");
        //    设置一个PositiveButton
        builder.setPositiveButton("骑车", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startBikeNavi();
            }
        });
        //    设置一个NegativeButton
        builder.setNegativeButton("步行", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startWalkNavi();
            }
        });
        //    设置一个NeutralButton
        builder.setNeutralButton("返回", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
//                finish();
                return;
            }
        });
        //    显示出该对话框
        builder.show();
    }

    private void startBikeNavi() {
        Log.d("View", "startBikeNavi");
        try {
            mNaviHelper.initNaviEngine(this, new IBEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d("View", "engineInitSuccess");
                    routePlanWithParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d("View", "engineInitFail");
                }
            });
        } catch (Exception e) {
            Log.d("Exception", "startBikeNavi");
            e.printStackTrace();
        }
    }

    private void startWalkNavi() {
        Log.d("View", "startBikeNavi");
        try {
            mWNaviHelper.initNaviEngine(this, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d("View", "engineInitSuccess");
                    routePlanWithWalkParam();
                }

                @Override
                public void engineInitFail() {
                    Log.d("View", "engineInitFail");
                }
            });
        } catch (Exception e) {
            Log.d("Exception", "startBikeNavi");
            e.printStackTrace();
        }
    }

    private void routePlanWithParam() {
        mNaviHelper.routePlanWithParams(param, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d("View", "onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("View", "onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), BNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError error) {
                Log.d("View", "onRoutePlanFail");
            }

        });
    }

    private void routePlanWithWalkParam() {
        mWNaviHelper.routePlanWithParams(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d("View", "onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("View", "onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d("View", "onRoutePlanFail");
            }

        });
    }
}
