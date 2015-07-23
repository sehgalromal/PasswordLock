package com.example.administrator.sudokulock;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;


public class UnLockActivity extends Activity {
    NineLockView nineLockView;
    String defualtPassword="34578";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //final NineLockView nineLockView=new NineLockView(this);
        setContentView(R.layout.activity_lock);
        nineLockView= (NineLockView) findViewById(R.id.passwordLock);
        nineLockView.registerPasswordCompleteListener(new NineLockView.PasswordInputComplete() {
            @Override
            public void inputComplete(String password) {
//                Toast.makeText(getBaseContext(),password,Toast.LENGTH_LONG).show();
                if(defualtPassword.equals(password)){
                    Toast.makeText(getBaseContext(),"密码输入正确",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getBaseContext(),"密码错误",Toast.LENGTH_LONG).show();
                }
            }
        });
        NineLockView.StyleConfig styleConfig=new NineLockView.StyleConfig();
        styleConfig.setBoxCheckedColor(Color.YELLOW);
        nineLockView.configStyle(styleConfig);
        View V= LayoutInflater.from(this).inflate(R.layout.activity_lock,null);
    }

}
