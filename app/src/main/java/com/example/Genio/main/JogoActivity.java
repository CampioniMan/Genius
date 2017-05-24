package com.example.Genio.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class JogoActivity extends AppCompatActivity implements SensorEventListener {

    static Point size;
    SensorManager sensorManager;
    BallView ballView;
    JogoCPU cpu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent it = getIntent();
        ballView = new BallView(this, Integer.parseInt(it.getStringExtra("ehHard")));
        setContentView(ballView);

        cpu = new JogoCPU();

        size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        size.x -= 80;
        size.y -= 320;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (ballView.getBola().getAngulo() >= 360)
                    ballView.getBola().setAngulo(0);// selecionar a cor como escolhida

                if (ballView.getBola().getAngulo() < 360)
                    ballView.getBola().setAngulo(ballView.getBola().getAngulo() + 18);
                h.postDelayed(this, 100);
            }
        }, 100);      // (takes millis)(alterar os dois!)
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop()
    {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            ballView.getBola().setxAce(sensorEvent.values[0]);
            ballView.getBola().setyAce(-sensorEvent.values[1]);
            ballView.getBola().atualizaLocal();
            ballView.getBola().verColisoes();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
