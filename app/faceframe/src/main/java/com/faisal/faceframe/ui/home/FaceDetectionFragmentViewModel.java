package com.faisal.faceframe.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.faisal.faceframe.util.GetContext;
import com.faisal.faceframe.util.googlevision.model.FaceDataModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class FaceDetectionFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> allPermission = new MutableLiveData<>();
    private MutableLiveData<Boolean> singlePermission = new MutableLiveData<>();
   // private MutableLiveData<Boolean> locationPermission = new MutableLiveData<>();
    public MutableLiveData<FaceDataModel> faceDataModelMutableLiveData = new MutableLiveData<FaceDataModel>();
    public MutableLiveData<Boolean> isActiveFrontCamera = new MutableLiveData<>();
    public MutableLiveData<Boolean> releasableFrame = new MutableLiveData<>(); //true; release timer
    public MutableLiveData<Boolean> isProcessing = new MutableLiveData<>(); //true; start processing
    public MutableLiveData<Boolean> isTimerFinished = new MutableLiveData<>();//detection timer
    public FaceDetectionFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void getAllPermissions(Context contex) {
        Dexter.withContext(GetContext.getApplicationUsingReflection()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                       if(multiplePermissionsReport.areAllPermissionsGranted())
                       {
                           allPermission.setValue(multiplePermissionsReport.areAllPermissionsGranted());
                          // locationPermission.setValue(multiplePermissionsReport.areAllPermissionsGranted());
                       }
                       else {
                           //Toaster.ShowText(getApplication().getResources().getString(R.string.permission_dialog_text));
                           Log.e("chkmodule","not all permission accepted");
                       }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    String getWindowSize(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return ""+height+":"+width;
    }
    public void getSinglePermission(Context context, String permission) {
        Dexter.withContext(context).withPermission(permission).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                singlePermission.setValue(true);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                singlePermission.setValue(false);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();
    }

    public MutableLiveData<Boolean> getAllPermission() {
        return allPermission;
    }

    public MutableLiveData<Boolean> getSinglePermission() {
        return singlePermission;
    }
}
