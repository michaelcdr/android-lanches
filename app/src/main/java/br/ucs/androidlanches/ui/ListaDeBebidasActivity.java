package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DAO.PedidosDAO;
import br.ucs.androidlanches.data.DAO.ProdutosDAO;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.recycleview.adapter.BebidasAdapter;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBebidaListener;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.rest.services.IProdutoApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListaDeBebidasActivity extends AppCompatActivity
{
    private List<Bebida> bebidas = new ArrayList<>();
    private ProdutosDAO _produtosDAO;
    private RecyclerView recycleViewListaDeBebidas;
    private PedidosDAO _pedidosDAO;
    private int mesaId;
    private int numeroPedido;
    private SwipeRefreshLayout swipe;

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_bebidas);
        setTitle("Lista de bebidas");

        _pedidosDAO = new PedidosDAO(this);
        _produtosDAO = new ProdutosDAO(this);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_lista_bebidas);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                obterBebidas();
            }
        });

        configurarRecicleView();
        obterBebidas();
    }

    private void configurarRecicleView()
    {
        recycleViewListaDeBebidas = findViewById(R.id.recycleViewBebidas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleViewListaDeBebidas.setLayoutManager(layoutManager);
    }

    private void obterBebidas()
    {
        Call<List<Bebida>> callBebidas = new RetrofitApiClient().getProdutoService().obterBebidas();
        callBebidas.enqueue(new Callback<List<Bebida>>() {
            @Override
            public void onResponse(Call<List<Bebida>> call, Response<List<Bebida>> response) {
                if (response.isSuccessful()) {
                    bebidas = response.body();
                    configurarAdapter(bebidas);
                    Log.i("LOG_ANDROID_LANCHES","Bebidas obtidas com sucesso. " );
                } else
                    Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro ao tentar obter a lista de bebibdas na API. " + response.message() );

                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Bebida>> call, Throwable exception) {
                if (exception instanceof ConnectException) {
                    bebidas = _produtosDAO.obterTodasBebidas();
                    configurarAdapter(bebidas);
                    Log.w("LOG_ANDROID_LANCHES","Não foi possivel obter a lista de bebidas na API devido a problemas de internet, resgatamos os dados locais. ");
                } else
                    Log.e("LOG_ANDROID_LANCHES","Não foi possivel obter a lista de bebidas. ");

                swipe.setRefreshing(false);
            }
        });
    }

    private void configurarAdapter(List<Bebida> bebidas)
    {
        BebidasAdapter adapter = new BebidasAdapter(this, bebidas);
        recycleViewListaDeBebidas.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new IOnItemClickBebidaListener() {
            @Override
            public void onItemClick(Bebida bebida) {
                Intent dadosActivityAnterior = getIntent();
                mesaId = dadosActivityAnterior.getIntExtra("mesaId",0);
                numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);

                if (numeroPedido == 0)
                {
                    int numeroPedido = _pedidosDAO.criarPedido(mesaId, bebida);
                    Intent detalhesDoPedido = new Intent(ListaDeBebidasActivity.this, DetalhesDoPedidoActivity.class);
                    detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                    startActivityForResult(detalhesDoPedido, 1);
                }
                else
                {
                    Intent detalhesDoPedido = new Intent(ListaDeBebidasActivity.this, DetalhesDoPedidoActivity.class);
                    detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                    _pedidosDAO.adicionarPedidoItem(numeroPedido, bebida.getProdutoId());
                    startActivityForResult(detalhesDoPedido, 1);
                }
            }
        });
    }
}