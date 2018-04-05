package com.example.chathumina.brainiac;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Settings extends AppCompatActivity{

    private SharedPreferences peferenceSettings;
    private SharedPreferences.Editor peferenceEditor;
    private SwitchCompat hints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        hints = (SwitchCompat) findViewById(R.id.switch_compat);

        peferenceSettings = getSharedPreferences("Hints",MODE_PRIVATE);
        peferenceEditor = peferenceSettings.edit();
        boolean switch1= peferenceSettings.getBoolean("service_status", false);

        if(switch1){
            hints.setChecked(true);
        }else {
            hints.setChecked(false);
        }


        hints.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                SharedPreferences.Editor editorS = getSharedPreferences("Hints", MODE_PRIVATE).edit();
                editorS.putBoolean("service_status", hints.isChecked());
                editorS.commit();

            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // to do
        SharedPreferences.Editor editorS = getSharedPreferences("Hints", MODE_PRIVATE).edit();
        editorS.putBoolean("service_status", hints.isChecked());
        editorS.commit();
        super.onBackPressed();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
