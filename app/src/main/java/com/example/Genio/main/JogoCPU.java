package com.example.Genius.main;

import java.util.Vector;

/**
 * A classe JogoCPU representa uma inteligência artificial para sortetios de inteiros que são
 * representados como cores para o jogo.
 *
 * A classe tem como base:
 * - 1 vetor que armazena inteiros;
 * - 1 inteiro que armazena a posição atual que está sendo percorrido no vetor;
 * - 2 booleans que armazenam a dificuldade configurada e o inicio de uma fase;
 *
 * Instâncias desta classe permitem gerênciar o vetor de cores e sortear uma mesma.
 *
 * @author Daniel Samogin Campioni e Pedro Luiz Pezoa
 * @since 2017
 * @version 1.0
 */

public class JogoCPU
{
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////ATRIBUTOS///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Atributo Vector<Integer> chamado 'fila', sua função é armazenar as cores utilizadas
     */
    private Vector<Integer> fila;

    /**
     * Atributo int chamado 'atual', sua função é armazenar a cor atual que percorre a 'fila'
     */
    public int atual;

    /**
     * Atributos booleans chamados 'ehHard' e 'inicioDeFase', suas funções são representar se o
     * jogo foi acionado e se a fase acabou de começar, respectivamente
     */
    private boolean ehHard, inicioDeFase;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////CONSTRUTOR///////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Contrutor da classe JogoCPU que instância todos os atributos com seus respectivos valores
     * @param _ehHard inteiro que representa em qual dificuldade o jogo começou
     */
    public JogoCPU(int _ehHard)
    {
        this.fila = new Vector<Integer>();
        this.atual = -1;
        this.inicioDeFase = true;

        if (_ehHard == 0) this.ehHard = false;
        else this.ehHard = true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////GETTERS E SETTERS///////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Método que retorna o valor do atributo 'ehHard'
     * @return um boolean que dirá o valor do atributo 'ehHard'
     */
    public boolean isHard()
    {
        return this.ehHard;
    }

    /**
     * Método que retorna o valor do atributo 'atual'
     * @return um inteiro que dirá o valor do atributo 'atual'
     */
    public int getAtual()
    {
        return this.fila.get(atual);
    }

    /**
     * Método que retorna o valor do atributo 'inicioDeFase'
     * @return um boolean que dirá o valor do atributo 'inicioDeFase'
     */
    public boolean isInicioDeFase() {
        return this.inicioDeFase;
    }

    /**
     * Método que atribui o valor no atributo 'inicioDeFase'
     * @param _inicioDeFase boolean que representa se irá recomeçar a fase ou não
     */
    public void setInicioDeFase(boolean _inicioDeFase) { this.inicioDeFase = _inicioDeFase; }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////MÉTODOS PRINCIPAIS/////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Método que sorteia uma cor entre as 4 opções que existe
     * @param cores vetor constante de inteiros que representa as cores que podem ser escolhidas
     */
    public void sortear(final int[] cores)
    {
        this.reseta();

        int sorteado = (int)(Math.random() * cores.length);
        // Max: 0.99 * 4 = 3
        // Min: 0.99 * 0 = 0
        this.fila.addElement(cores[sorteado]);
    }

    /**
     * Método que verifica se o atributo 'atual' está na última posição
     * @return um boolean que dirá se o atual esta na ultima posição ou não
     */
    public boolean estaNoUltimo()
    {
        if (this.fila.size() > 0)
            return this.atual == this.fila.size()-1;
        return true;
    }

    /**
     * Método que reseta o valor do atributo 'atual'
     */
    public void reseta()
    {
        this.atual = -1;
    }

    /**
     * Método que verifica se o valor do atributo 'atual' pode ser incrementado
     * @return um boolean que dirá se o valor do atributo 'atual'
     * menor que o tamanho do atributo 'fila'
     */
    public boolean podeAvancar()
    {
        return this.atual < this.fila.size()-1;
    }

    /**
     * Método que incrementa o valor do atributo 'atual'
     */
    public void avancar()
    {
        if (podeAvancar())
            this.atual++;
    }
}
