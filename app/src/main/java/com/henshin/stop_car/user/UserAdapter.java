package com.henshin.stop_car.user;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.henshin.stop_car.R;
import com.henshin.stop_car.Tools.ArrayTools;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by henshin on 2018/2/25.
 * 选择适配器，
 */

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>
{
    private List<String> list ;
    private Context context;
    private LayoutInflater inflater;
    UserAdapter(AppCompatActivity Userpage,Context context){
        this.context = context;
        this.list = ArrayTools.StringToList(context.getResources().getStringArray(R.array.sports)); ;
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
        
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}


