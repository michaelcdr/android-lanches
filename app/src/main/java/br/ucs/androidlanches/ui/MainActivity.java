package br.ucs.androidlanches.ui;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DAO.*;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnPagarPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnVerPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.PedidosAdapter;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.rest.services.IPedidoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
{
    private List<Pedido> pedidos = new ArrayList<>();
    private PedidosDAO _pedidosDao;
    private MesasDAO _mesasDao;
    private ProdutosDAO _produtosDao;
    private RecyclerView recyclerViewPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pedidos");

        _pedidosDao =  new PedidosDAO(this);
        _mesasDao = new MesasDAO(this);
        _produtosDao = new ProdutosDAO(this);

        obterTodosPedidosSemPagamentoEfetuado();
        configurarRecicleViewPedidos();
        gerarEventoCadastroPedido();
    }

    private void obterTodosPedidosSemPagamentoEfetuado()
    {
        Call<List<Pedido>> callPedidos = RetrofitApiClient.getPedidoService().obterTodosSemPagamentoEfetuado();
        callPedidos.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                Log.i("Pedidos Aberto", "Requisição com sucesso em obter pedidos não pagos: ");
                if (response.isSuccessful()){
                    pedidos = response.body();
                    configurarAdapter(pedidos, recyclerViewPedidos);
                } else{
                    Log.e("Pedidos Aberto", "Algo deu errado ao obter pedidos na API");
                    pedidos = _pedidosDao.obterTodosPedidosSemPagamentoEfetuado();
                    configurarAdapter(pedidos, recyclerViewPedidos);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("Pedidos Aberto","Falhou na requisição de pedidos");
                pedidos = _pedidosDao.obterTodosPedidosSemPagamentoEfetuado();
                configurarAdapter(pedidos, recyclerViewPedidos);
            }
        });
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
        switch (item.getItemId())
        {
            case R.id.action_atualizar_base:
            {
                Log.i("Menu","Atualizando base local.");
            }
            break;

            case R.id.action_limpar_base:
            {
                Log.i("Menu","Limpando base local.");
                _mesasDao.deletarTodas();
                _produtosDao.deletarTodos();
                _pedidosDao.deletarTodos();
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }
}