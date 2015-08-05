package com.example.administrator.materialapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;


public class BaseActivity2 extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public FrameLayout contentLayout;
    public Toolbar toolbar;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View contentView = LayoutInflater.from(this).inflate(layoutResID, null, false);
        contentLayout.addView(contentView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_activity2);
        setUpUi();
        closeDrawerMenu();//默认关闭滑动菜单
    }

    public void setUpUi() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        contentLayout = (FrameLayout) findViewById(R.id.contentLayout);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("BaseActivity2");
        toolbar.setBackgroundColor(Color.argb(0xff, 0x3b, 0x36, 0x36));
        setSupportActionBar(toolbar);
    }

    public void closeDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    //打开滑动菜单，使之可用，然后再设置菜单的各种属性比如header，menu
    public void openDrawerMenu() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abc_action_bar_home_description, R.string.abc_action_bar_home_description);
        actionBarDrawerToggle.syncState();//必须在调用setDisplayHomeAsUpEnabled方法之后再调用ActionBarDrawerToggle关联homeasup按钮，不然这个按钮的图标就不会是滑动菜单的那个图标（三根横线），而是他默认的图标了
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    public void setHeaderView(@LayoutRes int layoutId) {
        View header = LayoutInflater.from(this).inflate(layoutId, null, false);
        navigationView.addHeaderView(header);
    }

    public void setMenuView(@MenuRes int menuId) {
        navigationView.inflateMenu(menuId);
    }

}
