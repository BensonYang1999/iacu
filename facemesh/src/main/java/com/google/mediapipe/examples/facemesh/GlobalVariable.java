package com.google.mediapipe.examples.facemesh;

import android.app.Application;

public class GlobalVariable extends Application{
    private static GlobalVariable mInstance = null;
    private String Acupoint = "full";

    public static GlobalVariable getInstance(){
        if (mInstance == null){
            synchronized (GlobalVariable.class){
                if (mInstance == null){
                    mInstance = new GlobalVariable();
                }
            }
        }
        return mInstance;
    }

    public void setAcupoint(String acupoint){
        this.Acupoint = acupoint;
    }

    public String getAcupoint(){
        return Acupoint;
    }
}
