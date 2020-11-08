package br.ucs.androidlanches.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable
{
    @SerializedName("numero")
    @Expose
    private int numero;

    @SerializedName("pago")
    @Expose
    private boolean pago;

    @SerializedName("mesa")
    @Expose
    private Mesa mesa;

    @SerializedName("gorjeta")
    @Expose
    private double gorjeta;

    @SerializedName("itens")
    @Expose
    private List<PedidoItem> itens;

    public Pedido(int numero, boolean pago, Mesa mesa)
    {
        this.numero = numero;
        this.pago = pago;
        this.mesa = mesa;
    }

    public Pedido(int numero, boolean pago, int mesaId, int numeroMesa)
    {
        this.numero = numero;
        this.pago = pago;
        this.mesa = new Mesa(mesaId,numeroMesa);
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
    public int getMesaId(){ return this.getMesa().getMesaId();}

    public String detalharPedido()
    {
        String retorno = "";
        double total =0;
        for (int i = 0; i < this.getItens().size(); i++)
        {
            PedidoItem item = this.getItens().get(i);
            total += item.getQuantidade() * item.getProduto().getPreco();
            retorno += item.getQuantidade() + " x " + item.getProduto().getPreco()+ " »" + item.getProduto().getNome() + "\n";
            retorno += "sub total » " + item.getQuantidade() * item.getProduto().getPreco() + "\n\n";
        }
        retorno += "Total = " + total;
        return retorno;
    }
}