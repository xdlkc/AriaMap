package com.xidian.aria.ariamap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

import java.util.ArrayList;
import java.util.List;

public class NearBuildingActivity extends AppCompatActivity {
    private TextInputEditText radiusEdt;
    private LinearLayout foodLayout;
    private LinearLayout cinemaLayout;
    private LinearLayout marketLayout;
    private LinearLayout hotelLayout;
    private LinearLayout hospitalLayout;
    private LinearLayout otherLayout;
    private ListView nearLV;
    private ArrayAdapter<String> arrayAdapter;
    private List<String > dataList;
    private android.support.v7.widget.GridLayout gridLayout;
    private PoiSearch poiSearch;
    private LatLng centerPoi;
    private OnGetPoiSearchResultListener poiSearchResultListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_building);
        gridLayout = findViewById(R.id.grid_lay);
        foodLayout = findViewById(R.id.food_layout);
        cinemaLayout = findViewById(R.id.cinema_layout);
        marketLayout = findViewById(R.id.market_layout);
        hotelLayout = findViewById(R.id.hotel_layout);
        hospitalLayout = findViewById(R.id.hospital_layout);
        otherLayout = findViewById(R.id.other_layout);
        radiusEdt = findViewById(R.id.radius_edt);
        nearLV = findViewById(R.id.near_lv);
        dataList = new ArrayList<>();
        Intent intent = getIntent();

        centerPoi = intent.getParcelableExtra("center");
        System.out.println(centerPoi);
        poiSearchResultListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult.getAllPoi() == null){
                    Toast.makeText(getApplicationContext(),"未找到相关建筑...请扩大范围",Toast.LENGTH_LONG).show();
                    return;
                }
                dataList = new ArrayList<>();
                for (PoiInfo info : poiResult.getAllPoi()){
                    dataList.add(info.name+",地址："+info.address);
                }
                arrayAdapter = new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,dataList);
                nearLV.setAdapter(arrayAdapter);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(poiSearchResultListener);
        arrayAdapter = new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,dataList);
        nearLV.setAdapter(arrayAdapter);
        foodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPoiByKeyword("餐厅");
            }
        });
        cinemaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPoiByKeyword("KTV");
            }
        });
        marketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPoiByKeyword("超市");
            }
        });
        hotelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPoiByKeyword("酒店");
            }
        });
        hospitalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPoiByKeyword("医院");
            }
        });
        otherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPoiByKeyword("待定");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        poiSearch.destroy();
    }

    public void searchPoiByKeyword(String key){
        int radius;
        String r = radiusEdt.getText().toString();
        if (r.equals("")){
            radius = 5000;
        }else {
            radius = Integer.parseInt(r);
        }
        poiSearch.searchNearby(new PoiNearbySearchOption()
                .keyword(key)
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(centerPoi)
                .radius(radius)
                .pageNum(10));
    }
}
