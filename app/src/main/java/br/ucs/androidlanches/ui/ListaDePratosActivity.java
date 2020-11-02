package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.data.DAO.*;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickPratoListener;
import br.ucs.androidlanches.recycleview.adapter.PratoAdapter;

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
        pratos = _produtosDAO.obterTodosPratos();
        configurarAdpter(pratos);
    }

    private void configurarAdpter(List<Prato> pratos)
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