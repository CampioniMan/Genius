package com.example.Genio.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by u15163 on 22/05/2017.
 */

public class BallView extends View
{
    private Ball bola;
    private boolean ehHard;
    private int[] vetorCores;

    public BallView(Context context, int _ehHard)
    {
        super(context);
        bola = new Ball(getResources());
        vetorCores = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};

        if (_ehHard == 0) this.ehHard = false;
        else this.ehHard = true;
    }
    
    public Ball getBola()
    {
		return bola;
	}

	public void setBola(Ball bola)
    {
		this.bola = bola;
	}

    public void atualizar()
    {
        if (bola.estaEm(Color.BLUE))
        {
            vetorCores[0] = Color.rgb(0, 0, 153);
            if (bola.getLastColor() != Color.BLUE && !ehHard)
                bola.setAngulo(0);
            bola.setLastColor(Color.BLUE);
        }

        else if (bola.estaEm(Color.GREEN))
        {
            vetorCores[1] = Color.rgb(0, 102, 0);
            if (bola.getLastColor() != Color.GREEN && !ehHard)
                bola.setAngulo(0);
            bola.setLastColor(Color.GREEN);
        }

        else if (bola.estaEm(Color.RED))
        {
            vetorCores[2] = Color.rgb(153, 0, 0);
            if (bola.getLastColor() != Color.RED && !ehHard)
                bola.setAngulo(0);
            bola.setLastColor(Color.RED);
        }

        else if (bola.estaEm(Color.YELLOW))
        {
            vetorCores[3] = Color.rgb(204, 204, 0);
            if (bola.getLastColor() != Color.YELLOW && !ehHard)
                bola.setAngulo(0);
            bola.setLastColor(Color.YELLOW);
        }
    }

	@Override
    protected void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();

        // B G
        // R Y
        // left top right bottom
        vetorCores = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};
        atualizar();

        paint.setColor(vetorCores[0]);
        canvas.drawRect(0, 0, JogoActivity.size.x/2 + 38, JogoActivity.size.y/2 + 38, paint);

        paint.setColor(vetorCores[1]);
        canvas.drawRect(JogoActivity.size.x/2 + 38, 0, JogoActivity.size.x + 76, JogoActivity.size.y/2 + 38, paint);

        paint.setColor(vetorCores[2]);
        canvas.drawRect(0, JogoActivity.size.y/2 + 38, JogoActivity.size.x/2 + 38, JogoActivity.size.y + 76, paint);

        paint.setColor(vetorCores[3]);
        canvas.drawRect(JogoActivity.size.x/2 + 38, JogoActivity.size.y/2 + 38, JogoActivity.size.x + 76  , JogoActivity.size.y + 76,paint);

        canvas.drawBitmap(this.bola.getTextura(), bola.getLocal().x, bola.getLocal().y, null);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(bola.getLocal().x - 30, bola.getLocal().y -30, bola.getLocal().x+105, bola.getLocal().y+105);

        paint.setStrokeWidth(4);
        canvas.drawArc(rectF, (float)0, this.bola.getAngulo(), false, paint);
        paint.setStrokeWidth(1);

        invalidate();
    }
}