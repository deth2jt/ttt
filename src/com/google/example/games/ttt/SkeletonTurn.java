/*
 * Copyright (C) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.example.games.ttt;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * Basic turn data. It's just a blank data string and a turn number counter.
 * 
 * @author wolff
 * 
 */
public class SkeletonTurn {

    public static final String TAG = "EBTurnn";

    public String winner = "";
    public int turnCounter;
    public boolean isFinish = false;
    public int pts = 0;
    public JSONArray array;

    public SkeletonTurn() {
    }

    // This is the byte array we will write out to the TBMP API.
    @SuppressLint("NewApi")
	public byte[] persist() {
        JSONObject retVal = new JSONObject();

        try {
            retVal.put("data", winner);
            retVal.put("turnCounter", turnCounter);
            retVal.put("Pts", pts);
            retVal.put("isFinish", isFinish);
            retVal.put("Array", array);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String st = retVal.toString();

        Log.d(TAG, "==== PERSISTING\n" + st);

        return st.getBytes(Charset.forName("UTF-16"));
    }

    // Creates a new instance of SkeletonTurn.
    static public SkeletonTurn unpersist(byte[] byteArray) {

        if (byteArray == null) {
            Log.d(TAG, "Empty array---possible bug.");
            return new SkeletonTurn();
        }

        String st = null;
        try {
            st = new String(byteArray, "UTF-16");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }

        Log.d(TAG, "====UNPERSIST \n" + st);

        SkeletonTurn retVal = new SkeletonTurn();

        try {
            JSONObject obj = new JSONObject(st);
            
            if (obj.has("winner")) {
                retVal.winner = obj.getString("winner");
            }
            if (obj.has("turnCounter")) {
                retVal.turnCounter = obj.getInt("turnCounter");
            }
            if (obj.has("isFinish")) {
                retVal.isFinish = obj.getBoolean("isFinish");
            }
            if (obj.has("Pts")) {
                retVal.pts = obj.getInt("Pts");
            }
            if (obj.has("Array")) {
                retVal.array = obj.getJSONArray("Array");
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retVal;
    }
}
