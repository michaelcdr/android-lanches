package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.services.ListagemDeMesaService;
import br.ucs.androidlanches.data.DAO.MesasDAO;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickMesaListener;
import br.ucs.androidlanches.recycleview.adapter.MesaAdapter;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.rest.services.IMesaApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EscolherMesaActivity extends AppCompatActivity
{
    private List<Mesa> mesas = new ArrayList<>();
    private MesasDAO _mesasDAO;
    private RecyclerView recyclerViewMesas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_mesa);
        setTitle("Escolha uma mesa");

        _mesasDAO = new MesasDAO(this);
        carregarMesas();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        carregarMesas();
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }

    private void carregarMesas()
    {
        recyclerViewMesas = findViewById(R.id.recycleViewMesas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMesas.setLayoutManager(layoutManager);

        Retrofit retrofit = RetrofitApiClient.getClient();
        IMesaApiService mesaService = retrofit.create(IMesaApiService.class);
        Call<List<Mesa>> callMesas = mesaService.obterDesocupadas();

        callMesas.enqueue(new Callback<List<Mesa>>() {
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful()) {
                    mesas = response.body();
                    configurarAdapter(mesas, recyclerViewMesas);
                }
            }

            public void onFailure(Call<List<Mesa>> call, Throwable exception) {
                if (exception instanceof ConnectException)
                    mesas = _mesasDAO.obterTodasMesasDesocupadas();

                exception.printStackTrace();
            }
        });
    }

    private void configurarAdapter(List<Mesa> mesas, RecyclerView recyclerView)
    {
        MesaAdapter adapter = new MesaAdapter(this, mesas);
        recyclerViewMesas.setAdapter(adapter);

        adapter.setOnItemClickListener(new IOnItemClickMesaListener() {
            @Override
            public void onItemClick(Mesa mesaObtida) {
                //Toast.makeText(getApplicationContext(), mesaObtida.obterNumeroParaView(), Toast.LENGTH_SHORT).show();
                Intent abrirSelecaoPedidoComMesaSelecionada = new Intent(EscolherMesaActivity.this, EscolherTipoProdutoActivity.class);
                abrirSelecaoPedidoComMesaSelecionada.putExtra("mesaId", mesaObtida.getMesaId());
                startActivityForResult(abrirSelecaoPedidoComMesaSelecionada,1);
            }
        });
        adapter.notifyDataSetChanged();
    }
}