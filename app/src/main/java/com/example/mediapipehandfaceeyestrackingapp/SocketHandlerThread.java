package com.example.mediapipehandfaceeyestrackingapp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

public class SocketHandlerThread extends HandlerThread {

    private Handler handler;
    private LandmarkSocket landmarkSocket;

    public SocketHandlerThread(){
        super("SocketHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected void onLooperPrepared() {
        landmarkSocket = new LandmarkSocket();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                landmarkSocket.send(msg);
            }
        };
        landmarkSocket.start();
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public boolean quit() {
        try {
            landmarkSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.quit();
    }
}
