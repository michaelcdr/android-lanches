package br.ucs.androidlanches.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.data.DAO.PedidosDAO;
import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.recycleview.adapter.BebidasAdapter;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBebidaListener;


public class ListaDeBebidasActivity extends AppCompatActivity
{
    private List<Bebida> bebidas = new ArrayList<>();
    private DataAccessHelper db = new DataAccessHelper(this);
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
        bebidas = db.obterTodasBebidas();
        configurarAdapter(bebidas);
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