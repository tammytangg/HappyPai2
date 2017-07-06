package com.example.tammy.happypai2.share;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tammy.happypai2.R;
import com.example.tammy.happypai2.bean.ShareBean;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.CommCallback;
import com.hdl.myhttputils.utils.FailedMsgUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tammy on 17/6/12.
 */

class ViewHolder
{
    public ImageView img_icon;
    public TextView tv_username;
    public TextView tv_time;
    public Button bt_follow;
    public TextView tv_content;
    public ImageView img_content;
    public TextView tv_place;
    public ImageView img_compose;
    public TextView tv_count_share;
    public TextView tv_count_comment;
    public TextView tv_count_thumb;
}

public class ShareItemAdapter extends BaseAdapter {
    private LayoutInflater mInflater=null;
    private List<Map<String,Object>> data;
    private Context context;


    public ShareItemAdapter(Context context,List<Map<String,Object>> data) {
        // TODO Auto-generated constructor stub
        this.mInflater=LayoutInflater.from(context);  //这里就是确定你listview在哪一个layout里面展示
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size(); //这个决定你listview有多少个item
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder=null;
        if(convertView==null)
        {
            holder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.share_item,null); //这里确定你listview里面每一个item的layout
            holder.img_icon = (ImageView)convertView.findViewById(R.id.item_img_icon); //此处是将内容与控件绑定。
            holder.tv_username = (TextView) convertView.findViewById(R.id.item_user_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.item_time);
            holder.bt_follow = (Button) convertView.findViewById(R.id.item_bt_follow);//注意：此处的findVIewById前要加convertView.
            holder.tv_content = (TextView)convertView.findViewById(R.id.item_content);
            holder.img_content = (ImageView)convertView.findViewById(R.id.item_content_img);
            holder.tv_place = (TextView) convertView.findViewById(R.id.item_tv_place);
            holder.img_compose = (ImageView)convertView.findViewById(R.id.item_img_compose);
            holder.tv_count_share = (TextView)convertView.findViewById(R.id.item_share_count);
            holder.tv_count_comment = (TextView)convertView.findViewById(R.id.item_comment_count);
            holder.tv_count_thumb  = (TextView)convertView.findViewById(R.id.item_thumb_count);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag(); //这里是为了提高listview的运行效率
        }

        /**
         * map.put("img_icon", R.drawable.icon_user);
         map.put("username", "Tammy");
         map.put("time","2017/06/10 12:00");
         map.put("hasfollow",false);
         map.put("content", "contentcontentcontentcontentcontentcontent");
         map.put("img_content",R.drawable.icon_user);
         map.put("place","立命館大学");
         map.put("img_compose",R.drawable.button_effect_a);
         map.put("count_share","123");
         map.put("count_comment","123");
         map.put("count_thumb","123");

         public ImageView img_icon;
         public TextView tv_username;
         public TextView tv_time;
         public Button bt_follow;
         public TextView tv_content;
         public ImageView img_content;
         public TextView tv_place;
         public ImageView img_compose;
         public TextView tv_count_share;
         public TextView tv_count_comment;
         public TextView tv_count_thumb;
         */

        holder.img_icon.setBackgroundResource((Integer)data.get(position).get("img_icon"));
        holder.tv_username.setText((String)data.get(position).get("username"));
        holder.tv_time.setText((String)data.get(position).get("time"));
        if((Boolean)data.get(position).get("hasfollow")==true){
            holder.bt_follow.setText("Unfollow");
            holder.bt_follow.setTag(0);
        }
        else{
            holder.bt_follow.setText("Follow");
            holder.bt_follow.setTag(1);
        }
        Log.v("hs", (Boolean)data.get(position).get("hasfollow") + "");
        holder.bt_follow.setOnClickListener(new View.OnClickListener(){
            private Button followBtn;

            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences2 = context
                        .getSharedPreferences("user", Context.MODE_PRIVATE);
                String user_id = sharedPreferences2.getString("user_id", "null");
                final String followee = data.get(position).get("user_id") + "";

                Map<String, Object> params = new HashMap<>();//构造请求的参数
                params.put("action", "follow");
                params.put("follower_id",user_id);
                params.put("followee_id", followee);
                followBtn = (Button)view;

                MyHttpUtils.build()//构建myhttputils
                        .url("http://52.41.31.68/api")//请求的url
                        .addParams(params)
                        .setJavaBean(ShareBean.class)
                        .onExecuteByPost(new CommCallback<ShareBean>(){//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                            @Override
                            public void onSucceed(ShareBean shareBean) {//请求成功之后会调用这个方法----显示结果
                                //Toast.makeText(RegisterActivity.this,registerBean.getUser_id()+"",Toast.LENGTH_SHORT).show();
                                if(shareBean.getState()==0){
                                    followBtn.setText("Unfollow");
                                }else{

                                }

                            }
                            @Override
                            public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法

                            }
                        });
            }
        });
        holder.tv_content.setText((String)data.get(position).get("content"));
        holder.img_content.setImageResource((Integer)data.get(position).get("img_content"));
        holder.tv_place.setText((String)data.get(position).get("place"));
        holder.img_compose.setImageResource((Integer)data.get(position).get("img_compose"));
        holder.tv_count_share.setText((String)data.get(position).get("count_share"));
        holder.tv_count_comment.setText((String)data.get(position).get("count_comment"));
        holder.tv_count_thumb.setText((String)data.get(position).get("count_thumb"));


        return convertView;
    }
}

