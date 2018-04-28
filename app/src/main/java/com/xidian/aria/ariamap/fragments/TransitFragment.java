package com.xidian.aria.ariamap.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption.TacticsIncity;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.xidian.aria.ariamap.R;
import com.xidian.aria.ariamap.SerializableBaiduMap;
import com.xidian.aria.ariamap.listeners.MyOnGetRoutePlanResultListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitFragment extends Fragment {
    private MapView transitMapView = null;
    private BaiduMap massTransitMap = null;
    private MassTransitRouteResult massTransitRouteResult = null;
    private int zoomLevel;
    private LatLng startPoi;
    private LatLng endPoi;
    // 城市
    private String city;
    // 中心点
    private LatLng center;
    private Spinner spinner;
    private RoutePlanSearch search;
    private OnGetRoutePlanResultListener onGetRoutePlanResultListener;
    private ArrayAdapter<String > arrayAdapter;
    private List<String > ways;
    private PlanNode startNode;
    private PlanNode endNode;
    private Map<String , MassTransitRoutePlanOption.TacticsIncity> policyMap;
    private ListView listView;
    private List<String > routeList;
    private ArrayAdapter<String > routeAdapter;
    public static TransitFragment newInstance(SerializableBaiduMap map){
        TransitFragment fragment = new TransitFragment();
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
            massTransitMap = serializableBaiduMap.getMap();
            startNode = PlanNode.withLocation(startPoi);
            endNode = PlanNode.withLocation(endPoi);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pager_bus_page,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        policyMap = new HashMap<>();
        ways = new ArrayList<>();
        spinner = getView().findViewById(R.id.spinner);
        listView = getView().findViewById(R.id.route_lv);
        routeList = new ArrayList<>();
        routeAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,routeList);
        listView.setAdapter(routeAdapter);
        search = RoutePlanSearch.newInstance();
        policyMap.put("推荐", TacticsIncity.ETRANS_SUGGEST);
        policyMap.put("少换乘",TacticsIncity.ETRANS_LEAST_TRANSFER);
        policyMap.put("少步行",TacticsIncity.ETRANS_LEAST_WALK);
        policyMap.put("不坐地铁",TacticsIncity.ETRANS_NO_SUBWAY);
        policyMap.put("时间短",TacticsIncity.ETRANS_LEAST_TIME);
        policyMap.put("地铁优先",TacticsIncity.ETRANS_SUBWAY_FIRST);
        for (Map.Entry<String , TacticsIncity> entry : policyMap.entrySet()){
            ways.add(entry.getKey());
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search.masstransitSearch(new MassTransitRoutePlanOption().from(startNode).to(endNode)
                        .tacticsIncity(policyMap.get(ways.get(position))));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,ways);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        search.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            }
            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            }
            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
                routeList = new ArrayList<>();
                for (MassTransitRouteLine line : massTransitRouteResult.getRouteLines()){
                    StringBuilder builder = new StringBuilder();
                    for (List<MassTransitRouteLine.TransitStep> transitSteps : line.getNewSteps()){
                        for (MassTransitRouteLine.TransitStep step : transitSteps){
                            builder.append(step.getInstructions()).append("\n");
                        }
                    }
                    routeList.add(builder.toString());
                }
                routeAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,routeList);
                listView.setAdapter(routeAdapter);
//                routeAdapter.notifyDataSetChanged();
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

}
