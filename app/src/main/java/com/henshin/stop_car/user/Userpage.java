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
import android.support.v7.app.AppCompatActivity;
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
import com.henshin.stop_car.Tools.CropPic.PicTools;
import com.henshin.stop_car.Tools.myDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Userpage extends AppCompatActivity {


    private RelativeLayout toolsRe, backRe, overRe;
    private ViewPager viewPager;
    private View view1, view2;
    private List<View> viewList;
    private PagerAdapter pagerAdapter;
    public static Handler mHandler;
    private CircleImageView touxiang;
    private myDialog myDialog;
    private RecyclerView listView;
    private LinearLayoutManager layoutManager;
    private PicTools picTools;
    private Uri phoneUri=null;
    private String username;
    private TextView usernameText;
    private Handler updatapic ;
    private ProgressDialog progressDialog=null;
    private String pic;
    private SharedPreferences sp;
    private String Uesrid;
    private Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse("file:////sdcard/image_output.jpg").getPath());
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        init();
        onclicktouxiang();
        listView = (RecyclerView)view1. findViewById(R.id.list);
        listView.setAdapter(new UserAdapter(Userpage.this,this));
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
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
    private void init()
    {
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
        viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        sp=getSharedPreferences("setting", 0);
        username =sp.getString("username",null);
        usernameText.setText(username);
        sp=getSharedPreferences("setting", 0);
        Uesrid=sp.getString("Uesrid",null);
        touxiang.setImageBitmap(bitmap);
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
        builder.addFormDataPart("Userid",Uesrid);
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

}
