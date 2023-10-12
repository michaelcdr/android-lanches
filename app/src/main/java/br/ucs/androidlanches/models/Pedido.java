package br.ucs.androidlanches.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Pedido implements Serializable
{
    @SerializedName("numero")
    @Expose
    private Long numero;

    @SerializedName("pago")
    @Expose
    private boolean pago;

    @SerializedName("mesa")
    @Expose
    private Mesa mesa;

    @SerializedName("gorjeta")
    @Expose
    private boolean gorjeta;

    @SerializedName("itens")
    @Expose
    private List<PedidoItem> itens;

    @SerializedName("pedidoId")
    @Expose
    private int pedidoId;

    @SerializedName("pedidoIdApi")
    @Expose
    private int pedidoIdApi;

    public Pedido(Long numero, boolean pago, Mesa mesa)
    {
        this.numero = numero;
        this.pago = pago;
        this.mesa = mesa;
    }

    public Pedido(Long numero, boolean pago, int mesaId, int numeroMesa)
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

    public Long getNumero() {
        return numero;
    }
    public int getPedidoIdApi() {
        return pedidoIdApi;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
    public boolean getGorjeta(){ return this.gorjeta; }
    public void setGorjeta(boolean gorjeta){  this.gorjeta = gorjeta; }

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
        double total = 0;
        for (int i = 0; i < this.getItens().size(); i++)
        {
            PedidoItem item = this.getItens().get(i);
            total += item.getQuantidade() * item.getProduto().getPreco();
            retorno += item.getQuantidade() + " x " + item.getProduto().getPreco()+ " »" + item.getProduto().getNome() + "\n";
            retorno += "sub total » " + item.getQuantidade() * item.getProduto().getPreco() + "\n\n";
        }
        String valor = NumberFormat.getCurrencyInstance(new Locale("pt-BR", "BR")).format(total);
        retorno += "Total de " + valor.replace(".",",");
        return retorno;
    }

    public String obterTotalFormatado()
    {
        double total = 0;

        for (int i = 0; i < this.getItens().size(); i++)
        {
            PedidoItem item = this.getItens().get(i);
            total += item.getQuantidade() * item.getProduto().getPreco();
        }

        String valor = NumberFormat.getCurrencyInstance(new Locale("pt-BR", "BR")).format(total);
        return valor.replace(".",",");
    }

    public boolean getPago() {
        return this.pago;
    }

    public int getId() {
        return this.pedidoId;
    }
}