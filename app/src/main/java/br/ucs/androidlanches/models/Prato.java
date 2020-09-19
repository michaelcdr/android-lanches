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
}
