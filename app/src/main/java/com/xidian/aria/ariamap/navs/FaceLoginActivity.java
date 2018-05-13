package com.xidian.aria.ariamap.navs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xidian.aria.ariamap.R;

import java.io.File;

public class FaceLoginActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_CAMERA_IMAGE = 1;// 拍照
    public static final int CROP_PHOTO = 2;
    private Button takePhoto;
    private ImageView picture;
    private String fileSrc;
    public static File mPictureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_login);
        takePhoto = findViewById(R.id.take_photo);
        picture = findViewById(R.id.picture);
        takePhoto.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                break;
        }
    }



}
