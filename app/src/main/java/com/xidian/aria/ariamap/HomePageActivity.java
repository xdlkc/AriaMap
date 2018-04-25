package com.xidian.aria.ariamap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Button go = (Button) this.findViewById(R.id.go);
        final RadioGroup group = (RadioGroup) findViewById(R.id.radioGroup);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start= ((EditText) findViewById(R.id.start)).getText().toString();
                String end = ((EditText) findViewById(R.id.end)).getText().toString();
                String city = ((EditText) findViewById(R.id.city)).getText().toString();
                int id = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(id);

                Intent intent = new Intent(getApplicationContext(),ShowMapActivity.class);
                intent.putExtra("city",city);
                intent.putExtra("start",start);
                intent.putExtra("end",end);
                startActivity(intent);

            }
        });


    }
}
