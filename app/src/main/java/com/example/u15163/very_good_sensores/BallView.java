package com.example.u15190.genius;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
            paint.setColor(Color.GRAY);

        canvas.drawRect(0, 0, JogoActivity.size.x/2 + 38, JogoActivity.size.y/2 + 38, paint);

        paint.setColor(Color.GREEN);
        if (bola.estaEm(Color.GREEN))
            paint.setColor(Color.GRAY);

        canvas.drawRect(JogoActivity.size.x/2 + 38, 0, JogoActivity.size.x + 76, JogoActivity.size.y/2 + 38,paint);

        paint.setColor(Color.RED);
        if (bola.estaEm(Color.RED))
            paint.setColor(Color.GRAY);

        canvas.drawRect(0, JogoActivity.size.y/2 + 38, JogoActivity.size.x/2 + 38, JogoActivity.size.y + 76,paint);

        paint.setColor(Color.YELLOW);
        if (bola.estaEm(Color.YELLOW))
            paint.setColor(Color.GRAY);

        canvas.drawRect(JogoActivity.size.x/2 + 38, JogoActivity.size.y/2 + 38, JogoActivity.size.x + 76  , JogoActivity.size.y + 76,paint);

        canvas.drawBitmap(this.bola.getTextura(), bola.getLocal().x, bola.getLocal().y, null);

        invalidate();
    }
}