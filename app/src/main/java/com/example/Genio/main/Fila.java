package com.example.Genio.main;

public class Fila
{
    protected int inicio, fim, size;
    protected int[] fila;

    //---------------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------Construtores--------------------------------------------------------//
    //---------------------------------------------------------------------------------------------------------------------------//

    public Fila(){this (100);}

    @SuppressWarnings("unchecked")
    public Fila(int _tam)
    {
        if (_tam <= 0) this.fila = new int[100];
        else this.fila = new int[_tam];

        this.inicio = this.fim = -1;
        this.size = 0;
    }

    //---------------------------------------------------------------------------------------------------------------------------//
    //-----------------------------------------------------Métodos Principais----------------------------------------------------//
    //---------------------------------------------------------------------------------------------------------------------------//

    public void enfileirar(int _elem) throws Exception
    {
        if (isFull())
            throw new Exception("Fila Cheia");

        if (isEmpty()) inicio = 0;
        fim = (fim + 1) % this.fila.length;
        this.fila[fim] = _elem;
        this.size++;
    }

    public int desefileirar() throws Exception
    {
        if (isEmpty())
            throw new Exception("Fila Vazia");

        int indElemento = inicio;
        inicio = (inicio + 1) % fila.length;

        if (inicio == ((fim + 1) % fila.length))
            inicio = fim = -1;
        this.size--;

        return this.fila[indElemento];
    }

    public int consultaPrimeiro() throws Exception
    {
        if (isEmpty())
            throw new Exception("Fila Vazia");
        return this.fila[inicio];
    }

    public boolean isFull()
    {
        return (((inicio == 0) && ((fim + 1) == this.fila.length)) || (fim + 1 == inicio));
    }

    public boolean isEmpty()
    {
        return (inicio == -1);
    }

    public int getSize()
    {
        return this.size;
    }

    //---------------------------------------------------------------------------------------------------------------------------//
    //---------------------------------------------------Métodos Adicionais------------------------------------------------------//
    //---------------------------------------------------------------------------------------------------------------------------//

    public Fila clonarFila() throws Exception
    {
        Fila novaFila =  new Fila(this.fila.length);
        int i = inicio;
        boolean acabou = this.isEmpty();

        while (!acabou)
        {
            novaFila.enfileirar(this.fila[i]);
            i = (i + 1) % this.fila.length; // garante a circularidade

            if ((inicio == 0) && (fim < this.fila.length-1)) acabou = (i > fim);
            else if (fim == this.fila.length-1)              acabou = (i == 0);
            else if (inicio > fim)                           acabou = ((i <= inicio) && (i > fim));
            else                                             acabou = (i == inicio) || (i == inicio + 1);
        }
        return novaFila;
    }

    public void limparFila()
    {
        this.fila = new int[this.fila.length];
        this.size = 0;
        this.inicio = this.fim = -1;
    }

    public void inverteFila()throws Exception
    {
        if (this.isEmpty()) return;

        int aux = 0;
        int lento = inicio, rapido = 0;
        if (this.inicio != this.fila.length)
            rapido = inicio + 1;

        while (lento == this.fim)
        {
            aux = this.fila[lento];
            this.fila[lento] = this.fila[rapido];
            this.fila[rapido] = aux;
            lento++;
            rapido++;
        }
    }

    public void juntarFila(Fila _fila) throws Exception
    {
        if (_fila == null)
            throw new Exception("Fila Inválido");

        this.aumentaFila(_fila.getSize());
        int i = inicio;
        boolean acabou = this.isEmpty();

        while (!acabou)
        {
            this.fila[i] = 0;
            i = (i + 1) % this.fila.length; // garante a circularidade

            if ((inicio == 0) && (fim < this.fila.length-1)) acabou = (i > fim);
            else if (fim == this.fila.length-1)              acabou = (i == 0);
            else if (inicio > fim)                           acabou = ((i <= inicio) && (i > fim));
            else                                             acabou = (i == inicio) || (i == inicio + 1);
        }
    }

    private void aumentaFila(int _tam) throws Exception
    {
        int[] filaVelha = this.fila;
        this.fila = new int[(_tam + this.fila.length)];

        for (int i = 0; i < filaVelha.length; i++)
            this.fila[i] = filaVelha[i];
    }

    //---------------------------------------------------------------------------------------------------------------------------//
    //---------------------------------------------------Métodos Apocalipticos---------------------------------------------------//
    //---------------------------------------------------------------------------------------------------------------------------//

    public String toString()
    {
        if (this.isEmpty()) return "[]";

        String txt = "[";
        int i = inicio;
        boolean acabou = this.isEmpty();

        while (!acabou)
        {
            txt += this.fila[i] + ",";
            i = (i + 1) % this.fila.length; // garante a circularidade

            if ((inicio == 0) && (fim < this.fila.length-1)) acabou = (i > fim);
            else if (fim == this.fila.length-1)              acabou = (i == 0);
            else if (inicio > fim)                           acabou = ((i <= inicio) && (i > fim));
            else                                             acabou = (i == inicio) || (i == inicio + 1);
        }
        return txt.substring(0, txt.length()-1) + "]";
    }

    public int hashCode()
    {
        int ret = super.hashCode();

        ret = ret*7 + new Integer(this.inicio).hashCode();
        ret = ret*7 + new Integer(this.fim).hashCode();

        for (int i = inicio; i <= this.fim-1;)
        {
            ret = ret*7 + new Integer(fila[i]).hashCode();
            i = (i + 1) % this.fila.length; // garante a rotatividade
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object _obj)
    {
        if (_obj == null)
            return false;

        if (this == _obj)
            return true;

        if (this.getClass() == _obj.getClass())
        {
            Fila proxN = (Fila)_obj;

            if ((this.inicio == proxN.inicio) && (this.fim == proxN.fim))
            {
                int i = inicio;
                boolean acabou = this.isEmpty();

                while (!acabou)
                {
                    if (this.fila[i] != proxN.fila[i]) return false;

                    i = (i + 1) % this.fila.length; // garante a circularidade

                    if ((inicio == 0) && (fim < this.fila.length-1)) acabou = (i > fim);
                    else if (fim == this.fila.length-1)              acabou = (i == 0);
                    else if (inicio > fim)                           acabou = ((i <= inicio) && (i > fim));
                    else                                             acabou = (i == inicio) || (i == inicio + 1);
                }
            }
            return true;
        }

        if (_obj instanceof String)
        {
            String proxN = (String)_obj;

            if (this.toString().equals(proxN.toString()))
                return true;
        }
        return false;
    }
}

