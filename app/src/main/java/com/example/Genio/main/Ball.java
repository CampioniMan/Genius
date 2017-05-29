package com.example.Genio.main;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;

import com.example.u15190.genius.R;

public class Ball
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////ATRIBUTOS///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private Point local;
    private Bitmap textura;
    final Point tamanho = new Point(75, 75); // escala do desenho da bolinha

	private float xVel, yVel, xAce, yAce;
    private int angulo, lastColor;
    private static int raio;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////CONSTRUTOR///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Ball(Resources res)
    {
        xVel = yVel = xAce = yAce = 0.0f; // inicializando as velocidades e as acelerações para 0
        local = new Point(-50,-50);       // iniciando as coordenadas para default
        angulo = 0;                       // iniciando o ângulo com 0, para não começar errado
        lastColor = Color.BLUE;           // colocando a primeira cor como AZUL, pois é por default
        raio = 38;

        // iniciando a textura da bolinha e sua escala
        this.textura = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (res, R.drawable.balldois), tamanho.x, tamanho.y, true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////GETTERS E SETTERS///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Point getLocal() {
		return local;
	}

	public void setLocal(Point local) {
		this.local = local;
	}

	public void setxAce(float xAce) {
		this.xAce = xAce;
	}

	public void setyAce(float yAce) {
		this.yAce = yAce;
	}

	public Bitmap getTextura() {
		return textura;
	}

	public static int getRaio() {return raio;}

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////MÉTODOS PRINCIPAIS/////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
        if (this.local.x > JogoActivity.getSize().x) {
            this.local.x = JogoActivity.getSize().x;
            this.xAce = this.xVel = 0;
        }
        else if (this.local.x < 0) {
            this.local.x = 0;
            this.xAce = this.xVel = 0;
        }

        if (this.local.y > JogoActivity.getSize().y) {
            this.local.y = JogoActivity.getSize().y;
            this.yAce = this.yVel = 0;
        }
        else if (this.local.y < 0) {
            this.local.y = 0;
            this.yAce = this.yVel = 0;
        }
    }

    public boolean estaEm(int cor)
    {
        if (cor == Color.BLUE)
        {
            if (local.x + raio >= 0 &&
                local.y + raio >= 0 &&
                local.x + raio <= JogoActivity.getSize().x/2 + 38 &&
                local.y + raio <= JogoActivity.getSize().y/2 + 38)
                return true;

            return false;
        }
        else if (cor == Color.GREEN)
        {
            if (local.x + raio >= JogoActivity.getSize().x/2 + 38 &&
                local.y + raio >= 0 &&
                local.x + raio <= JogoActivity.getSize().x + 76 &&
                local.y + raio <= JogoActivity.getSize().y/2 + 38)
                return true;

            return false;
        }
        else if (cor == Color.RED)
        {
            if (local.x + raio >= 0 &&
                local.y + raio >= JogoActivity.getSize().y/2 + 38 &&
                local.x + raio <= JogoActivity.getSize().x/2 + 38 &&
                local.y + raio <= JogoActivity.getSize().y + 76)
                return true;

            return false;
        }
        else if (cor == Color.YELLOW)
        {
            if (local.x + raio >= JogoActivity.getSize().x/2 + 38 &&
                local.y + raio >= JogoActivity.getSize().y/2 + 38 &&
                local.x + raio <= JogoActivity.getSize().x + 76 &&
                local.y + raio <= JogoActivity.getSize().y + 76)
                return true;

            return false;
        }
        return false;
    }

}