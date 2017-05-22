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
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Point size;
    private SensorManager sensorManager;
    private BallView ballView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ballView = new BallView(this);
        setContentView(ballView);

        size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        size.x -= 80;
        size.y -= 320;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ballView.bola.xAce = sensorEvent.values[0];
            ballView.bola.yAce = -sensorEvent.values[1];
            ballView.bola.atualizaLocal();
            ballView.bola.verColisoes();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class Ball
    {
        Point local;
        float xVel, yVel, xAce, yAce;
        Bitmap textura;
        final Point tamanho = new Point(75, 75);
        int raio = 38;

        public Ball()
        {
            xVel = yVel = xAce = yAce = 0.0f;
            local = new Point(0, 0);
            this.textura = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.balldois), tamanho.x, tamanho.y, true);
        }

        private void atualizaLocal()
        {
            float frameTime = 0.666f;

            this.xVel += (this.xAce * frameTime);
            this.yVel += (this.yAce * frameTime);

            float xS = (this.xVel / 2) * frameTime;
            float yS = (this.yVel / 2) * frameTime;

            this.local.x -= xS;
            this.local.y -= yS;
        }

        private void verColisoes()
        {
            if (this.local.x > size.x) {
                this.local.x = size.x;
                this.xAce = 0;
                this.xVel = 0;
            }
            else if (this.local.x < 0) {
                this.local.x = 0;
                this.xAce = 0;
                this.xVel = 0;
            }

            if (this.local.y > size.y) {
                this.local.y = size.y;
                this.yAce = 0;
                this.yVel = 0;
            }
            else if (this.local.y < 0) {
                this.local.y = 0;
                this.yAce = 0;
                this.yVel = 0;
            }
        }

        private boolean estaEm(int cor)
        {
            if (cor == Color.BLUE)
            {
                if (local.x + raio >= 0 && local.y + raio >= 0 && local.x + raio <= size.x/2 + 38 && local.y + raio <= size.y/2 + 38)
                {
                    return true;
                }
                return false;
            }
            else if (cor == Color.GREEN)
            {
                if (local.x + raio >= size.x/2 + 38 && local.y + raio >= 0 && local.x + raio <= size.x + 76 && local.y + raio <= size.y/2 + 38)
                {
                    return true;
                }
                return false;
            }
            else if (cor == Color.RED)
            {
                if (local.x + raio >= 0 && local.y + raio >= size.y/2 + 38 && local.x + raio <= size.x/2 + 38 && local.y + raio <= size.y + 76)
                {
                    return true;
                }
                return false;
            }
            else if (cor == Color.YELLOW)
            {
                if (local.x + raio >= size.x/2 + 38 && local.y + raio >= size.y/2 + 38 && local.x + raio <= size.x + 76 && local.y + raio <= size.y + 76)
                {
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private class BallView extends View {

        private Ball bola;

        public BallView(Context context) {
            super(context);
            bola = new Ball();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            Paint paint = new Paint();

            // B G
            // R Y
            // left top right bottom

            paint.setColor(Color.BLUE);
            if (bola.estaEm(Color.BLUE))
                paint.setColor(Color.GRAY);
            canvas.drawRect(0          ,0          , size.x/2 + 38, size.y/2 + 38, paint);

            paint.setColor(Color.GREEN);
            if (bola.estaEm(Color.GREEN))
                paint.setColor(Color.GRAY);
            canvas.drawRect(size.x/2 + 38, 0         , size.x + 76  , size.y/2 + 38,paint);

            paint.setColor(Color.RED);
            if (bola.estaEm(Color.RED))
                paint.setColor(Color.GRAY);
            canvas.drawRect(0          ,size.y/2 + 38, size.x/2 + 38, size.y + 76,paint);

            paint.setColor(Color.YELLOW);
            if (bola.estaEm(Color.YELLOW))
                paint.setColor(Color.GRAY);
            canvas.drawRect(size.x/2 + 38, size.y/2 + 38, size.x + 76  , size.y + 76,paint);

            canvas.drawBitmap(this.bola.textura, bola.local.x, bola.local.y, null);

            invalidate();
        }
    }
}
