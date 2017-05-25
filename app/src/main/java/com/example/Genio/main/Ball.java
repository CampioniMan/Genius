package com.example.Genio.main;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;

import com.example.u15190.genius.R;

/**
 * Created by u15163 on 22/05/2017.
 */

public class Ball
{
    private Point local;

	private float xVel, yVel, xAce, yAce;

    private Bitmap textura;
    private final Point tamanho = new Point(75, 75);
    private int angulo, lastColor;
    static int raio = 38;

    public Ball(Resources res)
    {
        xVel = yVel = xAce = yAce = 0.0f;
        local = new Point(-50,-50);
        this.textura = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.balldois), tamanho.x, tamanho.y, true);
        angulo = 0;
        lastColor = Color.BLUE;
    }

    public Point getLocal() {
		return local;
	}

	public void setLocal(Point local) {
		this.local = local;
	}

	public float getxVel() {
		return xVel;
	}

	public void setxVel(float xVel) {
		this.xVel = xVel;
	}

	public float getyVel() {
		return yVel;
	}

	public void setyVel(float yVel) {
		this.yVel = yVel;
	}

	public float getxAce() {
		return xAce;
	}

	public void setxAce(float xAce) {
		this.xAce = xAce;
	}

	public float getyAce() {
		return yAce;
	}

	public void setyAce(float yAce) {
		this.yAce = yAce;
	}

	public Bitmap getTextura() {
		return textura;
	}

	public void setTextura(Bitmap textura) {
		this.textura = textura;
	}

	public int getRaio() {
		return raio;
	}

	public void setRaio(int raio) {
		this.raio = raio;
	}

	public Point getTamanho() {
		return tamanho;
	}
    
    public void atualizaLocal()
    {
        float frameTime = 0.666f;

        this.xVel += (this.xAce * frameTime);
        this.yVel += (this.yAce * frameTime);

        float xS = (this.xVel / 2) * frameTime;
        float yS = (this.yVel / 2) * frameTime;

        this.local.x -= xS;
        this.local.y -= yS;
    }

    public void verColisoes()
    {
        if (this.local.x > JogoActivity.size.x) {
            this.local.x = JogoActivity.size.x;
            this.xAce = 0;
            this.xVel = 0;
        }
        else if (this.local.x < 0) {
            this.local.x = 0;
            this.xAce = 0;
            this.xVel = 0;
        }

        if (this.local.y > JogoActivity.size.y) {
            this.local.y = JogoActivity.size.y;
            this.yAce = 0;
            this.yVel = 0;
        }
        else if (this.local.y < 0) {
            this.local.y = 0;
            this.yAce = 0;
            this.yVel = 0;
        }
    }

    public boolean estaEm(int cor)
    {
        if (cor == Color.BLUE)
        {
            if (local.x + raio >= 0 && local.y + raio >= 0 && local.x + raio <= JogoActivity.size.x/2 + 38 && local.y + raio <= JogoActivity.size.y/2 + 38)
            {
                return true;
            }
            return false;
        }
        else if (cor == Color.GREEN)
        {
            if (local.x + raio >= JogoActivity.size.x/2 + 38 && local.y + raio >= 0 && local.x + raio <= JogoActivity.size.x + 76 && local.y + raio <= JogoActivity.size.y/2 + 38)
            {
                return true;
            }
            return false;
        }
        else if (cor == Color.RED)
        {
            if (local.x + raio >= 0 && local.y + raio >= JogoActivity.size.y/2 + 38 && local.x + raio <= JogoActivity.size.x/2 + 38 && local.y + raio <= JogoActivity.size.y + 76)
            {
                return true;
            }
            return false;
        }
        else if (cor == Color.YELLOW)
        {
            if (local.x + raio >= JogoActivity.size.x/2 + 38 && local.y + raio >= JogoActivity.size.y/2 + 38 && local.x + raio <= JogoActivity.size.x + 76 && local.y + raio <= JogoActivity.size.y + 76)
            {
                return true;
            }
            return false;
        }
        return false;
    }

    public int getAngulo() {
        return angulo;
    }

    public void setAngulo(int angulo) {
        this.angulo = angulo;
    }

    public int getLastColor() {
        return lastColor;
    }

    public void setLastColor(int lastColor) {
        this.lastColor = lastColor;
    }
}