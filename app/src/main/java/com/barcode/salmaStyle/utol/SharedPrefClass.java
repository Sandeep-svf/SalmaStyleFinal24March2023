package com.barcode.salmaStyle.utol;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefClass {
    SharedPreferences.Editor  sharedPreferences;
    SharedPreferences prefs ;

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String LANGUAGE = "sharedlanguage";
    public static final String LANGUAGEVARIABLE = "languagevariable";
    public static final String NEWUSERLANGUAGE = "newuserlanguage";


    public SharedPrefClass(Context context){
        sharedPreferences = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE).edit();

        prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
    }

    public void putString(String key,String value){
        sharedPreferences.putString(key,value);
        sharedPreferences.apply();
    }

    public String getString(String key){
       String value = prefs.getString(key,"");

       return value;
    }

    public void putInt(String key,int value){
        sharedPreferences.putInt(key,value);
        sharedPreferences.apply();
    }

    public void putFloat(Float key, float value){
        sharedPreferences.putFloat(String.valueOf(key),value);
        sharedPreferences.apply();
    }

    public Float getFloat(String key){
        float value=prefs.getFloat(key,0);
        return value;
    }

    public int getInt(int key){
        int value = prefs.getInt(String.valueOf(key),0);

        return value;
    }

    public void putBoolean(String key,Boolean value){
        sharedPreferences.putBoolean(key,value);
        sharedPreferences.apply();
    }

    public Boolean getBoolean(String key){
        Boolean value = prefs.getBoolean(key,false);

        return value;
    }

}
