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

import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.WebTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.DrawStatus;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedEvent;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedListener;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.henshin.stop_car.TDmap.TianDiTuMethodsClass;
import com.henshin.stop_car.Tools.NetworkTools;
import com.henshin.stop_car.Tools.initimage;
import com.henshin.stop_car.Tools.myDialog;
import com.henshin.stop_car.login.LoginActivity;
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
    //按钮
    private  FloatingActionButton location ;
    private boolean locationorno=false;
    private com.henshin.stop_car.Tools.myDialog myDialog;
    private SharedPreferences sp;
    private String PicUrl;
    private String  username;
    private Bitmap bitmap;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initimage.initImageLoader(this);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        break;
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "Settings !", Toast.LENGTH_SHORT).show();
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
                return true;
            }
        });
        handler.postDelayed(runnable,2000);
        init();
        fadlocartion();
        //地图
        map();
        //侧滑栏
        cehualan(savedInstanceState,toolbar);
    }
    private void init()
    {
        NetworkTools.noteIntentConnect(getBaseContext());
        location = (FloatingActionButton)findViewById(R.id.fab);
        sp=getSharedPreferences("setting", 0);
        username =sp.getString("username",null);
        PicUrl  =sp.getString("PicUri",null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // 配置SearchView的属性
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Toast.makeText(getBaseContext(),"打开",Toast.LENGTH_SHORT).show();
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
               // Toast.makeText(getBaseContext(),"关闭",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
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
    public void map()
    {
        /////////////////////////////地图加载///////////////////////////////////
        ShowMapview();
        /////////////////////////定位//////////////////////////////////////////////
        LocationDisplay(mapView);
    }
    public void cehualan(Bundle savedInstanceState,Toolbar toolbar)
    {
        if(PicUrl!=null) {
            bitmap = BitmapFactory.decodeFile(Uri.parse(PicUrl).getPath());
        }
        else{
            bitmap = BitmapFactory.decodeFile(Uri.parse("file:////sdcard/image_output.jpg").getPath());
        }
        final IProfile profile = new ProfileDrawerItem().withName(username).withEmail("一切皆有可能").withIcon(bitmap).withIdentifier(100);
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
        if (savedInstanceState == null) {
            result.setSelection(21, false);
            headerResult.setActiveProfile(profile);
        }
        result.updateBadge(4, new StringHolder(10 + ""));

    }
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
                Toast.makeText(MainActivity.this,"点击了--取消--按钮",Toast.LENGTH_LONG).show();
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
    private void LocationDisplay(MapView mapView)
    {
        mLocationDisplay = mapView.getLocationDisplay();
        GPSquanxian(mLocationDisplay);
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        mLocationDisplay.startAsync();
        locationPoint(mLocationDisplay,point,mapView,wgs84Point);
    }
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
        mapView.setMap(mMap);
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
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void locationPoint(LocationDisplay locationDisplay, Point pPoint,MapView mapView,Point wgs84point)
    {
        pPoint = locationDisplay.getMapLocation();
        mapView.setViewpointCenterAsync(pPoint, 5000);
        wgs84point = (com.esri.arcgisruntime.geometry.Point) GeometryEngine.project(pPoint, SpatialReferences.getWgs84());
    }
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
}
