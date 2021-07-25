package com.faisal.faceframe.util.googlevision.model;

public class EvenbusFaceFreezeModel {
    private boolean capturable;
    private int faceId;

    public boolean isCapturable() {
        return capturable;
    }

    public void setCapturable(boolean capturable) {
        this.capturable = capturable;
    }

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }
}
