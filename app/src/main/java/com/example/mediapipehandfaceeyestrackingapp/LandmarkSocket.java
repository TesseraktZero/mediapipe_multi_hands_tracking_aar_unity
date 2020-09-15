package com.example.mediapipehandfaceeyestrackingapp;

import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList;
import com.google.mediapipe.formats.proto.RectProto.NormalizedRect;
import com.google.protobuf.CodedOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class LandmarkSocket {
    private static final int PORT = 9500;

    private Socket socket = null;
    private OutputStream outputStream = null;
    CodedOutputStream codedOutput = null;
    public boolean socketConnected = false;

    public void start() {
        while (true) {
            try {
                System.out.println("Starting Connection");
                socketConnected = false;
                //connect to the localhost port forwarded from PC
                socket = new Socket("127.0.0.1", PORT);
                outputStream = socket.getOutputStream();
                codedOutput = CodedOutputStream.newInstance(outputStream);
                socketConnected = true;
                System.out.println("Device Connected");
                return;
            } catch(IOException e) {
                System.out.println("Error connecting:");
                e.printStackTrace();
                try {
                    close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                SystemClock.sleep(3000);
                System.out.println("Attempting to reconnect");
            }
        }
    }

    public synchronized void send(Message message) {
        if(!socketConnected)
            return;

        try {
            LandmarkType type = LandmarkType.values()[message.what];

            codedOutput.writeRawByte(message.what);

            switch (type){
                case LeftHand:
                case RightHand:
                    NormalizedLandmarkList landmarkList = (NormalizedLandmarkList) message.obj;
                    int landmarkSize = landmarkList.getSerializedSize();
                    codedOutput.writeInt32NoTag(landmarkSize);
                    landmarkList.writeTo(codedOutput);
                    break;
                case Orientation:
                    float[] orientationAngles = (float[]) message.obj;
                    codedOutput.writeFloatNoTag(orientationAngles[0]);
                    codedOutput.writeFloatNoTag(orientationAngles[1]);
                    codedOutput.writeFloatNoTag(orientationAngles[2]);
                    break;
                default:
                    break;
            }
            codedOutput.flush();

        } catch (IOException e) {
            socketConnected = false;
            System.out.println("Exception occurred:");
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        if(socket != null)
            socket.close();
    }

}
