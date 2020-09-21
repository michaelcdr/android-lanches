package br.ucs.androidlanches.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.recycleview.adapter.IOnItemClickBtnDecrementarQtdItemPedido;
import br.ucs.androidlanches.recycleview.adapter.IOnItemClickBtnIncrementarQtdItemPedido;
import br.ucs.androidlanches.recycleview.adapter.IOnItemClickPratoListener;
import br.ucs.androidlanches.recycleview.adapter.PedidoItensAdapter;
import br.ucs.androidlanches.recycleview.adapter.PratoAdapter;

public class DetalhesDoPedidoActivity extends AppCompatActivity
{
    private int numeroPedido;
    private DataAccessHelper db = new DataAccessHelper(this);
    private RecyclerView recyclerViewItensDoPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_do_pedido);
        setTitle("Detalhes do pedido");
        obterPedidoAtual();
    }

    private void obterPedidoAtual()
    {
        Intent dadosActivityAnterior = getIntent();
        numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);

        recyclerViewItensDoPedido = findViewById(R.id.recycleDetalhesDoPedido);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItensDoPedido.setLayoutManager(layoutManager);

        Pedido pedido = db.obterPedido(numeroPedido);
        configurarAdapter(pedido);
    }

    public void configurarAdapter(Pedido pedido){
        PedidoItensAdapter adapter = new PedidoItensAdapter(this, pedido);
        recyclerViewItensDoPedido.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickBtnIncrementarQtdItemPedido(new IOnItemClickBtnIncrementarQtdItemPedido() {
            @Override
            public void onItemClick(PedidoItem pedidoItem) {
                Toast.makeText(DetalhesDoPedidoActivity.this, "incrementar " , Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemClickBtnDecrementarQtdItemPedido(new IOnItemClickBtnDecrementarQtdItemPedido() {
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