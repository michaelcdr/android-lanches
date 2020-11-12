package br.ucs.androidlanches.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Produto implements Serializable
{
    @SerializedName("produtoId")
    @Expose
    private int produtoId;

    @SerializedName("nome")
    @Expose
    private String nome;

    @SerializedName("descricao")
    @Expose
    private String descricao;

    @SerializedName("preco")
    @Expose
    private double preco;

    @SerializedName("foto")
    @Expose
    private String foto;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    public Produto(){}

    public Produto(String nome,String descricao, double preco){
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTipo() { return this.tipo; }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}