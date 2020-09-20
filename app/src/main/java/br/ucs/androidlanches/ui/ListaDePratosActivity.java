package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.recycleview.adapter.PratosAdapter;

public class ListaDePratosActivity extends AppCompatActivity
{
    private List<Prato> pratos = new ArrayList<>();
    private DataAccessHelper db = new DataAccessHelper(this);
    private RecyclerView recycleViewListaDePratos;

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

        PratosAdapter adapter = new PratosAdapter(pratos);
        recycleViewListaDePratos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}