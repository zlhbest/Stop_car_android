package com.henshin.stop_car.user;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.henshin.stop_car.R;

import java.util.ArrayList;
import java.util.List;

public class Userpage extends AppCompatActivity {


    private RelativeLayout toolsRe, backRe, overRe;
    private ViewPager viewPager;
    private View view1, view2;
    private List<View> viewList;
    private PagerAdapter pagerAdapter;
    public static Handler mHandler;


    private RecyclerView listView;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        toolsRe = (RelativeLayout) findViewById(R.id.toolsre);
        backRe = (RelativeLayout) findViewById(R.id.backre);
        overRe = (RelativeLayout) findViewById(R.id.over_re);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.layout1, null);
        view2 = lf.inflate(R.layout.layout2, null);
        viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);


        listView = (RecyclerView)view1. findViewById(R.id.list);
        listView.setAdapter(new UserAdapter(Userpage.this,this));
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        //listView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));//设置横线
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                float alpha = ((float) msg.what) / 100f;
                overRe.setAlpha(alpha);
                if (msg.what == 0) {
                    backRe.setVisibility(View.INVISIBLE);
                    toolsRe.setVisibility(View.VISIBLE);
                } else {
                    backRe.setVisibility(View.VISIBLE);
                    toolsRe.setVisibility(View.INVISIBLE);
                }
            }
        };
        pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }
            @Override
            public int getCount() {

                return viewList.size();
            }
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));
            }
            @Override
            public int getItemPosition(Object object) {

                return super.getItemPosition(object);
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));

                return viewList.get(position);
            }

        };


        viewPager.setAdapter(pagerAdapter);
        backRe.setPadding(0, 0, 0, DensityUtil.getZhuangtai(this));


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //添加背景
        collapsingToolbar.setBackgroundResource(R.drawable.header);
    }
    public void dodo(View v) {
        viewPager.setCurrentItem(0);
    }
    public void dodo1(View v) {
        viewPager.setCurrentItem(1);
    }
}
