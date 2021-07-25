package com.faisal.faceframe.util.googlevision;

import android.content.Context;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

public class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
    private GraphicOverlay mGraphicOverlay;
    private FaceGraphic.FaceDataUpdater faceDataUpdater;
    private FaceDetectListener faceDetectListener;
    private Context context;
    public GraphicFaceTrackerFactory(GraphicOverlay mGraphicOverlay, FaceGraphic.FaceDataUpdater faceDataUpdater,FaceDetectListener faceDetectListener,Context context) {
        this.mGraphicOverlay = mGraphicOverlay;
        this.faceDataUpdater = faceDataUpdater;
        this.faceDetectListener = faceDetectListener;
        this.context = context;
    }

    @Override
    public Tracker<Face> create(Face face) {
        return new GraphicFaceTracker(mGraphicOverlay,faceDataUpdater,faceDetectListener,context);
    }
}