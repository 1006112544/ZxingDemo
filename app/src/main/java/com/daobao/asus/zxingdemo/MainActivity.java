package com.daobao.asus.zxingdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yanzhenjie.permission.AndPermission;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button Qrcode;
    private Button ImgQrcode;
    private static final int REQUEST_CODE = 959;
    private static final int REQUEST_IMAGE = 960;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZXingLibrary.initDisplayOpinion(this);
        Qrcode = findViewById(R.id.imgv_qrcode);
        ImgQrcode = findViewById(R.id.imgv_img_qrcode);
        Qrcode.setOnClickListener(this);
        ImgQrcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgv_qrcode:
                // 先判断是否有权限。
                if(AndPermission.hasPermission(this, Manifest.permission.CAMERA)) {
                    Intent intent = new Intent(MainActivity.this, QrcodeActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    // 申请权限。
                    AndPermission.with(this)
                            .requestCode(100)
                            .permission(Manifest.permission.CAMERA)
                            .send();
                }
                break;
            case R.id.imgv_img_qrcode:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE);
                } else {
                    Toast.makeText(this, "打开相册失败", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
            break;
            case REQUEST_IMAGE:
                if (data != null) {
                    Uri uri = data.getData();
                    ContentResolver cr = getContentResolver();
                    try {
                        Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片
                        CodeUtils.analyzeBitmap(mBitmap, new CodeUtils.AnalyzeCallback() {
                            @Override
                            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onAnalyzeFailed() {
                                Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                            }
                        });

                        if (mBitmap != null) {
                            mBitmap.recycle();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
