package com.henshin.stop_car.Tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.henshin.stop_car.R;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by henshin on 2018/3/29.
 */
public class Connect {
    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=gbk");
    private String reslut ;
    public void getparkstate(String parkidname, Context context,final Handler handler)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("parkname",parkidname);
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(context.getString(R.string.urlc1001)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String reslut = "";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    String reslut = new String(responseBytes,"GBK");
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void updatestate21(String parkidname,String state,String userid, Context context,final Handler handler)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("parkid",parkidname);
            builder.add("state",state);
            builder.add("userid",userid);
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(context.getString(R.string.urlc1002)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                   reslut = "0";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    reslut = new String(responseBytes,"GBK");
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void updatestate02(String parkidname,String state,String userid,Context context,final Handler handler)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("parkid",parkidname);
            builder.add("state",state);
            builder.add("userid",userid);
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(context.getString(R.string.urlc1003)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    reslut = "0";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    reslut = new String(responseBytes,"GBK");
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //取消预约时使用
    public void removestate(String userid,final Handler handler,Context context)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("userid",userid);
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(context.getString(R.string.urlc1004)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    reslut = "0";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    reslut = new String(responseBytes,"GBK");
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //取消占用时使用
    public void removestatezy(String userid,final Handler handler,Context context)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("userid",userid);
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(context.getString(R.string.urlc1005)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    reslut = "0";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    reslut = new String(responseBytes,"GBK");
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void selectparkbyuser(String userid,final Handler handler,Context context)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("userid",userid);
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(context.getString(R.string.urla1005)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    reslut = "0";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    reslut = new String(responseBytes,"GBK");
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //模糊查询
    public  void mohu(String namesql,Context context,final Handler handler)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            String sql = "name"+"="+namesql;
            RequestBody requestBody =RequestBody.create(FORM_CONTENT_TYPE, sql);
            final Request request = new Request.Builder().url(context.getString(R.string.urld1002)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    reslut = "0";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    reslut = new String(responseBytes,"GBK");
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //查询停车场的信息
    public void selectpark(String parkid,Context context,final Handler handler)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("parkid",parkid);
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(context.getString(R.string.urlc1006)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    reslut = "0";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    reslut = new String(responseBytes,"GBK");
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //停车位全部信息
    public void parkone(String parkoneid,Context context,final Handler handler)
    {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("parkid",parkoneid);
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(context.getString(R.string.urle1002)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    reslut = "0";
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    reslut = new String(responseBytes,"GBK");
                    Message message = new Message();
                    message.obj = reslut;
                    handler.sendMessage(message);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
