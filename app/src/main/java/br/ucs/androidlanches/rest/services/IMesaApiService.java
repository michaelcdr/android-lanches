package br.ucs.androidlanches.rest.services;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import br.ucs.androidlanches.models.Mesa;

public interface IMesaApiService
{
    @GET("/v1/Mesas/Desocupadas")
    Call<List<Mesa>> obterDesocupadas();
}
