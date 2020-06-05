package com.example.lab3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingCanvas extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder container;
    private Thread drawThred;
    private boolean isThreadWorking = false;
    private Object blocker = new Object();
    private Bitmap bitmap = null;
    private Canvas canvas = null;
    Path path = null;
    Paint paint;

    public DrawingCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        container = getHolder();
        container.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawARGB(255,255,255,255);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        pauseDrawing();
    }

    @Override
    public void run() {
        while(isThreadWorking) {
            Canvas newCanvas = null;
            try {
                synchronized (container) {
                    if (!container.getSurface().isValid()) continue;
                    newCanvas = container.lockCanvas(null);
                    synchronized (blocker) {
                        if(isThreadWorking) {
                            newCanvas.drawBitmap(bitmap, 0, 0, null);
                        }
                    }
                }
            } finally {
                if (newCanvas != null) container.unlockCanvasAndPost(newCanvas);
            }
        }
        try {
            Thread.sleep(1000/25);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                path.moveTo(event.getX(), event.getY());
                canvas.drawPath(path, paint);
                break;

            case MotionEvent.ACTION_MOVE:
                paint.setStyle(Paint.Style.STROKE);
                path.lineTo(event.getX(), event.getY());
                canvas.drawPath(path, paint);
                break;

            case MotionEvent.ACTION_UP:
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(event.getX(), event.getY(), 8, paint);
                break;
        }
        synchronized(blocker) {

        }
        return true;
    }

    public boolean performClick() {
        return super.performClick();
    }

    public void draw() {
        drawThred = new Thread(this);
        isThreadWorking = true;
        drawThred.start();
    }

    public void pauseDrawing() {
        isThreadWorking = false;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void clearCanvas() {
        canvas.drawARGB(255,255,255,255);
        paint.setColor(Color.WHITE);
    }
}
