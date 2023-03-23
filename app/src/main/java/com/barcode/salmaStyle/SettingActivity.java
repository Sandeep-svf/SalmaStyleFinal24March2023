package com.barcode.salmaStyle;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.UiModeManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.barcode.salmaStyle.R;
import com.barcode.salmaStyle.login.ChangePasswordActivity;
import com.barcode.salmaStyle.utol.Originator;

public class SettingActivity extends Originator {
    TextView toolbar_text,change_password,change_language;
    ConstraintLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar_text=findViewById(R.id.toolbar_text);
        toolbar_text.setText(getString(R.string.setting));
        change_language=findViewById(R.id.setting_language);
        change_password=findViewById(R.id.setting_change_password);
        back=findViewById(R.id.cons_tool_img);
        backListener();
        changepasswordListener();
        languageListener();
    }

    private void languageListener() {
        change_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,LanguageActivity.class);
                intent.putExtra("login_key", "login_value");
                startActivity(intent);
            }
        });
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void changepasswordListener() {
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                intent.putExtra("login_key", "login_value");
                startActivity(intent);
            }
        });
    }
}