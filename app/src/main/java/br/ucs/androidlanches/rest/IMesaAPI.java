package br.ucs.androidlanches.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import br.ucs.androidlanches.models.Mesa;

public interface IMesaAPI
{
    @GET("/v1/Mesas/ObterDesocupadas")
    Call<List<Mesa>> getDesocupadas();
}
