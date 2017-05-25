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
    public int cronometro, tempoHabilitado = JogoActivity.tempoDeSegundos(1);
    final Handler h = new Handler();
    private GerenciaCores corGeren;

    public BallView(Context context, int _ehHard)
    {
        super(context); // construindo a View

        bola = new Ball(getResources()); // criando a classe que mantém a bola
        vetorCores = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW}; // iniciando os ...
        // ...valores do vetor que indica as cores atuais
        cpu = new JogoCPU(_ehHard);     // iniciando a CPU do jogo
        corGeren = new GerenciaCores(); // iniciando o gerenciador de cores

        estaMostrando = printaMeio = podeFazerIf = true; // iniciando as variáveis boolean para true
        estaNoUltimo = false; // iniciando a variável boolean para false

        this.cpu.sortear(vetorCores); // sorteando a primeira cor

        comecarHandler(); // começando a cronometrar
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
            vetorCores[1] = CORES[1];
            vetorCores[2] = CORES[2];
            vetorCores[3] = CORES[3];
            if (bola.getLastColor() != Color.BLUE && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.BLUE);
        }

        else if (bola.estaEm(Color.GREEN))
        {
            vetorCores[1] = Color.rgb(0, 102, 0);
            vetorCores[0] = CORES[0];
            vetorCores[2] = CORES[2];
            vetorCores[3] = CORES[3];
            if (bola.getLastColor() != Color.GREEN && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.GREEN);
        }

        else if (bola.estaEm(Color.RED))
        {
            vetorCores[2] = Color.rgb(153, 0, 0);
            vetorCores[1] = CORES[1];
            vetorCores[0] = CORES[0];
            vetorCores[3] = CORES[3];
            if (bola.getLastColor() != Color.RED && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.RED);
        }

        else if (bola.estaEm(Color.YELLOW))
        {
            vetorCores[3] = Color.rgb(204, 204, 0);
            vetorCores[1] = CORES[1];
            vetorCores[2] = CORES[2];
            vetorCores[0] = CORES[0];
            if (bola.getLastColor() != Color.YELLOW && !cpu.isHard())
                bola.setAngulo(0);
            bola.setLastColor(Color.YELLOW);
        }
    }

    public void comecarHandler()
    {
        pararHandler = false; // não é necessário parar o Handler quando ele inicia
        cronometro = 1;       // o cronômetro não pode começar com um número divisível por 20...
                              // ...no caso dessa aplicação e de seus cálculos de tempo

        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            { // essa função é recursiva e é chamada de tempos em tempos
                cronometro++; // somamos o cronômetro para demonstrar o tempo passado
                podeFazerIf = true; // podemos entrar no IF do método "onDraw"

                if (!pararHandler)  // if para parar a recursão se necessário
                    h.postDelayed(this, JogoActivity.TEMPO); // recursão
            }
        }, JogoActivity.TEMPO); // tempo para a primeira chamada
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
            canvas.drawRect(0,
                    0,
                    JogoActivity.size.x/2 + 38,
                    JogoActivity.size.y/2 + 38, paint);
            paint.setColor(vetorCores[1]);
            canvas.drawRect(JogoActivity.size.x/2 + 38,
                    0,
                    JogoActivity.size.x + 76,
                    JogoActivity.size.y/2 + 38, paint);
            paint.setColor(vetorCores[2]);
            canvas.drawRect(0,
                    JogoActivity.size.y/2 + 38,
                    JogoActivity.size.x/2 + 38,
                    JogoActivity.size.y + 76, paint);
            paint.setColor(vetorCores[3]);
            canvas.drawRect(JogoActivity.size.x/2 + 38,
                    JogoActivity.size.y/2 + 38,
                    JogoActivity.size.x + 76  ,
                    JogoActivity.size.y + 76,paint);

            // verificando que pode desenhar a bola
            if (!estaMostrando && !estaNoUltimo)              // se não está no modo de demonstração
            {
                if (printaMeio)
                { // posicionamos a bola no meio (apenas no começo da fase)
                    this.bola.getLocal().x = (JogoActivity.size.x/2 + 38) - Ball.raio;
                    this.bola.getLocal().y = (JogoActivity.size.y/2 + 38) - Ball.raio;
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

            // chamamos o método de desenhar
            invalidate();
        }
        catch(Exception e){}
    }

    public boolean contou(int segundos)
    {
        return cronometro % segundos == 0;
    }

    /*
    *   Essa classe serve para organizar as cores que serão colocadas na tela, ela é interna porque
    *   utiliza variáveis que estão nessa classe.
     */
    private class GerenciaCores
    {
        // método para mudar a cor do fundo da cor atualmente na demonstração
        public void mudarCor()
        {
            switch (cpu.getAtual()) // baseando-se na atual demonstrativa
            {
                case Color.BLUE: // se for azul, escuremos ela
                    bola.getLocal().x = 0;
                    bola.getLocal().y = 0;
                    break;

                case Color.GREEN: // se for verde, escuremos ela
                    bola.getLocal().x = JogoActivity.size.x/2 + 38;
                    bola.getLocal().y = 0;
                    break;

                case Color.RED: // se for vermelha, escuremos ela
                    bola.getLocal().x = 0;
                    bola.getLocal().y = JogoActivity.size.y/2 + 38;
                    break;

                case Color.YELLOW: // se for amarela, escuremos ela
                    bola.getLocal().x = JogoActivity.size.x/2 + 38;
                    bola.getLocal().y = JogoActivity.size.y/2 + 38;
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