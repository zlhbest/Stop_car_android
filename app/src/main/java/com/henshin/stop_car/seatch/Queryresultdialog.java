package com.henshin.stop_car.seatch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.henshin.stop_car.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by henshin on 2018/3/13.
 */

public class Queryresultdialog extends Dialog{
    private ArrayList<stopcar> sc;//用于存放查询出来的id
    private Button  forture;//相情页
    private Button forflase;//返回页
    private TextView titleTv;//消息标题文本
    private String titleStr;//从外界设置的title文本
    private RecyclerView listView;
    private LinearLayoutManager layoutManager;
    private Queryresultdialog.onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private Queryresultdialog.onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器
    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(Queryresultdialog.onNoOnclickListener onNoOnclickListener) {
        this.noOnclickListener = onNoOnclickListener;
    }
    /**
     * 设置确定按钮的显示内容和监听
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(Queryresultdialog.onYesOnclickListener onYesOnclickListener) {
        this.yesOnclickListener = onYesOnclickListener;
    }
    public Queryresultdialog(Context context) {
        super(context, R.style.MyDialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queryresultdialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        forture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        forflase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        //如果用户自定了title和message
        if (titleStr != null) {
            titleTv.setText(titleStr);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        forture = (Button) findViewById(R.id.fortrue);
        forflase = (Button) findViewById(R.id.forflase);
        titleTv = (TextView) findViewById(R.id.dialogtitle);
        listView = (RecyclerView)findViewById(R.id.listresult);
        listView.setAdapter(new QueryResultAdapter(Queryresultdialog.this,getContext()));
        layoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(layoutManager);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }
    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }
    public void setsc(ArrayList<stopcar> sc)
    {
        this.sc = sc;
    }
    public ArrayList<stopcar> getsc()
    {
        return this.sc;
    }
}

