package com.example.chathumina.brainiac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class LogoTrans extends AppCompatActivity {

    private TextView xlabs;
    private ImageView appLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_trans);
        SharedPreferences save = getSharedPreferences("GameStats", MODE_PRIVATE);
        boolean saveGame = save.getBoolean("save",false);
        if (saveGame){
            SharedPreferences.Editor editorS = getSharedPreferences("Hints", MODE_PRIVATE).edit();
            editorS.clear();
            editorS.commit();

        }else{
            SharedPreferences.Editor editorS = getSharedPreferences("Hints", MODE_PRIVATE).edit();
            editorS.clear();
            editorS.commit();
            SharedPreferences.Editor editor = getSharedPreferences("GameStats", MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
        }

        xlabs = (TextView)findViewById(R.id.xlabs);
        appLogo = (ImageView)findViewById(R.id.logox);
        Animation splash = AnimationUtils.loadAnimation(this,R.anim.logintransition);
        xlabs.startAnimation(splash);
        appLogo.startAnimation(splash);
        final Intent signup = new Intent(this, Menu.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(signup);
                    finish();
                }
            }
        };
        timer.start();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();
    }




    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
