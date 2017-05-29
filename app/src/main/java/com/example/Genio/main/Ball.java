package com.example.Genio.main;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;

import com.example.u15190.genius.R;

/**
 * Classe feita para conter os dados e métodos que o desenho da bola irá utilizar.
 * @author 15190 e 15163
 * @since 2017
 * @version 1.0
 */

public class Ball
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////ATRIBUTOS///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Atributos Point chamados 'local', 'coordVel' e 'coordAce', suas funções são armazenar as
     * coordenadas da imagem, da velocidade e da aceleração, respectivamente
     */
    private Point local, coordVel, coordAce;

    /**
     * Atributo Bitmap chamado 'textura', sua função é armazenar a imagem
     */
    private Bitmap textura;

    /**
     * Atributo constante Point chamado 'tamanho', sua função é armazenar o tamanho da imagem
     */
    final Point tamanho = new Point(75, 75); // escala do desenho da bolinha

    /**
     * Atributos ints chamados 'angulo' e 'lastColor', suas funções são armazenar os ângulos do
     * circulo da animação e a última cor que a imagem esteve, respectivamente
     */
    private int angulo, lastColor;

    /**
     * Atributo estático int chamado 'raio', sua função é armazenar o tamanho do raio da imagem
     */
    private static int raio;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////CONSTRUTOR///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Contrutor da classe Ball que instância todos os atributos com seus respectivos valores
     * @param res classe Resources que irá representar o diretório das imagens
     */
    public Ball(Resources res)
    {
        this.local = new Point(-50,-50);
        this.coordVel = new Point(0,0);
        this.coordAce = new Point(0,0);

        this.angulo = 0;
        this.lastColor = Color.BLUE;
        raio = 38;

        this.textura = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (res, R.drawable.balldois), tamanho.x, tamanho.y, true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////GETTERS E SETTERS///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Método que retorna o valor do atributo 'local'
     * @return um Point que representa as coordenadas atuais da imagem
     */
    public Point getLocal() {
		return local;
	}

    /**
     * Método que retorna o valor do atributo 'textura'
     * @return um Bitmap que representa a imagem
     */
    public Bitmap getTextura() {
        return textura;
    }

    /**
     * Método estático que retorna o valor do atributo 'raio'
     * @return um inteiro que representa o raio da imagem
     */
    public static int getRaio() { return raio; }

    /**
     * Método que retorna o valor do atributo 'angulo'
     * @return um inteiro que representa o ândulo da animação
     */
    public int getAngulo() {
        return angulo;
    }

    /**
     * Método que retorna o valor do atributo 'lastColor'
     * @return um inteiro que representa a última cor que a imagem ficou
     */
    public int getLastColor() {
        return lastColor;
    }

    /**
     * Método que atribui dados ao atributo 'inicioDeFase'
     * @param _local Point que representa as novas coordenadas da imagem
     */
	public void setLocal(Point _local) {
		this.local = _local;
	}

    /**
     * Método que atribui dados ao atributo 'coordAce'
     * @param _coordAce Point que representa as novas coordenadas da aceleração
     */
	public void setCoordAce(Point _coordAce) {
		this.coordAce = _coordAce;
	}

    /**
     * Método que atribui dados ao atributo 'angulo'
     * @param _angulo inteiro que representa o novo ângulo da animação
     */
    public void setAngulo(int _angulo) {
        this.angulo = _angulo;
    }

    /**
     * Método que atribui dados ao atributo 'lastColor'
     * @param _lastColor inteiro que representa a cor anterior que a imagem ficou
     */
    public void setLastColor(int _lastColor) {
        this.lastColor = _lastColor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////MÉTODOS PRINCIPAIS/////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Método que atualiza as coordenadas da imagem atualizando também as coordenadas da velocidade
     * e da aceleração
     */
    public void atualizaLocal()
    {
        float frameTime = 0.666f;

        this.coordVel.x += this.coordAce.x * frameTime;
        this.coordVel.y += this.coordAce.y * frameTime;

        float xS = (this.coordVel.x / 2) * frameTime;
        float yS = (this.coordVel.y / 2) * frameTime;

        this.local.x -= xS;
        this.local.y -= yS;
    }

    /**
     * Método que realiza uma série de verificações sobre colisões da imagem com o canvas do celular
     */
    public void verColisoes()
    {
        if (this.local.x > JogoActivity.getSize().x) {
            this.local.x = JogoActivity.getSize().x;
            this.coordAce.x = this.coordVel.x = 0;
        }
        else if (this.local.x < 0) {
            this.local.x = 0;
            this.coordAce.x = this.coordVel.x = 0;
        }

        if (this.local.y > JogoActivity.getSize().y) {
            this.local.y = JogoActivity.getSize().y;
            this.coordAce.y = this.coordVel.y = 0;
        }
        else if (this.local.y < 0) {
            this.local.y = 0;
            this.coordAce.y = this.coordVel.y = 0;
        }
    }

    /**
     * Método que faz uma série de verificações para saber em qual cor a imagem está
     * @param cor inteiro que representa a cor que a imagem esta atualmente
     * @return um boolean que representa se a cor passada como parãmetro é a mesma verificada
     */
    public boolean estaEm(int cor)
    {
        if (cor == Color.BLUE)
        {
            if (this.local.x + raio >= 0 &&
                this.local.y + raio >= 0 &&
                this.local.x + raio <= JogoActivity.getSize().x/2 + 38 &&
                this.local.y + raio <= JogoActivity.getSize().y/2 + 38)
                return true;

            return false;
        }
        else if (cor == Color.GREEN)
        {
            if (this.local.x + raio >= JogoActivity.getSize().x/2 + 38 &&
                this.local.y + raio >= 0 &&
                this.local.x + raio <= JogoActivity.getSize().x + 76 &&
                this.local.y + raio <= JogoActivity.getSize().y/2 + 38)
                return true;

            return false;
        }
        else if (cor == Color.RED)
        {
            if (this.local.x + raio >= 0 &&
                this.local.y + raio >= JogoActivity.getSize().y/2 + 38 &&
                this.local.x + raio <= JogoActivity.getSize().x/2 + 38 &&
                this.local.y + raio <= JogoActivity.getSize().y + 76)
                return true;

            return false;
        }
        else if (cor == Color.YELLOW)
        {
            if (this.local.x + raio >= JogoActivity.getSize().x/2 + 38 &&
                this.local.y + raio >= JogoActivity.getSize().y/2 + 38 &&
                this.local.x + raio <= JogoActivity.getSize().x + 76 &&
                this.local.y + raio <= JogoActivity.getSize().y + 76)
                return true;

            return false;
        }
        return false;
    }

}