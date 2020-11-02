package br.ucs.androidlanches.ui;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DAO.*;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnPagarPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnVerPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.PedidosAdapter;
import br.ucs.androidlanches.models.Pedido;

public class MainActivity extends AppCompatActivity
{
    private List<Pedido> pedidos = new ArrayList<>();
    private PedidosDAO _pedidosDao;
    private RecyclerView recyclerViewPedidos;
    private MesasDAO _mesasDao;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pedidos");

        _pedidosDao =  new PedidosDAO(this);
        _mesasDao = new MesasDAO(this);

        obterTodosPedidosSemPagamentoEfetuado();
        gerarEventoCadastroPedido();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        obterTodosPedidosSemPagamentoEfetuado();
    }

    private void gerarEventoCadastroPedido()
    {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EscolherMesaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void obterTodosPedidosSemPagamentoEfetuado()
    {
        recyclerViewPedidos = findViewById(R.id.recycleViewPedidos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPedidos.setLayoutManager(layoutManager);

        pedidos = _pedidosDao.obterTodosPedidosSemPagamentoEfetuado();
        configurarAdapter(pedidos, recyclerViewPedidos);
    }

    private void configurarAdapter(List<Pedido> pedidos, RecyclerView recyclerView)
    {
        PedidosAdapter adapter = new PedidosAdapter(this, pedidos);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickVerPedidoListener(new IOnItemClickBtnVerPedidoListener() {
            @Override
            public void onItemClick(Pedido pedido) {
                Intent irParaDetalhesPedido = new Intent(MainActivity.this, DetalhesDoPedidoActivity.class);
                irParaDetalhesPedido.putExtra("numeroPedido",pedido.getNumero());
                startActivityForResult(irParaDetalhesPedido, 1);
            }
        });
        adapter.setOnItemClickBtnPagarListener(new IOnItemClickBtnPagarPedidoListener() {
            @Override
            public void onItemClick(Pedido pedido) {
                Intent irParaResumoPedido = new Intent(MainActivity.this, ResumoPedidoActivity.class);
                irParaResumoPedido.putExtra("numeroPedido",pedido.getNumero());
                startActivityForResult(irParaResumoPedido, 1);
            }
        });

        adapter.notifyDataSetChanged();
    }
}