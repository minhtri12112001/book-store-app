package com.example.bookstore;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.text_view);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);

        progressAnimation();
    }

    public void progressAnimation(){
        ProgressBarAnimation anim = new ProgressBarAnimation(this, progressBar, textView, 0f, 100f);
        anim.setDuration(50000);
        progressBar.setAnimation(anim);
    }
}
