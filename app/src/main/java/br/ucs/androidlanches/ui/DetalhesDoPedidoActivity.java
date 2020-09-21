package br.ucs.androidlanches.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.recycleview.adapter.IOnItemClickBtnDecrementarQtdItemPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.IOnItemClickBtnIncrementarQtdItemPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.PedidoItensAdapter;

public class DetalhesDoPedidoActivity extends AppCompatActivity
{
    private int numeroPedido;
    private DataAccessHelper db = new DataAccessHelper(this);
    private RecyclerView recyclerViewItensDoPedido;
    private Pedido pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_do_pedido);
        setTitle("Detalhes do pedido");
        pedido = obterPedidoAtual();
        configurarReciclerView();
        configurarAdapter(pedido);
        findViewById(R.id.btnAdicionarItemPedido).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetalhesDoPedidoActivity.this, "adicionar item no pedido " , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Pedido obterPedidoAtual()
    {
        Intent dadosActivityAnterior = getIntent();
        numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);
        Pedido pedido = db.obterPedido(numeroPedido);
        return pedido;
    }

    public void configurarReciclerView()
    {
        recyclerViewItensDoPedido = findViewById(R.id.recycleDetalhesDoPedido);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItensDoPedido.setLayoutManager(layoutManager);
    }

    public void configurarAdapter(Pedido pedido){
        PedidoItensAdapter adapter = new PedidoItensAdapter(this, pedido);
        recyclerViewItensDoPedido.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickBtnIncrementarQtdItemPedido(new IOnItemClickBtnIncrementarQtdItemPedidoListener() {
            @Override
            public void onItemClick(PedidoItem pedidoItem) {
                Toast.makeText(DetalhesDoPedidoActivity.this, "incrementar " , Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemClickBtnDecrementarQtdItemPedido(new IOnItemClickBtnDecrementarQtdItemPedidoListener() {
            @Override
            public void onItemClick(PedidoItem pedidoItem) {
                Toast.makeText(DetalhesDoPedidoActivity.this, "decrementar " , Toast.LENGTH_SHORT).show();

                /*
                Intent dadosActivityAnterior = getIntent();
                mesaId = dadosActivityAnterior.getIntExtra("mesaId",0);
                numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);



                if (numeroPedido == 0){
                    //Toast.makeText(ListaDePratosActivity.this, "clico botom,vai cria pedido mesa " + mesaId, Toast.LENGTH_SHORT).show();
                    int numeroPedido = db.criarPedido(mesaId, prato);
                    Intent detalhesDoPedido = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity.class);
                    detalhesDoPedido.putExtra("numeroPedido", numeroPedido);
                    startActivityForResult(detalhesDoPedido, 1);
                } else{
                    Toast.makeText(ListaDePratosActivity.this, "clico botom, ja tem  pedido mesa " + mesaId, Toast.LENGTH_SHORT).show();
                    db.adicionarPedidoItem(numeroPedido, prato.getProdutoId());
                }*/
            }
        });
    }
}