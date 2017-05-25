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
    private Ball bola;
    public static final int[] CORES = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};
    public int[] vetorCores;
    private JogoCPU cpu;
    private boolean estaMostrando, podeFazerIf, printaMeio, estaNoUltimo, pararHandler;
    private int cronometro;
    final Handler h = new Handler();

    public BallView(Context context, int _ehHard)
    {
        super(context);

        bola = new Ball(getResources());
        vetorCores = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};
        cpu = new JogoCPU(_ehHard);

        estaMostrando = printaMeio = podeFazerIf = true;
        estaNoUltimo = false;

        comecarHandler();
    }
    
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

    public void setMostrando(boolean _mostrando)
    {
        this.estaMostrando = _mostrando;
    }

	public void setBola(Ball bola)
    {
		this.bola = bola;
	}

    public void setPrintarNoMeio(boolean printar)
    {
        this.printaMeio = printar;
    }

    public void atualizar()
    {
        if (bola.estaEm(Color.BLUE))
        {
            vetorCores[0] = Color.rgb(0, 0, 153);
            if (bola.getLastColor() != Color.BLUE && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.BLUE);
        }

        else if (bola.estaEm(Color.GREEN))
        {
            vetorCores[1] = Color.rgb(0, 102, 0);
            if (bola.getLastColor() != Color.GREEN && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.GREEN);
        }

        else if (bola.estaEm(Color.RED))
        {
            vetorCores[2] = Color.rgb(153, 0, 0);
            if (bola.getLastColor() != Color.RED && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.RED);
        }

        else if (bola.estaEm(Color.YELLOW))
        {
            vetorCores[3] = Color.rgb(204, 204, 0);
            if (bola.getLastColor() != Color.YELLOW && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.YELLOW);
        }
    }

    public void comecarHandler()
    {
        pararHandler = false;
        cronometro = 1;

        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                cronometro++;
                podeFazerIf = true;

                if (!pararHandler)
                    h.postDelayed(this, 100);
            }
        }, 100);
    }

	@Override
    protected void onDraw(Canvas canvas)
    {
        try
        {
            Paint paint = new Paint();

            // B G
            // R Y
            // left top right bottom
            vetorCores = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};

            if (podeFazerIf && estaMostrando && cronometro % 10 == 0)
            {
                estaMostrando = birl();
                demonstrarCores();
                if (!estaMostrando)
                {
                    estaNoUltimo = true;
                    cronometro++;
                }
                podeFazerIf = false;
            }
            else
            {
                if (estaNoUltimo && cronometro % 10 == 0)
                {
                    estaNoUltimo = false;
                    pararHandler = true;
                }
            }
            atualizar();

            paint.setColor(vetorCores[0]);
            canvas.drawRect(0, 0, JogoActivity.size.x/2 + 38, JogoActivity.size.y/2 + 38, paint);

            paint.setColor(vetorCores[1]);
            canvas.drawRect(JogoActivity.size.x/2 + 38, 0, JogoActivity.size.x + 76, JogoActivity.size.y/2 + 38, paint);

            paint.setColor(vetorCores[2]);
            canvas.drawRect(0, JogoActivity.size.y/2 + 38, JogoActivity.size.x/2 + 38, JogoActivity.size.y + 76, paint);

            paint.setColor(vetorCores[3]);
            canvas.drawRect(JogoActivity.size.x/2 + 38, JogoActivity.size.y/2 + 38, JogoActivity.size.x + 76  , JogoActivity.size.y + 76,paint);

            if (!estaMostrando && !estaNoUltimo)
            {
                if (printaMeio)
                {
                    this.bola.getLocal().x = (JogoActivity.size.x/2 + 38) - Ball.raio;
                    this.bola.getLocal().y = (JogoActivity.size.y/2 + 38) - Ball.raio;
                    printaMeio = false;
                }
                canvas.drawBitmap(this.bola.getTextura(), this.bola.getLocal().x, this.bola.getLocal().y, null);

                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                RectF rectF = new RectF(bola.getLocal().x - 30, bola.getLocal().y - 30, bola.getLocal().x + 105, bola.getLocal().y + 105);

                paint.setStrokeWidth(4);
                canvas.drawArc(rectF, (float) 0, this.bola.getAngulo(), false, paint);
                paint.setStrokeWidth(1);
            }

            paint.setTextSize(30);
            paint.setColor(Color.BLACK);
            canvas.drawText(cpu.getFilaAux().toString().replace(Color.GREEN+"", "verde").replace(Color.RED+"", "vermelho").replace(Color.YELLOW+"", "amarelo")
                    .replace(Color.BLUE+"", "azul"),100, 200, paint);
            canvas.drawText(cpu.atual+"", 100, 300, paint);

            invalidate();
        }
        catch(Exception e){}
    }

    private void demonstrarCores() throws Exception
    {
        switch (this.cpu.getAtual())
        {
            case Color.BLUE:
                bola.getLocal().x = 0;
                bola.getLocal().y = 0;
                break;

            case Color.GREEN:
                bola.getLocal().x = JogoActivity.size.x/2 + 38;
                bola.getLocal().y = 0;
                break;

            case Color.RED:
                bola.getLocal().x = 0;
                bola.getLocal().y = JogoActivity.size.y/2 + 38;
                break;

            case Color.YELLOW:
                bola.getLocal().x = JogoActivity.size.x/2 + 38;
                bola.getLocal().y = JogoActivity.size.y/2 + 38;
                break;
        }
    }

    public boolean birl()
    {
        try
        {
            boolean taVazio = false;
            if (this.cpu.estaNoUltimo())
            {
                this.cpu.sortear(this.CORES);
                this.cpu.avancar();
                taVazio = true;
                this.cpu.reseta();
            }
            else
                this.cpu.avancar();
            return !taVazio;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}