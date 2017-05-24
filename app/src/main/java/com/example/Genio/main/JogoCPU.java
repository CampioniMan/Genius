package com.example.Genio.main;

public class JogoCPU
{
    private Fila filaFixa, filaAux;

    public JogoCPU()
    {
        this.resetar();
    }

    public void sortear(int[] cores) throws Exception
    {
        int sorteado = (int)(Math.random() * cores.length-1);
        this.filaFixa.enfileirar(sorteado);
        this.filaAux.enfileirar(sorteado);
    }

    public boolean verificar(int cor) throws Exception
    {
        return this.filaAux.desefileirar() == cor;
    }

    public void resetar()
    {
        this.filaFixa = new Fila();
        this.filaAux = new Fila();
    }
}
