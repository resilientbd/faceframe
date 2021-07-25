package com.faisal.faceframe.util.googlevision;

import android.content.Context;
import android.media.FaceDetector;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;

class GraphicFaceTracker extends Tracker<Face> {
    private GraphicOverlay mOverlay;
    private FaceGraphic mFaceGraphic;
    private FaceGraphic.FaceDataUpdater faceDataUpdater;
    GraphicFaceTracker(GraphicOverlay overlay, FaceGraphic.FaceDataUpdater faceDataUpdater, FaceDetectListener faceDetectListener, Context context) {
        mOverlay = overlay;
        mFaceGraphic = new FaceGraphic(overlay,faceDetectListener,context);
        this.faceDataUpdater = faceDataUpdater;
    }

    /**
     * Start tracking the detected face instance within the face overlay.
     */
    @Override
    public void onNewItem(int faceId, Face item) {
        mFaceGraphic.setId(faceId);
    }

    /**
     * Update the position/characteristics of the face within the overlay.
     */
    @Override
    public void onUpdate(Detector.Detections<Face> detectionResults, Face face) {
        mOverlay.add(mFaceGraphic);
        mFaceGraphic.updateFace(face);
        if(faceDataUpdater!=null)
        {
            faceDataUpdater.onTrackFaceData(face);
        }
    }

    /**
     * Hide the graphic when the corresponding face was not detected.  This can happen for
     * intermediate frames temporarily (e.g., if the face was momentarily blocked from
     * view).
     */
    @Override
    public void onMissing(Detector.Detections<Face> detectionResults) {
        mOverlay.remove(mFaceGraphic);
    }

    /**
     * Called when the face is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    @Override
    public void onDone() {
        mOverlay.remove(mFaceGraphic);
    }
}
