package com.example.lifeprice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private DrawThread drawThread;
    private ArrayList<Sprite> sprites=new ArrayList<>();
    float xTouch=-1,yTouch=-1;
    private Bitmap bmp;
    private int core=0;
    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context) {
        super(context);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);


        sprites.add(new Sprite(R.drawable.gophers));
        sprites.add(new Sprite(R.drawable.gophers));
        sprites.add(new Sprite(R.drawable.gophers));
        sprites.add(new Sprite(R.drawable.gophers));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xTouch = event.getX();
        yTouch = event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP && bmp != null) {
            xTouch = -bmp.getWidth();
            yTouch = -bmp.getHeight();
        }
        //通知改组件重绘
        this.invalidate();
        //返回true表明处理方法已经处理该事件
        return true;
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if(null==drawThread){
            drawThread=new DrawThread();
            drawThread.start();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if(null!=drawThread){
            drawThread.stopThread();
            drawThread=null;
        }
    }

    private  class DrawThread extends Thread{
        private boolean beAlive=false;
        public void run(){
            beAlive=true;
            while (beAlive){
                Canvas canvas=null;
                try{
                    synchronized (surfaceHolder){
                        canvas=surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.WHITE);

                        Paint paint=new Paint();
                        paint.setTextSize(50);
                        paint.setColor(Color.BLACK);

                        for(int i=0;i<sprites.size();i++)
                            sprites.get(i).move();
                        for(int i=0;i<sprites.size();i++)
                            sprites.get(i).draw(canvas);

                        if(xTouch>0) {
                            canvas.drawText("你点击了"+xTouch+","+yTouch,20,110,paint);
                            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.c1);
                            canvas.drawBitmap(bmp,xTouch,yTouch,paint);
                            judgeCore(xTouch,yTouch);

                        }
                        canvas.drawText("得分："+core,40,50,paint);
                    }

                }catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    if(null!=canvas)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void stopThread(){
            beAlive=false;
            while(true){
                try {
                    this.join();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public void judgeCore(float x,float y)
    {
        float spriteX,spriteY;
        for(int i=0;i<sprites.size();i++) {
            spriteX = sprites.get(i).getX();
            spriteY=sprites.get(i).getY();
            if((x-(spriteX+100)>-40)&&(x-(spriteX+100)<40))
                if((y-(spriteY+100)>-40)&&(y-(spriteY+100)<40))
                    core++;
        }
    }


    private class Sprite {
        private int resouceId;
        private int x,y;
        private double direction;
        public Sprite(int resouceId) {
            this.resouceId=resouceId;
            x= (int) (Math.random()*getWidth());
            y= (int) (Math.random()*getHeight());
            direction=Math.random()*2*Math.PI;
        }
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void move()
        {
            x+=15*Math.cos(direction);
            if(x<0)
                x=getWidth();
            else if(x>getWidth())
                x=0;
            y+=15*Math.sin(direction);
            if(y<0)
                y=getHeight();
            else if(y>getHeight())
                y=0;
            if(direction<0.05)
                direction=Math.random()*2*Math.PI;

        }
        public void draw(Canvas canvas)
        {
            Drawable drawable= getContext().getResources().getDrawable(R.drawable.gophers);
            Rect drawableRect=new Rect(x,y,x+drawable.getIntrinsicWidth(),y+drawable.getIntrinsicHeight());
            drawable.setBounds(drawableRect);
            drawable.draw(canvas);
        }

    }
}
