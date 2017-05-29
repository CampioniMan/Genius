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
        super(context); // construindo a View

        bola = new Ball(getResources()); // criando a classe que mantém a bola
        vetorCores = new int[]{          // iniciando os valores do vetor que indica as cores atuais
                Color.BLUE,
                Color.GREEN,
                Color.RED,
                Color.YELLOW};
        cpu = new JogoCPU(_ehHard);      // iniciando a CPU do jogo
        corGeren = new GerenciaCores();  // iniciando o gerenciador de cores

        estaMostrando = printaMeio = podeFazerIf = true; // iniciando as variáveis boolean para true
        estaNoUltimo = false;                            // iniciando a variável boolean para false

        tempoHabilitado = JogoActivity.tempoDeSegundos(1);
        qtoEscuro = 0;
        velEscuridao = 25f;

        this.cpu.sortear(vetorCores); // sorteando a primeira cor

        comecarHandler();             // começando a cronometrar
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
        // verifica qual cor a bola esta
        if (bola.estaEm(Color.BLUE))
        {
            // se não estiver mostrando as cores, elas serão colocadas como cores de jogo
            if (!estaMostrando)
                vetorCores[0] = Color.rgb(CORESESCURASRGB[0][0],
                                          CORESESCURASRGB[0][1],
                                          CORESESCURASRGB[0][2]);
            // se estiver mostrando as cores, elas serão colocadas de modo a piscarem para o jogador
            else
                vetorCores[0] = Color.rgb(CORESRGB[0][0],
                                          CORESRGB[0][1],
                                          CORESRGB[0][2] - qtoEscuro);

            // atribui as cores restamtes para seus respectivos quadrantes
            vetorCores[1] = CORES[1];
            vetorCores[2] = CORES[2];
            vetorCores[3] = CORES[3];

            // verifica se a ultima cor que a bola ficou é diferente da cor que ele esta
            if (bola.getLastColor() != Color.BLUE && !cpu.isHard())
                bola.setAngulo(0); // se for diferente, a animação irá resetar
            bola.setLastColor(Color.BLUE); // no final, será atribuido ao atributo lastColor...
                                           // ... a cor atual
        }

        // repete o mesmo processo acima
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

        // repete o mesmo processo acima
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

        // repete o mesmo processo acima
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
            // criando o pincel para alteração das cores
            Paint paint = new Paint();

            // 'entrado' a cada 1 segundos(no caso de estar demonstrando)
            if (podeFazerIf && estaMostrando && contou(tempoHabilitado))
            {
                if (cpu.isComecouLoopAgora())        // se o loop da demonstração começou AGORA
                {
                    cpu.setComecouLoopAgora(false);  // falo que não começou mais AGORA
                    cpu.reseta();                    // coloco o atual do Vector no -1
                }
                estaMostrando = corGeren.certezaMostrando();// obtendo se acabou ou não a mostração
                qtoEscuro = 0;
                velEscuridao = 25f;
                if (!estaMostrando)                  // se não mais está na demonstração
                {
                    pararHandler = true;             // paramos o handler da demonstração
                    cpu.setComecouLoopAgora(true);   // falamos que vai começar AGORA a demonstração
                    cronometro++;                    // tiramos COM CERTEZA o cronômetro da contagem
                }
                else
                    corGeren.mudarCor();             // altera a cor do fundo da bolinha
                podeFazerIf = false;                 // já foi um loop, não pode mais ir
            }

            // atualiza a cor de fundo de onde a bola está
            atualizar();

            // pintando as cores do fundo nas suas respectivas coordenadas
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

            // verificando que pode desenhar a bola
            if (!estaMostrando && !estaNoUltimo) // se não está no modo de demonstração
            {
                if (printaMeio)
                { // posicionamos a bola no meio (apenas no começo da fase)
                    this.bola.getLocal().x = (JogoActivity.getSize().x/2 + 38) - Ball.getRaio();
                    this.bola.getLocal().y = (JogoActivity.getSize().y/2 + 38) - Ball.getRaio();
                    printaMeio = false; // impedimos o loop
                }

                // desenhamos a bola
                canvas.drawBitmap(this.bola.getTextura(),
                        this.bola.getLocal().x,
                        this.bola.getLocal().y, null);

                // desenhamos o "loading" circular da bola
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

            // chamamos o método de desenhar
            invalidate();
        }
        catch(Exception e){}
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////MÉTODOS AUXILIARES////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void comecarHandler()
    {
        this.pararHandler = false; // não é necessário parar o Handler quando ele inicia
        this.cronometro = 1;       // o cronômetro não pode começar com um número divisível por 20...
                              // ...no caso dessa aplicação e de seus cálculos de tempo

        this.h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {             // essa função é recursiva e é chamada de tempos em tempos
                cronometro++;             // somamos o cronômetro para demonstrar o tempo passado
                podeFazerIf = true;       // podemos entrar no IF do método "onDraw"
                qtoEscuro += velEscuridao;
                velEscuridao -= 0.5f;
                if (!pararHandler)        // if para parar a recursão se necessário
                    h.postDelayed(this, JogoActivity.TEMPO); // começa a recursão
            }
        }, JogoActivity.TEMPO);           // tempo para a primeira chamada
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
        // método para mudar a posição da bolinha para mudar atualmente na demonstração
        public void mudarCor()
        {
            switch (cpu.getAtual()) // baseando-se na atual demonstrativa
            {
                case Color.BLUE:    // se for azul, escuremos ela
                    bola.getLocal().x = bola.getLocal().y = 0;
                    break;

                case Color.GREEN: // se for verde, escuremos ela
                    bola.getLocal().x = JogoActivity.getSize().x/2 + 38;
                    bola.getLocal().y = 0;
                    break;

                case Color.RED:   // se for vermelha, escuremos ela
                    bola.getLocal().x = 0;
                    bola.getLocal().y = JogoActivity.getSize().y/2 + 38;
                    break;

                case Color.YELLOW: // se for amarela, escuremos ela
                    bola.getLocal().x = JogoActivity.getSize().x/2 + 38;
                    bola.getLocal().y = JogoActivity.getSize().y/2 + 38;
                    break;
            }
        }

        // Esse método verifica a vericidade do avanço para a próxima posição da demonstração
        public boolean certezaMostrando()
        {
            boolean acabou = false; // retorno (caso tenha acabado o loop de demonstração)
            if (cpu.estaNoUltimo()) // caso o atual esteja no último
            {
                acabou = true;      // então acabou
                cpu.reseta();       // e voltamos o atual para o -1
            }
            cpu.avancar();          // avançamos para o atual ficar 0
            return !acabou;         // retornamos o inverso do acabou...
                                    // ...(para melhor entendimento do código de fora dessa função)
        }
    }
}