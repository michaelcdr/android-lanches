package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.recycleview.adapter.BebidasAdapter;

public class ListaDeBebidas extends AppCompatActivity
{
    private List<Bebida> bebidas = new ArrayList<>();
    private DataAccessHelper db = new DataAccessHelper(this);
    private RecyclerView recycleViewListaDeBebidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_bebidas);
        setTitle("Lista de bebidas");
        obterBebidas();
    }

    private void obterBebidas()
    {
        recycleViewListaDeBebidas = findViewById(R.id.recycleViewBebidas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleViewListaDeBebidas.setLayoutManager(layoutManager);

        bebidas = db.obterTodasBebidas();

        BebidasAdapter adapter = new BebidasAdapter(bebidas);
        recycleViewListaDeBebidas.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}