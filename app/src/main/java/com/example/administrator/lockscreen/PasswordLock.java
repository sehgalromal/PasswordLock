package com.example.administrator.lockscreen;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/7/21 0021.
 */
public class PasswordLock extends ViewGroup{
    final int MAX_COLUMN=3;//每行最多显示View的个数为3
    int CIRCLE_WIDTH_OR_HEIGHT;
    int MARGIN_BOTTOM=30;
    public PasswordLock(Context context ,OnClickListener clickListener) {
        super(context);
        init(clickListener);
    }

    public PasswordLock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private void init(OnClickListener clickListener){
        for(int i=1;i<=10;i++){
            TextView textView =new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(140,140));
            textView.setText(String.valueOf(i % 10));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.digital_bg_selector);
            textView.setClickable(true);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(20);
            textView.setOnClickListener(clickListener);
            addView(textView);
        }
        setBackgroundResource(R.drawable.lock_background);
        invalidate();
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top=l;
        Log.i("TAG",MARGIN_BOTTOM+"");
        int i;
        for(i=0;i<getChildCount()&&(getChildCount()-i)>=MAX_COLUMN;i+=MAX_COLUMN){
            layoutRow(i,MAX_COLUMN,l,top);
            top+=CIRCLE_WIDTH_OR_HEIGHT+MARGIN_BOTTOM;
        }
        layoutRow(i,getChildCount()-i,l,top);
    }

    private void layoutRow(int start,int count, int l, int t){
        int gap=(getMeasuredWidth()-count*getChildAt(start).getMeasuredWidth())/(count+1);
        int left=l+gap;
        int end=start+count;
        for(;start<end;start++){
            View v=getChildAt(start);
            v.layout(left,t,left+v.getMeasuredWidth(),t+CIRCLE_WIDTH_OR_HEIGHT);
            left+=v.getMeasuredWidth()+gap;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for(int i=0;i<getChildCount();i++){
            View view =getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
            CIRCLE_WIDTH_OR_HEIGHT=view.getMeasuredHeight();
        }
        MARGIN_BOTTOM=(getMeasuredWidth()-MAX_COLUMN*CIRCLE_WIDTH_OR_HEIGHT)/(MAX_COLUMN+1);
        int rows=getChildCount()/MAX_COLUMN+getChildCount()%MAX_COLUMN;
        int measuredHeight=CIRCLE_WIDTH_OR_HEIGHT*rows+MARGIN_BOTTOM*(rows-1);
        setMeasuredDimension(widthMeasureSpec,measuredHeight);
    }
}
