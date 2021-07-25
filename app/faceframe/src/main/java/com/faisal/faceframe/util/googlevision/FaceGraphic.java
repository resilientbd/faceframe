package com.faisal.faceframe.util.googlevision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


import com.faisal.faceframe.util.Constants;
import com.faisal.faceframe.util.googlevision.model.EvenbusFaceDetectModel;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

import org.greenrobot.eventbus.EventBus;

public class FaceGraphic extends GraphicOverlay.Graphic {
    public static final float FACE_POSITION_RADIUS = 10.0f;
    public static final float ID_TEXT_SIZE = 40.0f;
    public static final float ID_Y_OFFSET = 50.0f;
    public static final float ID_X_OFFSET = -50.0f;
    public static final float BOX_STROKE_WIDTH = 5.0f;
    private FaceDetectListener faceDetectListener;
    private Context context;
    private static final int COLOR_CHOICES[] = {
            Color.WHITE

    };
    private static int mCurrentColorIndex = 0;

    public static Paint mFacePositionPaint;
    public Paint mIdPaint;
    public static Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;

    public void setContext(Context context) {
        this.context = context;
    }

    FaceGraphic(GraphicOverlay overlay, FaceDetectListener faceDetectListener, Context context) {
        super(overlay);
        this.faceDetectListener = faceDetectListener;
        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];
        this.context = context;
        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void setId(int id) {
        mFaceId = id;
        if (faceDetectListener != null) {
            faceDetectListener.onNewIdDetects(id);
        }
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateFace(Face face) {
        mFace = face;

        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;


        if (face == null) {
            return;
        }
        float centerX = canvas.getWidth() / 2;//x1
        float cny = canvas.getHeight();
        float centerYpoint= (((cny/2)/3)+(cny+(cny/4)))/2;
        float centerY = centerYpoint / 2;//y1


        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);//x2
        float y = translateY(face.getPosition().y + face.getHeight() / 2);//y2
        // canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);


        double distance = Math.sqrt(Math.pow((centerX - x), 2) + Math.pow((centerY - y), 2));
        // Log.d("chkdistance",""+distance);
        boolean isInside = false;

        //canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
        Log.d("chklandmark", "faceid:" + mFaceId);
        // Draws a bounding box around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        float right = x + xOffset;
        float bottom = y + yOffset;
        canvas.drawRect(left, top, right, bottom, mBoxPaint);
       // canvas.drawRoundRect(new RectF(left, top, right, bottom),6,6, mBoxPaint);
       // canvas.drawCircle(x,y,(right-left)/2,mBoxPaint);
        /*Path path = new Path();
       *//* Path path = new Path();
        path.moveTo(left,0);
        path.lineTo(left,20);*//*
        path.moveTo(left, 0);
        path.lineTo(left,50);

        path.close();


        canvas.drawPath(path,mBoxPaint);*/

        float c2x = canvas.getWidth();
        float boxWidth = (c2x + (c2x / 2)) - c2x;
        float rectWidth = bottom - left;
        Log.d("chkboxwidth", "oval width: " + boxWidth);
        Log.d("chkboxwidth", "face rect width: " + rectWidth);


        if (distance <= Constants.MINIMUM_DISTANCE_OFFSET &&
                !isInside ) {

            Log.d("checkinside", "inside the oval");
            isInside = true;
            EvenbusFaceDetectModel model = new EvenbusFaceDetectModel();
            model.setInside(isInside);
            model.setFaceId(face.getId());
            EventBus.getDefault().post(model);

        } else {
            isInside = false;
            EvenbusFaceDetectModel model = new EvenbusFaceDetectModel();
            model.setInside(isInside);
            EventBus.getDefault().post(model);
        }
        int i = 0;

        // Draws a circle for each face feature detected
        for (Landmark landmark : face.getLandmarks()) {
            // the preview display of front-facing cameras is flipped horizontally
            float cx = canvas.getWidth() - scaleX(landmark.getPosition().x);
            float cy = scaleY(landmark.getPosition().y);
            canvas.drawCircle(cx, cy, 10, mIdPaint);
            // Log.d("chklandmark","Landmark point "+i+" Landmark Type: "+landmark.getType()+" Position:"+landmark.getPosition().toString());
            i++;
        }
    }

    public interface FaceDataUpdater {
        void onTrackFaceData(Face face);
    }
}
