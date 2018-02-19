package com.henshin.stop_car.welcomepage;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.henshin.stop_car.MainActivity;
import com.henshin.stop_car.R;
import com.henshin.stop_car.login.LoginActivity;


/**
 * Created by henshin on 2018/2/12.
 */

public class welcome extends AppCompatActivity
{
    private SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        sp=getSharedPreferences("setting", 0);
        String username =sp.getString("username",null);
        if(username != null){
            new Handler().postDelayed(new Runnable() {  //开启线程阻塞，以便查看是否登录成功。做调试用
                public void run() {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(welcome.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
        else {
            new Handler().postDelayed(new Runnable() {  //开启线程阻塞，以便查看是否登录成功。做调试用
                public void run() {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(welcome.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }


}
