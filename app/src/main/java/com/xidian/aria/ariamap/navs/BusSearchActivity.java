package com.xidian.aria.ariamap.navs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.xidian.aria.ariamap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 公交信息检索
 */
public class BusSearchActivity extends AppCompatActivity implements OnGetBusLineSearchResultListener{

    ImageButton searchBtn = null;
    EditText searchBusTxt = null;
    String city = null;
    BusLineSearch mSearch = null;
    PoiSearch poiSearch = null;
    ArrayAdapter<String > arrayAdapter;
    List<String > busList;
    ListView busListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search);
        searchBtn = findViewById(R.id.bus_search_btn);
        searchBusTxt = findViewById(R.id.bus_search_edt);
        getIntentData();
        mSearch = BusLineSearch.newInstance();
        mSearch.setOnGetBusLineSearchResultListener(BusSearchActivity.this);
        poiSearch = PoiSearch.newInstance();
        busList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,busList);
        busListView = findViewById(R.id.bus_lv);
        busListView.setAdapter(arrayAdapter);
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                List<PoiInfo> infos = poiResult.getAllPoi();
                for (PoiInfo info : infos){
                    if (info.type == PoiInfo.POITYPE.BUS_LINE){
                        busList = new ArrayList<>();
                        mSearch.searchBusLine(new BusLineSearchOption().city(city).uid(info.uid));
                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String line = searchBusTxt.getText().toString();
                if (line.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"请输入公交线路！",Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    poiSearch.searchInCity(new PoiCitySearchOption().city(city).keyword(line));
                }
            }
        });
    }

    @Override
    public void onGetBusLineResult(BusLineResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast toast = Toast.makeText(getApplicationContext(),"未找到相应公交，请重新输入!",Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("首班时间：").append(result.getStartTime()).append("\n");
        builder.append("末班时间：").append(result.getEndTime()).append("\n");
        for (BusLineResult.BusStation station : result.getStations()){
            builder.append(station.getTitle()).append("\n");
        }
        busList.add(builder.toString());
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,busList);
        busListView.setAdapter(arrayAdapter);
    }
    public void getIntentData(){
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
    }
}
