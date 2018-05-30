package com.henshin.stop_car.user;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.henshin.stop_car.R;
import com.henshin.stop_car.Tools.ArrayTools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henshin on 2018/2/25.
 * 选择适配器，
 */

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>
{
    private String result;
    private List<String> list ;
    private Context context;
    private LayoutInflater inflater;
    UserAdapter(AppCompatActivity Userpage,Context context){
        if(Userpage.getIntent().getStringExtra("result")==null)
        {
            result="0#0#0#0#0#0#0#0#0#0#0";
        }
        else {
            result = Userpage.getIntent().getStringExtra("result");
        }
        this.context = context;
        this.list = StringaToLiet();
        inflater = LayoutInflater.from(Userpage);
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    /*
    导入布局文件
     */
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item,null,false);
        UserViewHolder userViewHolder = new  UserViewHolder(view);
        userViewHolder.setContext(this.context);
        return userViewHolder;
    }
    /*
    绑定数据
     */
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.getTextView().setText(list.get(position));
        holder.getRelativeLayout().setTag(list.get(position));
        //ImageLoader.getInstance().displayImage(context.getString(R.string.yue),((UserViewHolder)holder).getImageView());
        int pic = Topic(position);
        holder.getImageView().setImageResource(pic);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    private List<String> StringaToLiet()
    {
        List<String> list = new ArrayList<>();

        for(int i=2;i<result.split("#").length;i++)//第一个1表示成功登陆，第二个1代表序号
        {
            String string = result.split("#")[i];
            list.add(string);
        }
        updata(list);
        return list;
    }
    private void updata(List<String> list)
    {
        if(list.get(0).equals("1"))
        {
            list.set(0,"性别: "+"男");
        }
        else
        {
            list.set(0,"性别: "+"女");
        }
        if(list.get(1).equals("1"))
        {
            list.set(1,"车位使用情况: "+"您正在使用车位");
        }
        else if(list.get(1).equals("0"))
        {
            list.set(1,"车位使用情况: "+"您还没有预定车位");
        }
        else
        {
            list.set(1,"车位使用情况: "+"您已经预定车位,但是未使用");
        }
        String yue  =list.get(2);
        list.set(2,"您的余额:"+yue);
        String email = list.get(3);
        list.set(3,"您的邮箱为: "+email);
        String phono = list.get(4);
        list.set(4,"您的手机号码: "+phono);
        String gxqm = list.get(5);
        list.set(5,"您的个性签名: "+gxqm);
        list.remove(6);
    }
    private int Topic(int position)
    {
        switch (position) {
            case 0:
                return R.drawable.sex;
            case 1:
                return R.drawable.car;
            case 2:
                return R.drawable.yue;
            case 3:
                return R.drawable.email;
            case 4:
                return R.drawable.phono;
            case 5:
                return R.drawable.qianming;
            default:
                break;
        }
        return 0;
    }
}


