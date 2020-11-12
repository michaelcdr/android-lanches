package br.ucs.androidlanches.services;

import android.content.Context;
import java.util.List;
import br.ucs.androidlanches.data.DAO.MesasDAO;
import br.ucs.androidlanches.data.DAO.iterfaces.IMesaDAO;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.rest.services.IMesaApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListagemDeMesaService
{
    private IMesaDAO _mesasDAO;
    private List<Mesa> _mesas;

    public ListagemDeMesaService(Context context)
    {
        _mesasDAO = new MesasDAO(context);
        _mesas = null;
    }

    public List<Mesa> obter()
    {
        //_mesas = _mesasDAO.obterTodasMesasDesocupadas();
        IMesaApiService mesaService = new RetrofitApiClient().getMesaService();
        Call<List<Mesa>> callMesas = mesaService.obterDesocupadas();

        callMesas.enqueue(new Callback<List<Mesa>>() {
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful()) {
                    _mesas = response.body();
                }
            }

            public void onFailure(Call<List<Mesa>> call, Throwable exception) {
                exception.printStackTrace();
            }
        });

        return _mesas;
    }
}
