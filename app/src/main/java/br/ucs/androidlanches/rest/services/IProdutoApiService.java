package br.ucs.androidlanches.rest.services;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.rest.filtros.FiltroBebidas;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


public interface IProdutoApiService
{
    @GET("/v1/Produtos/Bebidas")
    Call<List<Bebida>> obterBebidas(@QueryMap Map<String,String> parameters);

    @GET("/v1/Produtos/Pratos")
    Call<List<Prato>> obterPratos(@QueryMap Map<String,String> parameters);
}

