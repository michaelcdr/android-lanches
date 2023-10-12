package br.ucs.androidlanches.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ucs.androidlanches.data.DAO.*;
import br.ucs.androidlanches.helpers.NetworkHelper;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.ui.adapter.BebidasAdapter;
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickBebidaListener;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.ui.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaDeBebidasActivity extends AppCompatActivity
{
    private List<Bebida> bebidas = new ArrayList<>();
    private ProdutosDAO _produtosDAO;
    private RecyclerView recycleViewListaDeBebidas;
    private PedidosDAO _pedidosDAO;
    private int mesaId;
    private Long numeroPedido;
    private SwipeRefreshLayout swipe;
    private String TAG_LOG = "LOG_ANDROID_LANCHES";
    private String ERRO_BEBIDAS = "Ocorreu um erro ao tentar obter a lista de bebibdas na API. ";
    private String ERRO_CRIAR_PEDIDO = "Não foi possivel criar o pedido e adicionar o produto.";

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
                obterBebidas("");
            }
        });

        Log.i(TAG_LOG,"ENTRO CREATE");
        configurarRecicleView();
        obterBebidas("");
    }

    private void configurarRecicleView()
    {
        recycleViewListaDeBebidas = findViewById(R.id.recycleViewBebidas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleViewListaDeBebidas.setLayoutManager(layoutManager);
    }

    private void obterBebidas(String filtro)
    {
        Map<String,String> filtros = new HashMap<>();
        filtros.put("descricao",filtro);

        if (NetworkHelper.temInternet(getBaseContext()))
        {
            Call<List<Bebida>> callBebidas = new RetrofitApiClient().getProdutoService().obterBebidas(filtros);
            callBebidas.enqueue(new Callback<List<Bebida>>() {
                @Override
                public void onResponse(Call<List<Bebida>> call, Response<List<Bebida>> response) {
                    if (response.isSuccessful()) {
                        bebidas = response.body();
                        configurarAdapter(bebidas);
                        Log.i(TAG_LOG,bebidas.size() + " Bebidas obtidas com sucesso. filtro = " + filtro );
                    } else
                        Log.e(TAG_LOG, ERRO_BEBIDAS + response.message());

                    swipe.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<Bebida>> call, Throwable exception) {
                    Log.w(TAG_LOG,ERRO_BEBIDAS + ", erro: " + exception.getMessage());
                    swipe.setRefreshing(false);
                }
            });
        } else {
            bebidas = _produtosDAO.obterTodasBebidas();
            configurarAdapter(bebidas);
            swipe.setRefreshing(false);
            Log.i(TAG_LOG,"Obtendo dados locais, " + bebidas.size() + " bebidas encontradas.");
        }
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
                numeroPedido = dadosActivityAnterior.getLongExtra("numeroPedido",0);

                if (numeroPedido == 0)
                    criarPedidoComProdutoSelecionado(bebida);
                else
                    adicionarProdutoNoPedidoAtual(bebida);
            }
        });
    }

    private void adicionarProdutoNoPedidoAtual(Bebida bebida) {
        if(NetworkHelper.temInternet(getBaseContext()))
        {
            Call<Void> callPedido = new RetrofitApiClient().getPedidoService()
                    .adicionarItem(numeroPedido, bebida.getProdutoId());
            callPedido.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()){
                        Log.i(TAG_LOG,"adicionou bebida no pedido " + numeroPedido + " com sucesso.");
                        irParaDetalhesPedido(numeroPedido);
                    } else {
                        Log.e(TAG_LOG, "erro ocorrido: " + response.message());
                        Toast.makeText(ListaDeBebidasActivity.this, response.message(), Toast.LENGTH_LONG);
                    }
                    swipe.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e(TAG_LOG, "obtendo dados locais devido ao erro ocorrido: " + t.getMessage());
                    Toast.makeText(ListaDeBebidasActivity.this, "Não foi possivel adicionar a bebida.", Toast.LENGTH_LONG);
                    swipe.setRefreshing(false);
                }
            });
        } else{
            _pedidosDAO.adicionarPedidoItem(numeroPedido, bebida.getProdutoId());
            irParaDetalhesPedido(numeroPedido);
            swipe.setRefreshing(false);
        }
    }

    private void criarPedidoComProdutoSelecionado(Bebida bebida)
    {
        if (NetworkHelper.temInternet(getBaseContext()))
        {
            Call<Long> callPedido = new RetrofitApiClient().getPedidoService().criar(mesaId, bebida.getProdutoId());
            callPedido.enqueue(new Callback<Long>() {
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {
                    if (response.isSuccessful()){
                        numeroPedido = response.body();
                        irParaDetalhesPedido(numeroPedido);
                        Log.i(TAG_LOG,"Criou pedido novo e adicionou bebida");
                    } else {
                        Log.e(TAG_LOG, "erro ocorrido: " + response.message());
                        Toast.makeText(ListaDeBebidasActivity.this, response.message(), Toast.LENGTH_LONG);
                    }
                    swipe.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {
                    Log.e(TAG_LOG, "Obtendo dados locais devido ao erro ocorrido: " + t.getMessage());
                    Toast.makeText(ListaDeBebidasActivity.this, ERRO_CRIAR_PEDIDO, Toast.LENGTH_LONG);
                    swipe.setRefreshing(false);
                }
            });
        } else {
            numeroPedido = _pedidosDAO.criar(mesaId, bebida);
            irParaDetalhesPedido(numeroPedido);
            swipe.setRefreshing(false);
        }
    }

    private void irParaDetalhesPedido(Long numeroPedido) {
        Intent intent = new Intent(ListaDeBebidasActivity.this, DetalhesDoPedidoActivity.class);
        intent.putExtra("numeroPedido", numeroPedido);
        finish();
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_bebidas,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search_bebidas).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                obterBebidas(query);
                Log.i(TAG_LOG,"fez submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                obterBebidas(newText);
                return false;
            }
        });
        return true;
    }
}