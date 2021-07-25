package com.faisal.faceframe.util.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public abstract class FaceBaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String IMAGE_DIRECTORY = "santra ptt";

    public abstract int setLayoutId();

    private ViewDataBinding viewDataBinding;

    public abstract void startUI(Bundle savedInstanceState);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setCustomTheme();
        super.onCreate(savedInstanceState);

        viewDataBinding = DataBindingUtil.setContentView(this, setLayoutId());
        viewDataBinding.setLifecycleOwner(this);
        startUI(savedInstanceState);
        setObservers();
    }

    public void setCustomTheme() {
    }

    protected abstract void setObservers();

    public Context getActivity() {
        return viewDataBinding.getRoot().getContext();
    }


    public ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    //setting up back functionality with custom view
    public void setBackPressViews(View... Views) {
        for (View v : Views) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    //setting up back functionality with custom view
    public void setClickListeners(View... Views) {
        for (View v : Views) {
            v.setOnClickListener(this);
        }
    }
    public AndroidViewModel getViewModel(Class<? extends AndroidViewModel> viewmodelclass)
    {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(viewmodelclass);
    }
    public void showLongToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void showShortToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void resetViews(View... Views) {
        for (View v : Views) {
            boolean b1 = v instanceof TextView;
            if (b1 == true) {
                TextView textView = (TextView) v;

                textView.setText("");


            }
            boolean b2 = v instanceof EditText;
            if (b2 == true) {
                EditText editText = (EditText) v;

                editText.setText("");


            }
            boolean b3 = v instanceof ImageView;

            if (b3 == true) {
                ImageView imageView = (ImageView) v;

                imageView.setVisibility(View.GONE);

            }
        }
    }

    public boolean getValidation(String validationMsg, View... Views) {
        boolean flag = true;
        for (View v : Views) {
            boolean b1 = v instanceof TextView;
            if (b1 == true) {
                TextView textView = (TextView) v;
                if (textView.getText().toString().trim().isEmpty()) {
                    textView.setError(validationMsg);
                    flag = false;

                }
            }
            boolean b2 = v instanceof EditText;
            if (b2 == true) {
                EditText editText = (EditText) v;
                if (editText.getText().toString().trim().isEmpty()) {
                    editText.setError(validationMsg);
                    flag = false;

                }
            }
            boolean b3 = v instanceof ImageView;

            if (b3 == true) {
                ImageView imageView = (ImageView) v;
                if (imageView.getVisibility() == View.GONE || imageView.getVisibility() == View.INVISIBLE) {
                    flag = false;
                }
            }
        }
        return flag;
    }


    public String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    public void onClick(View v) {

    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    public void loadFragment(FragmentManager fragmentManager, int id, Fragment fragment) {
        fragmentManager.beginTransaction().replace(id, fragment).commit();
    }

}
