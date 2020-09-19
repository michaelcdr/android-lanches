package br.ucs.androidlanches.ui;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Mesa;
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
/*
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);*/
        FloatingActionButton fab = findViewById(R.id.fab);

        carregarPedidos();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Snackbar.make(view, "Nada implementado", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Intent intent = new Intent(getBaseContext(), EscolherMesaActivity.class);
            startActivity(intent);
            }
        });
    }

    private void seed()
    {
        for (int i =1; i<=20; i++)
        {
            db.adicionarMesa(new Mesa(i));
        }
    }

    private void carregarPedidos()
    {
        recyclerViewPedidos = findViewById(R.id.recycleViewPedidos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPedidos.setLayoutManager(layoutManager);

        pedidos = db.obterTodosPedidosSemPagamentoEfetuado();

        //seed();

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