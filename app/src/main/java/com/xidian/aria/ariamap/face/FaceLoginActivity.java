package com.xidian.aria.ariamap.face;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.IdentityListener;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.IdentityVerifier;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.xidian.aria.ariamap.R;
import com.xidian.aria.ariamap.ShowMapActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class FaceLoginActivity extends Activity implements View.OnClickListener {
    private final String TAG = "FaceLoginActivity";
    private final int REQUEST_PICTURE_CHOOSE = 1;
    private final int REQUEST_CAMERA_IMAGE = 2;

    private Bitmap mImage = null;
    private byte[] mImageData = null;
    // authid为6-18个字符长度，用于唯一标识用户
    private String mAuthid = null;
    private Toast mToast;
    // 进度对话框
    private ProgressDialog mProDialog;
    private EditText online_authid;
    // 拍照得到的照片文件
    private File mPictureFile;
    // FaceRequest对象，集成了人脸识别的各种功能
    //private FaceRequest mFaceRequest;

    //采用身份识别接口进行在线人脸识别
    private IdentityVerifier mIdVerifier;

    // 模型操作
    private int mModelCmd;
    // 删除模型
    private final static int MODEL_DEL = 1;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.face_login);
        findViewById(R.id.online_pick).setOnClickListener(FaceLoginActivity.this);
        findViewById(R.id.online_reg).setOnClickListener(FaceLoginActivity.this);
        findViewById(R.id.online_verify).setOnClickListener(FaceLoginActivity.this);
        findViewById(R.id.online_camera).setOnClickListener(FaceLoginActivity.this);
        findViewById(R.id.btn_modle_delete).setOnClickListener(FaceLoginActivity.this);
        online_authid = (EditText) findViewById(R.id.online_authid);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");

        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作
                if (null != mIdVerifier) {
                    mIdVerifier.cancel();
                }
            }
        });

        mIdVerifier = IdentityVerifier.createVerifier(FaceLoginActivity.this, new InitListener() {
            @Override
            public void onInit(int errorCode) {
                if (ErrorCode.SUCCESS == errorCode) {
                    showTip("引擎初始化成功");
                } else {
                    showTip("引擎初始化失败，错误码：" + errorCode);
                }
            }
        });
    }

    /**
     * 人脸注册监听器
     */
    private IdentityListener mEnrollListener = new IdentityListener() {

        @Override
        public void onResult(IdentityResult result, boolean islast) {
            Log.d(TAG, result.getResultString());

            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            try {
                JSONObject object = new JSONObject(result.getResultString());
                int ret = object.getInt("ret");

                if (ErrorCode.SUCCESS == ret) {
                    showTip("注册成功");
                }else {
                    showTip(new SpeechError(ret).getPlainDescription(true));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }

        @Override
        public void onError(SpeechError error) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            showTip(error.getPlainDescription(true));
        }

    };

    /**
     * 人脸验证监听器
     */
    private IdentityListener mVerifyListener = new IdentityListener() {

        @Override
        public void onResult(IdentityResult result, boolean islast) {
            Log.d(TAG, result.getResultString());

            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            try {
                JSONObject object = new JSONObject(result.getResultString());
                Log.d(TAG,"object is: "+object.toString());
                String decision = object.getString("decision");

                if ("accepted".equalsIgnoreCase(decision)) {
                    showTip("通过验证");
                    Intent intent = new Intent();
                    String id = online_authid.getText().toString();
                    intent.putExtra("auth_id",id);
                    setResult(0,intent);
                    finish();
                } else {
                    showTip("验证失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }

        @Override
        public void onError(SpeechError error) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            showTip(error.getPlainDescription(true));
        }

    };

    /**
     * 人脸模型操作监听器
     */
    private IdentityListener mModelListener = new IdentityListener() {

        @Override
        public void onResult(IdentityResult result, boolean islast) {
            Log.d(TAG, result.getResultString());

            JSONObject jsonResult = null;
            int ret = ErrorCode.SUCCESS;
            try {
                jsonResult = new JSONObject(result.getResultString());
                ret = jsonResult.getInt("ret");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 根据操作类型判断结果类型
            switch (mModelCmd) {
                case MODEL_DEL:
                    if (ErrorCode.SUCCESS == ret) {
                        online_authid.setEnabled(true);
                        showTip("删除成功");
                    } else {
                        showTip("删除失败");
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }

        @Override
        public void onError(SpeechError error) {
            // 弹出错误信息
            showTip(error.getPlainDescription(true));
        }

    };



    private void executeModelCommand(String cmd) {
        // 设置人脸模型操作参数
        // 清空参数
        mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
        // 设置会话场景
        mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
        // 用户id
        mIdVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthid);

        // 设置模型参数，若无可以传空字符传
        StringBuffer params = new StringBuffer();
        // 执行模型操作
        mIdVerifier.execute("ifr", cmd, params.toString(), mModelListener);
    }

    @Override
    public void onClick(View view) {
        int ret = ErrorCode.SUCCESS;
        mAuthid = online_authid.getText().toString();
        if(TextUtils.isEmpty(mAuthid)) {
            showTip("请输入用户ID");
            return;
        }else {
            online_authid.setEnabled(false);
        }

        switch (view.getId()) {
            case R.id.online_pick:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
                break;
            case R.id.online_reg:
                if (TextUtils.isEmpty(mAuthid)) {
                    showTip("authid不能为空");
                    return;
                }

                if (null != mImageData) {
                    mProDialog.setMessage("注册中...");
                    mProDialog.show();
                    // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                    // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                    // 设置人脸注册参数
                    // 清空参数
                    mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
                    // 设置会话场景
                    mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
                    // 设置会话类型
                    mIdVerifier.setParameter(SpeechConstant.MFV_SST, "enroll");
                    // 设置用户id
                    mIdVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    // 设置监听器，开始会话
                    mIdVerifier.startWorking(mEnrollListener);

                    // 子业务执行参数，若无可以传空字符传
                    StringBuffer params = new StringBuffer();
                    // 向子业务写入数据，人脸数据可以一次写入
                    mIdVerifier.writeData("ifr", params.toString(), mImageData, 0, mImageData.length);
                    // 停止写入
                    mIdVerifier.stopWrite("ifr");
                } else {
                    showTip("请选择图片后再注册");
                }
                break;
            case R.id.online_verify:
                mAuthid = ((EditText) findViewById(R.id.online_authid)).getText().toString();
                if (TextUtils.isEmpty(mAuthid)) {
                    showTip("authid不能为空");
                    return;
                }

                if (null != mImageData) {
                    mProDialog.setMessage("验证中...");
                    mProDialog.show();
                    // 设置人脸验证参数
                    // 清空参数
                    mIdVerifier.setParameter(SpeechConstant.PARAMS, null);
                    // 设置会话场景
                    mIdVerifier.setParameter(SpeechConstant.MFV_SCENES, "ifr");
                    // 设置会话类型
                    mIdVerifier.setParameter(SpeechConstant.MFV_SST, "verify");
                    // 设置验证模式，单一验证模式：sin
                    mIdVerifier.setParameter(SpeechConstant.MFV_VCM, "sin");
                    // 用户id
                    mIdVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    // 设置监听器，开始会话
                    mIdVerifier.startWorking(mVerifyListener);

                    // 子业务执行参数，若无可以传空字符传
                    StringBuffer params = new StringBuffer();
                    // 向子业务写入数据，人脸数据可以一次写入
                    mIdVerifier.writeData("ifr", params.toString(), mImageData, 0, mImageData.length);
                    // 停止写入
                    mIdVerifier.stopWrite("ifr");
                } else {
                    showTip("请选择图片后再验证");
                }
                break;

            case R.id.online_camera:
                // 设置相机拍照后照片保存路径
                mPictureFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/face/" + System.currentTimeMillis() + ".jpg");
                mPictureFile.getParentFile().mkdirs();
                // 启动拍照,并保存到临时文件
                Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //改变Uri
                Uri uri = FileProvider.getUriForFile(this, "com.xidian.aria.ariamap.fileprovider", mPictureFile);
                //添加权限
                mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);
                break;
            case R.id.btn_modle_delete:
                // 人脸模型删除
                mModelCmd = MODEL_DEL;
                executeModelCommand("delete");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        String fileSrc = null;
        if (requestCode == REQUEST_PICTURE_CHOOSE) {
            if ("file".equals(data.getData().getScheme())) {
                // 有些低版本机型返回的Uri模式为file
                fileSrc = data.getData().getPath();
            } else {
                // Uri模型为content
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), proj,
                        null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileSrc = cursor.getString(idx);
                cursor.close();
            }
            crop_picture(fileSrc);
        } else if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (null == mPictureFile) {
                showTip("拍照失败，请重试");
                return;
            }
            fileSrc = mPictureFile.getAbsolutePath();
            updateGallery(fileSrc);
            crop_picture(fileSrc);
        }

    }

    @Override
    public void finish() {
        if (null != mProDialog) {
            mProDialog.dismiss();
        }
        super.finish();
    }

    private void updateGallery(String filename) {
        // 将图片扫描至媒体库
        MediaScannerConnection.scanFile(this, new String[] {filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
    public void crop_picture(String fileSrc){
        // 获取图片的宽和高
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 压缩图片
        options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                (double) options.outWidth / 1024f,
                (double) options.outHeight / 1024f)));
        options.inJustDecodeBounds = false;
        mImage = BitmapFactory.decodeFile(fileSrc, options);
        // 若mImageBitmap为空则图片信息不能正常获取
        if(null == mImage) {
            showTip("图片信息无法正常获取！");
            return;
        }
        // 部分手机会对图片做旋转，这里检测旋转角度
        int degree = FaceUtil.readPictureDegree(fileSrc);
        if (degree != 0) {
            // 把图片旋转为正的方向
            mImage = FaceUtil.rotateImage(degree, mImage);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //可根据流量及网络状况对图片进行压缩
        mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        mImageData = baos.toByteArray();
        ((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);

    }
}
