package com.henshin.stop_car.seatch;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.henshin.stop_car.R;
import com.henshin.stop_car.Tools.ArrayTools;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by henshin on 2018/3/13.
 */

public class QueryResultAdapter  extends RecyclerView.Adapter<QueryResultViewHolder> {
    private List<String> list ;
    private List<Double> listdis;
    private Context context;
    private LayoutInflater inflater;
    QueryResultAdapter(Queryresultdialog dialog, Context context){
        this.context = context;
        this.list = ArrayTools.ArraytoList(dialog.getsc());
        this.listdis=ArrayTools.ArraytoListscdis(dialog.getsc());
        inflater = LayoutInflater.from(dialog.getContext());
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    /*
    导入布局文件
     */
    @Override
    public QueryResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.simpleesultitem,null,false);
        QueryResultViewHolder queryResultViewHolder = new  QueryResultViewHolder(view);
        queryResultViewHolder.setContext(this.context);
        return queryResultViewHolder;
    }
    /*
    绑定数据
     */
    @Override
    public void onBindViewHolder(QueryResultViewHolder holder, int position) {
        holder.getTextView().setText(list.get(position));
        holder.getDis().setText("距离您:"+Math.round(listdis.get(position))+"米");
        holder.getRelativeLayout().setTag(list.get(position));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
}
