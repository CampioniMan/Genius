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

    static Point size;
    SensorManager sensorManager;
    BallView ballView;
    boolean perdeu;
    public static final int TEMPO = 50;

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

        perdeu = false;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        final Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (ballView.getBola().getAngulo() >= 360) // deu a volta
                {
                    ballView.getBola().setAngulo(0);// selecionar a cor como escolhida
                    acabouRetacao();
                }
                else
                {
                    ballView.getBola().setAngulo(ballView.getBola().getAngulo() + 9);
                }
                if (ballView.isMostrando())
                {
                    ballView.getBola().setAngulo(0);// selecionar a cor como escolhida
                }
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
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            if (!ballView.isMostrando())
            {
                ballView.getBola().setxAce(sensorEvent.values[0]);
                ballView.getBola().setyAce(-sensorEvent.values[1]);
                ballView.getBola().atualizaLocal();
                ballView.getBola().verColisoes();
            }
        }
    }

    public void acabouRetacao()
    {
        if (ballView.getCPU().getAtual() != ballView.getBola().getLastColor()) // se o atual é diferente do que ele tava, perdeu
        {
            perdeu = true;
            finish();
        }
        else if (!ballView.isMostrando()) // se não está mostrando
        {
            if (ballView.getCPU().estaNoUltimo()) // acertou e o índice tá no último
            {
                ballView.getCPU().reseta();
                ballView.setMostrando(true);
                ballView.setPrintarNoMeio(true);
                ballView.getBola().setLocal(new Point(-50, -50));
                ballView.comecarHandler();
                ballView.cronometro = 0;
                ballView.getBola().setAngulo(0);
                ballView.setPrintarNoMeio(true);
                ballView.getCPU().sortear(this.ballView.CORES);
            }
            else // acertou e o índice não tá no último
            {
                ballView.getCPU().avancar();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void Alerta(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }
}
