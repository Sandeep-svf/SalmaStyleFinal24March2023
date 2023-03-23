package com.barcode.salmaStyle.utol;

import android.app.UiModeManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public abstract class Originator  extends AppCompatActivity {

    public static Originator contextOriginator;
    private SharedPreferences sharedPreferencesLanguageName;
    private String lang_txt;
    private String str_lanuage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contextOriginator = Originator.this;

        //   backCreate();

        //   decimalFormat = new DecimalFormat("#.##");

        sharedPreferencesLanguageName = getSharedPreferences("LANGUAGE_NAME", Context.MODE_PRIVATE);
        if (sharedPreferencesLanguageName.getString("language_text", "").equals("en")) {
            lang_txt = "en";
            str_lanuage = "1";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ps")) {
            lang_txt = "ps";
        } else {
            lang_txt = "en";
            str_lanuage = "1";
            SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
            editor.putString("language_text", lang_txt);
            editor.putString("language_id", str_lanuage);
            editor.apply();
        }

        Locale locale2 = new Locale(lang_txt);
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.setLocale(locale2);
        config2.setLayoutDirection(locale2);
        getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        // registerReceiver(broadcastReceiver, filter);

        ui();
    }

    private void ui() {
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
    }

    public static Originator getContextOriginator() {
        return contextOriginator;
    }

    public String get_language_text() {
        return lang_txt;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }


    private Context updateBaseContextLocale(Context context) {
        sharedPreferencesLanguageName = getSharedPreferences("LANGUAGE_NAME", Context.MODE_PRIVATE);
        if (sharedPreferencesLanguageName.getString("language_text", "").equals("en")) {
            lang_txt = "en";
            str_lanuage = "1";
        } else if (sharedPreferencesLanguageName.getString("language_text", "").equals("ps")) {
            lang_txt = "ps";
        } else {
            lang_txt = "en";
            str_lanuage = "1";
            SharedPreferences.Editor editor = sharedPreferencesLanguageName.edit();
            editor.putString("language_text", lang_txt);
            editor.putString("language_id", str_lanuage);
            editor.apply();
        }

        Locale locale = new Locale(lang_txt);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            return updateResourcesLocale(context, locale);
        }

        return updateResourcesLocaleLegacy(context, locale);
    }

    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    @Override
    protected void onResume() {
        ui();
        super.onResume();
    }

    @Override
    protected void onStart() {
        ui();
        super.onStart();
    }
}

