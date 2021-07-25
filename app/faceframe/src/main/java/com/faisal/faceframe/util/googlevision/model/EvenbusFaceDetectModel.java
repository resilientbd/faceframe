package com.faisal.faceframe.util.googlevision.model;

public class EvenbusFaceDetectModel {
    private boolean isInside;
    private int faceId;

    public boolean isInside() {
        return isInside;
    }

    public void setInside(boolean inside) {
        isInside = inside;
    }

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }
}
