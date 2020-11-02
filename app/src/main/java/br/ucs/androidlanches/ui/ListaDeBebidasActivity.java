package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
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
        Retrofit retrofit = RetrofitApiClient.getClient();
        IProdutoApiService produtosService = retrofit.create(IProdutoApiService.class);
        Call<List<Bebida>> callBebidas = produtosService.obterBebidas();

        callBebidas.enqueue(new Callback<List<Bebida>>() {
            @Override
            public void onResponse(Call<List<Bebida>> call, Response<List<Bebida>> response) {
                if(response.isSuccessful()){
                    bebidas = response.body();
                    configurarAdapter(bebidas);
                }
            }

            @Override
            public void onFailure(Call<List<Bebida>> call, Throwable exception) {
                if (exception instanceof ConnectException){
                    bebidas = _produtosDAO.obterTodasBebidas();
                    configurarAdapter(bebidas);
                }
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