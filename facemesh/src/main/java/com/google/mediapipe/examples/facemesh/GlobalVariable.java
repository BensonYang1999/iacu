package com.google.mediapipe.examples.facemesh;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;

public class GlobalVariable extends Application{
    private static GlobalVariable mInstance = null;
    private String Acupoint[] = {"test"};
    private JSONArray acu_jsonarry = null;
    private int acu_idx[] = {0};

    private String Disease[] = {"test"};
    private JSONArray dis_jsonarry = null;
    private int dis_idx[] = {0};

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

    public void setAcupoint(String[] acupoint) {
        this.Acupoint = acupoint;
    }

    public void setAcuJson(String jsonArray) throws JSONException {
        this.acu_jsonarry = new JSONArray(jsonArray);
    }

    public void setAcuIdx(int[] index) {
        this.acu_idx = index;
    }


    public String[] getAcupoint() {
        return Acupoint;
    }

    public JSONArray getAcuJson() {
        return acu_jsonarry;
    }

    public int[] getAcuIdx() {
        return acu_idx;
    }


    public void setDisease(String[] disease) {
        this.Disease = disease;
    }

    public void setDisJson(String jsonArray) throws JSONException {
        this.dis_jsonarry = new JSONArray(jsonArray);
    }

    public void setDisIdx(int[] index) {
        this.dis_idx = index;
    }


    public String[] getDisease() {
        return Disease;
    }

    public JSONArray getDisJson() {
        return dis_jsonarry;
    }

    public int[] getDisIdx() {
        return dis_idx;
    }
}
