package com.faisal.faceframe.util.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.disposables.CompositeDisposable;


/**
 * Abstract class. All common ViewModel related task happens here. Internally disposable is managed
 * through this base class.
 */
public class FaceBaseRxViewModel extends AndroidViewModel {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public FaceBaseRxViewModel(@NonNull Application application) {
        super(application);
    }


    protected CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }


}
