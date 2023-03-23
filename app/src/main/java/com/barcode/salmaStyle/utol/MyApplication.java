package com.barcode.salmaStyle.utol;

import android.app.Application;
import android.app.UiModeManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        //uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
        if (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES){
        }else {
        }
    }
}