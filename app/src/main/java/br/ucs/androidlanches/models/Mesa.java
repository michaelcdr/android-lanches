package br.ucs.androidlanches.models;

import java.io.Serializable;

public class Mesa implements Serializable
{

    private int numero;
    private int mesaId;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    public Mesa(){}
    public Mesa(int numero){
        this.numero=numero;
    }
    public Mesa(int mesaId, int numero)
    {
        this.mesaId = mesaId;
        this.numero = numero;
    }

    public int getMesaId() {
        return mesaId;
    }

    public void setMesaId(int mesaId) {
        this.mesaId = mesaId;
    }

    public String obterNumeroParaView()
    {
        if (this.getNumero() <= 9 && this.getNumero() > 0)
            return "Mesa 0" + new Integer(this.getNumero()).toString();

        return "Mesa " + new Integer(this.getNumero()).toString();
    }
}
