package com.barcode.salmaStyle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;


import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.utol.Originator;
import com.barcode.salmaStyle.utol.SharedPrefClass;
import com.barcode.salmaStyle.utol.Singleton;

import java.util.Locale;

public class LanguageActivity extends Originator {
    TextView toolbar_text;
    ConstraintLayout back;
    Switch switch_farsi, switch_english;
    SharedPrefClass sharedPrefClass;
    String str_lanuage = "1";
    SharedPreferences sharedPreferencesLanguageName;
    Context context;
    SharedPreferences sharedPreferences;
    Singleton singleton = Singleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar_text=findViewById(R.id.toolbar_text);
        back=findViewById(R.id.cons_tool_img);

        switch_farsi = (Switch) findViewById(R.id.switch_farsi);
        switch_english = (Switch) findViewById(R.id.switch_english);

        toolbar_text.setText(getString(R.string.change_language));
        sharedPrefClass=new SharedPrefClass(this);
        sharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE);
        context= LanguageActivity.this;
        sharedPreferencesLanguageName = getSharedPreferences("LANGUAGE_NAME", Context.MODE_PRIVATE);
        singleton.language_name = sharedPreferencesLanguageName.getString("language_text", "");

        if (sharedPrefClass.getString(SharedPrefClass.LANGUAGE).equals("ps")) {
            switch_farsi.setChecked(true);
            Log.e("valuecheck","spanish    " +sharedPrefClass.getString(SharedPrefClass.LANGUAGE));
        } else  {
            switch_english.setChecked(true);
        }

        backListener();

        switch_farsi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    switch_english.setChecked(false);
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGE,"ps");
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGEVARIABLE,"es");
                    Log.e("value", "spanish       " + "     " + "11");

                    Locale locale2 = new Locale("ps");
                    Locale.setDefault(locale2);
                    Configuration config2 = new Configuration();
                    config2.locale = locale2;
                    getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());

                    str_lanuage = "2";
                    singleton.language_name = "ps";

                    sharedPreferencesLanguageName = getSharedPreferences("LANGUAGE_NAME", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
                    editor.putString("language_text", "ps");
                    editor.putString("language_id", str_lanuage);
                    editor.apply();

                    Intent intents = new Intent(context, MainActivity.class);
                    intents.putExtra("login_key", "login_value");
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intents);

                } else {
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGE,"en");
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGEVARIABLE,"en");
                    switch_english.setChecked(true);
                }
            }
        });

        switch_english.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    sharedPrefClass.putString(SharedPrefClass.LANGUAGE,"en");
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGEVARIABLE,"en");
                    //  switch_english.setChecked(true);
                    switch_farsi.setChecked(false);

                    Locale locale2 = new Locale("en");
                    Locale.setDefault(locale2);
                    Configuration config2 = new Configuration();
                    config2.locale = locale2;
                    getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());
                    str_lanuage = "1";
                    singleton.language_name = "en";
                    sharedPreferencesLanguageName = getSharedPreferences("LANGUAGE_NAME", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
                    editor.putString("language_text", "en");
                    editor.putString("language_id", str_lanuage);
                    editor.apply();

                    Intent intents = new Intent(context, MainActivity.class);
                    intents.putExtra("login_key", "login_value");
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intents);

                } else {
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGE,"ps");
                    sharedPrefClass.putString(SharedPrefClass.LANGUAGEVARIABLE,"ps");
                    switch_farsi.setChecked(true);
                }

            }
        });
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
