package com.example.Genio.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;

import java.util.Timer;

public class BallView extends View
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////ATRIBUTOS///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    final Handler h = new Handler();
    static final int[] CORES = new int[]{
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.YELLOW};
    static final int[][] CORESRGB = new int[][]{
            {0, 0, 255},
            {0, 255, 0},
            {255, 0, 0},
            {255, 255, 0}};
    static final int[][] CORESESCURASRGB = new int[][]{
            {0, 0, 153},
            {0, 153, 0},
            {153, 0, 0},
            {204, 204, 0}};

    private int[] vetorCores;
    private boolean estaMostrando, podeFazerIf, printaMeio, estaNoUltimo, pararHandler;
    private int cronometro, tempoHabilitado, qtoEscuro;
    private float velEscuridao;

    private Ball bola;
    private JogoCPU cpu;
    private GerenciaCores corGeren;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////CONSTRUTOR///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public BallView(Context context, int _ehHard)
    {
        super(context);

        bola = new Ball(getResources());
        vetorCores = new int[]{
                Color.BLUE,
                Color.GREEN,
                Color.RED,
                Color.YELLOW};
        cpu = new JogoCPU(_ehHard);
        corGeren = new GerenciaCores();

        estaMostrando = printaMeio = podeFazerIf = true;
        estaNoUltimo = false;

        tempoHabilitado = JogoActivity.tempoDeSegundos(1);
        qtoEscuro = 0;
        velEscuridao = 25f;

        this.cpu.sortear(vetorCores);

        comecarHandler();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////GETTERS E SETTERS///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Ball getBola()
    {
		return bola;
	}

    public JogoCPU getCPU()
    {
        return this.cpu;
    }

    public boolean isMostrando()
    {
        return this.estaMostrando;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////MÉTODOS PRINCIPAIS/////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void atualizar()
    {
        if (bola.estaEm(Color.BLUE))
        {
            if (!estaMostrando)
                vetorCores[0] = Color.rgb(CORESESCURASRGB[0][0],
                                          CORESESCURASRGB[0][1],
                                          CORESESCURASRGB[0][2]);
            else
                vetorCores[0] = Color.rgb(CORESRGB[0][0],
                                          CORESRGB[0][1],
                                          CORESRGB[0][2] - qtoEscuro);
            vetorCores[1] = CORES[1];
            vetorCores[2] = CORES[2];
            vetorCores[3] = CORES[3];

            if (bola.getLastColor() != Color.BLUE && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.BLUE);
        }

        else if (bola.estaEm(Color.GREEN))
        {
            if (!estaMostrando)
                vetorCores[1] = Color.rgb(CORESESCURASRGB[1][0],
                                          CORESESCURASRGB[1][1],
                                          CORESESCURASRGB[1][2]);
            else
                vetorCores[1] = Color.rgb(CORESRGB[1][0],
                                          CORESRGB[1][1] - qtoEscuro,
                                          CORESRGB[1][2]);
            vetorCores[0] = CORES[0];
            vetorCores[2] = CORES[2];
            vetorCores[3] = CORES[3];
            if (bola.getLastColor() != Color.GREEN && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.GREEN);
        }

        else if (bola.estaEm(Color.RED))
        {
            if (!estaMostrando)
                vetorCores[2] = Color.rgb(CORESESCURASRGB[2][0],
                                          CORESESCURASRGB[2][1],
                                          CORESESCURASRGB[2][2]);
            else
                vetorCores[2] = Color.rgb(CORESRGB[2][0] - qtoEscuro,
                                          CORESRGB[2][1],
                                          CORESRGB[2][2]);
            vetorCores[1] = CORES[1];
            vetorCores[0] = CORES[0];
            vetorCores[3] = CORES[3];
            if (bola.getLastColor() != Color.RED && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.RED);
        }

        else if (bola.estaEm(Color.YELLOW))
        {
            if (!estaMostrando)
                vetorCores[3] = Color.rgb(CORESESCURASRGB[3][0],
                                          CORESESCURASRGB[3][1],
                                          CORESESCURASRGB[3][2]);
            else
                vetorCores[3] = Color.rgb(CORESRGB[3][0] - (qtoEscuro/2),
                                          CORESRGB[3][1] - (qtoEscuro/2),
                                          CORESRGB[3][2]);
            vetorCores[1] = CORES[1];
            vetorCores[2] = CORES[2];
            vetorCores[0] = CORES[0];
            if (bola.getLastColor() != Color.YELLOW && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.YELLOW);
        }
    }

	@Override
    protected void onDraw(Canvas canvas)
    {
        try
        {
            Paint paint = new Paint();

            if (podeFazerIf && estaMostrando && contou(tempoHabilitado))
            {
                if (cpu.isInicioDeFase())
                {
                    cpu.setInicioDeFase(false);
                    cpu.reseta();
                }
                estaMostrando = corGeren.certezaMostrando();
                qtoEscuro = 0;
                velEscuridao = 25f;
                if (!estaMostrando)
                {
                    pararHandler = true;
                    cpu.setInicioDeFase(true);
                    cronometro++;
                }
                else
                    corGeren.mudarCor();
                podeFazerIf = false;
            }

            atualizar();

            paint.setColor(vetorCores[0]);
            canvas.drawRect(0, 0,
                            JogoActivity.getSize().x/2 + 38,
                            JogoActivity.getSize().y/2 + 38, paint);

            paint.setColor(vetorCores[1]);
            canvas.drawRect(JogoActivity.getSize().x/2 + 38, 0,
                            JogoActivity.getSize().x + 76,
                            JogoActivity.getSize().y/2 + 38, paint);

            paint.setColor(vetorCores[2]);
            canvas.drawRect(0,
                            JogoActivity.getSize().y/2 + 38,
                            JogoActivity.getSize().x/2 + 38,
                            JogoActivity.getSize().y + 76, paint);

            paint.setColor(vetorCores[3]);
            canvas.drawRect(JogoActivity.getSize().x/2 + 38,
                            JogoActivity.getSize().y/2 + 38,
                            JogoActivity.getSize().x + 76  ,
                            JogoActivity.getSize().y + 76,paint);

            if (!estaMostrando && !estaNoUltimo)
            {
                if (printaMeio)
                {
                    this.bola.getLocal().x = (JogoActivity.getSize().x/2 + 38) - Ball.getRaio();
                    this.bola.getLocal().y = (JogoActivity.getSize().y/2 + 38) - Ball.getRaio();
                    printaMeio = false;
                }

                canvas.drawBitmap(this.bola.getTextura(),
                        this.bola.getLocal().x,
                        this.bola.getLocal().y, null);

                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);

                RectF rectF = new RectF(
                        bola.getLocal().x - 30,
                        bola.getLocal().y - 30,
                        bola.getLocal().x + 105,
                        bola.getLocal().y + 105);

                paint.setStrokeWidth(4);
                canvas.drawArc(rectF, (float) 0, this.bola.getAngulo(), false, paint);
                paint.setStrokeWidth(1);
            }

            canvas.drawText(vetorCores[0]+"", 100, 100, paint);
            canvas.drawText(vetorCores[1]+"", 100, 200, paint);
            canvas.drawText(vetorCores[2]+"", 100, 300, paint);
            canvas.drawText(vetorCores[3]+"", 100, 400, paint);

            invalidate();
        }
        catch(Exception e){}
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////MÉTODOS AUXILIARES////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void comecarHandler()
    {
        this.pararHandler = false;
        this.cronometro = 1;

        this.h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                cronometro++;
                podeFazerIf = true;
                qtoEscuro += velEscuridao;
                velEscuridao -= 0.5f;
                if (!pararHandler)
                    h.postDelayed(this, JogoActivity.TEMPO);
            }
        }, JogoActivity.TEMPO);
    }

    public boolean contou(int segundos)
    {
        return cronometro % segundos == 0;
    }

    public void remomecarFase()
    {
        this.cpu.reseta();
        this.estaMostrando = this.printaMeio = true;
        this.bola.setLocal(new Point(-50, -50));
        this.comecarHandler();
        this.bola.setAngulo(0);
        this.cpu.sortear(this.CORES);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////CLASSE AUXILIAR/////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    *   Essa classe serve para organizar as cores que serão colocadas na tela, ela é interna porque
    *   utiliza variáveis que estão nessa classe.
     */
    private class GerenciaCores
    {
        public void mudarCor()
        {
            switch (cpu.getAtual())
            {
                case Color.BLUE:
                    bola.getLocal().x = bola.getLocal().y = 0;
                    break;

                case Color.GREEN:
                    bola.getLocal().x = JogoActivity.getSize().x/2 + 38;
                    bola.getLocal().y = 0;
                    break;

                case Color.RED:
                    bola.getLocal().x = 0;
                    bola.getLocal().y = JogoActivity.getSize().y/2 + 38;
                    break;

                case Color.YELLOW:
                    bola.getLocal().x = JogoActivity.getSize().x/2 + 38;
                    bola.getLocal().y = JogoActivity.getSize().y/2 + 38;
                    break;
            }
        }

        public boolean certezaMostrando()
        {
            boolean acabou = false;
            if (cpu.estaNoUltimo())
            {
                acabou = true;
                cpu.reseta();
            }
            cpu.avancar();
            return !acabou;
        }
    }
}