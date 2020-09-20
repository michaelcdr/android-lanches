package br.ucs.androidlanches.models;

import java.io.Serializable;

public class Prato extends Produto implements Serializable
{
    private int serveQuantasPessoas;

    public int getServeQuantasPessoas() {
        return serveQuantasPessoas;
    }

    public void setServeQuantasPessoas(int serveQuantasPessoas) {
        this.serveQuantasPessoas = serveQuantasPessoas;
    }

    public Prato(){}

    public Prato(String nome,String descricao, double preco, int serveQuantasPessoas,String foto)
    {
        this.setTipo("prato");
        this.setNome(nome);
        this.setDescricao(descricao);
        this.setPreco(preco);
        this.serveQuantasPessoas = serveQuantasPessoas;
        this.setFoto(foto);
    }
}
