package com.example.Genio.main;

import java.util.Vector;

public class JogoCPU
{
    private Vector<Integer> fila;
    public int atual;
    private boolean ehHard, comecouLoopAgora;

    public JogoCPU(int _ehHard)
    {
        this.fila = new Vector<Integer>();
        atual = -1;
        comecouLoopAgora = true;

        if (_ehHard == 0) this.ehHard = false;
        else this.ehHard = true;
    }

    // soreia um novo e bota no final
    public void sortear(final int[] cores)
    {
        this.reseta();

        int sorteado = (int)(Math.random() * cores.length);
        // Max: 0.99 * 4 = 3
        // Min: 0.99 * 0 = 0
        this.fila.addElement(cores[sorteado]);
    }

    public boolean estaNoUltimo()
    {
        if (this.fila.size() > 0)
            return this.atual == this.fila.size()-1;
        else
            return true;
    }

    // se está hard
    public boolean isHard()
    {
        return this.ehHard;
    }

    public Vector<Integer> getFilaAux() throws Exception
    {
        return this.fila;
    }

    // volta o atual para o início
    public void reseta()
    {
        this.atual = -1;
    }

    public boolean podeAvancar()
    {
        return this.atual < this.fila.size()-1;
    }

    public void avancar()
    {
        if (podeAvancar())
            this.atual++;
    }


    // pega o valor atual
    public int getAtual()
    {
        return this.fila.get(atual);
    }

    public boolean isComecouLoopAgora() {
        return comecouLoopAgora;
    }

    public void setComecouLoopAgora(boolean comecouLoopAgora) {
        this.comecouLoopAgora = comecouLoopAgora;
    }
}
