package com.barcode.salmaStyle.utol;


import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public static Originator contextOriginator;
    private SharedPreferences sharedPreferencesLanguageName;
    private String lang_txt;
    private String str_lanuage;

}