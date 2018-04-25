package com.xidian.aria.ariamap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class gerenzhongxinActivity extends AppCompatActivity {
    private Button mReturnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenzhongxin);
        mReturnButton = (Button)findViewById(R.id.returnback);
    }
    public void back_to_login(View view) {
        Intent intent3 = new Intent(gerenzhongxinActivity.this,dengluActivity.class) ;
        startActivity(intent3);
        finish();
    }
}

