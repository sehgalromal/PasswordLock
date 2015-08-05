package com.example.administrator.materialapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class NewActivity2 extends BaseActivity2 {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity2);
        getSupportActionBar().setTitle("NewActivity");
        openDrawerMenu();
        setHeaderView(R.layout.navigation_header_layout);
        setMenuView(R.menu.menu_base_navigation);
        imageView = (ImageView) findViewById(R.id.imageView2);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_file_download_black_24dp);
        Bitmap redBitmap = produceSpecifyColorBitmap(bitmap, Color.RED);
        imageView.setImageBitmap(redBitmap);

        setUpUiListener();
    }

    /**
     * 利用ColorMatrix颜色矩阵更改bitmap的每个像素点的颜色
     * 比如将图片的颜色全改为红色
     */

    public Bitmap produceSpecifyColorBitmap(Bitmap oldBitmap, int specifyColor) {
        Bitmap newBitmap = Bitmap.createBitmap(oldBitmap.getWidth(), oldBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        /**
         *
         * 一个新的齐次颜色矩阵左乘上一个原来的颜色向量矩阵即得到一个新的颜色向量矩阵
         * a1,b1,c1,d1,e1       R        R1          R1=a1*R+b1*G+c1*B+d1*A+e1
         * a2,b2,c2,d2,e2       G        G1          G1=a2*R+b2*G+c2*B+d2*A+e2
         * a3,b3,c3,d3,e3   *   B   =    B1   ->     B1=a3*R+b3*G+c3*B+d3*A+e3
         * a4,b4,c4,d4,e4       A        A1          A1=a4*R+b4*G+c4*B+d4*A+e4
         *                      1
         *
         * 如果现在我想的到透明度不变的红色图片  那么R1=255,G1=0，B1=0，A1=A
         * 那么颜色矩阵就应该是
         *                      0,0,0,0,255
         *                      0,0,0,0,0
         *                      0,0,0,0,0
         *                      0,0,0,1,0
         */
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{0, 0, 0, 0, Color.red(specifyColor),
                0, 0, 0, 0, Color.green(specifyColor),
                0, 0, 0, 0, Color.blue(specifyColor),
                0, 0, 0, 1, 0});
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(oldBitmap, 0, 0, paint);

        return newBitmap;
    }


    /**
     * 将图片的主要颜色转换成newColor，透明的不变，主要适用于改变纯色图片的颜色
     *
     * @param bitmap
     * @param newColor
     * @return
     */
    public Bitmap changePicColor(Bitmap bitmap, int newColor) {
        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);//mutable 必须设置为true，即这个bitmap是可变的，才能改变图片的颜色，否则会抛出IllegalStateException异常，默认情况是为false，只有调用bitmap的copy方法才能设置bitmap可变
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                int oldColor = bitmap.getPixel(i, j);
                if ((oldColor & Color.BLACK) != 0) {
                    newBitmap.setPixel(i, j, Color.argb(Color.alpha(oldColor), Color.red(newColor), Color.green(newColor), Color.blue(newColor)));
                }
            }
        }
        return newBitmap;
    }

    public void setUpUiListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(getBaseContext(), menuItem.getTitle(), Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        navigationView.setClickable(true);
        navigationView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
