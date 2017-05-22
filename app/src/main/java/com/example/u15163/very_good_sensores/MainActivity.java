package com.example.u15163.very_good_sensores;

import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    private float xPos, xAccel, xVel = 0.0f, yPos, yAccel, yVel = 0.0f, xMax, yMax;
    private Bitmap ball;
    private SensorManager sensorManager;
    private Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        BallView ballView = new BallView(this);
        setContentView(ballView);

        size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);

        xMax = (float) size.x - 100;
        yMax = (float) size.y - 340;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
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
            xAccel = sensorEvent.values[0];
            yAccel = -sensorEvent.values[1];
            updateBall();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){}

    private void updateBall()
    {
        float frameTime = 0.666f;
        xVel += (xAccel * frameTime);
        yVel += (yAccel * frameTime);

        xPos -= (xVel / 2) * frameTime;
        yPos -= (yVel / 2) * frameTime;

        if (xPos > xMax)
        {
            xPos = xMax;
            xAccel = xVel = 0;
        }
        else if (xPos < 0)
            xPos = xAccel = xVel = 0;

        if (yPos > yMax)
        {
            yPos = yMax;
            yAccel = yVel = 0;
        }
        else if (yPos < 0)
            yPos = yAccel = yVel = 0;
    }

    private class BallView extends View
    {

        public BallView(Context context)
        {
            super(context);
            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

            ball = Bitmap.createScaledBitmap(ballSrc, 100, 100, true);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            Paint paint = new Paint();

            paint.setColor(Color.BLUE);
            canvas.drawRect(0,0,size.x/2,size.y/2,paint);

            paint.setColor(Color.GREEN);
            canvas.drawRect(size.x/2,0,size.x,size.y/2,paint);

            paint.setColor(Color.RED);
            canvas.drawRect(0,size.y/2,size.x/2,size.y,paint);

            paint.setColor(Color.YELLOW);
            canvas.drawRect(size.x/2,size.y/2,size.x,size.y,paint);

            canvas.drawBitmap(ball, xPos, yPos, null);

            invalidate();
        }
    }
}
