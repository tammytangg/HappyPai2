package com.example.tammy.happypai2.effect;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tammy.happypai2.CameraActivity;
import com.example.tammy.happypai2.CameraResultActivity;
import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.util.Util;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;

public class EffectActivity extends AppCompatActivity implements View.OnClickListener{


    ImageView imageView, iv_original;
    ImageButton ibt_back,ibt_sure,bt_compare;
    Button bt_edit, bt_strengthen, bt_effect, bt_clear;

    private GPUImage gpuImage;

    private String path;
    private String pathTemp;
    private Bitmap bm;
    private Bitmap bm_effect;

    private final int EDIT=0;
    private final int STRENGTHEN=1;
    private final int EFFECT=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);

        Intent intent=getIntent();
        path=intent.getStringExtra("path");
        bm = BitmapFactory.decodeFile(path);

        Matrix matrix = new Matrix();
        matrix.setScale(0.4f, 0.4f);
        bm_effect = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                bm.getHeight(), matrix, true);
        bm = bm_effect;
        String fileName = "temp_effect.png";
        pathTemp = Util.saveImage(getApplicationContext(),bm_effect,fileName);

        initView();

        imageView.setImageBitmap(bm_effect);
        iv_original.setImageBitmap(bm_effect);



    }

    private void initView(){
        imageView = (ImageView) findViewById(R.id.iv_effect_pic);
        iv_original = (ImageView)findViewById(R.id.iv_original_pic);
        ibt_back = (ImageButton)findViewById(R.id.bt_back);
        ibt_sure = (ImageButton)findViewById(R.id.bt_sure);
        bt_edit = (Button)findViewById(R.id.bt_edit);
        bt_strengthen = (Button)findViewById(R.id.bt_strengthen);
        bt_effect = (Button)findViewById(R.id.bt_effect);
        bt_clear = (Button)findViewById(R.id.bt_clear);
        bt_compare = (ImageButton)findViewById(R.id.bt_compare);

        ibt_back.setOnClickListener(this);
        ibt_sure.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        bt_strengthen.setOnClickListener(this);
        bt_effect.setOnClickListener(this);
        bt_clear.setOnClickListener(this);

        bt_compare.bringToFront();

        bt_compare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    iv_original.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    iv_original.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_sure:
                Util.saveImageToGallery(getApplicationContext(),bm_effect);
                Util.dialog("info","Saved Successfully!",this,this);
                break;
            case R.id.bt_edit:
                intent.setClass(this,EditActivity.class);
                intent.putExtra("path", pathTemp);
                startActivityForResult(intent,EDIT);
                break;
            case R.id.bt_strengthen:
                break;
            case R.id.bt_effect:
                intent.setClass(this,EffectFilterActivity.class);
                intent.putExtra("path",pathTemp);
                startActivityForResult(intent,EFFECT);
                break;
            case R.id.bt_clear:
                bm_effect = bm;
                imageView.setImageBitmap(bm_effect);
                String fileName = "temp_effect.png";
                pathTemp = Util.saveImage(getApplicationContext(),bm_effect,fileName);
                break;
            default:
                break;
        }
    }

//    public void testGPUImage(){
//        // 使用GPUImage处理图像
//        gpuImage = new GPUImage(this);
//        gpuImage.setImage(bm_effect);
//        gpuImage.setFilter(new GPUImageGrayscaleFilter());
//        bm_effect = gpuImage.getBitmapWithFilterApplied();
//        //显示处理后的图片
//        imageView.setImageBitmap(bm_effect);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == EDIT && resultCode == RESULT_OK) {
            Log.v("test","test_edit");
            bm_effect = BitmapFactory.decodeFile(pathTemp);
            imageView.setImageBitmap(bm_effect);
        }else if(requestCode == EFFECT && resultCode == RESULT_OK){
            Log.v("test","test_effect");
            bm_effect = BitmapFactory.decodeFile(pathTemp);
            imageView.setImageBitmap(bm_effect);
        }
    }


}
