package br.ucs.androidlanches.ui;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.ucs.androidlanches.data.DAO.PedidosDAO;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnDecrementarQtdItemPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnIncrementarQtdItemPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.PedidoItensAdapter;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesDoPedidoActivity extends AppCompatActivity
{
    private int numeroPedido;
    private RecyclerView recyclerViewItensDoPedido;
    private Pedido pedido;
    private PedidosDAO _pedidoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_do_pedido);
        setTitle("Detalhes do pedido");

        _pedidoDAO = new PedidosDAO(this);

        pedido = obterPedidoAtual();

        findViewById(R.id.btnAdicionarItemPedido).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(DetalhesDoPedidoActivity.this, "adicionar item no pedido " , Toast.LENGTH_SHORT).show();
                Intent adicionarNovoProduto = new Intent(DetalhesDoPedidoActivity.this, EscolherTipoProdutoActivity.class);
                adicionarNovoProduto.putExtra("mesaId", pedido.getMesaId());
                adicionarNovoProduto.putExtra("numeroPedido", pedido.getNumero());
                startActivityForResult(adicionarNovoProduto, 1);
            }
        });
    }

    private Pedido obterPedidoAtual()
    {
        Intent dadosActivityAnterior = getIntent();
        numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);

        Call<Pedido> pedidoCall = RetrofitApiClient.getPedidoService().obter(numeroPedido);
        pedidoCall.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful()){
                    pedido = response.body();
                    configurarReciclerView();
                    configurarAdapter(pedido);
                } else {
                    Toast.makeText(DetalhesDoPedidoActivity.this, "Não foi possível obter os dados devido a um erro na API de destino, tente novamente mais tarde. " , Toast.LENGTH_LONG).show();
                    Log.e("LOG_ANDROID_LANCHES","não foi possivel obter o pedido.");
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","não foi possivel obter o pedido na api. " + t.getMessage());
                pedido = _pedidoDAO.obterPedido(numeroPedido);
                configurarReciclerView();
                configurarAdapter(pedido);
            }
        });
        return pedido;
    }

    public void configurarReciclerView()
    {
        recyclerViewItensDoPedido = findViewById(R.id.recycleDetalhesDoPedido);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItensDoPedido.setLayoutManager(layoutManager);
    }

    public void configurarAdapter(final Pedido pedido)
    {
        PedidoItensAdapter adapter = new PedidoItensAdapter(this, pedido);
        recyclerViewItensDoPedido.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickBtnIncrementarQtdItemPedido(new IOnItemClickBtnIncrementarQtdItemPedidoListener() {
            @Override
            public void onItemClick(PedidoItem pedidoItem) {
<<<<<<< HEAD
                //Toast.makeText(DetalhesDoPedidoActivity.this, "incrementar " , Toast.LENGTH_SHORT).show();
                Intent dadosActivityAnterior = getIntent();
                pedidoItem.incrementarQtd();
                db.atualizarPedidoItem(pedidoItem);
                finish();
                startActivityForResult(getIntent(), 1);
=======
                Call<Void> callIncrementarItemPedido = RetrofitApiClient.getPedidoService().incrementarQtdProduto(pedidoItem.getPedidoItemId());
                callIncrementarItemPedido.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            finish();
                            startActivityForResult(getIntent(), 1);
                        } else {
                            Log.e("LOG_ANDROID_LANCHES","Não foi possível obter os dados devido a um erro na API de destino, tente novamente mais tarde.");
                            Toast.makeText(
                            DetalhesDoPedidoActivity.this,
                            "Não foi possível obter os dados devido a um erro na API de destino, tente novamente mais tarde. " , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("LOG_ANDROID_LANCHES","Incrementando no banco local." + t.getMessage() );
                        pedidoItem.incrementarQtd();
                        _pedidoDAO.atualizarPedidoItem(pedidoItem);
                        finish();
                        startActivityForResult(getIntent(), 1);
                    }
                });
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb
            }
        });

        adapter.setOnItemClickBtnDecrementarQtdItemPedido(new IOnItemClickBtnDecrementarQtdItemPedidoListener() {
            @Override
            public void onItemClick(PedidoItem pedidoItem) {
<<<<<<< HEAD
                //Toast.makeText(DetalhesDoPedidoActivity.this, "decrementar " , Toast.LENGTH_SHORT).show();
                Intent dadosActivityAnterior = getIntent();
                pedidoItem.decrementarQtd();
                db.atualizarPedidoItem(pedidoItem);
                finish();
                startActivityForResult(getIntent(), 1);
=======
                Call<Void> callDecrementarItemPedido = RetrofitApiClient.getPedidoService().decrementarQtdProduto(pedidoItem.getPedidoItemId());
                callDecrementarItemPedido.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            finish();
                            startActivityForResult(getIntent(), 1);
                        } else {
                            Log.e("LOG_ANDROID_LANCHES","Não foi possível obter os dados devido a um erro na API de destino, tente novamente mais tarde. " );
                            Toast.makeText(DetalhesDoPedidoActivity.this, "Não foi possível obter os dados devido a um erro na API de destino, tente novamente mais tarde. " , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.i("LOG_ANDROID_LANCHES","decrementando no banco local");
                        pedidoItem.decrementarQtd();

                        if (pedidoItem.getQuantidade() == 0)
                            _pedidoDAO.deletarPedidoItem(pedidoItem);
                        else
                            _pedidoDAO.atualizarPedidoItem(pedidoItem);

                        finish();
                        startActivityForResult(getIntent(), 1);
                    }
                });
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb
            }
        });
    }
}