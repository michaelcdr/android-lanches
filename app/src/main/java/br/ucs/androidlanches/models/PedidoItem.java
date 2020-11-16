package br.ucs.androidlanches.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PedidoItem implements Serializable
{
    @SerializedName("pedidoItemId")
    @Expose
    private int pedidoItemId;

    @SerializedName("pedidoId")
    @Expose
    private int pedidoId;

    @SerializedName("quantidade")
    @Expose
    private int quantidade;

    private Produto produto;

    public PedidoItem(){}

    public PedidoItem(int pedidoId, int quantidade)
    {
        this.pedidoId = pedidoId;
        this.quantidade = quantidade;
    }

    public int getPedidoItemId() {
        return this.pedidoItemId;
    }


    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setProduto(Produto produto){
        this.produto = produto;
    }

    public Produto getProduto(){
        return this.produto;
    }

    public void setPedidoItemId(int pedidoItemId){
        this.pedidoItemId =  pedidoItemId;
    }

    public String getNomeProdutoComQtd() {
        if (produto == null) return "";

        return this.produto.getNome() + " ("+this.quantidade+")";
    }

    public void incrementarQtd() {
        this.quantidade = this.quantidade +1;
    }

    public void decrementarQtd() {
        if (this.quantidade > 0){
            this.quantidade = this.quantidade -1;
        }
    }

    public int getPedidoId() {
        return this.pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }
}