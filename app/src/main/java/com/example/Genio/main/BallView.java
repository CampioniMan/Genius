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

    public BallView(Context context)
    {
        super(context);
        bola = new Ball(getResources());
    }
    
    public Ball getBola() {
		return bola;
	}

	public void setBola(Ball bola) {
		this.bola = bola;
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
        {
            paint.setColor(Color.GRAY);
            if (bola.getLastColor() != Color.BLUE)
                bola.setAngulo(0);
            bola.setLastColor(Color.BLUE);
        }
        canvas.drawRect(0, 0, JogoActivity.size.x/2 + 38, JogoActivity.size.y/2 + 38, paint);

        paint.setColor(Color.GREEN);
        if (bola.estaEm(Color.GREEN))
        {
            paint.setColor(Color.GRAY);
            if (bola.getLastColor() != Color.GREEN)
                bola.setAngulo(0);
            bola.setLastColor(Color.GREEN);
        }
        canvas.drawRect(JogoActivity.size.x/2 + 38, 0, JogoActivity.size.x + 76, JogoActivity.size.y/2 + 38, paint);

        paint.setColor(Color.RED);
        if (bola.estaEm(Color.RED))
        {
            paint.setColor(Color.GRAY);
            if (bola.getLastColor() != Color.RED)
                bola.setAngulo(0);
            bola.setLastColor(Color.RED);
        }
        canvas.drawRect(0, JogoActivity.size.y/2 + 38, JogoActivity.size.x/2 + 38, JogoActivity.size.y + 76, paint);

        paint.setColor(Color.YELLOW);
        if (bola.estaEm(Color.YELLOW))
        {
            paint.setColor(Color.GRAY);
            if (bola.getLastColor() != Color.YELLOW)
                bola.setAngulo(0);
            bola.setLastColor(Color.YELLOW);
        }
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