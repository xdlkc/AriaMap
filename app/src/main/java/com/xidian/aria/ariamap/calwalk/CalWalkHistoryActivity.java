package com.xidian.aria.ariamap.calwalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xidian.aria.ariamap.R;
import com.xidian.aria.ariamap.adapters.CommonAdapter;
import com.xidian.aria.ariamap.adapters.CommonViewHolder;
import com.xidian.aria.ariamap.dao.StepDo;
import com.xidian.aria.ariamap.utils.DbUtils;

import java.util.List;


public class CalWalkHistoryActivity extends AppCompatActivity {
    private LinearLayout layout_titlebar;
    private ImageView iv_left;
    private ImageView iv_right;
    private ListView lv;

    private void assignViews() {
        layout_titlebar = (LinearLayout) findViewById(R.id.layout_titlebar);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        lv = (ListView) findViewById(R.id.lv);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_history);
        assignViews();
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        initData();
    }

    private void initData() {
        setEmptyView(lv);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "calwalk");
        }
        List<StepDo> stepDos =DbUtils.getQueryAll(StepDo.class);
        Log.d("","stepDos="+ stepDos);
        lv.setAdapter(new CommonAdapter<StepDo>(this, stepDos,R.layout.walk_step_item) {
            @Override
            protected void convertView(View item, StepDo stepDo) {
                TextView tv_date= CommonViewHolder.get(item,R.id.tv_date);
                TextView tv_step= CommonViewHolder.get(item,R.id.tv_step);
                tv_date.setText(stepDo.getToday());
                tv_step.setText(stepDo.getStep()+"步");
            }
        });
    }

    protected <T extends View> T setEmptyView(ListView listView) {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
    }
}
