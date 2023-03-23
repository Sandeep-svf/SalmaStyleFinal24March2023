package com.barcode.salmaStyle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.barcode.salmaStyle.R;

public class AppGuidanceActivity extends AppCompatActivity {
    TextView toolbar_text;
    ConstraintLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guidance);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar_text=findViewById(R.id.toolbar_text);
        back=findViewById(R.id.cons_tool_img);
        toolbar_text.setText(R.string.app_guidance);
        backListener();
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}