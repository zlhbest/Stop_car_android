package com.henshin.stop_car.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henshin.stop_car.R;

/**
 * Created by henshin on 2018/2/25.
 */

public class UserViewHolder extends RecyclerView.ViewHolder{
    private TextView tv ;
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    private Context context;
    public ImageView getImageView(){
        return this.imageView;
    }
    public void setContext(Context context)
    {
        this.context = context;
    }
    public TextView getTextView()
    {
        return this.tv;
    }
    public RelativeLayout getRelativeLayout()
    {
        return this.relativeLayout;
    }
    public UserViewHolder(View itemView) {
        super(itemView);
        tv = (TextView)itemView.findViewById(R.id.image_text);
        imageView = (ImageView)itemView.findViewById(R.id.image);
        relativeLayout = (RelativeLayout)itemView.findViewById(R.id.layout);
        relativeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context, ""+v.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
