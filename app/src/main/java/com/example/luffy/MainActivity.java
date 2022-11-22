package com.example.luffy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView tv;
    ProgressBar pb;
    LinearLayout l;
    MediaPlayer mp;
    final Handler handler = new Handler();
    private SensorManager sensorManager;
    private Sensor accelerometer;
    int X, Y, Z;
    int CurrentProgress = 0;
    int N = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.ScoreID);
        pb = findViewById(R.id.progressBar);
        l = findViewById(R.id.LinearLayoutID);
        mp = MediaPlayer.create(this, R.raw.pop_effect);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        tv.setText(new StringBuilder().append("X:").append(event.values[0]).append("\n").
                append("Y:").append(event.values[1]).append("\n").
                append("Z:").append(event.values[2]).toString());
        X = (int) event.values[0];
        Y = (int) event.values[1];
        Z = (int) event.values[2];

        pb.setProgress(CurrentProgress);
        pb.setMax(N);

        if (moving(X, Y)) {
            int score = Math.abs(X + Y);

            CurrentProgress += score;
        }

        Runnable r = () -> mp.stop();
        if (CurrentProgress >= N) {
            //l.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.popped));
            l.setBackground(getBaseContext().getDrawable(R.drawable.popped));
            mp.start();
            handler.postDelayed(r, 200);
            pb.setVisibility(View.GONE);
        }
    }

    private boolean moving(int x, int y) {
        return Math.abs(x) >= 4 && Math.abs(y) >= 4;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}