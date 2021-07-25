package com.faisal.faceframe.ui.home;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.lifecycle.ViewModelProvider;


import com.faisal.faceframe.util.Constants;
import com.faisal.faceframe.util.SharedPrefUtil;
import com.faisal.faceframe.util.Toaster;
import com.faisal.faceframe.util.base.FaceBaseFragment;
import com.faisal.faceframe.util.googlevision.CameraPreview;
import com.faisal.faceframe.util.googlevision.ConverterUtil;
import com.faisal.faceframe.util.googlevision.DetectFacesFromSingleImage;
import com.faisal.faceframe.util.googlevision.FaceDetectListener;
import com.faisal.faceframe.util.googlevision.FaceFrameListener;
import com.faisal.faceframe.util.googlevision.FaceGraphic;
import com.faisal.faceframe.util.googlevision.GraphicFaceTrackerFactory;
import com.faisal.faceframe.util.googlevision.GraphicOverlay;
import com.faisal.faceframe.util.googlevision.MyFaceDetector;
import com.faisal.faceframe.util.googlevision.model.EvenbusCaptureCommand;
import com.faisal.faceframe.util.googlevision.model.EvenbusFaceDetectModel;
import com.faisal.faceframe.util.googlevision.model.FaceDataModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;


public abstract class FaceDetectionFragmentFace extends FaceBaseFragment implements FaceGraphic.FaceDataUpdater, FaceFrameListener, FaceDetectListener {

    private static final int RC_HANDLE_GMS = 10;
    private FaceDetectionFragmentViewModel activityViewModel;
    private CameraSource mCameraSource = null;
    private int faceid = 0;

    @Override
    public void startUI() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activityViewModel = new ViewModelProvider(this).get(FaceDetectionFragmentViewModel.class);
        setObservers();
        startNybfaceUI();
        EventBus.getDefault().register(this);
        activityViewModel.releasableFrame.postValue(false);
    }

    protected void getPermission() {
        activityViewModel.getAllPermissions(getActivity());
    }

    protected abstract void startNybfaceUI();

    private void setObservers() {

        activityViewModel.getAllPermission().observe(this, aBoolean -> {

            createCameraSource();
        });
        activityViewModel.getSinglePermission().observe(this, aBoolean -> {
            if (aBoolean) {
                createCameraSource();
            }
        });
        activityViewModel.isActiveFrontCamera.observe(this, aBoolean -> {
            if (mCameraSource != null) {
                mCameraSource.release();
                createCameraSource();
            }
        });
    }

    @Subscribe
    public void onFaceInOval(EvenbusCaptureCommand evenbusCaptureCommand) {
        Log.d("chkdistance", "callback recieved");

        if (evenbusCaptureCommand.isCapturabale()) {
            activityViewModel.isProcessing.postValue(true);
            new Handler().post(() -> {
                try {
                    getCameraPreview().stop();
                    mCameraSource.stop();
                } catch (Exception e) {
                    try {
                        mCameraSource.stop();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Log.d("chkpauselog", "exception:" + e1.getMessage());
                    }
                }
                // getCameraPreview().release();
                new CountDownTimer(15000, 500) {
                    int count = 0;

                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.d("chktik", "tiking,count:" + count);
                        if (activityViewModel.releasableFrame.getValue().booleanValue()) {
                           // Log.d("chktik", "tiking,count:" + count);
                            /*EvenbusFaceDetectModel evenbusFaceDetectModel = new EvenbusFaceDetectModel();
                            evenbusFaceDetectModel.setInside(false);
                            EventBus.getDefault().post(evenbusFaceDetectModel);
                           // createCameraSource();
                            cancel();
                            activityViewModel.releasableFrame.postValue(false); //reset value*/
                            if (count > 0) {
                                EvenbusFaceDetectModel evenbusFaceDetectModel = new EvenbusFaceDetectModel();
                                evenbusFaceDetectModel.setInside(false);
                                EventBus.getDefault().post(evenbusFaceDetectModel);
                                activityViewModel.isTimerFinished.postValue(false);
                                cancel();
                                onResume();
                            }
                            count++;
                        }
                    }

                    @Override
                    public void onFinish() {
                        new Handler().post(() -> {
                            activityViewModel.isTimerFinished.postValue(true);
                            // startUI();
                            EvenbusFaceDetectModel evenbusFaceDetectModel = new EvenbusFaceDetectModel();
                            evenbusFaceDetectModel.setInside(false);
                            EventBus.getDefault().post(evenbusFaceDetectModel);
                            onResume();

                            /*createCameraSource();
                            activityViewModel.releasableFrame.postValue(false); //reset value*/

                        });
                    }
                }.start();

            });
        }
        else {
            activityViewModel.isProcessing.postValue(false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("chkpauselog","datached fragment!!");
        try
        {
            mCameraSource.stop();
        }
        catch (Exception e)
        {
            Log.e("chk","exception:"+e.getMessage());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            Log.d("chkpauselog","distroyed!!");
            mCameraSource.release();

        } catch (NullPointerException e) {
            Log.e("chkpauselog", "Exception: " + e.getMessage());
        }

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("chkpauselog", "fragment started!");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (activityViewModel != null) {
            if (activityViewModel.getAllPermission().getValue() != null) {
                if (activityViewModel.getAllPermission().getValue().booleanValue()) {
                    startCameraSource();
                    //createCameraSource();
                    /*try {
                        //getCameraPreview().start(mCameraSource);
                       // startCameraSource();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                }
                else {
                    Toaster.ShowText("value null");
                }
            }
        }
    }

    private void createCameraSource() {
        try {
            FaceDetector detector = new FaceDetector.Builder(getActivity())
                    .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                    // .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .build();

            // This is how you merge myFaceDetector and google.vision detector


            MyFaceDetector myFaceDetector = new MyFaceDetector(getActivity(), detector);
            myFaceDetector.setFaceFrameListener(this);

            myFaceDetector.setProcessor(new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory(getGraphicsOverLay(), this, this, getActivity()))
                    .build());


        /*detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory(mBinding.graphicsOverlay,this))
                        .build());
*/
            //String[] heightWidth=activityViewModel.getWindowSize(getActivity()).split(":");

            CameraSource.Builder builder = new CameraSource.Builder(getActivity(), myFaceDetector);
            builder.setRequestedPreviewSize(640, 480);
            if (activityViewModel.isActiveFrontCamera.getValue() != null) {
                if (activityViewModel.isActiveFrontCamera.getValue()) {
                    builder.setFacing(CameraSource.CAMERA_FACING_FRONT);
                    SharedPrefUtil.ADD_PREFERENCE(getActivity(), Constants.CAMERA_FACING,""+0,false);
                } else {
                    builder.setFacing(CameraSource.CAMERA_FACING_BACK);
                    SharedPrefUtil.ADD_PREFERENCE(getActivity(), Constants.CAMERA_FACING,""+1,false);

                }
            } else {
                builder.setFacing(CameraSource.CAMERA_FACING_FRONT);
            }

            builder.setRequestedFps(30.0f);


            mCameraSource = builder.build();
            startCameraSource();
        } catch (Exception e) {
            Log.d("chklog", "Exception:" + e.getMessage());
        }
    }

    protected abstract GraphicOverlay getGraphicsOverLay();

    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                CameraPreview cameraPreview = getCameraPreview();
                cameraPreview.start(mCameraSource, getGraphicsOverLay());
            } catch (IOException e) {
                //   Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    protected abstract CameraPreview getCameraPreview();


    @Override
    public void onTrackFaceData(Face face) {
//        face.
//        Log.d("chklandmark","faceid traced:"+face.getId());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("chkpauselog","paused fragment!!");
        try {
            mCameraSource.stop();
            Log.d("chkpauselog","paused fragment!!");
        } catch (NullPointerException e) {
            Log.e("chkpauselog", "Exception: " + e.getMessage());
        }
    }

    @Override
    public void onFacesDetects(Bitmap bitmap) {
        new AsyncTask<Void, Void, FaceDataModel>() {
            @Override
            protected void onPostExecute(FaceDataModel faceDataModel) {
                super.onPostExecute(faceDataModel);
                if (faceDataModel != null) {
                    activityViewModel.faceDataModelMutableLiveData.postValue(faceDataModel);
                }
            }

            @Override
            protected FaceDataModel doInBackground(Void... voids) {
                List<Bitmap> bitmaps = DetectFacesFromSingleImage.getImages(getActivity(), bitmap);

                FaceDataModel faceDataModel = null;
                if (bitmaps != null) {
                    if (bitmaps.size() > 0) {
                        faceDataModel = new FaceDataModel();
                        faceDataModel.setId(faceid);
                        faceDataModel.setImageString(ConverterUtil.convertBitmapToBase64(bitmaps.get(0)));

                    }
                }

                return faceDataModel;
            }


        }.execute();

    }

    @Override
    public void onNewIdDetects(int id) {
        this.faceid = id;
    }

    public FaceDetectionFragmentViewModel getActivityViewModel() {
        return activityViewModel;
    }


}