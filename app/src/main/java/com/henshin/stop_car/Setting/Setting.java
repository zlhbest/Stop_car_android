package com.henshin.stop_car.Setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.henshin.stop_car.R;

/**
 * Created by henshin on 2018/3/8.
 */

public class Setting extends AppCompatActivity {
    private LinearLayout near;
    private searchdialog searchdialog;
    private SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        init();
        //初始化界面控件的事件
        initEvent();
    }
    private void init()
    {
        near = (LinearLayout)findViewById(R.id.nearfw);
        sp = getSharedPreferences("setting",0);
    }
    private void initEvent()
    {
        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });
    }
    private void showdialog()
    {
        searchdialog = new searchdialog(Setting.this);
        searchdialog.setYesOnclickListener(new searchdialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
               int near = searchdialog.getSectionvalue();
                SharedPreferences.Editor editor =sp.edit();
                editor.putInt("near",near);
                editor.apply();
               Toast.makeText(Setting.this,"您设置的搜索停车场范围是：  "+near+"  米",Toast.LENGTH_SHORT).show();
            }
        });
        searchdialog.setNoOnclickListener(new searchdialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                searchdialog.dismiss();
            }
        });
        searchdialog.show();
    }

}
