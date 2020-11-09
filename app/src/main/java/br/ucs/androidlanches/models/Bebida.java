package br.ucs.androidlanches.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bebida extends Produto implements Serializable
{
    @SerializedName("embalagem")
    @Expose
    private String embalagem;

    public String getEmbalagem() {
        return embalagem;
    }

    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    public Bebida(){};

    public Bebida(String nome,String descricao, double preco, String embalagem,String foto)
    {
        this.setTipo("bebida");
        this.setNome(nome);
        this.setDescricao(descricao);
        this.setPreco(preco);
        this.embalagem = embalagem;
        this.setFoto(foto);
    }

    public Bebida(int produtoId, String nome,String descricao, double preco, String embalagem)
    {
        this.setProdutoId(produtoId);
        this.setTipo("bebida");
        this.setNome(nome);
        this.setDescricao(descricao);
        this.setPreco(preco);
        this.embalagem = embalagem;
    }
}