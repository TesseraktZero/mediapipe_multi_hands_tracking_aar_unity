package com.example.mediapipehandfaceeyestrackingapp;

import android.os.Message;

import com.google.mediapipe.formats.proto.ClassificationProto.ClassificationList;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList;

import java.util.List;

// Either the handednesses or the landmarkLists can come first
// When both the data are received, pair them up and send to the socket
public class HandPairSender {
    public List<ClassificationList> handednesses = null;
    public List<NormalizedLandmarkList> landmarkLists = null;
    private final SocketHandlerThread socketHandlerThread;
    private final OrientationSensorManager orientationManager;
    public final Object lock = new Object();

    public HandPairSender(SocketHandlerThread socketHandlerThread, OrientationSensorManager orientationManager){
        this.socketHandlerThread = socketHandlerThread;
        this.orientationManager = orientationManager;
    }

    public void UpdateHandednesses(List<ClassificationList> handednesses){
        synchronized(this.lock) {
            this.handednesses = handednesses;
            sendIfPaired();
        }
    }

    public void UpdateLandmarkLists(List<NormalizedLandmarkList> landmarkLists){
        synchronized(this.lock) {
            this.landmarkLists = landmarkLists;
            sendIfPaired();
        }
    }

    private boolean IsPaired(){
        return handednesses != null && landmarkLists != null;
    }

    private void sendIfPaired(){
        if(!IsPaired())
            return;

        if(landmarkLists.size() > 0 && handednesses.size() > 0){
            NormalizedLandmarkList landmarkList = landmarkLists.get(0);

            String handedness = handednesses.get(0).getClassification(0).getLabel();
            LandmarkType type = "Left".equals(handedness) ?
                    LandmarkType.LeftHand : LandmarkType.RightHand;

            sendLandmarkList(type, landmarkList);
        }
        if(landmarkLists.size() > 1 && handednesses.size() > 1){
            NormalizedLandmarkList landmarkList = landmarkLists.get(1);
            String handedness = handednesses.get(1).getClassification(0).getLabel();
            LandmarkType type = "Left".equals(handedness) ?
                    LandmarkType.LeftHand : LandmarkType.RightHand;

            sendLandmarkList(type, landmarkList);
            sendOrientation();
        }

        handednesses = null;
        landmarkLists = null;
    }

    private void sendLandmarkList(LandmarkType landmarkType, NormalizedLandmarkList landmarkList) {
        if (landmarkList.getLandmarkList().size() == 0) {
            return;
        }
        Message message = Message.obtain(socketHandlerThread.getHandler());
        message.what = landmarkType.getValue();
        message.obj = landmarkList;
        message.sendToTarget();
    }

    private void sendOrientation() {
        float[] orientation = orientationManager.getOrientationAngles();
        Message message = Message.obtain(socketHandlerThread.getHandler());
        message.what = LandmarkType.Orientation.getValue();
        message.obj = orientation;
        message.sendToTarget();
    }

}
