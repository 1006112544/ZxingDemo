package com.daobao.asus.zxingdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * Created by db on 2018/6/28.
 */

public class CreateQrcodeActivity extends AppCompatActivity{
    private EditText mEditText;
    private ImageView mImageView;
    private Bitmap mBitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qrcode);
        mEditText = findViewById(R.id.mEditText);
        mImageView = findViewById(R.id.img_qrcode);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.create_btn1:
                String textContent1 = mEditText.getText().toString();
                if (TextUtils.isEmpty(textContent1)) {
                    Toast.makeText(this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mEditText.setText("");
                mBitmap = CodeUtils.createImage(textContent1, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                mImageView.setImageBitmap(mBitmap);
                break;
            case R.id.create_btn2:
                String textContent2 = mEditText.getText().toString();
                if (TextUtils.isEmpty(textContent2)) {
                    Toast.makeText(this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mEditText.setText("");
                mBitmap = CodeUtils.createImage(textContent2, 400, 400, null);
                mImageView.setImageBitmap(mBitmap);
                break;
            case R.id.create_btn3:
                String textContent3 = mEditText.getText().toString();
                if (TextUtils.isEmpty(textContent3)) {
                    Toast.makeText(this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Integer.valueOf(textContent3);
                }catch (Exception e){
                    Toast.makeText(this,"条形码只能是数字",Toast.LENGTH_SHORT).show();
                    return;
                }
                mEditText.setText("");
                mBitmap = BarCodeUtils.creatBarcode(textContent3,300,50);
                mImageView.setImageBitmap(mBitmap);
                break;
        }
    }

}
