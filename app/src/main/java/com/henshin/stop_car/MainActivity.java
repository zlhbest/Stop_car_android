package com.henshin.stop_car;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.henshin.stop_car.user.Userpage;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MainActivity extends AppCompatActivity {
    //用于侧滑栏
    private static final int PROFILE_SETTING = 100000;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    final IProfile profile = new ProfileDrawerItem().withName("henshin").withEmail("一切皆有可能").withIcon(R.drawable.profile).withIdentifier(100);
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
                    new PrimaryDrawerItem().withName("我的主页").withIcon(FontAwesome.Icon.faw_home).withIdentifier(2).withSelectable(false)
                            )
                            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (drawerItem != null) {
                Intent intent = null;
                if (position==1) {
                    intent = new Intent(getBaseContext(), Userpage.class);
                    startActivity(intent);
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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search:
                        //Intent intent = new Intent();
                        //intent.setClass(MainActivity.this, LoginActivity.class);
                        //startActivity(intent);
                        break;
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "Settings !", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu, menu);
        return true;
    }
}
