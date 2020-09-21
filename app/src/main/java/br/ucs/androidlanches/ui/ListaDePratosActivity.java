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
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.recycleview.adapter.IOnItemClickPratoListener;
import br.ucs.androidlanches.recycleview.adapter.PratoAdapter;

public class ListaDePratosActivity extends AppCompatActivity
{
    private List<Prato> pratos = new ArrayList<>();
    private DataAccessHelper db = new DataAccessHelper(this);
    private RecyclerView recycleViewListaDePratos;
    private int mesaId;

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


        //Intent dadosActivityAnterior = getIntent();
        //mesaId = dadosActivityAnterior.getIntExtra("mesaId",0);
        //Toast.makeText(this, "Mesa selecionada " + mesaId, Toast.LENGTH_SHORT).show();
    }

    private void obterPratos()
    {
        recycleViewListaDePratos = findViewById(R.id.recycleViewPratos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleViewListaDePratos.setLayoutManager(layoutManager);

        pratos = db.obterTodosPratos();

        PratoAdapter adapter = new PratoAdapter(this, pratos);
        recycleViewListaDePratos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new IOnItemClickPratoListener() {
            @Override
            public void onItemClick(Prato prato) {
                Toast.makeText(ListaDePratosActivity.this, "clico botom", Toast.LENGTH_SHORT).show();
            }
        });
    }
}