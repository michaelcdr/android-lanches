package br.ucs.androidlanches.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.data.DAO.*;
import br.ucs.androidlanches.helpers.NetworkHelper;
import br.ucs.androidlanches.models.*;
import br.ucs.androidlanches.services.GerenciadorDadosLocais;
import br.ucs.androidlanches.ui.adapter.listeners.*;
import br.ucs.androidlanches.ui.adapter.PedidosAdapter;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.ui.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private List<Pedido> pedidos = new ArrayList<>();
    private PedidosDAO _pedidosDao;
    private RecyclerView recyclerViewPedidos;
    private SwipeRefreshLayout swipe;
    private String TAG_LOGI = "LOG_ANDROID_LANCHES_I";
    private String TAG_LOG = "LOG_ANDROID_LANCHES";
    private String ERRO_PEDIDOS = "Algo deu errado ao tentar obter os pedidos.";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pedidos");

        _pedidosDao =  new PedidosDAO(this);

        configurarSwipeListaPedidos();
        configurarRecicleViewPedidos();
        obterTodosPedidosSemPagamentoEfetuado();
        gerarEventoCadastroPedido();
    }

    private void configurarSwipeListaPedidos() {
        swipe = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_lista_pedidos);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                obterTodosPedidosSemPagamentoEfetuado();
            }
        });
    }

    private void obterTodosPedidosSemPagamentoEfetuado()
    {
        if (NetworkHelper.temInternet(getBaseContext()))
        {
            Call<List<Pedido>> callPedidos = RetrofitApiClient.getPedidoService().obterTodosSemPagamentoEfetuado();
            callPedidos.enqueue(new Callback<List<Pedido>>() {
                @Override
                public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                    if (response.isSuccessful()){
                        Log.i(TAG_LOGI, "Requisição com sucesso em obter pedidos não pagos: ");
                        pedidos = response.body();
                        configurarAdapter(pedidos, recyclerViewPedidos);
                    }  else {
                        Log.e(TAG_LOG, ERRO_PEDIDOS +", erro: " + response.message());
                        Toast.makeText(MainActivity.this,ERRO_PEDIDOS,Toast.LENGTH_LONG).show();
                        configurarAdapter(pedidos, recyclerViewPedidos);
                    }
                    swipe.setRefreshing(false);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e(TAG_LOG,ERRO_PEDIDOS +", erro: " + t.getMessage());
                    swipe.setRefreshing(false);
                }
            });
        }
        else
        {
            pedidos = _pedidosDao.obterTodosPedidosSemPagamentoEfetuado();
            List<Pedido> todos = _pedidosDao.obterTodos();
            Log.i(TAG_LOGI,"Pedidos obtidos no banco local, total de " + pedidos.size() + " pedidos encontrados.");
            Log.i(TAG_LOGI,"Todos Pedidos obtidos no banco local, total de " + todos.size() + " pedidos encontrados.");
            configurarAdapter(pedidos, recyclerViewPedidos);
            swipe.setRefreshing(false);
        }
    }

    private void configurarRecicleViewPedidos()
    {
        recyclerViewPedidos = findViewById(R.id.recycleViewPedidos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPedidos.setLayoutManager(layoutManager);
    }

    private void configurarAdapter(List<Pedido> pedidos, RecyclerView recyclerView)
    {
        PedidosAdapter adapter = new PedidosAdapter(this, pedidos);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickVerPedidoListener(new IOnItemClickBtnVerPedidoListener() {
            @Override
            public void onItemClick(Pedido pedido) {
                Intent irParaDetalhesPedido = new Intent(MainActivity.this, DetalhesDoPedidoActivity.class);
                irParaDetalhesPedido.putExtra("numeroPedido",pedido.getNumero());
                startActivityForResult(irParaDetalhesPedido, 1);
            }
        });

        adapter.setOnItemClickBtnPagarListener(new IOnItemClickBtnPagarPedidoListener() {
            @Override
            public void onItemClick(Pedido pedido) {
                Intent irParaResumoPedido = new Intent(MainActivity.this, ResumoPedidoActivity.class);
                irParaResumoPedido.putExtra("numeroPedido",pedido.getNumero());
                startActivityForResult(irParaResumoPedido, 1);
            }
        });

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //garantindo que ao voltarmos para a lista de pedidos, ocorra uma atualização da lista.
        obterTodosPedidosSemPagamentoEfetuado();
    }

    private void gerarEventoCadastroPedido()
    {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EscolherMesaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        GerenciadorDadosLocais gerenciador = new GerenciadorDadosLocais(this);
        switch (item.getItemId())
        {
            case R.id.action_atualizar_base:
            {
                gerenciador.atualizar();
                break;
            }

            case R.id.action_limpar_base:
            {
                gerenciador.limpar();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}