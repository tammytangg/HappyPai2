package com.example.tammy.happypai2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.tammy.happypai2.util.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AskActivity extends AppCompatActivity implements View.OnClickListener,SurfaceHolder.Callback{

    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;


    private Context context = null;
    private PopupWindow popupWindow;

    private static final int IMAGE = 1;
    private static final int POSITION=2;
    private static final int CAPTURE =3;
    private int mCurrentCameraId = 0; // 1是前置 0是后置

    ImageButton bt_capture,bt_flash,bt_turn, bt_cancel;
    ImageView iv_refer;

    ImageLoader imageLoader;

    private ImageButton bt_com, bt_com_four, bt_com_six;
    private LinearLayout toolLayout;
    private ImageView iv_composition;
    private int type=-1;


    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File tempFile = new File("/sdcard/temp.png");
            try {
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();
                Intent intent = new Intent(AskActivity.this, CameraResultActivity.class);
                intent.putExtra("picPath",tempFile.getAbsolutePath());
                startActivity(intent);
//                CameraActivity.this.finish();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_ask);
        bt_capture = (ImageButton) findViewById(R.id.btCapture);
        bt_flash = (ImageButton)findViewById(R.id.bt_flash);
        bt_turn = (ImageButton)findViewById(R.id.bt_turn);
        bt_cancel = (ImageButton)findViewById(R.id.btCancel);
        iv_refer = (ImageView)findViewById(R.id.image_refer);
        bt_com = (ImageButton)findViewById(R.id.btComposition);
        bt_com_four = (ImageButton) findViewById(R.id.btn_com_four);
        bt_com_six = (ImageButton) findViewById(R.id.btn_com_six);
        toolLayout = (LinearLayout) findViewById(R.id.toolLayout);
        iv_composition = (ImageView) findViewById(R.id.iv_composition);

        bt_com.setOnClickListener(this);
        bt_com_four.setOnClickListener(this);
        bt_com_six.setOnClickListener(this);

        bt_capture.setOnClickListener(this);
        bt_flash.setOnClickListener(this);
        bt_turn.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        iv_refer.setOnClickListener(this);

        mPreview=(SurfaceView)findViewById(R.id.preview);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btCapture:
                Log.v("button_test","button_capture");
                capture();
                break;
            case R.id.image_refer:
                Log.v("button_test","image_refer");
                initPopupWindow();
                break;
            case R.id.bt_flash:
                turnLight();
                break;
            case R.id.bt_turn:
                switchCamera();
                break;
            case R.id.btCancel:
                finish();
                break;
            case R.id.btComposition:
                showToolLayout();
                break;
            case R.id.btn_com_four:
                showComposition(0);
                break;
            case R.id.btn_com_six:
                showComposition(1);
                break;
            default:
                break;
        }
    }

    private void showComposition(int mType){

        if (type == mType){
            iv_composition.setVisibility(View.INVISIBLE);
            bt_com_four.setImageResource(R.drawable.button_com_four);
            bt_com_six.setImageResource(R.drawable.button_com_six);
            type = -1;
            toolLayout.setVisibility(View.INVISIBLE);
            return;
        }

        switch (mType){
            case 0:
                iv_composition.setImageResource(R.drawable.img_com_four);
                bt_com_four.setImageResource(R.drawable.button_com_four_b);
                bt_com_six.setImageResource(R.drawable.button_com_six);
                type = 0;
                break;
            case 1:
                iv_composition.setImageResource(R.drawable.img_com_six);
                bt_com_six.setImageResource(R.drawable.button_com_six_b);
                bt_com_four.setImageResource(R.drawable.button_com_four);
                type = 1;
                break;
            default:
                break;
        }
        iv_composition.setVisibility(View.VISIBLE);
        toolLayout.setVisibility(View.INVISIBLE);
    }

    private void showToolLayout(){
        if (toolLayout.getVisibility()==View.INVISIBLE){
            toolLayout.setVisibility(View.VISIBLE);
        }else{
            toolLayout.setVisibility(View.INVISIBLE);
        }

    }

    class popupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }



    protected void initPopupWindow(){
        View popupWindowView = getLayoutInflater().inflate(R.layout.popmenu_ask, null);
        //内容，高度，宽度

        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //宽度
        //popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        //高度
//        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //显示位置


        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

        //设置背景半透明
        backgroundAlpha(0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());

        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*if( popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow=null;
                }*/
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        Button btn_takephoto = (Button)popupWindowView.findViewById(R.id.ask_btn_takephoto);
        Button btn_choose = (Button)popupWindowView.findViewById(R.id.ask_btn_choose);
        Button btn_cancel = (Button)popupWindowView.findViewById(R.id.ask_btn_cancel);


        btn_takephoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "take a photo", Toast.LENGTH_LONG).show();

                Intent intent=new Intent();
                intent.setClass(context,CameraActivity.class);
                intent.putExtra("select",true);
                startActivityForResult(intent,POSITION);

                popupWindow.dismiss();
            }
        });

        btn_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "choose a photo", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Bundle bundle=new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, IMAGE);

                popupWindow.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "cancel", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 选择完图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
//            showImage(imagePath);
            c.close();

            Intent intent=new Intent();
            intent.setClass(this,AskPositionActivity.class);
            intent.putExtra("path", imagePath);
            Bundle bundle=new Bundle();
            intent.putExtras(bundle);
            startActivityForResult(intent, POSITION);
//            startActivity(intent);

        }else if(requestCode == POSITION && resultCode == Activity.RESULT_OK){
            String path = data.getStringExtra("path");
            Log.v("path",path);
            //imageLoader.loadImage(path,iv_refer);
            showImage(path);
            Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
        }


    }

    //加载图片
    private void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        iv_refer.setImageBitmap(bm);
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void capture(){
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPreviewSize(800,480);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(success){
                    mCamera.takePicture(null,null,mPictureCallback);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCamera == null){
            mCamera = getCamera();

            if (mCamera == null){
                Log.v("error","no camera");
            }

            if (mHolder != null ){
                setStartPreview(mCamera,mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * get camera
     * @return
     */
    private Camera getCamera(){
        Camera camera;
        try {
            camera = Camera.open(0);
        }catch (Exception e){
            camera = null;
            e.printStackTrace();
        }

        return camera;
    }

    /**
     * start to show the preview
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            //set camera vertical
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * release the camera resource
     */
    private void releaseCamera(){
        if(mCamera!=null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
//            mHolder = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera,mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }


    /**
     * 闪光灯开关 开->关->自动
     */
    private void turnLight() {
        if (mCamera == null || mCamera.getParameters() == null
                || mCamera.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        String flashMode = mCamera.getParameters().getFlashMode();
        List<String> supportedModes = mCamera.getParameters()
                .getSupportedFlashModes();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {// 关闭状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(parameters);
            bt_flash.setImageResource(R.drawable.button_flash_on);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {// 开启状态
            if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
//                flashBtn.setImageResource(R.drawable.camera_flash_auto);
                mCamera.setParameters(parameters);
                bt_flash.setImageResource(R.drawable.button_flash_auto);
            } else if (supportedModes
                    .contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                flashBtn.setImageResource(R.drawable.camera_flash_off);
                mCamera.setParameters(parameters);
                bt_flash.setImageResource(R.drawable.button_flash_off);
            }
            bt_flash.setImageResource(R.drawable.button_flash_auto);
        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
//            flashBtn.setImageResource(R.drawable.camera_flash_off);
            bt_flash.setImageResource(R.drawable.button_flash_off);
        }
    }

    /**
     * 切换前后置摄像头
     */
    private void switchCamera() {
        mCurrentCameraId = (mCurrentCameraId + 1) % Camera.getNumberOfCameras();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        try {
            mCamera = Camera.open(mCurrentCameraId);
            setStartPreview(mCamera,mHolder);
        } catch (Exception e) {
            Toast.makeText(this, "未发现相机", Toast.LENGTH_LONG).show();
        }

    }

}
