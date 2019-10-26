package com.example.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View view = findViewById(R.id.roundProgressBar);

       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ObjectAnimator.ofInt(view,"progress",0,100).setDuration(3000).start();
           }
       });

    }
}
