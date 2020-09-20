package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.recycleview.adapter.MesasAdapter;
import br.ucs.androidlanches.recycleview.adapter.PedidosAdapter;

public class EscolherMesaActivity extends AppCompatActivity
{
    private List<Mesa> mesas = new ArrayList<>();
    private DataAccessHelper db = new DataAccessHelper(this);
    private RecyclerView recyclerViewMesas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_mesa);
        setTitle("Escolha uma mesa");
        carregarMesas();
    }

    private void carregarMesas()
    {
        recyclerViewMesas = findViewById(R.id.recycleViewMesas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMesas.setLayoutManager(layoutManager);

        mesas = db.obterTodasMesasDesocupadas();
        MesasAdapter adapter = new MesasAdapter(mesas);
        recyclerViewMesas.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}