package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DAO.*;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickPratoListener;
import br.ucs.androidlanches.recycleview.adapter.PratoAdapter;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.rest.services.IProdutoApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListaDePratosActivity extends AppCompatActivity
{
    private List<Prato> pratos = new ArrayList<>();
    private ProdutosDAO _produtosDAO;
    private PedidosDAO _pedidosDAO;
    private RecyclerView recycleViewListaDePratos;
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
        setContentView(R.layout.activity_lista_de_pratos);
        setTitle("Lista de lanches");

        _pedidosDAO = new PedidosDAO(this);
        _produtosDAO = new ProdutosDAO(this);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_lista_pratos);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                obterPratos();
            }
        });

        configurarRecicleView();
        obterPratos();
    }

    private void configurarRecicleView()
    {
        recycleViewListaDePratos = findViewById(R.id.recycleViewPratos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleViewListaDePratos.setLayoutManager(layoutManager);
    }

    private void obterPratos()
    {
        IProdutoApiService produtosService = new RetrofitApiClient().getProdutoService();
        Call<List<Prato>> callPratos = produtosService.obterPratos();

        callPratos.enqueue(new Callback<List<Prato>>() {
            @Override
            public void onResponse(Call<List<Prato>> call, Response<List<Prato>> response) {
                if (response.isSuccessful())
                {
                    pratos = response.body();
                    configurarAdapter(pratos);
                }
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Prato>> call, Throwable exception) {
                if (exception instanceof ConnectException)
                {
                    pratos = _produtosDAO.obterTodosPratos();
                    configurarAdapter(pratos);
                }
                swipe.setRefreshing(false);
            }
        });
    }

    private void configurarAdapter(List<Prato> pratos)
    {
        PratoAdapter adapter = new PratoAdapter(this, pratos);
        recycleViewListaDePratos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new IOnItemClickPratoListener() {
            @Override
            public void onItemClick(Prato prato) {
                Intent dadosActivityAnterior = getIntent();
                mesaId = dadosActivityAnterior.getIntExtra("mesaId",0);
                numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);
                Log.i("LOG_ANDROID_LANCHES", "Clicou em escolher produto no pedido " + numeroPedido +".");
                try {
                    if (numeroPedido == 0)
                    {
                        int numeroPedido = _pedidosDAO.criarPedido(mesaId, prato);
                        Intent detalhesDoPedido = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity.class);
                        detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                        startActivityForResult(detalhesDoPedido, 1);
                    }
                    else
                    {
                        Intent detalhesDoPedido = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity.class);
                        detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                        _pedidosDAO.adicionarPedidoItem(numeroPedido, prato.getProdutoId());
                        startActivityForResult(detalhesDoPedido, 1);
                    }
                }
                catch (Exception e){
                    Log.e("LOG_ANDROID_LANCHES", "Não foi possivel adicionar o item no pedido." +  e.getMessage());
                    Toast.makeText(ListaDePratosActivity.this, "Não foi possivel adicionar o item no pedido.", Toast.LENGTH_LONG);
                }
            }
        });
    }
}