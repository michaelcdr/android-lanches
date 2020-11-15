package br.ucs.androidlanches.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DAO.MesasDAO;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickMesaListener;
import br.ucs.androidlanches.ui.adapter.MesaAdapter;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.ui.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EscolherMesaActivity extends AppCompatActivity
{
    private List<Mesa> mesas = new ArrayList<>();
    private MesasDAO _mesasDAO;
    private RecyclerView recyclerViewMesas;
    private SwipeRefreshLayout swipe;
    private String TAG_LOG ="LOG_ANDROID_LANCHES";
    private String ERRO_API = "Não foi possivel carregar as mesas pela API, erro ocorrido: ";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_mesa);
        setTitle("Escolha uma mesa");

        _mesasDAO = new MesasDAO(this);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_lista_mesas);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carregarMesas();
            }
        });

        carregarMesas();
    }

    /*
    @Override
    protected void onStart()
    {
        super.onStart();
        carregarMesas();
    }*/

    private void carregarMesas()
    {
        configurarReciclerViewMesas();

        Call<List<Mesa>> callMesas = RetrofitApiClient.getMesaService().obterDesocupadas();
        callMesas.enqueue(new Callback<List<Mesa>>() {
            public void onResponse(Call<List<Mesa>> call, Response<List<Mesa>> response) {
                if (response.isSuccessful()) {
                    mesas = response.body();
                    configurarAdapter(mesas, recyclerViewMesas);
                    swipe.setRefreshing(false);
                    Log.i(TAG_LOG,"Numero de mesas "+ mesas.size());
                } else {
                    swipe.setRefreshing(false);
                    Log.i(TAG_LOG,ERRO_API + response.message());
                }
            }

            public void onFailure(Call<List<Mesa>> call, Throwable exception) {
                Log.e(TAG_LOG,ERRO_API + exception.getMessage());
                mesas = _mesasDAO.obterTodasMesasDesocupadas();
                configurarAdapter(mesas, recyclerViewMesas);
                Log.i(TAG_LOG,"Mesas no banco local: " + mesas.size());

                swipe.setRefreshing(false);
            }
        });
    }

    private void configurarReciclerViewMesas()
    {
        recyclerViewMesas = findViewById(R.id.recycleViewMesas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMesas.setLayoutManager(layoutManager);
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
        //adapter.notifyDataSetChanged();
    }

    public boolean isInternetAvailable()
    {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }
}