package com.example.Genio.main;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.Toast;

public class JogoActivity extends AppCompatActivity implements SensorEventListener {

    private static Point size;
    private SensorManager sensorManager;
    private BallView ballView;
    static final int TEMPO = 50;

    public static Point getSize()
    {
        return size;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        size.x -= 80;
        size.y -= 320;

        Intent it = getIntent();
        ballView = new BallView(this, Integer.parseInt(it.getStringExtra("ehHard")));
        setContentView(ballView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                if (ballView.getImagemBola().getAngulo() >= 360) // deu a volta
                {
                    ballView.getImagemBola().setAngulo(0);// selecionar a cor como escolhida
                    acabouRetacao();
                }
                else
                    ballView.getImagemBola().setAngulo(ballView.getImagemBola().getAngulo() + 9);

                if (ballView.isMostrando())
                    ballView.getImagemBola().setAngulo(0);// selecionar a cor como escolhida

                h.postDelayed(this, TEMPO);
            }
        }, TEMPO);      // (takes millis)(alterar os dois!)
    }

    // você passa os segundos e ele devolve quantas chamadas da função demora (tipo pra 1s demora 20 chamadas)
    static public int tempoDeSegundos(int segundos)
    {
        return (1000*segundos)/TEMPO;
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
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && !ballView.isMostrando())
        {
            ballView.getImagemBola().setCoordAce(new Point((int)sensorEvent.values[0], (int)-sensorEvent.values[1]));
            ballView.getImagemBola().atualizaLocal();
            ballView.getImagemBola().verColisoes();
        }
    }

    public void acabouRetacao()
    {
        // se o atual é diferente do que ele tava, perdeu
        if (ballView.getCPU().getAtual() != ballView.getImagemBola().getLastColor())
            finish();

            // se não está mostrando
        else if (!ballView.isMostrando())
        {
            // acertou e o índice tá no último
            if (ballView.getCPU().estaNoUltimo())
                ballView.remomecarFase();

            // acertou e o índice não tá no último
            else
                ballView.getCPU().avancar();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void Alerta(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }
}
