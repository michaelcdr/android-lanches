package br.ucs.androidlanches.ui;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.models.Prato;
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

    private void executarSeedSeNomTiverMesas()
    {
        List<Mesa> mesas = db.obterTodasMesas();

        if (mesas.size() == 0)
        {
            for (int i = 1; i <= 20; i++)
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
        }
    }

    private void obterTodosPedidosSemPagamentoEfetuado()
    {
        recyclerViewPedidos = findViewById(R.id.recycleViewPedidos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPedidos.setLayoutManager(layoutManager);

        pedidos = db.obterTodosPedidosSemPagamentoEfetuado();

        PedidosAdapter adapter = new PedidosAdapter(pedidos);
        recyclerViewPedidos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}