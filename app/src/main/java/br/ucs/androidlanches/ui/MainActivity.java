package br.ucs.androidlanches.ui;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnPagarPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickBtnVerPedidoListener;
import br.ucs.androidlanches.recycleview.adapter.PedidosAdapter;
import br.ucs.androidlanches.models.Pedido;

public class MainActivity extends AppCompatActivity
{
    private List<Pedido> pedidos = new ArrayList<>();
    private DataAccessHelper db = new DataAccessHelper(this);
    private RecyclerView recyclerViewPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pedidos");

        obterTodosPedidosSemPagamentoEfetuado();

        executarSeedSeNomTiverMesas();

        //clique que ira abrir o cadastro de pedidos...
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), EscolherMesaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        obterTodosPedidosSemPagamentoEfetuado();
    }

    private void executarSeedSeNomTiverMesas()
    {
        List<Mesa> mesas = db.obterTodasMesas();

        if (mesas.size() == 0)
        {
            for (int i = 1; i <= 100; i++)
            {
                db.adicionarMesa(new Mesa(i));
            }

            //adicionando bebidas
            db.adicionarBebida(new Bebida("Coca cola", "Zero", 8.0, "2 litros", "cocacola_zero_2l"));
            db.adicionarBebida(new Bebida("Coca cola", "Zero", 5.0, "600 ml", "cocacola_zero_600ml"));
            db.adicionarBebida(new Bebida("Coca cola", "Zero Lata", 3.5, "350 ml", "cocacola_zero_350ml"));

            db.adicionarBebida(new Bebida("Coca cola", "Normal", 8.0, "2 litros", "cocacola_2l"));
            db.adicionarBebida(new Bebida("Coca cola", "Normal", 5.0, "600 ml", "cocacola_600ml"));
            db.adicionarBebida(new Bebida("Coca cola", "Normal Lata", 3.5, "350 ml", "cocacola_350ml"));

            //db.adicionarBebida(new Bebida("Pepsi", "Coca cola é melhor mas quebra um galho.",5.0,"600 ml"));
            //db.adicionarBebida(new Bebida("Cachaça 51", "cachaça da boa!",3.50,"1 lt"));

            //adicionando pratos
            db.adicionarPrato(new Prato("Xis salada", "Hamburguer, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 20.0, 1, "xis_salada"));
            db.adicionarPrato(new Prato("Xis Calabresa", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 20.0, 1, "xis_calabresa"));
            db.adicionarPrato(new Prato("Pizza Calabresa", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza Palmito", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza Bacon", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza 4 Queijos", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza 5 Queijos", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
            db.adicionarPrato(new Prato("Pizza 8 Queijos", "Calabresa, alface, queijo, presunto, tomate, milho, erviolha, salada, acompanha fritas ", 50.0, 3, "pizza_calabresa"));
        }
    }

    private void obterTodosPedidosSemPagamentoEfetuado()
    {
        recyclerViewPedidos = findViewById(R.id.recycleViewPedidos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPedidos.setLayoutManager(layoutManager);

        pedidos = db.obterTodosPedidosSemPagamentoEfetuado();
        configurarAdapter(pedidos,recyclerViewPedidos);
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