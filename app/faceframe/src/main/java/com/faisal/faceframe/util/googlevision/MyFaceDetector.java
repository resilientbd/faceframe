package com.faisal.faceframe.util.googlevision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.SparseArray;


import com.faisal.faceframe.util.Constants;
import com.faisal.faceframe.util.googlevision.model.EvenbusCaptureCommand;
import com.faisal.faceframe.util.googlevision.model.EvenbusFaceFreezeModel;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;

public class MyFaceDetector extends Detector<Face> {
    private FaceFrameListener faceFrameListener;
    private Detector<Face> mDelegate;
    private boolean isProcessAble = false;
    private Context context;
    private int currentFaceId = 0;
    int frameCount = 0;
    private boolean cancelTimer = false;
    private boolean isRunning = false;
    private int faceId = -1;

    private CountDownTimer timer = new CountDownTimer(2000, 50) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (cancelTimer) {
                timer.cancel();
                cancelTimer = false;
                isRunning = false;
                faceId = -1;
                Log.d("chkcapture", "Cancel capture !! id: " + faceId);
            }
        }

        @Override
        public void onFinish() {

            isRunning = false;
            EvenbusCaptureCommand evenbusCaptureCommand = new EvenbusCaptureCommand();
            evenbusCaptureCommand.setCapturabale(true);
            EventBus.getDefault().post(evenbusCaptureCommand);
            if(frame!=null)
            {
                detectAndSendImageData(frame,faces);
            }

            Log.d("chkcapture", "Should Capture this!! id: " + faceId);
        }
    };

    public MyFaceDetector(Context context, Detector<Face> delegate) {
        mDelegate = delegate;
        this.context = context;
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void EventbusFaceCapture(EvenbusFaceFreezeModel evenbusFaceFreezeModel) {
        //   Log.d("chkcapture","databus: "+evenbusFaceFreezeModel.isCapturable());

        if (evenbusFaceFreezeModel.isCapturable()) {
            if (timer != null && !isRunning && faceId != evenbusFaceFreezeModel.getFaceId()) {
                timer.start();
                isRunning = true;
                faceId = evenbusFaceFreezeModel.getFaceId();
                Log.d("chkcapture", "start timer..");
               /* if(frame!=null)
                {
                    detectAndSendImageData(frame,faces);
                }*/
            }
        } else {
            if (timer != null) {
                if (isRunning) {
                    cancelTimer = true;
                }
            }
        }
    }

    private void detectAndSendImageData(Frame frame, SparseArray<Face> faces) {
        Log.d("chksendableData","Data Send");
        YuvImage yuvImage = new YuvImage(frame.getGrayscaleImageData().array(), ImageFormat.NV21, frame.getMetadata().getWidth(), frame.getMetadata().getHeight(), null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, frame.getMetadata().getWidth(), frame.getMetadata().getHeight()), 100, byteArrayOutputStream);
        byte[] jpegArray = byteArrayOutputStream.toByteArray();
        Bitmap tempBitmap = BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.length);
        if (faces.size() > 0) {

            if (faceFrameListener != null) {
                for (int i = 0; i < faces.size(); i++) {
                    int key = this.faces.keyAt(i);
                    if (faces.get(key) != null) {
                        Face face = faces.get(key);
                        //  Log.d("chksentdata","tracking face, position: "+i);
                        if (currentFaceId != face.getId()) {
                            currentFaceId = face.getId();
                            frameCount = 0;
                            // Log.d("chksentdata","reset frame ");
                        }

                        if (frameCount < Constants.NUMBER_OF_IMAGES) {
                            faceFrameListener.onFacesDetects(ConverterUtil.RotateBitmap(tempBitmap, -90));
                            frameCount++;
                            //    Log.d("chksentdata","faceid:"+face.getId()+" currentid:"+currentFaceId+" id:"+currentFaceId+" count:"+frameCount);

                            break;
                        }

                    } else {
                        Log.d("chksentdata", "null finde at " + i);
                    }
                }

            }
        }

        /* SparseArray<Face> faces = mDelegate.detect(frame);

         *//* if(i<=10){
                String encoded = Base64.encodeToString(jpegArray, Base64.DEFAULT);
                Log.d("readframe","Frame:"+encoded);
                i++;
            }*//*

        //TempBitmap is a Bitmap version of a frame which is currently captured by your CameraSource in real-time
        //So you can process this TempBitmap in your own purposes adding extra code here
        List<FaceDataModel> faceDataModels = new ArrayList<>();*/
       /* for (int i = 0; i < faces.size(); ++i) {
            Face face = faces.valueAt(i);
           *//* Path path = new Path();
            path.moveTo(face.getPosition().x, face.getPosition().y);
            path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y);
            path.lineTo(face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight());
            path.lineTo(face.getPosition().x, face.getPosition().y + face.getHeight());
            path.close();
            // Create a bitmap with just the cropped area.
            Region region = new Region();
            Region clip = new Region(0, 0, tempBitmap.getWidth(), tempBitmap.getHeight());
            region.setPath(path, clip);
            Rect bounds = region.getBounds();
            Bitmap cropBitmap =
                    Bitmap.createBitmap(tempBitmap, bounds.left, bounds.top,
                            bounds.width(), bounds.height());*//*

            String imagString=ConverterUtil.convertBitmapToBase64(tempBitmap);
            FaceDataModel model = new FaceDataModel();
            model.setId(face.getId());
            model.setImageString(imagString);

                    faceDataModels.add(model);




        }
*/

//        // Cut out the selected portion of the image...
//        Paint redPaint = new Paint();
//        canvas.drawPath(path, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(mutableBitmap, 0, 0, paint);


//        redPaint.setColor(0XFFFF0000);
//        redPaint.setStyle(Paint.Style.STROKE);
//        redPaint.setStrokeWidth(8.0f);
//        canvas.drawPath(path, redPaint);

    }

    private Frame frame;
    private SparseArray<Face> faces = new SparseArray<>();
    public SparseArray<Face> detect(Frame frame) {
        Log.d("chkframe","chk frame detection");
        SparseArray<Face> faces = mDelegate.detect(frame);
        if(faces.size()>0)
        {
            this.frame = frame;
            this.faces = faces;
        }

        return faces;
    }

    public boolean isOperational() {
        return mDelegate.isOperational();
    }

    public boolean setFocus(int id) {
        return mDelegate.setFocus(id);
    }


    public void setFaceFrameListener(FaceFrameListener faceFrameListener) {
        this.faceFrameListener = faceFrameListener;
    }
}