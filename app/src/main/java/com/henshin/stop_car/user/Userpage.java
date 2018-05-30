package com.henshin.stop_car.user;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.henshin.stop_car.R;
import com.henshin.stop_car.Tools.ArrayTools;
import com.henshin.stop_car.Tools.Connect;
import com.henshin.stop_car.Tools.CropPic.PicTools;
import com.henshin.stop_car.Tools.myDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Userpage extends AppCompatActivity {
    //layout2
    private CardView nowstate;
    private TextView nowstatetext;
    private CardView pay;
    private TextView paytext;
    private Connect connect;
    private CardView remove;
    private TextView removetext;
    private Handler removehandler;
    private SwipeRefreshLayout layout2;
    private SwipeRefreshLayout layout1;
    private Handler selecthandle;
    private Handler updatestate;
    //end
    private PromptDialog promptDialogerror;
    private PromptDialog promptDialogsuccess;
    private myDialog removeDialogyuyue;
    private myDialog removeDialogzhanyiong;
    private RelativeLayout toolsRe, backRe, overRe;
    private ViewPager viewPager;
    private View view1, view2;
    private List<View> viewList;
    private PagerAdapter pagerAdapter;
    public static Handler mHandler;
    private CircleImageView touxiang;
    private RecyclerView listView;
    private myDialog myDialog;
    private LinearLayoutManager layoutManager;
    private PicTools picTools;
    private Uri phoneUri=null;
    private String username;
    private TextView usernameText;
    private Handler updatapic ;
    private ProgressDialog progressDialog=null;
    private String pic;
    private SharedPreferences sp;
    private String[] resultforpark;
    private Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse("file:////sdcard/image_output.jpg").getPath());
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        init();
        onclicktouxiang();
        layout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                connection();
            }
        });
        layout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selectpark();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paytext.getText().equals("车位未付款")&&resultforpark.length>1)
                {
                    String userid = sp.getString("result",null).split("#")[1];
                    updatestate(userid,resultforpark[1]);
                }
            }
        });
        listView = (RecyclerView)view1. findViewById(R.id.list);
        listView.setAdapter(new UserAdapter(Userpage.this,this));
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        selecthandle=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String)msg.obj;
                String[] results = result.split("#");
                resultforpark = result.split("#");
                handlertoui(results);
            }
        };
        mHandler = new Handler()
        {
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
        updatapic =new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String)msg.obj;
                if(!result.equals("error")) {
                    pic=getResources().getString(R.string.url)+result;
                    Glide.with(getBaseContext())
                            .load(pic)
                            .into(touxiang);
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(),"上传头像成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Glide.with(getBaseContext())
                            .load(getResources().getIdentifier("profile", "drawable","com.henshin.stop_car"))
                            .into(touxiang);
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(),"上传头像失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
        updatestate=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result =msg.obj.toString().replace("\r\n","");
                if(result.equals("1")) {
                    promptDialogsuccess.show();
                }
                else
                {
                    promptDialogerror.show();
                }
            }
        };
        removehandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                removeDialogyuyue.dismiss();
                String result =msg.obj.toString().replace("\r\n","");
                if(result.equals("1")) {
                    promptDialogsuccess.show();
                }
                else
                {
                    promptDialogerror.show();
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
        creatPromptDialog();
        creatDialog();
        viewPager.setAdapter(pagerAdapter);
        backRe.setPadding(0, 0, 0, DensityUtil.getZhuangtai(this));


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //添加背景
        collapsingToolbar.setBackgroundResource(R.drawable.header);
        if(getIntent().getStringExtra("result")==null)
        {
            connection();
        }
        removeonclick();
    }
    private void removeonclick()
    {
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("result")==null) return;
                if(getIntent().getStringExtra("result").split("#")[3].equals("2"))
                {
                    if(resultforpark.length>1&&resultforpark[2].equals("2")) {
                        removeDialogyuyue.show();
                    }
                } else if(getIntent().getStringExtra("result").split("#")[3].equals("1"))
                {
                    if(resultforpark.length>1&&resultforpark[2].equals("1")) {
                        removeDialogzhanyiong.show();
                    }
                }
                else
                {
                    new PromptDialog(view2.getContext())
                            .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                            .setAnimationEnable(true)
                            .setTitleText("错误")
                            .setContentText("您未预定车位，不能取消订单")
                            .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });
    }
    public void dodo(View v) {
        viewPager.setCurrentItem(0);
    }
    public void dodo1(View v) {
        viewPager.setCurrentItem(1);
    }
    private void init()
    {
        connect = new Connect();
        toolsRe = (RelativeLayout) findViewById(R.id.toolsre);
        backRe = (RelativeLayout) findViewById(R.id.backre);
        overRe = (RelativeLayout) findViewById(R.id.over_re);
        touxiang = (CircleImageView)findViewById(R.id.touxiang);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        usernameText=(TextView)findViewById(R.id.username) ;
        picTools = new PicTools();
        picTools.setActivity(this);
        picTools.setContext(this);
        picTools.setPhotoUri(phoneUri);
        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.layout1, null);
        view2 = lf.inflate(R.layout.layout2, null);
        initlayout2();
        viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        layout1 = (SwipeRefreshLayout) view1.findViewById(R.id.swipe_ly);
        sp=getSharedPreferences("setting", 0);
        username =sp.getString("username",null);
        usernameText.setText(username);
        sp=getSharedPreferences("setting", 0);
        touxiang.setImageBitmap(bitmap);
    }
    private void initlayout2()
    {
        remove = (CardView)view2.findViewById(R.id.remove);
        pay=(CardView)view2.findViewById(R.id.pay);
        nowstate = (CardView)view2.findViewById(R.id.nowstate);
        paytext=(TextView)view2.findViewById(R.id.paytext);
        nowstatetext=(TextView)view2.findViewById(R.id.nowstatetext);
        removetext = (TextView)view2.findViewById(R.id.removetext);
        layout2 = (SwipeRefreshLayout)view2.findViewById(R.id.swipe_ly_layout2);
    }
    private void onclicktouxiang()
    {
        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });
    }
    private void ShowDialog()
    {
        myDialog = new myDialog(Userpage.this);
        myDialog.setTitle("更换头像");
        myDialog.setMessage("打开应用");
        myDialog.setYesOnclickListener("相册", new myDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                picTools.choiceFromAlbum();
            }
        });
        myDialog.setNoOnclickListener("相机", new myDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                picTools.PhoneRoot(getBaseContext(),Userpage.this);
                picTools.startCamera();
            }
        });
        myDialog.show();
    }
    private void  uploadImage(String uri)
    {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("image", uri,
                RequestBody.create(MediaType.parse("image/jpeg"), new File(uri)));
        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(getResources().getString(R.string.urla1004))
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String reslut = "error";
                Message message = new Message();
                message.obj = reslut;
                updatapic.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultValue = response.body().string();
                Message message = new Message();
                message.obj = resultValue;
                updatapic.sendMessage(message);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        picTools.oncallResult( requestCode,  resultCode,  data, touxiang);
        if(requestCode==5) {
            phoneUri = picTools.getPhotoOutputUri();
            uploadImage(phoneUri.getPath());
            progressDialog = new ProgressDialog(Userpage.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("头像上传中...");
            progressDialog.show();
            myDialog.dismiss();
        }
    }
    private void creatPromptDialog()
    {
        promptDialogsuccess = new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setTitleText("修改成功")
                .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                });
        promptDialogerror=new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText("修改失败")
                .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                });
    }
    private void connection()
    {
        try {
            String password = sp.getString("password",null);
            String date = "username/" + username + "#password/" + password;
            final HashMap<String, Object> map = ArrayTools.splitArray(date);
            OkHttpClient okHttpClient = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    builder.add(entry.getKey(), entry.getValue().toString());
                }
            }
            RequestBody requestBody = builder.build();
            final Request request = new Request.Builder().url(getString(R.string.urla1001)).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    String reslut = "";
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] responseBytes=response.body().bytes();
                    String reslut = new String(responseBytes,"GBK");
                    Intent intent = new Intent(getApplicationContext(),Userpage.class);
                    intent.putExtra("result",reslut);
                    startActivity(intent);
                    finish();
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void selectpark()
    {
        String userid = sp.getString("result",null).split("#")[1];
        connect.selectparkbyuser(userid,selecthandle,getApplicationContext());
    }
    private void creatDialog()
    {
       creatyuyue();
       creatzhanyong();
    }
    private void creatzhanyong()
    {
        removeDialogzhanyiong =new myDialog(Userpage.this);
        removeDialogzhanyiong.setTitle("取消使用停车位");

        removeDialogzhanyiong.setYesOnclickListener("确定", new myDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                String userid = getIntent().getStringExtra("result").split("#")[1];
                connect.removestatezy(userid,removehandler,Userpage.this);
            }
        });
        removeDialogzhanyiong.setNoOnclickListener("取消", new myDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                removeDialogzhanyiong.dismiss();
            }
        });
    }
    private void creatyuyue()
    {
        removeDialogyuyue =new myDialog(Userpage.this);
        removeDialogyuyue.setTitle("取消预约");
        removeDialogyuyue.setMessage("取消确定不收取费用，也可以再进行预定");
        removeDialogyuyue.setYesOnclickListener("确定", new myDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                String userid = getIntent().getStringExtra("result").split("#")[1];
                connect.removestate(userid,removehandler,Userpage.this);
            }
        });
        removeDialogyuyue.setNoOnclickListener("取消", new myDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                removeDialogyuyue.dismiss();
            }
        });
    }
    //第一个是查询到数据，第二个是停车位的id，第三个是状态，第四个是车位备注，
    private void handlertoui(String[] result)
    {
        if(result.length>1) {
            StringBuilder nowstatetextstring = new StringBuilder()
                    .append("预定停车位id号          " + result[1] + "\n")
                    .append("停车位状态                  " + result[2] + "\n")
                    .append("停车位详情                  " + result[3] + "\n")
                    .append("停车位所属停车场      " + result[6] + "\n")
                    .append("停车场目前空余车位  " + result[5] + "\n")
                    .append("停车场占用车辆          " + result[4] + "\n");
            nowstatetext.setText(nowstatetextstring.toString());
            if (result[2].equals("2")) {
                paytext.setText("车位未付款");
                removetext.setText("车位可以取消预定");
            } else if (result[2].equals("1")) {
                paytext.setText("车位已付款");
                removetext.setText("车位占用中");
            } else {
                paytext.setText("没有查找到您的信息");
                removetext.setText("没有查找到您的信息");
            }
        }
        else
        {
            nowstatetext.setText("没有查找到您的信息");
            paytext.setText("没有查找到您的信息");
            removetext.setText("没有查找到您的信息");
        }
        layout2.setRefreshing(false);
    }
    private void updatestate(String userid,String parkid)
    {
        connect.updatestate21(parkid,"1",userid,getApplicationContext(),updatestate);
    }
}
