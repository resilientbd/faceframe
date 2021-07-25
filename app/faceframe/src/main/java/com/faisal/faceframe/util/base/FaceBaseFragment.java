package com.faisal.faceframe.util.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class FaceBaseFragment extends Fragment {
   public abstract int setLayoutId();
   private ViewDataBinding viewDataBinding;
   public abstract void startUI();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding= DataBindingUtil.inflate(inflater,setLayoutId(),container,false);
        viewDataBinding.setLifecycleOwner(this);
        startUI();
        return viewDataBinding.getRoot();
    }




    public ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }


    public String getCurrentTime()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("DD-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}
