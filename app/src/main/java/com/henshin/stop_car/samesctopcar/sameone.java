package com.henshin.stop_car.samesctopcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.data.Feature;

import com.esri.arcgisruntime.data.ServiceFeatureTable;

import com.esri.arcgisruntime.layers.FeatureLayer;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.BackgroundGrid;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;

import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;

import com.henshin.stop_car.R;
import com.henshin.stop_car.Tools.ArrayTools;
import com.henshin.stop_car.Tools.Connect;
import com.henshin.stop_car.Tools.GISTools.IQueryResult;
import com.henshin.stop_car.Tools.GISTools.MapQueryClass;
import com.henshin.stop_car.Tools.myDialog;


import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

/**
 * 这是关于选择车位的界面，
 * 0——代表没有预约没有到位
 * 1——代表车位已经有车了
 * 2——代表预约但是没有车进入
 * Created by henshin on 2018/3/23.
 */

public class sameone extends AppCompatActivity {
    private static final String TAG = sameone.class.getSimpleName();
    private myDialog myDialog;
    private Intent intent;
    private Handler parkoneid;
    private String sameoneid;
    private MapView mapView;
    private FeatureLayer parkname;
    private FeatureLayer parkgroundname;
    private ServiceFeatureTable parknameTable ;
    private TextView  samename;
    private TextView samekongyu;
    private TextView sameprice;
    private TextView same;
    private Handler showstate ;
    private Handler updatestate;
    private Connect connect = new Connect();
    private Map<String, Object> result;
    private PromptDialog promptDialogerror;
    private PromptDialog promptDialogsuccess;
    private ProgressDialog progressDialog=null;
    private GraphicsOverlay mGraphicsOverlay = new GraphicsOverlay();
    //用于渲染三种状态的停车位
    SimpleFillSymbol select1 = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.argb(100,255,0,0), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.argb(100,255,0,0), 2));
    SimpleFillSymbol select0 = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.argb(100,0,255,0), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.argb(100,0,255,0), 2));
    SimpleFillSymbol select_1 = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,Color.argb(100,255,255,0), new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.argb(100,255,255,0), 2));
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showsameone);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.sameonetoolbar);
        intent = getIntent();
        sameoneid = intent.getStringExtra("sanmeonename");
        toolbar.setTitle(sameoneid + "详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });
        init();
        showhandler();
        showupdatestate();
        map();

    }
    private void creatPromptDialog()
    {
        promptDialogsuccess = new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.success))
                .setContentText(getString(R.string.text))
                .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                });
        promptDialogerror=new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.error))
                .setContentText(getString(R.string.text_error))
                .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                });
    }
    //初始化切片
    private void init()
    {
        mapView = (MapView)findViewById(R.id.sameonemapview);
        samename = (TextView)findViewById(R.id.samename);
        samekongyu = (TextView)findViewById(R.id.samekongyu);
        sameprice = (TextView)findViewById(R.id.sameprice);
        same = (TextView)findViewById(R.id.same);
        creatPromptDialog();
        parkoneid = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();
                addpark(result);
            }
        };
    }
    //地图切片
    private void map()
    {
        showmap();
        //加载地图的时候依据状态的不同显示出不用的形式
        //这里的parkidname应该是MainActivity传过来的值
        progressDialog= new ProgressDialog(sameone.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("查询中");
        progressDialog.show();
        connect.getparkstate("1",getBaseContext(),showstate);
    }
    public ProgressDialog getprogressDialog()
    {
        return this.progressDialog;
    }
    //展示地图
    private void showmap()
    {
        ServiceFeatureTable parkgroundnameTable = new ServiceFeatureTable(getString(R.string.parkgroundname));
       parkgroundname = new FeatureLayer(parkgroundnameTable);
        Basemap basemap = new Basemap(parkgroundname);
        ArcGISMap mMap = new ArcGISMap(basemap);
        addFearturelayer(mMap);
        mapView.setMap(mMap);
        BackgroundGrid backgroundGrid  = new BackgroundGrid();
        backgroundGrid.setColor(Color.GRAY);
        backgroundGrid.setGridLineWidth(0);
        mapView.setBackgroundGrid(backgroundGrid);
        mapView.getGraphicsOverlays().add(mGraphicsOverlay);
    }
    //加载图层
    private void addFearturelayer(ArcGISMap map)
    {
        parknameTable = new ServiceFeatureTable(getString(R.string.parkname));
        parkname = new FeatureLayer(parknameTable);
        map.getOperationalLayers().add(parkname);
        StartMapTouchFunction();
    }
    //点击查看
    protected void StartMapTouchFunction()
    {
        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                parkname.clearSelection();
                mGraphicsOverlay.getGraphics().clear();
                final android.graphics.Point screenPoint =
                        new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
                final MapQueryClass mapQueryClass = new MapQueryClass();
                mapQueryClass.Identify(
                        mapView,
                        screenPoint,
                        5,
                        new IQueryResult() {
                            @Override
                            public void getQuery() {
                                List<MapQueryClass.MapQueryResult> mapQueryResults = mapQueryClass.getMapQueryResult();
                                if (mapQueryResults.size() != 0) {
                                    for (MapQueryClass.MapQueryResult mapQueryResult : mapQueryResults) {
                                        if(mapQueryResult.getLayerTitle().equals("parkdatabase.DBO.park_1")) {
                                            for (Feature feature : mapQueryResult.features) {
                                                connect.parkone(feature.getAttributes().get("id").toString(),getApplicationContext(),parkoneid);
                                                Rightmanage(feature.getAttributes().get("id").toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                );
                return super.onSingleTapConfirmed(e);
            }
        });
    }
    private void addpark(String result)
    {
        String[] results = result.split("#");
        samename.setText("停车位id:" + results[1]);
        samekongyu.setText("停车位所在停车场:"+results[2]);
        if (results[3].equals("0")) {
            sameprice.setText("停车位价格是:"+"免费");
        }
        else
        {
            sameprice.setText("停车位价格是:"+results[2]+"小时/元");
        }
        if(results[0].equals("0"))
        {
            same.setText("车位使用人:"+"空");
        }
        else
        {
            same.setText("车位使用人:"+results[0]);
        }

    }
    private void updateAttributes(String parkid,String state)
    {
        progressDialog= new ProgressDialog(sameone.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("查询中");
        progressDialog.show();
        if(state.equals("1")) {
            connect.updatestate21(parkid, state, "1", getBaseContext(),null);
            connect.getparkstate("1", getBaseContext(), showstate);
        }
        else if(state.equals("2"))
        {
            connect.updatestate02(parkid, state, "1", getBaseContext(),updatestate);
            connect.getparkstate("1", getBaseContext(), showstate);
        }
        getfeaturename(parkname);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.sameitem,menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void showhandler()
    {
       showstate = new Handler()
       {
           @Override
           public void handleMessage(Message msg) {
               super.handleMessage(msg);
               if(msg.obj.toString()!=null) {
                   result = ArrayTools.splitArray(msg.obj.toString().replace("\r\n",""));
                   getfeaturename(parkname);
                   progressDialog.dismiss();
               }
           }
       };
    }
    private void showupdatestate()
    {
        updatestate = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                myDialog.dismiss();
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
    }
    //扫描所有的feature
    private void getfeaturename(FeatureLayer featureLayer)
    {
        featureLayer.clearSelection();
        mGraphicsOverlay.getGraphics().clear();
        final MapQueryClass mapQueryClass = new MapQueryClass();
        mapQueryClass.Query(parknameTable,
                featureLayer,
                "1=1",
                new IQueryResult() {
                    @Override
                    public void getQuery() {
                        List<MapQueryClass.MapQueryResult> mapQueryResults = mapQueryClass.getMapQueryResult();
                        for (MapQueryClass.MapQueryResult mapQueryResult : mapQueryResults) {
                            for(Feature feature:mapQueryResult.features)
                            {
                                addGraphics(feature);
                            }
                        }
                    }
                });
    }
    private void addGraphics(Feature feature)
    {
        String parkidname = feature.getAttributes().get("id").toString();
        String resultstate = result.get(parkidname).toString();
        switch (resultstate)
        {
            case "1":
                Graphic graphic1 = new Graphic(feature.getGeometry(), select1);
                mGraphicsOverlay.getGraphics().add(graphic1);
                break;
            case "2":
                Graphic graphic_1 = new Graphic(feature.getGeometry(), select_1);
                mGraphicsOverlay.getGraphics().add(graphic_1);
                break;
            case "0":
                Graphic graphic0 = new Graphic(feature.getGeometry(), select0);
                mGraphicsOverlay.getGraphics().add(graphic0);
                break;
        }
    }
    private void Rightmanage(String right)
    {
        String resultstate = result.get(right).toString();
        switch (resultstate)
        {
            case "1":
                RightOne();
                break;
            case "2":
                Righttoe();
                break;
            case "0":
                Rightthree(right,"2");
                break;
        }
    }
    //关于出现停车位是1时侯的情况
    private void RightOne()
    {
        myDialog = new myDialog(sameone.this);
        myDialog.setTitle("位置被占用");
        myDialog.setMessage("如果影响到您，点击详情联系车主");
        myDialog.setYesOnclickListener("详情", new myDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                Toast.makeText(getBaseContext(),"该功能未实现，逗你玩的",Toast.LENGTH_SHORT);
            }
        });
        myDialog.setNoOnclickListener("取消", new myDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
    //出现停车位是2的情况
    private void Righttoe()
    {
        myDialog = new myDialog(sameone.this);
        myDialog.setTitle("位置以被预定");
        myDialog.setMessage("如果想要占用车位，可以进行排队等候");
        myDialog.setYesOnclickListener("详情", new myDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                Toast.makeText(getBaseContext(),"该功能未实现，逗你玩的",Toast.LENGTH_SHORT);
                getfeaturename(parkname);

            }
        });
        myDialog.setNoOnclickListener("取消", new myDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                getfeaturename(parkname);
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
    //出现停车位是0的情况，如果选择的是预定那么状态从0改为2，现在需要设计的逻辑就是
    //如果是2但是想要转成状态1需要再次确认，设置一个倒计时，即可
    private void Rightthree(final String parkid,final String state)
    {
        myDialog = new myDialog(sameone.this);
        myDialog.setTitle("选择车位");
        myDialog.setMessage("您可以预定位置");
        myDialog.setYesOnclickListener("预定", new myDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                //现再的状态是2已经改变
                updateAttributes(parkid,state);
                myDialog.dismiss();
            }
        });
        myDialog.setNoOnclickListener("取消", new myDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                getfeaturename(parkname);
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
}
