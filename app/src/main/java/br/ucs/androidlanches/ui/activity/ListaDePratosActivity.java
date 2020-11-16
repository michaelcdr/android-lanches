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
import br.ucs.androidlanches.helpers.NetworkHelper;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickPratoListener;
import br.ucs.androidlanches.ui.adapter.PratoAdapter;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.ui.R;
import okhttp3.ResponseBody;
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
    private Long numeroPedido;
    private SwipeRefreshLayout swipe;
    private String TAG_LOG ="LOG_ANDROID_LANCHES";
    private String TAG_LOGI ="LOG_ANDROID_LANCHES_I";
    private String ERRO_INTERNET = "Não foi possivel obter a lista de pratos na API devido a problemas de internet.";
    private String ERRO_API = "Ocorreu um erro ao tentar obter a dados na API. ";
    private String LOG_ADD_PEDIDO = "Pedido criado, e prato adicionado com sucesso pela API. ";
    private String ERRO_CRIAR_PEDIDO = "Ocorreu um erro ao tentar criar pedido com produto. ";

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
        Map<String, String> filtros = new HashMap<>();
        filtros.put("descricao","");

        if (NetworkHelper.temInternet(getBaseContext()))
        {
            Call<List<Prato>> callPratos = new RetrofitApiClient().getProdutoService().obterPratos(filtros);
            callPratos.enqueue(new Callback<List<Prato>>() {
                @Override
                public void onResponse(Call<List<Prato>> call, Response<List<Prato>> response) {
                    if (response.isSuccessful()) {
                        pratos = response.body();
                        configurarAdapter(pratos);
                        Log.i(TAG_LOG,"Pratos obtidos com sucesso pela API. " );
                    } else
                        Log.e(TAG_LOG, ERRO_API + response.message());

                    swipe.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<Prato>> call, Throwable exception) {
                    Log.w(TAG_LOG,"Erro ao carregar pratos, erro: " + exception.getMessage());
                    swipe.setRefreshing(false);
                }
            });
        } else {
            pratos = _produtosDAO.obterTodosPratos();
            configurarAdapter(pratos);
            swipe.setRefreshing(false);
            Log.i(TAG_LOG,"Obtendo dados locais, " + pratos.size() + " pratos encontrados.");
        }
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
                numeroPedido = dadosActivityAnterior.getLongExtra("numeroPedido",0);

                Log.i(TAG_LOGI, "Clicou em escolher produto no pedido " + numeroPedido +".");

                if (numeroPedido == 0)
                    criarPedidoComProdutoSelecionado(prato);
                else
                    adicionarItemNoPedidoAtual(prato);
            }
        });
    }

    private void adicionarItemNoPedidoAtual(Prato prato)
    {
        if (NetworkHelper.temInternet(getBaseContext()))
        {
            Call<Void> callCriarPedido = new RetrofitApiClient().getPedidoService().adicionarItem(numeroPedido, prato.getProdutoId());
            callCriarPedido.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        finish();
                        Log.i(TAG_LOG,"Prato adicionado com sucesso pela API.");
                    } else {
                        Log.e(TAG_LOG,"Ocorreu um erro ao tentar adicionar pratos pela API. " + response.message());
                        Toast.makeText(ListaDePratosActivity.this,"Ocorreu um erro ao tentar adicionar o prato. ", Toast.LENGTH_LONG).show();
                    }
                    swipe.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable exception) {
                    Toast.makeText(ListaDePratosActivity.this,ERRO_CRIAR_PEDIDO, Toast.LENGTH_LONG).show();
                    swipe.setRefreshing(false);
                }
            });
        } else {
            _pedidosDAO.adicionarPedidoItem(numeroPedido, prato.getProdutoId());
            finish();
            swipe.setRefreshing(false);
            Log.i(TAG_LOG, "Item adicionado no pedido, pelo banco local");
        }
    }

    private void criarPedidoComProdutoSelecionado(Prato prato)
    {
        if(NetworkHelper.temInternet(getBaseContext()))
        {
            Call<Long> criarPedido = new RetrofitApiClient().getPedidoService().criar(mesaId, prato.getProdutoId());
            criarPedido.enqueue(new Callback<Long>() {
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {
                    if (response.isSuccessful()) {
                        numeroPedido = response.body();
                        irParaDetalhesPedido(numeroPedido);
                        Log.i(TAG_LOG, LOG_ADD_PEDIDO);
                    } else {
                        Log.e(TAG_LOG,ERRO_API + " Erro: " + response.message());
                        Toast.makeText(ListaDePratosActivity.this, ERRO_API, Toast.LENGTH_LONG).show();
                    }
                    swipe.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<Long> call, Throwable exception) {
                    Log.e(TAG_LOG,ERRO_API + exception.getMessage());
                    Toast.makeText(ListaDePratosActivity.this, ERRO_API, Toast.LENGTH_LONG).show();
                    swipe.setRefreshing(false);
                }
            });
        } else {
            Log.i(TAG_LOG, "Pedido criado no banco local, e prato adicionado.");
            numeroPedido = _pedidosDAO.criar(mesaId, prato);
            irParaDetalhesPedido(numeroPedido);
            swipe.setRefreshing(false);
        }
    }

    private void irParaDetalhesPedido(Long numeroPedido) {
        Intent intent = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity.class);
        intent.putExtra("numeroPedido", numeroPedido);
        finish();
        startActivityForResult(intent, 1);
    }
}