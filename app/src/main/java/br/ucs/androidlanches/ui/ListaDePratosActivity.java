package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;

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
        Retrofit retrofit = RetrofitApiClient.getClient();
        IProdutoApiService produtosService = retrofit.create(IProdutoApiService.class);
        Call<List<Prato>> callPratos = produtosService.obterPratos();

        callPratos.enqueue(new Callback<List<Prato>>() {
            @Override
            public void onResponse(Call<List<Prato>> call, Response<List<Prato>> response) {
                if (response.isSuccessful())
                {
                    pratos = response.body();
                    configurarAdapter(pratos);
                }
            }

            @Override
            public void onFailure(Call<List<Prato>> call, Throwable exception) {
                if (exception instanceof ConnectException)
                {
                    pratos = _produtosDAO.obterTodosPratos();
                    configurarAdapter(pratos);
                }
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
        });
    }
}