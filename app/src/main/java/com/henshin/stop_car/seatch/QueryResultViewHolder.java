package com.henshin.stop_car.seatch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.henshin.stop_car.R;

/**
 * Created by henshin on 2018/3/13.
 */

public class QueryResultViewHolder extends RecyclerView.ViewHolder {
    private TextView tv ;
    private TextView dis;
    private LinearLayout linearLayout;
    private Context context;
    public void setContext(Context context)
    {
        this.context = context;
    }
    public TextView getTextView()
    {
        return this.tv;
    }
    public TextView getDis(){
        return this.dis;
    }
    public LinearLayout getRelativeLayout()
    {
        return this.linearLayout;
    }
    public QueryResultViewHolder(View itemView) {
        super(itemView);
        tv = (TextView)itemView.findViewById(R.id.textid);
        dis=(TextView)itemView.findViewById(R.id.iddes) ;
        linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(context, ""+v.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
