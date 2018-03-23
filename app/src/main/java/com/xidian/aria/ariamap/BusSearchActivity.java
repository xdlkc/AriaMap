package com.xidian.aria.ariamap;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.SearchResult;

/**
 * 公交信息检索
 */
public class BusSearchActivity extends AppCompatActivity implements OnGetBusLineSearchResultListener{

    ImageButton searchBtn = null;
    EditText searchBusTxt = null;
    String city = null;
    BusLineSearch mSearch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search);
        searchBtn = findViewById(R.id.bus_search_btn);
        searchBusTxt = findViewById(R.id.bus_search_edt);
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        mSearch = BusLineSearch.newInstance();
        mSearch.setOnGetBusLineSearchResultListener(BusSearchActivity.this);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String line = searchBusTxt.getText().toString();
                if (line.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"请输入公交线路！",Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    mSearch.searchBusLine(new BusLineSearchOption().city(city).uid(line));

                }
            }
        });
    }

    @Override
    public void onGetBusLineResult(BusLineResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            System.out.println(result.error);
            Toast toast = Toast.makeText(getApplicationContext(),"未找到相应公交，请重新输入!",Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        System.out.println(result.getStartTime());
    }
}
