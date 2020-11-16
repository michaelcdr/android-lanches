package br.ucs.androidlanches.ui.activity;
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
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickBtnDecrementarQtdItemPedidoListener;
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickBtnIncrementarQtdItemPedidoListener;
import br.ucs.androidlanches.ui.adapter.PedidoItensAdapter;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.ui.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesDoPedidoActivity extends AppCompatActivity
{
    private Long numeroPedido;
    private RecyclerView recyclerViewItensDoPedido;
    private Pedido pedido;
    private PedidosDAO _pedidoDAO;
    private String TAG_LOG = "LOG_ANDROID_LANCHES";

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i(TAG_LOG, "Entrou em onResume detalhes do pedido");
        pedido = obterPedidoAtual();
    }

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
        numeroPedido = dadosActivityAnterior.getLongExtra("numeroPedido",0);

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
                    Log.e(TAG_LOG,"não foi possivel obter o pedido.");
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Log.e(TAG_LOG,"não foi possivel obter o pedido na api. " + t.getMessage());
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

                Call<Void> callIncrementarItemPedido = RetrofitApiClient.getPedidoService().incrementarQtdProduto(pedidoItem.getPedidoItemId());
                callIncrementarItemPedido.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            finish();
                            startActivityForResult(getIntent(), 1);
                        } else {
                            Log.e(TAG_LOG,"Não foi possível obter os dados devido a um erro na API de destino, tente novamente mais tarde.");
                            Toast.makeText(DetalhesDoPedidoActivity.this,"Não foi possível obter os dados devido a um erro na API de destino, tente novamente mais tarde. " , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG_LOG,"Incrementando no banco local." + t.getMessage() );
                        pedidoItem.incrementarQtd();
                        _pedidoDAO.atualizarPedidoItem(pedidoItem);
                        finish();
                        startActivityForResult(getIntent(), 1);
                    }
                });
            }
        });

        adapter.setOnItemClickBtnDecrementarQtdItemPedido(new IOnItemClickBtnDecrementarQtdItemPedidoListener() {
            @Override
            public void onItemClick(PedidoItem pedidoItem) {


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
            }
        });
    }
}