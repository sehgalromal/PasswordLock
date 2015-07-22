package com.example.administrator.lockscreen;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PasswordLock passwordLock=new PasswordLock(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),((TextView)v).getText(),Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(passwordLock);
    }
}
