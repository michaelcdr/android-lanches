package br.ucs.androidlanches.rest.services;

import java.util.List;
import br.ucs.androidlanches.models.Bebida;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IProdutoApiService
{
    @GET("/v1/Produtos/Bebidas")
    Call<List<Bebida>> obterBebidas();
}
