package com.lincoln4791.dailyexpensemanager.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lincoln4791.dailyexpensemanager.R;

public class SplashActivity extends AppCompatActivity {
    private Animation animation_iv,animation_tv;
    private ImageView imageView;
    private TextView tv1,tv2,tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        imageView = findViewById(R.id.iv_splash);
        tv1 = findViewById(R.id.tv1_splash);
        tv2 = findViewById(R.id.tv2_splash);
        tv3 = findViewById(R.id.tv3_splash);


        animation_iv = AnimationUtils.loadAnimation(this,R.anim.splash_animation);
        animation_iv.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        imageView.startAnimation(animation_iv);


    }


    @Override
    protected void onStart() {
        super.onStart();



    }
}