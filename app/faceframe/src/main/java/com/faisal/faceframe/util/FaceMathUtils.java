package com.faisal.faceframe.util;

public class FaceMathUtils {
    public static double GetTwoPointDistance(float x1, float y1, float x2, float y2)
    {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }
}
