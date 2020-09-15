package com.example.mediapipehandfaceeyestrackingapp;

public enum LandmarkType {
    None(0),
    LeftHand(1),
    RightHand(2),
    Orientation(3),
    ;

    private final int value;
    private LandmarkType(int value){
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
