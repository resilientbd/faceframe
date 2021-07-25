package com.faisal.faceframe.util;

import android.util.Log;

public class ValidationUtil {
    public static boolean isMailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        boolean isValid = email.matches(regex);
        Log.d("chkemailvalidation","validation:"+isValid);
        return isValid;
    }
}
