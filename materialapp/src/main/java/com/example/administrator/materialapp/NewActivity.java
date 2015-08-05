package com.example.administrator.materialapp;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        openDrawerMenu();
        setMenuView(R.layout.menu);
        getSupportActionBar().setTitle("NewActivity");
    }

    public void menuItemClick(View view) {
        Toast.makeText(getBaseContext(), ((TextView) view).getText(), Toast.LENGTH_LONG).show();
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
