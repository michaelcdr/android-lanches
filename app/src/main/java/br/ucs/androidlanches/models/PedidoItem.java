package br.ucs.androidlanches.models;

import java.io.Serializable;

public class PedidoItem implements Serializable {
    private int pedidoItemId;
    private int pedidoId;
    private int quantidade;

    public PedidoItem(){}

    public PedidoItem(int pedidoId, int quantidade){
        this.pedidoId = pedidoId;
        this.quantidade = quantidade;
    }

    public int getPedidoItemId() {
        return this.pedidoItemId;
    }
    public int getPedidoId() {
        return this.pedidoId;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}