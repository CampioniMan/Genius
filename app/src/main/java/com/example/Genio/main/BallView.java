package com.example.Genius.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;

import java.util.Timer;

/**
 * A classe BallView representa um gerênciador de desenhos para as cores e a imagem 'ball.png'
 * que herda o método 'onDraw' da classe View.
 * A classe tem como base:
 * - 1 instância constante da classe Handler que gerência uma Thread de cronometro;
 * - 1 vetor de inteiros constante e estático que armazena as cores padrões;
 * - 2 matrizes constantes e estáticas que armazenam as cores padrões normais e mais escuras no
 *   no formato RGB;
 * - 1 vetor de inteiros que armazena as cores que serão demonstradas para o jogador;
 * - 5 booleans que armazenam se o aplicativo está mostrando as cores para o usuário, se pode ser
 *   feito uma condição no método 'onDraw', se a imagem pode ser mostrada no meio, se a cor a ser
 *   mostrada é a última e se o Handler deve ser parado;
 * - 3 inteiros que armazenam o tempo do cronometro, a quantidade de tempo que o Handler foi
 *   abilitado e o tom de preto que as cores irão ter ao serem mostradas;
 * - 1 float que armazena a velocidade de escurecer uma cor;
 * - 1 instância da classe Ball que gerência os dados da imagem 'ball.png';
 * - 1 instância da classe JogoCPU que armazena a CPU do jogo;
 * - 1 instância da classe GerenciaCores que gerência as cores do jogo
 *
 * Instâncias desta classe permitem gerênciar os dados da imagem no jogo.
 *
 * @author Daniel Samogin Campioni e Pedro Luiz Pezoa
 * @since 2017
 * @version 1.0
 */

public class BallView extends View
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////ATRIBUTOS////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Atributo Handler chamado 'h', sua função é gerênciar a Thread de cronometro
     */
    final Handler h = new Handler();

    /**
     * Atributo constante e estático int chamado 'CORES', sua função é armazenar as cores padrões da
     * classe Color
     */
    static final int[] CORES = new int[]{ Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW };

    /**
     * Atributo constante e estático int chamado 'CORESESCURASRGB', sua função
     * é armazenar as cores padrões escuras em formato RGB, respectivamente
     */
    static final int[][]
            CORESESCURASRGB = new int[][]{ {0, 0, 103}, {0, 103, 0}, {103, 0, 0}, {103, 103, 0} };

    /**
     * Atributo int[] chamado 'coresParaMostar', sua função é gerênciar as cores que serão mostradas
     * ao jogador na troca de fases
     */
    private int[] coresParaMostar;

    /**
     * Atributos booleans chamados 'estaMostrando', 'podeFazerIf', 'botaNoMeio', 'estaNoUltimo' e
     * 'pararHandler', suas funções são dizer se a aplicação está mostrando as cores para o jogador,
     * se dentro do método 'onDraw' será realizado uma condição, se a aplicação pode desenhar a
     * imagem 'ball.png' no centro da tela, se a cor que está sendo mostrada é a última da fila
     * e se pode ser interrompido a classe Handler, respectivamente
     */
    private boolean estaMostrando, podeFazerIf, botaNoMeio, pararHandler;

    /**
     * Atributos booleans chamados 'cronometro', 'tempoHabilitado' e 'tomDePreto', suas funções são
     * armazenar o tempo de demonstração da cor para o jogador, o tempo que a classe Handler está
     * abilitada e a tonalidade da cor preta que as cores a serem mostradas terão, respectivamente
     */
    private int cronometro, tempoHabilitado, tomDePreto;

    /**
     * Atributo float chamado 'velEscuridao', sua função é armazenar a velocidade das cores para 
     * escurecerem
     */
    private float velEscuridao;

    /**
     * Atributo Ball chamado 'bolinha', sua função é armazenar a imagem 'boladois.png'
     */
    private Ball bolinha;

    /**
     * Atributo JogoCPU chamado 'cpu', sua função é armazenar a CPU do jogo
     */
    private JogoCPU cpu;

    /**
     * Atributo GerenciaCores chamado 'corGeren', sua função é armazenar o gerênciador de cores
     */
    private GerenciaCores corGeren;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////CONSTRUTOR////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Contrutor da classe BallView que instância todos os atributos com seus respectivos valores
     * @param context classe Context que irá representar o local onde será desenhado
     * @param _ehHard inteiro que representa em qual dificuldade o jogo começou
     */
    public BallView(Context context, int _ehHard)
    {
        super(context);
        this.bolinha = new Ball(getResources());
        this.coresParaMostar = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};
        this.cpu = new JogoCPU(_ehHard);
        this.corGeren = new GerenciaCores();

        this.estaMostrando = botaNoMeio = podeFazerIf = true;

        this.tempoHabilitado = JogoActivity.tempoDeSegundos(1);
        this.tomDePreto = 0;
        this.velEscuridao = 0;

        this.cpu.sortear(this.coresParaMostar);

        comecarHandler();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////GETTERS E SETTERS///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Método que retorna o valor do atributo 'bolinha'
     * @return uma instância da classe Ball que dirá o valor do atributo 'bolinha'
     */
    public Ball getbolinha()
    {
		return this.bolinha;
	}

    /**
     * Método que retorna o valor do atributo 'cpu'
     * @return uma instância da classe JogoCPU que dirá o valor do atributo 'cpu'
     */
    public JogoCPU getCPU()
    {
        return this.cpu;
    }

    /**
     * Método que retorna o valor do atributo 'estaMostrando'
     * @return um boolean dirá o valor do atributo 'estaMostrando'
     */
    public boolean isMostrando()
    {
        return this.estaMostrando;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////MÉTODOS PRINCIPAIS/////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Método que atualiza as cores da tela para o jogador dependendo da cor que a imagem esta.
     * As cores serão mostradas de maneiras diferentes dependendo do estado do jogo, ou seja, se o
     * jogador começou uma nova fase ou se este esta jogando a fase.
     */
    public void atualizar()
    {
        if (bolinha.estaEm(Color.BLUE)) {
            if (!estaMostrando)
                coresParaMostar[0] = Color.rgb(CORESESCURASRGB[0][0],
                        CORESESCURASRGB[0][1],
                        CORESESCURASRGB[0][2]);
            else
                coresParaMostar[0] = Color.rgb(CORESESCURASRGB[0][0],
                        CORESESCURASRGB[0][1],
                        CORESESCURASRGB[0][2] + tomDePreto);
            coresParaMostar[1] = CORES[1];
            coresParaMostar[2] = CORES[2];
            coresParaMostar[3] = CORES[3];

            if (bolinha.getLastColor() != Color.BLUE) {
                if (!cpu.isHard())
                    bolinha.setAngulo(0);
                else
                this.bolinha.setTextura(Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.ball2), bolinha.escala.x, bolinha.escala.y, true));
            }
            bolinha.setLastColor(Color.BLUE);
            ////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (bolinha.estaEm(Color.GREEN)) {
            if (!estaMostrando)
                coresParaMostar[1] = Color.rgb(CORESESCURASRGB[1][0],
                        CORESESCURASRGB[1][1],
                        CORESESCURASRGB[1][2]);
            else
                coresParaMostar[1] = Color.rgb(CORESESCURASRGB[1][0],
                        CORESESCURASRGB[1][1] + tomDePreto,
                        CORESESCURASRGB[1][2]);
            coresParaMostar[0] = CORES[0];
            coresParaMostar[2] = CORES[2];
            coresParaMostar[3] = CORES[3];
            if (bolinha.getLastColor() != Color.GREEN) {
                if (!cpu.isHard())
                    bolinha.setAngulo(0);
                else
                this.bolinha.setTextura(Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.ball5), bolinha.escala.x, bolinha.escala.y, true));
            }
            bolinha.setLastColor(Color.GREEN);
            ////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (bolinha.estaEm(Color.RED)) {
            if (!estaMostrando)
                coresParaMostar[2] = Color.rgb(CORESESCURASRGB[2][0],
                        CORESESCURASRGB[2][1],
                        CORESESCURASRGB[2][2]);
            else
                coresParaMostar[2] = Color.rgb(CORESESCURASRGB[2][0] + tomDePreto,
                        CORESESCURASRGB[2][1],
                        CORESESCURASRGB[2][2]);
            coresParaMostar[1] = CORES[1];
            coresParaMostar[0] = CORES[0];
            coresParaMostar[3] = CORES[3];
            if (bolinha.getLastColor() != Color.RED) {
                if (!cpu.isHard())
                    bolinha.setAngulo(0);
                else
                this.bolinha.setTextura(Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.ball3), bolinha.escala.x, bolinha.escala.y, true));
            }
            bolinha.setLastColor(Color.RED);
            ////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (bolinha.estaEm(Color.YELLOW)) {
            if (!estaMostrando)
                coresParaMostar[3] = Color.rgb(CORESESCURASRGB[3][0],
                        CORESESCURASRGB[3][1],
                        CORESESCURASRGB[3][2]);
            else
                coresParaMostar[3] = Color.rgb(CORESESCURASRGB[3][0] + tomDePreto,
                        CORESESCURASRGB[3][1] + tomDePreto,
                        CORESESCURASRGB[3][2]);
            coresParaMostar[1] = CORES[1];
            coresParaMostar[2] = CORES[2];
            coresParaMostar[0] = CORES[0];
            if (bolinha.getLastColor() != Color.YELLOW) {
                if (!cpu.isHard())
                    bolinha.setAngulo(0);
                else
                this.bolinha.setTextura(Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                        (getResources(), R.drawable.ball4), bolinha.escala.x, bolinha.escala.y, true));
            }
            bolinha.setLastColor(Color.YELLOW);
        }
    }

    /**
     * Método sobrescrito da classe View que desenha as cores e a imagem 'ball.png' na tela do
     * aparelho do jogador, este é um dos mais importantes métodos do jogo, ja que este se baseia
     * inteiramente demonstar as cores para o jogador.
     */
	@Override
    protected void onDraw(Canvas canvas)
    {
        Paint paint = new Paint();
        if (podeFazerIf && estaMostrando && contou(tempoHabilitado)) // se contou e está mostrando
        {
            tomDePreto = 0;                 // resetamos o tom de preto
            velEscuridao = 0f;              // resetamos a velocidade do aumento da escuridão
            if (cpu.isInicioDeFase()) // se está iniciando a fase(jogar) AGORA
            {
                cpu.setInicioDeFase(false); // não está mais iniciando a fase AGORA
                cpu.reseta();               // resetamos o índice do Vector de cores
            }
            estaMostrando = corGeren.avancarCor(); // avançamos e vemos se deve mostrar ainda
            if (!estaMostrando)       // se não está mais mostrando
            {
                pararHandler = true;        // paramos a Thread
                cpu.setInicioDeFase(true);  // colocamos que agora a fase será iniciada
                cronometro++;               // aumentamos o cronômetro para impossibilitar a entrada
                this.bolinha.setLocal(new Point(-100, -100));
            }
            else                      // se está mostrando ainda
            {
                corGeren.mudarCor();        // alteramos a cor que está sendo mostrada
            }
            podeFazerIf = false;            // impossibilitamos a entrada no If novamente
        }

        if (cpu.isHard() && podeFazerIf && estaMostrando && contou(tempoHabilitado/2)) // se contou e está mostrando
        {
            tomDePreto = 0;                 // resetamos o tom de preto
            velEscuridao = 0f;              // resetamos a velocidade do aumento da escuridão
            if (cpu.isInicioDeFase()) // se está iniciando a fase(jogar) AGORA
            {
                cpu.setInicioDeFase(false); // não está mais iniciando a fase AGORA
                cpu.reseta();               // resetamos o índice do Vector de cores
            }
            estaMostrando = corGeren.avancarCor(); // avançamos e vemos se deve mostrar ainda
            if (!estaMostrando)       // se não está mais mostrando
            {
                pararHandler = true;        // paramos a Thread
                cpu.setInicioDeFase(true);  // colocamos que agora a fase será iniciada
                cronometro++;               // aumentamos o cronômetro para impossibilitar a entrada
                this.bolinha.setLocal(new Point(-100, -100));
            }
            else                      // se está mostrando ainda
            {
                corGeren.mudarCor();        // alteramos a cor que está sendo mostrada
            }
            podeFazerIf = false;            // impossibilitamos a entrada no If novamente
        }

        atualizar();

        paint.setColor(coresParaMostar[0]);
        canvas.drawRect(0, 0,
                        JogoActivity.getSize().x/2,
                        JogoActivity.getSize().y/2, paint);

        paint.setColor(coresParaMostar[1]);
        canvas.drawRect(JogoActivity.getSize().x/2, 0,
                        JogoActivity.getSize().x,
                        JogoActivity.getSize().y/2, paint);

        paint.setColor(coresParaMostar[2]);
        canvas.drawRect(0,
                        JogoActivity.getSize().y/2,
                        JogoActivity.getSize().x/2,
                        JogoActivity.getSize().y, paint);

        paint.setColor(coresParaMostar[3]);
        canvas.drawRect(JogoActivity.getSize().x/2,
                        JogoActivity.getSize().y/2,
                        JogoActivity.getSize().x,
                        JogoActivity.getSize().y,paint);

        if (!estaMostrando) // para botar no meio e desenhar a bolinha com o loading
        {
            if (botaNoMeio)
            {
                this.bolinha.getLocal().x = JogoActivity.getSize().x/2 - Ball.getRaio();
                this.bolinha.getLocal().y = JogoActivity.getSize().y/2 - Ball.getRaio();
                botaNoMeio = false;
            }

            canvas.drawBitmap(this.bolinha.getTextura(),
                    this.bolinha.getLocal().x,
                    this.bolinha.getLocal().y, null);

            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);

            RectF rectF = new RectF(
                    bolinha.getLocal().x - 30,
                    bolinha.getLocal().y - 30,
                    bolinha.getLocal().x + 105,
                    bolinha.getLocal().y + 105);

            paint.setStrokeWidth(4);
            canvas.drawArc(rectF, (float) 0, this.bolinha.getAngulo(), false, paint);
            paint.setStrokeWidth(1);
        }

        invalidate();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////MÉTODOS AUXILIARES////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Método que instância um método dentro de Handler, que consiste em uma Thread de cronometro
     * para a demonstração de cores para o jogador.
     */
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
                tomDePreto += velEscuridao;
                velEscuridao += (!cpu.isHard())?0.75f:1.50f;
                if (!pararHandler)
                    h.postDelayed(this, JogoActivity.TEMPO);
            }
        }, JogoActivity.TEMPO);
    }

    /**
     * Método que verifica quantos segundos a Thread do atributo Handler contou.
     * @param segundos inteiro que representa os segundos que serão verificados.
     * @return um boolean que dirá se o a Thread do atributo Handler contou a mesma quantidade que
     *         foi passada como parâmetro.
     */
    public boolean contou(int segundos)
    {
        return cronometro % segundos == 0;
    }

    /**
     * Método que reseta as váriaveis principais do jogo, este método só será realizado quando o
     * jogador conseguir acertar todas as cores.
     */
    public void remomecarFase()
    {
        this.cpu.reseta();
        this.estaMostrando = this.botaNoMeio = true;
        this.bolinha.setLocal(new Point(-50, -50));
        this.comecarHandler();
        this.bolinha.setAngulo(0);
        this.cpu.sortear(this.CORES);
        if (this.cpu.isHard())
            this.cpu.sortear(this.CORES);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////CLASSE AUXILIAR/////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * A subclasse GerenciaCores representa um gerenciador para mudança de cores e verificação se
     * a cor atual será a cor a ser mostrada. Esta classe não possui atributos porque este usa os
     * atributos da classe BallView
     *
     * Instâncias desta classe permitem gerênciar os dados das cores no jogo.
     *
     * @author Daniel Samogin Campioni e Pedro Luiz Pezoa
     * @since 2017
     * @version 1.0
     */
    private class GerenciaCores
    {
        /**
         * Método que altera as cores para serem demonstradas para o jogador, para isso nós
         * alteramos a posição da imagem 'ball.png', que esta invisivel, para podermos utilizar
         * métodos ja criados para atualizar cores da classe BallView.
         */
        public void mudarCor()
        {
            switch (cpu.getAtual())
            {
                case Color.BLUE:
                    bolinha.getLocal().x = bolinha.getLocal().y = 0;
                    break;

                case Color.GREEN:
                    bolinha.getLocal().x = JogoActivity.getSize().x/2;
                    bolinha.getLocal().y = 0;
                    break;

                case Color.RED:
                    bolinha.getLocal().x = 0;
                    bolinha.getLocal().y = JogoActivity.getSize().y/2;
                    break;

                case Color.YELLOW:
                    bolinha.getLocal().x = JogoActivity.getSize().x/2;
                    bolinha.getLocal().y = JogoActivity.getSize().y/2;
                    break;
            }
        }

        /**
         * Método que verifica se o jogo está ainda demonstrando as cores para o jogador.
         * @return um boolean que dirá se a demonstração acabou ou não.
         */
        public boolean avancarCor()
        {
            boolean naoAcabou = true; // iniciando a variável
            if (cpu.estaNoUltimo()) // se está na última cor
            {
                naoAcabou = false; // acabou
                cpu.reseta();      // resetamos o índice do Vector de cores
            }
            cpu.avancar();         // avançamos para não ficar no -1 e para avançar mesmo...
            return naoAcabou;      // retornamos se !não acabou ou não acabou
        }
    }
}