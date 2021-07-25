package com.faisal.faceframe.util;

import com.google.android.gms.vision.face.Landmark;

public class Constants {
    public static final String SHARED_PREF = "tacShf";
    public static final String SELECTED_CHANNEL_NAME = "selected_channel";
    public static final String USER_MOBILE_NUMBER = "8803333";
    public static final String USER_PASS_FROM_API = "gUNmJx";
    public static final String MUMBLE_IP = "3.129.13.191";
    public static final int SERVER_PORT = 64738;
    public static final String USERNAME = "username";
    public static final String ML_TEM = "ml-mumble";
    public static final float MINIMUM_WIDTH_OFFSET = 70;
    public static final float MAXUMUM_WIDTH_OFFSET = 200;
    public static final double MINIMUM_DISTANCE_OFFSET = 200;
    public static final int NUMBER_OF_IMAGES = 1;
   // public static final String TOKEN = "";
    public static final String CAMERA_FACING = "camera_facing";
    public static final String INTENT_MESSAGE = "intent_message";
    public static final double VALUE_COMPARISON_MINIMUM_THRESOLD = 6;
    public static float EyeOpenThresold = 0.1954096f;
    // public static final Float

    public static String getFeature(int value) {
        switch (value) {
            case Landmark.BOTTOM_MOUTH:
                return "BOTTOM_MOUTH";

            case Landmark.LEFT_CHEEK:
                return "LEFT_CHEEK";

            case Landmark.RIGHT_CHEEK:
                return "RIGHT_CHEEK";

            case Landmark.LEFT_EYE:
                return "LEFT_EYE";

            case Landmark.RIGHT_EYE:
                return "RIGHT_EYE";

            case Landmark.LEFT_MOUTH:
                return "LEFT_MOUTH";

            case Landmark.RIGHT_MOUTH:
                return "RIGHT_MOUTH";

            case Landmark.LEFT_EAR:
                return "LEFT_EAR";

            case Landmark.RIGHT_EAR:
                return "RIGHT_EAR";

            case Landmark.LEFT_EAR_TIP:
                return "LEFT_EAR_TIP";

            case Landmark.RIGHT_EAR_TIP:
                return "RIGHT_EAR_TIP";

            case Landmark.NOSE_BASE:
                return "NOSE_BASE";

            default:
                return "Unknown Landmark";

        }
    }
}
