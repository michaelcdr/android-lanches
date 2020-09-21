package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.recycleview.adapter.IOnItemClickPratoListener;
import br.ucs.androidlanches.recycleview.adapter.PratoAdapter;

public class ListaDePratosActivity extends AppCompatActivity
{
    private List<Prato> pratos = new ArrayList<>();
    private DataAccessHelper db = new DataAccessHelper(this);
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
        obterPratos();
    }

    private void obterPratos()
    {
        recycleViewListaDePratos = findViewById(R.id.recycleViewPratos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleViewListaDePratos.setLayoutManager(layoutManager);

        pratos = db.obterTodosPratos();
        configurarAdpter(pratos,recycleViewListaDePratos);
    }

    private void configurarAdpter(List<Prato> pratos, RecyclerView recyclerView)
    {
        PratoAdapter adapter = new PratoAdapter(this, pratos);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new IOnItemClickPratoListener() {
            @Override
            public void onItemClick(Prato prato) {
                Intent dadosActivityAnterior = getIntent();
                mesaId = dadosActivityAnterior.getIntExtra("mesaId",0);
                numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);

                //Toast.makeText(ListaDePratosActivity.this, "clico botom, mesa " + mesaId, Toast.LENGTH_SHORT).show();
                //Intent adicionarPratoNoPedido = new Intent(ListaDePratosActivity.this, DetalhesDoPedidoActivity);
                if (numeroPedido == 0){
                    Toast.makeText(ListaDePratosActivity.this, "clico botom,vai cria pedido mesa " + mesaId, Toast.LENGTH_SHORT).show();

                    int numeroPedido = db.criarPedido(mesaId, prato);
                } else{
                    Toast.makeText(ListaDePratosActivity.this, "clico botom, ja tem  pedido mesa " + mesaId, Toast.LENGTH_SHORT).show();
                    db.adicionarPedidoItem(numeroPedido, prato.getProdutoId());
                }
            }
        });
    }
}