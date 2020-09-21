package br.ucs.androidlanches.models;

import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable
{
    private int numero;
    private boolean pago;
    private Mesa mesa;
    private double gorjeta;
    private List<PedidoItem> itens;

    public Pedido(int numero, boolean pago, Mesa mesa) {
        this.numero = numero;
        this.pago = pago;
        this.mesa = mesa;
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
    public double getGorjeta(){ return this.gorjeta; }
    public void setGorjeta(double gorjeta){  this.gorjeta = gorjeta; }

    public void setItens(List<PedidoItem> itens)
    {
        this.itens = itens;
    }

    public List<PedidoItem> getItens()
    {
        return this.itens;
    }
}