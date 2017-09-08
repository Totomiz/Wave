package com.zt.tz.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by zt on 15-11-3.
 */
public class WaveView extends View {
    private ArrayList<Wave> list;
    public static final int DESC_LOACATION=10;
    private boolean isRunning=false;
    private int[] colors={Color.RED,Color.YELLOW,Color.GRAY,Color.CYAN,Color.GREEN};

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        list=new ArrayList<Wave>();
//        waves = new ArrayList<Wave>();
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            refreshPaint();
            invalidate();
            if(isRunning){
                mHandler.sendEmptyMessageDelayed(0,10);
            }

        }
    };

    private void refreshPaint() {
        for(int i=0;i<list.size();i++){
            Wave w=list.get(i);
            w.r+=4;
            int alpha=w.paint.getAlpha();
            alpha-=5;
            if(alpha<5){
                alpha=0;
            }
            w.paint.setAlpha(alpha);
            if(alpha==0) {
                list.remove(i);
            }
            w.paint.setStrokeWidth(w.r/3);
        }
        if(list.size()==0){
            isRunning=false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for(Wave w:list){
            canvas.drawCircle(w.rx,w.ry,w.r,w.paint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int x= (int) event.getX();
                int y= (int) event.getY();
                addWave(x,y);
                break;
            default:break;
        }
        return true;
    }

    private void addWave(int x, int y) {
        Wave wave=new Wave(x, y);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(colors[(int) (Math.random() * 5)]);
        paint.setStyle(Paint.Style.STROKE);
        wave.paint=paint;
        addWaveToList(wave,x,y);

    }

    private void addWaveToList(Wave wave,int x,int y) {
        //第一个点不需判断
        if(list.size()==0){
            list.add(wave);
            isRunning=true;
            mHandler.sendEmptyMessage(0);
        }else{
        Wave lastwave=list.get(list.size()-1);
            if(Math.abs(x - lastwave.rx)>DESC_LOACATION&&Math.abs(y-lastwave.ry)>DESC_LOACATION){
                list.add(wave);
            }
        }
    }

    private class Wave {
        private int rx;
        private int ry;
        private Paint paint;
        private int r;
        public Wave(int x,int y){
            this.rx=x;
            this.ry=y;
        }
    }
}
