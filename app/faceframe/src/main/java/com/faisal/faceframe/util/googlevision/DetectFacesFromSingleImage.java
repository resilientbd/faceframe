package com.faisal.faceframe.util.googlevision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.util.SparseArray;


import com.faisal.faceframe.R;
import com.faisal.faceframe.util.Constants;
import com.faisal.faceframe.util.FaceConstants;
import com.faisal.faceframe.util.FaceMathUtils;
import com.faisal.faceframe.util.GetContext;
import com.faisal.faceframe.util.SharedPrefUtil;
import com.faisal.faceframe.util.ValidationObject;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DetectFacesFromSingleImage {
    public static List<Bitmap> getImages(Context context, Bitmap bitmap) {
        int cameraFacing = SharedPrefUtil.GET_INT_PREFERENCE(GetContext.getApplicationUsingReflection(), Constants.CAMERA_FACING);
        if (cameraFacing != 0) { //back camera active
            bitmap = ConverterUtil.RotateBitmap(bitmap, -90);
        }

        List<Bitmap> bitmapList = new ArrayList<>();
        try {

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            final FaceDetector detector = new FaceDetector.Builder(GetContext.getApplicationUsingReflection())
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .build();
            SparseArray<Face> faces = detector.detect(frame);
            if (faces.size() == 0) {
                bitmap = ConverterUtil.RotateBitmap(bitmap, -90);
                Frame frame2 = new Frame.Builder().setBitmap(bitmap).build();
                faces = detector.detect(frame2);
            }
            Paint paint = new Paint();
            //  paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            // paint.setStrokeWidth(5);

            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            //getImageValidation(context, bitmap);
            Canvas canvas = new Canvas(mutableBitmap);
            Log.d("chksubface", "size:" + faces.size());
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);


                Path path = new Path();
                path.moveTo(face.getPosition().x, face.getPosition().y);
                path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y);
                path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight());
                path.lineTo(face.getPosition().x, face.getPosition().y + face.getHeight());
                path.close();
                // Cut out the selected portion of the image...
                //  Paint redPaint = new Paint();
                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(mutableBitmap, 0, 0, paint);


            /*redPaint.setColor(0XFFFF0000);
            redPaint.setStyle(Paint.Style.STROKE);
            redPaint.setStrokeWidth(8.0f);
            canvas.drawPath(path, redPaint);*/

                // Create a bitmap with just the cropped area.
                Region region = new Region();
                Region clip = new Region(0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight());
                region.setPath(path, clip);
                Rect bounds = region.getBounds();
                Bitmap cropBitmap =
                        Bitmap.createBitmap(mutableBitmap, bounds.left, bounds.top,
                                bounds.width(), bounds.height());
                bitmapList.add(cropBitmap);
            }

        } catch (Exception e) {
            Log.e("chklog", "error:" + e.getMessage());
        }
        return bitmapList;
    }

    public static String getImageValidation(Context context, Bitmap bitmap) {
        int cameraFacing = SharedPrefUtil.GET_INT_PREFERENCE(GetContext.getApplicationUsingReflection(), Constants.CAMERA_FACING);
        if (cameraFacing != 0) { //back camera active
            bitmap = ConverterUtil.RotateBitmap(bitmap, -90);
        }
        ValidationObject validationObject = new ValidationObject();
        validationObject.setSuccess(true);
        validationObject.setMessage("");

        try {

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            final FaceDetector detector = new FaceDetector.Builder(GetContext.getApplicationUsingReflection())
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                    .build();
            SparseArray<Face> faces = detector.detect(frame);
            if (faces.size() == 0) {
                bitmap = ConverterUtil.RotateBitmap(bitmap, -90);
                Frame frame2 = new Frame.Builder().setBitmap(bitmap).build();
                faces = detector.detect(frame2);
            }
            Paint paint = new Paint();
            //  paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            // paint.setStrokeWidth(5);

            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(mutableBitmap);
            Log.d("chksubface", "size:" + faces.size());
            int midpointX = mutableBitmap.getWidth() / 2;
            int midPointY = mutableBitmap.getHeight() / 2;
            if (faces != null) {


                if (faces.size() == 0) {
                    //no face detected!
                    validationObject.setSuccess(false);
                    validationObject.setMessage(FaceConstants.MessageStrings.no_faces_found);
                    // Toaster.ShowText(getApplication().getResources().getString(R.string.no_faces_found));

                } else if (faces.size() == 1) {
                    //valid case
                    // for (int i = 0; i < faces.size(); ++i) {
                    Face face = faces.valueAt(0);
                    Log.d("chklandmarks", "landmarks of id :" + face.getId());
                    Log.d("chklandmarks", "face height :" + face.getHeight() + " face width:" + face.getWidth());
                    Log.d("chklandmarks", "bitmap height :" + mutableBitmap.getHeight() + " bitmap width:" + mutableBitmap.getWidth());


                    Path path = new Path();
                    path.moveTo(face.getPosition().x, face.getPosition().y);
                    path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y);
                    path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight());
                    path.lineTo(face.getPosition().x, face.getPosition().y + face.getHeight());
                    path.close();
                    // Cut out the selected portion of the image...
                    //  Paint redPaint = new Paint();
                    canvas.drawPath(path, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(mutableBitmap, 0, 0, paint);
                    Paint paint1 = new Paint();
                    paint1.setStyle(Paint.Style.STROKE);
                    paint1.setColor(0XFFFF0000);
                    paint1.setStrokeWidth(1.0f);
                    paint1.setTextAlign(Paint.Align.CENTER);
                    paint1.setTextSize(context.getResources().getDimension(R.dimen.text_size_4));
                    //to detect face correct position calculate distance between 6-1,6-7
                    PointF point6 = new PointF();//nosebase
                    PointF point1 = new PointF();
                    PointF point7 = new PointF();
                    // face.getLandmarks().get()
                    for (Landmark landmark : face.getLandmarks()) {

                        Log.d("chklandmarks", "type:" + Constants.getFeature(landmark.getType()) + " position:" + landmark.getPosition());
                        if (landmark.getType() == 1) {
                            point1 = landmark.getPosition();
                        } else if (landmark.getType() == 6) {
                            point6 = landmark.getPosition();
                        } else if (landmark.getType() == 7) {
                            point7 = landmark.getPosition();
                        }
                        // canvas.drawCircle(landmark.getPosition().x, landmark.getPosition().y, 1, paint1);
                        // canvas.drawText(""+landmark.getType(),landmark.getPosition().x,landmark.getPosition().y,paint1);
                    }
                    if (point1.x != 0 && point6.x != 0 && point7.x != 0) {
                        Double dist6_1 = FaceMathUtils.GetTwoPointDistance(point6.x, point6.y, point1.x, point1.y);
                        Double dist6_7 = FaceMathUtils.GetTwoPointDistance(point6.x, point6.y, point7.x, point7.y);
                        Double distMid_6 = FaceMathUtils.GetTwoPointDistance(midpointX, midPointY, point6.x, point6.y);
                        Log.d("chkfaceposition", "6-1:" + dist6_1);
                        Log.d("chkfaceposition", "6-7:" + dist6_7);
                        Log.d("chkfaceposition", "mid_point6:" + distMid_6);
                        //getValidationByPoints(dist6_1,dist6_7,distMid_6);
                        double valueComparison = Math.abs(dist6_1 - dist6_7);
                        Log.d("chkfaceposition", "valuecomparison:" + valueComparison);
                        if (valueComparison > Constants.VALUE_COMPARISON_MINIMUM_THRESOLD) {
                            validationObject.setSuccess(false);
                            validationObject.setMessage(FaceConstants.MessageStrings.error_face_position);
                        } else if (distMid_6 > (Constants.MINIMUM_DISTANCE_OFFSET - 130)) {
                            validationObject.setSuccess(false);
                            validationObject.setMessage(
                                    FaceConstants.MessageStrings.error_face_center);
                        } else if (face.getIsLeftEyeOpenProbability() <= Constants.EyeOpenThresold && face.getIsRightEyeOpenProbability() <= Constants.EyeOpenThresold) {
                            validationObject.setSuccess(false);
                            validationObject.setMessage(FaceConstants.MessageStrings.error_eyes_closed);
                        } else if (face.getIsLeftEyeOpenProbability() <= Constants.EyeOpenThresold) {
                            validationObject.setSuccess(false);
                            validationObject.setMessage(FaceConstants.MessageStrings.error_left_eyes_closed);
                        } else if (face.getIsRightEyeOpenProbability() <= 0.8) {
                            validationObject.setSuccess(false);
                            validationObject.setMessage(FaceConstants.MessageStrings.error_right_eyes_closed);
                        }
                    }

                    Log.d("chkfaceposition", "lefeye: " + face.getIsLeftEyeOpenProbability());
                    Log.d("chkfaceposition", "righteye: " + face.getIsRightEyeOpenProbability());

                    Log.d("chk", "done");
                    // }
                } else if (faces.size() > 1) {
                    //multi face detected
                    validationObject.setSuccess(false);
                    validationObject.setMessage(FaceConstants.MessageStrings.more_image);


                    //Toaster.ShowText(getApplication().getResources().getString(R.string.more_image));

                }
            } else {
                //handle if list null
                validationObject.setSuccess(false);
                validationObject.setMessage(FaceConstants.MessageStrings.no_faces_found);

                //getApplication().getResources().getString(R.string.no_faces_found);
                //Toaster.ShowText(getApplication().getResources().getString(R.string.no_faces_found));
            }


        } catch (Exception e) {
            validationObject.setSuccess(false);
            validationObject.setMessage(e.getMessage());
        }
        Gson gson = new Gson();
        return gson.toJson(validationObject);
    }


}
