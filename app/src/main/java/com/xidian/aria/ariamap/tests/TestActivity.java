package com.xidian.aria.ariamap.tests;

import android.app.Activity;
import android.content.Context;
import android.speech.RecognitionListener;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xidian.aria.ariamap.R;

import java.util.ArrayList;

import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

public class TestActivity extends Activity{
    private Button btn;
    InitListener initListener;
    RecognizerDialogListener recognizerDialogListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSpeech();
            }
        });

    }
    /**
     * 初始化语音识别
     */
    public void initSpeech() {


        ///1.创建 RecognizerDialog 对象
        RecognizerDialog mDialog = new RecognizerDialog(this, initListener);

        //若要将 RecognizerDialog 用于语义理解，必须添加以下参数设置，设置之后 onResult 回调返回将是语义理解的结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "3.0");

        //3.设置回调接口
        mDialog.setListener( recognizerDialogListener );

        //4.显示 dialog，接收语音输入
        mDialog.show();
    }

}
