package com.faisal.faceframe.util.googlevision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import com.faisal.faceframe.R;
import com.faisal.faceframe.util.googlevision.model.EvenbusFaceDetectModel;
import com.faisal.faceframe.util.googlevision.model.EvenbusFaceFreezeModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GraphicOverlay extends View {
    private final Object mLock = new Object();
    private int mPreviewWidth;
    private float mWidthScaleFactor = 1.0F;
    private int mPreviewHeight;
    private float mHeightScaleFactor = 1.0F;
    private int mFacing = 0;
    private Set<Graphic> mGraphics = new HashSet();
    private Context context;
    private Canvas canvas;
    private boolean isInside = false;
    private int faceId= 0;
    public GraphicOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        EventBus.getDefault().register(this);
    }

    public void clear() {
        Object var1 = this.mLock;
        synchronized(this.mLock) {
            this.mGraphics.clear();
        }

        this.postInvalidate();
      //  EventBus.getDefault().unregister(this);
    }

    public void add(Graphic graphic) {
        Object var2 = this.mLock;
        synchronized(this.mLock) {
            this.mGraphics.add(graphic);
        }

        this.postInvalidate();
    }

    public void remove(Graphic graphic) {
        Object var2 = this.mLock;
        synchronized(this.mLock) {
            this.mGraphics.remove(graphic);
        }

        this.postInvalidate();

    }

    public void setCameraInfo(int previewWidth, int previewHeight, int facing) {
        Object var4 = this.mLock;
        synchronized(this.mLock) {
            this.mPreviewWidth = previewWidth;
            this.mPreviewHeight = previewHeight;
            this.mFacing = facing;
        }

        this.postInvalidate();
    }

    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        Paint myPaint = new Paint();
        EvenbusFaceFreezeModel model = new EvenbusFaceFreezeModel();;
        if(!isInside)
        {
            myPaint.setColor(getResources().getColor(R.color.colorBlack));
            model.setCapturable(false);
        }
        else {
            myPaint.setColor(getResources().getColor(android.R.color.holo_blue_bright));
            model.setCapturable(true);
        }
        model.setFaceId(faceId);
        EventBus.getDefault().post(model);
        myPaint.setStrokeWidth(4);
        myPaint.setStyle(Paint.Style.STROKE);
        float cx=canvas.getWidth()/2;
        float cy=canvas.getHeight()/2;




        RectF oval = new RectF(cx/2,(cy/2)/3,cx+(cx/2),cy+(cy/4));


        Paint recpaint = new Paint();
        recpaint.setColor(getResources().getColor(R.color.colorWhite));
        recpaint.setAlpha(100);

        Rect rec = new Rect(0,0,canvas.getWidth(),canvas.getHeight());
        canvas.drawRect(rec,recpaint);

        Paint clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        RectF removeBrush = new RectF(cx/2,(cy/2)/3,cx+(cx/2),cy+(cy/4));
        canvas.drawCircle(cx,(2*cy)-50,cx,myPaint);
        canvas.drawOval(removeBrush,  clearPaint);
        canvas.drawOval(oval,  myPaint);




       // canvas.drawCircle(c1x, c2y, 2*radius, myPaint);

       // canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        /*Drawable drawable = GetContext.getApplicationUsingReflection().getResources().getDrawable(R.drawable.ic_avatar, null);
        drawable.setBounds(0,0,0,0);
        drawable.draw(canvas);
        canvas.drawBitmap(BitmapFactory.decodeResource(GetContext.getApplicationUsingReflection().getResources().getDrawable(R.drawable.ic_avatar)),null,myPaint);*/
      //  Bitmap bitmap= BitmapToDrawable.drawableToBitmap(GetContext.getApplicationUsingReflection().getResources().getDrawable(R.drawable.ic_avatar));
//        canvas.drawBitmap(bitmap,0,0,null);
        super.onDraw(canvas);
        Object var2 = this.mLock;
        synchronized(this.mLock) {
            if (this.mPreviewWidth != 0 && this.mPreviewHeight != 0) {
                this.mWidthScaleFactor = (float)canvas.getWidth() / (float)this.mPreviewWidth;
                this.mHeightScaleFactor = (float)canvas.getHeight() / (float)this.mPreviewHeight;
            }

            Iterator var3 = this.mGraphics.iterator();

            while(var3.hasNext()) {
                Graphic graphic = (Graphic)var3.next();
                graphic.draw(canvas);
            }

        }
    }

    @Subscribe
    public void faceDetacstInsideTheOvale(EvenbusFaceDetectModel evenbusFaceDetectModel)
    {
        isInside = evenbusFaceDetectModel.isInside();
        faceId = evenbusFaceDetectModel.getFaceId();
       /* Log.d("checkinside","color should changed!");
        if(evenbusFaceDetectModel.isInside())
        {
            if(canvas!=null)
            {
                if(!isInside)
                {
                    Log.d("checkinside","draw!");
                    isInside = true;
                    Paint myPaint = new Paint();
                    myPaint.setColor(getResources().getColor(android.R.color.holo_blue_bright));
                    myPaint.setStrokeWidth(2);
                    myPaint.setStyle(Paint.Style.STROKE);
                    float cx=canvas.getWidth()/2;
                    float cy=canvas.getHeight()/2;
                    RectF rect = new RectF(cx/2,cy/2,cx+(cx/2),cy+(cy/2));
                    canvas.drawOval(rect,myPaint);
                }
            }
        }*/
    }
    public abstract static class Graphic {
        private GraphicOverlay mOverlay;

        public Graphic(GraphicOverlay overlay) {
            this.mOverlay = overlay;
        }

        public abstract void draw(Canvas var1);

        public  float scaleX(float horizontal) {
            return horizontal * this.mOverlay.mWidthScaleFactor;
        }

        public  float scaleY(float vertical) {
            return vertical * this.mOverlay.mHeightScaleFactor;
        }

        public  float translateX(float x) {
            return this.mOverlay.mFacing == 1 ? (float)this.mOverlay.getWidth() - this.scaleX(x) : this.scaleX(x);
        }

        public  float translateY(float y) {
            return this.scaleY(y);
        }

        public void postInvalidate() {
            this.mOverlay.postInvalidate();
        }
    }
}