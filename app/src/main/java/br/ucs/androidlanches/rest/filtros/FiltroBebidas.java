package br.ucs.androidlanches.rest.filtros;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class FiltroBebidas implements Serializable {

    @SerializedName("nome")
    @Expose
    public String nome;

    @SerializedName("embalagem")
    @Expose
    public String embalagem;

    @SerializedName("descricao")
    @Expose
    public String descricao;

    public FiltroBebidas(String nome, String descricao, String embalagem)
    {
        this.nome = nome;
        this.descricao = descricao;
        this.embalagem = embalagem;
    }

}
