package com.example.sajithjayasekara.batterymanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation animBlink;
    TextView txtBlink;
    private BroadcastReceiver brBatteryLevel = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
            progressBar.setProgress(level);
            TextView textView = findViewById(R.id.textfield);
            textView.setText(Integer.toString(level) + "%");

            if (level <= 20) {
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.redbar));

            } else {
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar));

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        registerReceiver(brBatteryLevel, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        Intent intent = new Intent(this, BMBackgroundService.class);
        startService(intent);

        configureButton();
        //adding animation to the text view
        txtBlink=(TextView)findViewById(R.id.textfield);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        txtBlink.setVisibility(View.VISIBLE);
        txtBlink.startAnimation(animBlink);

    }

    private void configureButton() {

        Button button = findViewById(R.id.nextbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
    }


}
