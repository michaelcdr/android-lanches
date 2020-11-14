package br.ucs.androidlanches.ui.activity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ucs.androidlanches.data.DAO.*;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickPratoListener;
import br.ucs.androidlanches.ui.adapter.PratoAdapter;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.ui.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
<<<<<<< HEAD
        pratos = db.obterTodosPratos();
        configurarAdpter(pratos);
    }

    private void configurarAdpter(List<Prato> pratos)
=======
        Map<String,String> filtros = new HashMap<>();
        filtros.put("descricao","");
        Call<List<Prato>> callPratos = new RetrofitApiClient().getProdutoService().obterPratos(filtros);
        callPratos.enqueue(new Callback<List<Prato>>() {
            @Override
            public void onResponse(Call<List<Prato>> call, Response<List<Prato>> response) {
                if (response.isSuccessful()) {
                    pratos = response.body();
                    configurarAdapter(pratos);
                    Log.i("LOG_ANDROID_LANCHES","Pratos obtidos com sucesso pela API. " );
                } else {
                    Log.e(
                    "LOG_ANDROID_LANCHES",
                    "Ocorreu um erro ao tentar obter a lista de pratos na API. " + response.message()
                    );
                }
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Prato>> call, Throwable exception) {
                if (exception instanceof ConnectException) {
                    pratos = _produtosDAO.obterTodosPratos();
                    configurarAdapter(pratos);
                    Log.w(
                    "LOG_ANDROID_LANCHES",
                    "Não foi possivel obter a lista de pratos na API devido a problemas de internet, " +
                        "resgatamos os dados locais, " + pratos.size() + " pratos encontradas."
                    );
                } else
                    Log.e("LOG_ANDROID_LANCHES","Não foi possivel obter a lista de pratos. ");

                swipe.setRefreshing(false);
            }
        });
    }

    private void configurarAdapter(List<Prato> pratos)
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb
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
<<<<<<< HEAD
                //Toast.makeText(ListaDePratosActivity.this, "clico botom, mesa " + mesaId, Toast.LENGTH_SHORT).show();

                if (numeroPedido == 0){
                    //Toast.makeText(ListaDePratosActivity.this, "clico botom,vai cria pedido mesa " + mesaId, Toast.LENGTH_SHORT).show();
                    int numeroPedido = db.criarPedido(mesaId, prato);
                    Intent detalhesDoPedido = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity.class);
                    detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                    startActivityForResult(detalhesDoPedido, 1);
                } else {
                    //Toast.makeText(ListaDePratosActivity.this, "clico botom, ja tem  pedido mesa " + mesaId, Toast.LENGTH_SHORT).show();
                    Intent detalhesDoPedido = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity.class);
                    detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                    db.adicionarPedidoItem(numeroPedido, prato.getProdutoId());
                    startActivityForResult(detalhesDoPedido, 1);
=======
                Log.i("LOG_ANDROID_LANCHES", "Clicou em escolher produto no pedido " + numeroPedido +".");

                try {
                    if (numeroPedido == 0)
                    {
                        Call<Integer> callCriarPedido = new RetrofitApiClient().getPedidoService().criar(mesaId,prato.getProdutoId());
                        callCriarPedido.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.isSuccessful()) {
                                    numeroPedido = response.body();
                                    Intent detalhesDoPedido = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity.class);
                                    detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                                    startActivityForResult(detalhesDoPedido, 1);
                                    Log.i("LOG_ANDROID_LANCHES","Pedido criado, e prato adicionado com sucesso pela API. " );
                                } else {
                                    Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro ao tentar obter a lista de pratos na API. " + response.message());
                                    Toast.makeText(ListaDePratosActivity.this,"Não foi possivel obter a lista de pratos na API. ", Toast.LENGTH_LONG).show();
                                }
                                swipe.setRefreshing(false);
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable exception) {
                                if (exception instanceof ConnectException) {
                                    numeroPedido = _pedidosDAO.criarPedido(mesaId, prato);
                                    configurarAdapter(pratos);
                                    Log.w("LOG_ANDROID_LANCHES", "Não foi possivel obter a lista de pratos na API devido a problemas de internet, " +"resgatamos os dados locais, " + pratos.size() + " pratos encontradas.");
                                } else
                                    Log.e("LOG_ANDROID_LANCHES","Não foi possivel obter a lista de pratos. ");

                                swipe.setRefreshing(false);
                            }
                        });
                    }
                    else
                    {
                        Call<Void> callCriarPedido = new RetrofitApiClient().getPedidoService().adicionarItem(numeroPedido,prato.getProdutoId());
                        callCriarPedido.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    finish();
                                    Intent detalhesDoPedido = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity.class);
                                    detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                                    startActivityForResult(detalhesDoPedido, 1);
                                    Log.i("LOG_ANDROID_LANCHES","Prato adicionado com sucesso pela API.");
                                } else {
                                    Log.e("LOG_ANDROID_LANCHES","Ocorreu um erro ao tentar adicionar pratos pela API. " + response.message());
                                    Toast.makeText(ListaDePratosActivity.this,"Ocorreu um erro ao tentar adicionar o prato. ", Toast.LENGTH_LONG).show();
                                }
                                swipe.setRefreshing(false);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable exception) {
                                if (exception instanceof ConnectException) {
                                    Intent detalhesDoPedido = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity.class);
                                    detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                                    _pedidosDAO.adicionarPedidoItem(numeroPedido, prato.getProdutoId());
                                    startActivityForResult(detalhesDoPedido, 1);
                                    Log.w("LOG_ANDROID_LANCHES", "Não foi possivel obter a lista de pratos na API devido a problemas de internet, " +"resgatamos os dados locais, " + pratos.size() + " pratos encontradas.");
                                } else
                                    Log.e("LOG_ANDROID_LANCHES","Não foi possivel obter a lista de pratos. ");

                                swipe.setRefreshing(false);
                            }
                        });
                    }
                }
                catch (Exception e){
                    Log.e("LOG_ANDROID_LANCHES", "Não foi possivel adicionar o item no pedido." +  e.getMessage());
                    Toast.makeText(ListaDePratosActivity.this, "Não foi possivel adicionar o item no pedido.", Toast.LENGTH_LONG);
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb
                }
            }
        });
    }
}