package com.example.administrator.sudokulock;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/22 0022.
 */
public class NineLockView extends View {
    private StyleConfig styleConfig=new StyleConfig();
    private PasswordInputComplete passwordInputCompleteListener=null;
    boolean firstDraw=true;
    boolean moveEnable=false;//只有当点击了圆点后才可以滑动
    int currentCheckedBoxIndex=-1;
    float curX,curY;
    ArrayList<Integer> checkedBoxPath=new ArrayList<>();
    Bitmap bg;
    ArrayList<Box> boxs=new ArrayList<>();
    Vibrator vibrator;
    Paint normalPaint,checkedPaint,linePaint,bgPaint;
    public NineLockView(Context context) {
        super(context);
    }

    public NineLockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void registerPasswordCompleteListener(PasswordInputComplete passwordInputCompleteListener){
        synchronized (PasswordInputComplete.class){
            this.passwordInputCompleteListener=passwordInputCompleteListener;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //自定义九宫格密码锁控件的宽度为match_parent，高度为三个格子的高度+三个格子之间的间距之和，三个格子的高度为三个被选中后的格子的大小，而不是正常状态的格子的大小，因为被选中后我会将格子的大小放大
        int marginX = (getMeasuredWidth()-3*styleConfig.getBoxRadius())/4;
        int measuredHeight=(styleConfig.getBoxRadius()+styleConfig.getBoxRange())*3+marginX*2;
        setMeasuredDimension(getMeasuredWidth(), measuredHeight);//设置高度和宽度
    }

    public void configStyle(StyleConfig styleConfig){
        synchronized (this){
            this.styleConfig=styleConfig;
            firstDraw=true;
        }
        invalidate();
    }
    private void init(){
        int marginX = (getMeasuredWidth()-3*styleConfig.getBoxRadius())/4;//九宫格密码锁每一格左右之间的距离
//        int marginY = (getMeasuredHeight()-3*styleConfig.getBoxRadius())/4;
        Log.i("TAG",marginX+"");
        int marginY=marginX;//上下左右的距离一样
        int left = marginX;
        int top=(getMeasuredHeight()-3*styleConfig.getBoxRadius()-2*marginY)/2;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                Box box=new Box(left+styleConfig.getBoxRadius(),top+styleConfig.getBoxRadius(),styleConfig.getBoxRadius(),false);
                Log.i("TAG",box.toString());
                boxs.add(box);
                left+=styleConfig.getBoxRadius()+marginX;
            }
            left=marginX;
            top+=styleConfig.getBoxRadius()+marginY;
        }
        vibrator= (Vibrator) getContext().getSystemService(Service.VIBRATOR_SERVICE);

        normalPaint=new Paint();//九宫格中每一格正常的时候背景笔刷
        normalPaint.setColor(styleConfig.getBoxNormalColor());
        normalPaint.setStyle(Paint.Style.FILL);
        normalPaint.setAntiAlias(true);

        checkedPaint=new Paint();//九宫格中每一格当被选中的时候的背景笔刷
        checkedPaint.setColor(styleConfig.getBoxCheckedColor());
        checkedPaint.setStyle(Paint.Style.FILL);
        checkedPaint.setAntiAlias(true);

        linePaint=new Paint();//九宫格中每两个被选中的格子之间的连线的背景笔刷
        linePaint.setColor(styleConfig.getLineColor());
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(styleConfig.getLineWidth());
        linePaint.setStyle(Paint.Style.FILL);

        bgPaint=new Paint();//View的背景笔刷

        Bitmap oldbg=BitmapFactory.decodeResource(getContext().getResources(),R.drawable.bg);
        int oldWidth=oldbg.getWidth();
        int oldHeight=oldbg.getHeight();
        Matrix matrix=new Matrix();
        matrix.postScale((float)getMeasuredWidth()/oldWidth,(float)getMeasuredHeight()/oldHeight);//缩放到View的宽高，即填满View
        bg=Bitmap.createBitmap(oldbg,0,0,oldWidth,oldHeight,matrix,true);
//        Toast.makeText(getContext(),"width:"+bg.getWidth()+" height:"+bg.getHeight(),Toast.LENGTH_LONG).show();
        oldbg.recycle();
        //这是九宫格密码锁View的背景图片
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(firstDraw){
            init();
            firstDraw=false;
        }
//        canvas.drawBitmap(bg, 0, 0, bgPaint);
        drawNineBox(canvas);//绘制九宫格的格子
        drawCheckedBoxsLine(canvas);//绘制被选中的格子之间的连线
        drawOtherLine(canvas);//绘制最后一个被选中的格子和当前的点之间的连线，还没到选中的格子
    }
    private void drawNineBox(Canvas canvas){
        for(Box box:boxs){
            if(box.checkedState){
                box.drawCheckedStatus(canvas);
            }else{
                box.drawNormalStatus(canvas);
            }
        }
    }
    private void drawCheckedBoxsLine(Canvas canvas){
        for(int i=0;i<checkedBoxPath.size()-1;i++){
            boxs.get(checkedBoxPath.get(i)).drawCheckedBoxLine(boxs.get(checkedBoxPath.get(i + 1)), canvas);
        }
    }
    private void drawOtherLine(Canvas canvas){
        if(moveEnable){
            Box box=boxs.get(checkedBoxPath.get(checkedBoxPath.size()-1));
            box.drawOtherLine(curX,curY,canvas);
        }

    }
    public void clearPassword(){
        checkedBoxPath.clear();
        for(Box box:boxs){
            box.checkedState=false;
        }
        currentCheckedBoxIndex=-1;
        moveEnable=false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        float x=event.getX();
        float y=event.getY();
        curX=x;
        curY=y;
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                clearPassword();
                currentCheckedBoxIndex=isOrNotInBoxs(x,y);//判断当前手指滑动的点是否在九宫格的某一个格子上，就是判断用户是否点击到格子上
                if(currentCheckedBoxIndex!=-1){
                    moveEnable=true;
                    checkedBoxPath.add(currentCheckedBoxIndex);//将当前选中的格子的序号添加在选中的格子的路径中去
                    boxs.get(currentCheckedBoxIndex).checkedState=true;//设置格子为选中状态
                    vibrator.vibrate(styleConfig.getVibrateTime());
                }
                else
                    moveEnable=false;//用户按下后必须是选中格子，否则不能进行滑动。

                break;
            case MotionEvent.ACTION_MOVE:
                if(moveEnable){
                    currentCheckedBoxIndex=isOrNotInBoxs(x,y);
                    if(currentCheckedBoxIndex!=-1){
                        if(!checkedBoxPath.contains(currentCheckedBoxIndex)){
                            checkedBoxPath.add(currentCheckedBoxIndex);
                            boxs.get(currentCheckedBoxIndex).checkedState=true;
                            vibrator.vibrate(styleConfig.getVibrateTime());
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                StringBuilder password=new StringBuilder();
                for(int i=0;i<checkedBoxPath.size();i++){
                    password.append(checkedBoxPath.get(i));
                    Log.i("TAG",""+checkedBoxPath.get(i));
                }
                Log.i("TAG","password:"+password);
                if(passwordInputCompleteListener!=null){
                    passwordInputCompleteListener.inputComplete(password.toString());
                }
                moveEnable=false;
                break;
        }
        invalidate();
        return true;
    }
    private int isOrNotInBoxs(float x,float y){
        for(Box box:boxs){
            int boxIndex=(box.withinCircle(x,y));
            if(boxIndex!=-1)
                return boxIndex;
        }
        return -1;
    }
    private class Box{
        int centerX,centerY;
        int radius;
        boolean checkedState;//选中状态，是否选中

        private Box(int centerX, int centerY, int radius, boolean checkedState) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.checkedState = checkedState;
        }
        public void drawNormalStatus(Canvas canvas){
            canvas.drawCircle(centerX,centerY,radius,normalPaint);
        }
        public void drawCheckedStatus(Canvas canvas){
            canvas.drawCircle(centerX,centerY,radius+10,checkedPaint);
        }
        public void drawCheckedBoxLine(Box dst,Canvas canvas){
            canvas.drawLine(centerX,centerY,dst.centerX,dst.centerY,linePaint);
        }
        public void drawOtherLine(float x,float y,Canvas canvas){
            canvas.drawLine(centerX,centerY,x,y,linePaint);
        }
        public int withinCircle(float x,float y){//判断给定的点是否在圆内或圆上
            boolean withinCircle=Math.pow(x-centerX,2)+Math.pow(y-centerY,2)<=Math.pow(radius+50,2);
            if(withinCircle){
               int index = boxs.indexOf(this);
                return index;
            }
            return -1;
        }
        @Override
        public String toString() {
            return "Box{" +
                    "centerX=" + centerX +
                    ", centerY=" + centerY +
                    ", radius=" + radius +
                    ", checkedState=" + checkedState +
                    '}';
        }
    }
    /*
      九宫格密码锁，输入密码完成接口，
      password：用户输入的密码
     */
    public interface PasswordInputComplete{
        public void inputComplete(String password);
    }

    /**
     * 九宫格密码锁样式配置
     */
    public static class StyleConfig{
        public StyleConfig() {
            //默认配置
            boxNormalColor=Color.WHITE;
            boxCheckedColor=Color.RED;
            boxRadius=10;

            lineWidth=5;
            lineColor=Color.WHITE;
            vibrateTime=80;
            boxRange=boxRadius+50;
        }

        public int getBoxNormalColor() {
            return boxNormalColor;
        }

        public void setBoxNormalColor(int boxNormalColor) {
            this.boxNormalColor = boxNormalColor;
        }

        public int getBoxCheckedColor() {
            return boxCheckedColor;
        }

        public void setBoxCheckedColor(int boxCheckedColor) {
            this.boxCheckedColor = boxCheckedColor;
        }

        public int getBoxRadius() {
            return boxRadius;
        }

        public void setBoxRadius(int boxRadius) {
            this.boxRadius = boxRadius;
        }

        public int getLineWidth() {
            return lineWidth;
        }

        public void setLineWidth(int lineWidth) {
            this.lineWidth = lineWidth;
        }

        public int getLineColor() {
            return lineColor;
        }

        public void setLineColor(int lineColor) {
            this.lineColor = lineColor;
        }

        public int getBoxHorizontalMargin() {
            return boxHorizontalMargin;
        }

        public void setBoxHorizontalMargin(int boxHorizontalMargin) {
            this.boxHorizontalMargin = boxHorizontalMargin;
        }

        public int getBoxVerticalMargin() {
            return boxVerticalMargin;
        }

        public void setBoxVerticalMargin(int boxVerticalMargin) {
            this.boxVerticalMargin = boxVerticalMargin;
        }

        private int boxNormalColor;
        private int boxCheckedColor;
        private int boxRadius;//正常状态下的格子的大小

        private int lineWidth;
        private int lineColor;

        private int boxHorizontalMargin;
        private int boxVerticalMargin;

        private int vibrateTime;//选中格子的时候手机震动的时间（毫秒）
        private int boxRange;//就是被选中后格子的增大的半径  这个半径必须设置的大一些，不然很不容易被选中，解锁的速度就会很慢。

        public int getVibrateTime() {
            return vibrateTime;
        }

        public void setVibrateTime(int vibrateTime) {
            this.vibrateTime = vibrateTime;
        }

        public int getBoxRange() {
            return boxRange;
        }

        public void setBoxRange(int boxRange) {
            this.boxRange = boxRange;
        }
    }
}