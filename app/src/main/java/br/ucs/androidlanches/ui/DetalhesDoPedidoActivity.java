package br.ucs.androidlanches.ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.ucs.androidlanches.data.DAO.PedidosDAO;
import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnDecrementarQtdItemPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnIncrementarQtdItemPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.PedidoItensAdapter;
import android.widget.Toast;

public class DetalhesDoPedidoActivity extends AppCompatActivity
{
    private int numeroPedido;
    private DataAccessHelper db = new DataAccessHelper(this);
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
        configurarReciclerView();
        configurarAdapter(pedido);
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
        Pedido pedido = _pedidoDAO.obterPedido(numeroPedido);
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
                //Toast.makeText(DetalhesDoPedidoActivity.this, "incrementar " , Toast.LENGTH_SHORT).show();
                Intent dadosActivityAnterior = getIntent();
                pedidoItem.incrementarQtd();
                _pedidoDAO.atualizarPedidoItem(pedidoItem);
                finish();
                startActivityForResult(getIntent(), 1);
            }
        });

        adapter.setOnItemClickBtnDecrementarQtdItemPedido(new IOnItemClickBtnDecrementarQtdItemPedidoListener() {
            @Override
            public void onItemClick(PedidoItem pedidoItem) {
                //Toast.makeText(DetalhesDoPedidoActivity.this, "decrementar " , Toast.LENGTH_SHORT).show();
                Intent dadosActivityAnterior = getIntent();
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
}