package com.henshin.stop_car;
//
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//
//
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//               佛祖保佑         永无BUG
//
//
//
import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.loadable.LoadStatusChangedEvent;
import com.esri.arcgisruntime.loadable.LoadStatusChangedListener;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.DrawStatus;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedEvent;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.henshin.stop_car.Setting.Setting;
import com.henshin.stop_car.TDmap.TianDiTuMethodsClass;
import com.henshin.stop_car.Tools.ArrayTools;
import com.henshin.stop_car.Tools.CropPic.PicTools;
import com.henshin.stop_car.Tools.GISTools.IQueryResult;
import com.henshin.stop_car.Tools.GISTools.MapQueryClass;
import com.henshin.stop_car.Tools.NetworkTools;
import com.henshin.stop_car.Tools.Select;
import com.henshin.stop_car.Tools.drawCircle;
import com.henshin.stop_car.Tools.initimage;
import com.henshin.stop_car.Tools.myDialog;
import com.henshin.stop_car.login.LoginActivity;
import com.henshin.stop_car.seatch.Queryresultdialog;
import com.henshin.stop_car.seatch.stopcar;
import com.henshin.stop_car.user.Userpage;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.com.mauker.materialsearchview.MaterialSearchView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    //用于侧滑栏
    private static final int PROFILE_SETTING = 100000;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    //地图
    private MapView mapView;
    private ArcGISMap mMap;
    private ProgressBar wait;
    private LocationDisplay mLocationDisplay;
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};
    private int requestCode = 2;
    private com.esri.arcgisruntime.geometry.Point point;
    private com.esri.arcgisruntime.geometry.Point wgs84Point;
    private GraphicsOverlay mGraphicsOverlay = new GraphicsOverlay();
    //按钮
    private MaterialSearchView searchView;
    private  FloatingActionButton location ;
    private FloatingActionButton nearstopcar;
    private boolean locationorno=false;
    private com.henshin.stop_car.Tools.myDialog myDialog;
    private SharedPreferences sp;
    private String  username;
    private Bitmap bitmap;
    private IProfile profile;
    private Handler PicUpdate = new Handler();
    private Runnable PicUpdaterun = new Runnable() {
        @Override
        public void run() {
            bitmap = BitmapFactory.decodeFile(Uri.parse("file:////sdcard/image_output.jpg").getPath());
            PicUpdate.postDelayed(this, 2000);
        }
    };
    private FeatureLayer mainFeatureLayer;
    private int near;
    private  Polygon mPolygon;
    private Queryresultdialog queryresultdialog;
    private ArrayList<stopcar> fanweipoints;//记录被选中的点的xy坐标
    private String[] featurepoints;//图层内的点元素
    private SimpleMarkerSymbol symbol =
            new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 12);
    //Handle
    private Handler handler = new Handler();
    private Runnable runnable=new Runnable(){
        @Override
        public void run() {

            if(!NetworkTools.isGpsEnabled(getBaseContext()))
            {
                location.setImageResource(R.drawable.location_no);
                locationorno=false;
            }
            else{
                location.setImageResource(R.drawable.location);
                locationorno=true;
            }
            handler.postDelayed(this, 2000);
        }
    };
    private Handler showdialog = new Handler();
    private Runnable runnabledialog = new Runnable() {
        @Override
        public void run() {
            showresultdialog(fanweipoints);
        }
    } ;
    //创建activity时的操作；
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initimage.initImageLoader(this);
        handler.postDelayed(runnable,2000);
        PicUpdate.postDelayed(PicUpdaterun,2000);
        PicTools.PhoneRoot(getBaseContext(),MainActivity.this);
        init();
        search();
        searchonitem();
        //定位开启
        fadlocartion();
        //搜索周边
        fadnearstopcar();
        //地图
        map();
        //侧滑栏
        cehualan(savedInstanceState,toolbar);
    }
    //这是初始化所有应该在一开始就实例化的值
    private void init()
    {
        NetworkTools.noteIntentConnect(getBaseContext());
        location = (FloatingActionButton)findViewById(R.id.fab);
        nearstopcar =(FloatingActionButton)findViewById(R.id.nearSC);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setTintAlpha(200);
        searchView.adjustTintAlpha(0.8f);
        fanweipoints = new ArrayList<stopcar>();
        sp=getSharedPreferences("setting", 0);
        username =sp.getString("username",null);

    }
    //创建一个菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.first_menu, menu);
        return true;
    }
    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
    //地图加载
    public void map()
    {
        /////////////////////////////地图加载///////////////////////////////////
        ShowMapview();

        /////////////////////////定位//////////////////////////////////////////////
        LocationDisplay(mapView);

    }
    //侧滑栏的实现
    public void cehualan(Bundle savedInstanceState,Toolbar toolbar)
    {
        bitmap = BitmapFactory.decodeFile(Uri.parse("file:////sdcard/image_output.jpg").getPath());
        profile= new ProfileDrawerItem().withName(username).withEmail("一切皆有可能").withIcon(bitmap).withIdentifier(100);
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileSettingDrawerItem().withName("添加好友").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder(this)
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("我的主页").withIcon(FontAwesome.Icon.faw_home).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName("退出").withIcon(R.drawable.exit).withIdentifier(2).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            switch ((int)drawerItem.getIdentifier()) {
                                case 1:
                                    intent = new Intent(getBaseContext(), Userpage.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    showNormalDialog();
                                    break;
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .withShowDrawerUntilDraggedOpened(true)
                .build();
            result.setSelection(21, false);
            headerResult.setActiveProfile(profile);
        result.updateBadge(4, new StringHolder(10 + ""));

    }
    //点击退出时的弹窗
    private void showNormalDialog()
    {
        myDialog = new myDialog(MainActivity.this);
        myDialog.setTitle("提示");
        myDialog.setMessage("确定退出应用?");
        myDialog.setYesOnclickListener("确定", new myDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                SharedPreferences spout =getSharedPreferences("setting", 0);
                SharedPreferences.Editor ed =spout.edit();
                ed.clear();
                ed.apply();
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                myDialog.dismiss();
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
    //定位
    private void LocationDisplay(MapView mapView)
    {
        mLocationDisplay = mapView.getLocationDisplay();
        GPSquanxian(mLocationDisplay);
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        mLocationDisplay.startAsync();
        point = mLocationDisplay.getMapLocation();
        locationPoint(mLocationDisplay,mapView);


    }
    //加载天地图
    private void ShowMapview()
    {
        //定义mapview控件
        mapView = (MapView)findViewById(R.id.mMapview);
        wait = (ProgressBar)findViewById(R.id.wait);
        //定义天地图图层
        WebTiledLayer webTiledLayer = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_VECTOR_2000);
        Basemap tdtBasemap = new Basemap(webTiledLayer);
        WebTiledLayer webTiledLayer1 = TianDiTuMethodsClass.CreateTianDiTuTiledLayer(TianDiTuMethodsClass.LayerType.TIANDITU_VECTOR_ANNOTATION_CHINESE_2000);//加载中文标注
        tdtBasemap.getBaseLayers().add(webTiledLayer1);
        //将天地图图层加入进mapview中
        mMap = new ArcGISMap(tdtBasemap);
        ///////////////////////////////加载图层//////////////////////////////
        ShowArcgisMap();
        mapView.setMap(mMap);
        mapView.getGraphicsOverlays().add(mGraphicsOverlay);
        //监听地图的绘制，当地图进行绘制的时候，wait就进行转动，当地图绘制完毕的时候wait就结束
        mapView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
            @Override
            public void drawStatusChanged(DrawStatusChangedEvent drawStatusChangedEvent) {
                if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.IN_PROGRESS) {
                    wait.setVisibility(View.VISIBLE);
                } else if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED) {
                    wait.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    //加载图层
    private void ShowArcgisMap()
    {
        //final ArcGISMapImageLayer  mainMapImageLayer = new ArcGISMapImageLayer(getString(R.string.parkpointMapserver));
        final ServiceFeatureTable mainServiceFeatureTable = new ServiceFeatureTable(getString(R.string.parkpointFeatureServer));
        mainServiceFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_NO_CACHE);
        mainServiceFeatureTable.addLoadStatusChangedListener(new LoadStatusChangedListener() {
            @Override
            public void loadStatusChanged(LoadStatusChangedEvent loadStatusChangedEvent) {
                if (loadStatusChangedEvent.getNewLoadStatus() == LoadStatus.LOADED) {
                    ServiceFeatureTable.FeatureRequestMode mainFeatureRequestMode =
                            mainServiceFeatureTable.getFeatureRequestMode();
                    String mainFeatureRequestModeName = mainFeatureRequestMode.name();
                }
            }
        });
        mainFeatureLayer  = new FeatureLayer(mainServiceFeatureTable);
        getfeaturename(mainFeatureLayer);
       // mainMapImageLayer.setOpacity(0.5f);
        //mMap.getOperationalLayers().add(mainMapImageLayer);
        mMap.getOperationalLayers().add(mainFeatureLayer);
        StartMapTouchFunction();
    }
    //获取feature的名字
    private void getfeaturename(FeatureLayer featureLayer)
    {
        final MapQueryClass mapQueryClass = new MapQueryClass();
        mapQueryClass.Query(featureLayer,
                "1=1",
                new IQueryResult() {
                    @Override
                    public void getQuery() {
                        List<MapQueryClass.MapQueryResult> mapQueryResults= mapQueryClass.getMapQueryResult();
                        for (MapQueryClass.MapQueryResult mapQueryResult:mapQueryResults) {
                            int i=0;
                            int count=mapQueryResult.features.size();
                            featurepoints = new String[count];
                          for(Feature feature:mapQueryResult.features)
                          {
                              featurepoints[i] = feature.getAttributes().get("OBJECTID").toString();
                              i++;
                          }
                        }
                        searchView.addSuggestions(featurepoints);
                    }
                });
    }
    //点击查看
    protected void StartMapTouchFunction()
    {
        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this,mapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
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

                                List<MapQueryClass.MapQueryResult> mapQueryResults= mapQueryClass.getMapQueryResult();
                                if(mapQueryResults.size()!=0) {
                                    for (MapQueryClass.MapQueryResult mapQueryResult : mapQueryResults) {
                                        String a = new String();
                                        for (Feature feature : mapQueryResult.features) {
                                            a = a + feature.getAttributes().get("OBJECTID").toString() + "  ";
                                        }
                                        Toast.makeText(getBaseContext(), a, Toast.LENGTH_SHORT).show();
                                        showCallout(screenPoint).show();
                                    }
                                }
                                else
                                {
                                    showCallout(screenPoint).dismiss();
                                }
                            }
                        }
                );
                return super.onSingleTapConfirmed(e);
            }
        });
    }
    //范围查看
    protected void StartfanweiFunction()
    {
        final MapQueryClass mapQueryClass = new MapQueryClass();
        mapQueryClass.Identify(
                mainFeatureLayer,
                mPolygon,
                new IQueryResult() {
                    @Override
                    public void getQuery() {
                        List<MapQueryClass.MapQueryResult> mapQueryResults= mapQueryClass.getMapQueryResult();
                        for (MapQueryClass.MapQueryResult mapQueryResult:mapQueryResults) {
                            if(mapQueryResults.size()==0)
                            {
                                Toast.makeText(getBaseContext(),"无信息",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                fanweipoints.clear();
                                for (Feature feature : mapQueryResult.features) {
                                    double jingdu = feature.getGeometry().getExtent().getXMax();
                                    double weidu = feature.getGeometry().getExtent().getYMax();
                                    Point pointlin = new Point(jingdu,weidu);
                                    stopcar sc = new stopcar(feature.getAttributes().get("OBJECTID").toString(),pointlin);
                                    fanweipoints.add(sc);
                                    Select.SelectAndPaixu(fanweipoints,point);
                                }
                                showdialog.postDelayed(runnabledialog,0);
                            }
                        }
                    }
                }
        );
    }
    //GPS权限管理
    private void GPSquanxian(LocationDisplay locationDisplay)
    {
        if(!NetworkTools.isGpsEnabled(getBaseContext()))
        {
            Toast.makeText(getBaseContext(),"请打开GPS",Toast.LENGTH_SHORT).show();
            location.setImageResource(R.drawable.location_no);
        }
        //判断开始定位时位置服务权限有没有开启
        locationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                if (dataSourceStatusChangedEvent.isStarted())
                    return;
                if (dataSourceStatusChangedEvent.getError() == null)
                    return;
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermissions[0]) == PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermissions[1]) == PackageManager.PERMISSION_GRANTED;
                if (!(permissionCheck1 && permissionCheck2)) {
                    ActivityCompat.requestPermissions(MainActivity.this, reqPermissions, requestCode);
                } else {
                    String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(MainActivity.this, "GPS未开启", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //定位点
    private void locationPoint(LocationDisplay locationDisplay,MapView mapView)
    {
        point = locationDisplay.getMapLocation();
        mapView.setViewpointCenterAsync(point, 5000);
        wgs84Point = (com.esri.arcgisruntime.geometry.Point) GeometryEngine.project(point, SpatialReferences.getWgs84());
    }
    //右下角定位点
    private void fadlocartion()
    {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locationorno)
                {
                    LocationDisplay(mapView);
                }
                else
                {
                    Toast.makeText(getBaseContext(),"请先打开GPS",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //显示范围内要素
    private void fadnearstopcar()
    {
        nearstopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchnearstopcar();
            }
        });
    }
    //查到的数据
    private void searchnearstopcar()
    {
        if(mGraphicsOverlay.getGraphics().size()==0&&(point.getY()!=0||point.getX()!=0)) {
            drawrange();
        }
        else if(mGraphicsOverlay.getGraphics().size()!=0)
        {
            mGraphicsOverlay.getGraphics().clear();
            drawrange();
        }
    }
    private void drawrange()
    {
        near = sp.getInt("near",0);
        if(near==0)
        {
            near=1000;
        }
        Toast.makeText(getBaseContext(),"您的搜索范围是"+near+"米",Toast.LENGTH_SHORT).show();
        PointCollection pointsPoly = new PointCollection(mapView.getSpatialReference());
        double Dis = drawCircle.JWtoM(near);
        int length = drawCircle.getpoint(point, Dis).length;
        Point[] points = drawCircle.getpoint(point, Dis);
        for (int i = 0; i < length; i++) {
            pointsPoly.add(points[i]);
        }
        mPolygon= new Polygon(pointsPoly);
        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF000000, 1);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.argb(100, 220, 220, 220), lineSymbol);
        mGraphicsOverlay.getGraphics().add(new Graphic(mPolygon, fillSymbol));
        mapView.setViewpointGeometryAsync(mPolygon);
        StartfanweiFunction();
    }
    //弹窗
    private Callout showCallout(android.graphics.Point point)
    {
        LayoutInflater inflater = LayoutInflater
                .from(MainActivity.this);
        View view = inflater.inflate(R.layout.calloutdisplay, null);
        Callout callout = mapView.getCallout();
        Callout.Style style = new Callout.Style(getBaseContext(),R.xml.calloutstyle);
        callout.setStyle(style);
        callout.show(view,mapView.screenToLocation(point));
        return callout;
    }
    //显示结果弹窗
    private void showresultdialog(ArrayList<stopcar> sc)
    {
        queryresultdialog = new Queryresultdialog(MainActivity.this);
        queryresultdialog.setTitle("搜索结果");
        queryresultdialog.setYesOnclickListener(new Queryresultdialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                Toast.makeText(getBaseContext(),"暂时信息没有",Toast.LENGTH_SHORT).show();
            }
        });
        queryresultdialog.setNoOnclickListener(new Queryresultdialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                queryresultdialog.dismiss();
            }
        });
        queryresultdialog.setsc(sc);
        queryresultdialog.show();
    }
    @Override
    public void onBackPressed()
    {
        if (searchView.isOpen()) {
            // Close the search on the back button press.
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        searchView.clearSuggestions();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        searchView.activityResumed();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void search()
    {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final MapQueryClass mapQueryClass = new MapQueryClass();
                mainFeatureLayer.clearSelection();
                mGraphicsOverlay.getGraphics().clear();
                mapQueryClass.Search(mainFeatureLayer,
                        query,
                        new IQueryResult() {
                            @Override
                            public void getQuery() {
                                List<MapQueryClass.MapQueryResult> mapQueryResults= mapQueryClass.getMapQueryResult();
                                for (MapQueryClass.MapQueryResult mapQueryResult:mapQueryResults) {
                                    double x= mapQueryResult.features.get(0).getGeometry().getExtent().getXMax();
                                    double y = mapQueryResult.features.get(0).getGeometry().getExtent().getYMax();
                                    Point pointselect = new Point(x,y);
                                    Graphic graphic = new Graphic(pointselect, symbol);
                                    mGraphicsOverlay.getGraphics().add(graphic);
                                    mapView.setViewpointCenterAsync(pointselect, 5000);
                                    showCallout(mapView.locationToScreen(pointselect)).show();
                                }
                                searchView.clearFocus();
                            }
                        });
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void searchonitem()
    {
        final Context context = this;
        searchView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "Long clicked position: " + i, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        searchView.setOnVoiceClickedListener(new MaterialSearchView.OnVoiceClickedListener() {
            @Override
            public void onVoiceClicked() {
                Toast.makeText(context, "Voice clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        searchView.setSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewOpened() {
                // Do something once the view is open.
            }

            @Override
            public void onSearchViewClosed() {
                // Do something once the view is closed.
            }
        });
        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Do something when the suggestion list is clicked.
                String suggestion = searchView.getSuggestionAtPosition(position);

                searchView.setQuery(suggestion, false);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle toolbar item clicks here. It'll
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                searchView.openSearch();
                break;
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, Setting.class));
                break;
            case R.id.location:
                if(!NetworkTools.isGpsEnabled(getBaseContext()))
                {
                    Toast.makeText(getBaseContext(),"请先打开GPS",Toast.LENGTH_SHORT).show();
                }
                else {
                    LocationDisplay(mapView);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
