package br.ucs.androidlanches.rest.services;

import java.util.List;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Prato;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IProdutoApiService
{
    @GET("/v1/Produtos/Bebidas")
    Call<List<Bebida>> obterBebidas();

    @GET("/v1/Produtos/Pratos")
    Call<List<Prato>> obterPratos();
}
