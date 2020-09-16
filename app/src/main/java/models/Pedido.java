package models;

import java.io.Serializable;

public class Pedido implements Serializable
{
    private int numero;
    private boolean pago;
    private Mesa mesa;


    public Pedido(int numero, boolean pago) {
        this.numero = numero;
        this.pago = pago;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}
