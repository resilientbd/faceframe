
package com.faisal.faceframe.util.googlevision.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceDataRecieveModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("reg-id")
    @Expose
    private Integer regId;
    @SerializedName("face-id")
    @Expose
    private Integer faceId;
    @SerializedName("device-id")
    @Expose
    private Integer deviceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRegId() {
        return regId;
    }

    public void setRegId(Integer regId) {
        this.regId = regId;
    }

    public Integer getFaceId() {
        return faceId;
    }

    public void setFaceId(Integer faceId) {
        this.faceId = faceId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

}
