package com.example.administrator.materialapp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class BaseActivity extends AppCompatActivity {
    Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout drawerLayout;
    FrameLayout contentLayout;
    LinearLayout menuLayout;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View contentView = LayoutInflater.from(this).inflate(layoutResID, null, false);
        contentLayout.addView(contentView);
    }

    public void setMenuView(@LayoutRes int layoutResID) {
        View menuView = LayoutInflater.from(this).inflate(layoutResID, null, false);
        menuLayout.addView(menuView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void closeDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void openDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abc_action_bar_home_description,
                R.string.abc_action_bar_up_description);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);//HomeAsUp  =转换成<-
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//设置滑动菜单可以滑动，默认状态不是打开的
        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        setUpUi();
        setUpToolBarAndMenu();
        closeDrawerMenu();
    }

    public void setUpUi() {
        contentLayout = (FrameLayout) findViewById(R.id.contentLayout);
        menuLayout = (LinearLayout) findViewById(R.id.drawerMenuLayout);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
    }

    public void setUpToolBarAndMenu() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("BaseActivity");
        setSupportActionBar(toolbar);


    }

}
