package com.example.tammy.happypai2.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.bean.RegisterBean;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;
import com.hdl.myhttputils.utils.FailedMsgUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements View.OnClickListener{

    private Button bt_login;
    private Button bt_register;

    private TextView tv_email, tv_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


    }

    private void initView(){
        bt_login = (Button)findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);

        bt_register = (Button)findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);

        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_password = (TextView)findViewById(R.id.tv_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                Log.v("button_test","button_login");
                login();
                break;
            case R.id.bt_register:
                startActivity(new Intent(this,RegisterActivity.class));
                Log.v("button_test","button_register");
                break;
            default:
                break;
        }
    }

    private void login(){
        String email = tv_email.getText().toString();
        String password = tv_password.getText().toString();
        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("action", "login");
        params.put("mail",email);
        params.put("password",password);

        MyHttpUtils.build()//构建myhttputils
                .url("http://52.41.31.68/api")//请求的url
                .addParams(params)
                .setJavaBean(RegisterBean.class)
                .onExecuteByPost(new CommCallback<RegisterBean>(){//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                    @Override
                    public void onSucceed(RegisterBean registerBean) {//请求成功之后会调用这个方法----显示结果
                        //Toast.makeText(RegisterActivity.this,registerBean.getUser_id()+"",Toast.LENGTH_SHORT).show();

                        if(registerBean.getState()==0){
                            //SharedPreferences 保存数据的实现代码
                            SharedPreferences sharedPreferences =
                                    getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //如果不能找到Editor接口。尝试使用 SharedPreferences.Editor
                            editor.putString("user_id", registerBean.getUser_id()+"");
                            //我将用户信息保存到其中，你也可以保存登录状态
                            editor.commit();

//                                    String user_id=null;
//                                    SharedPreferences sharedPreferences2 =
//                                            getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
//                                    user_id = sharedPreferences2.getString("user_id", "null");
//
//                                    Toast.makeText(RegisterActivity.this,user_id,Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this,ShareMainActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this,registerBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
                        Toast.makeText(LoginActivity.this, FailedMsgUtils.getErrMsgStr(throwable),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
