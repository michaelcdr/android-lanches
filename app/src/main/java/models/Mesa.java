package models;

import java.io.Serializable;

public class Mesa implements Serializable {
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

    public int getMesaId() {
        return mesaId;
    }

    public void setMesaId(int mesaId) {
        this.mesaId = mesaId;
    }
}
