package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.data.DAO.MesasDAO;
import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.recycleview.adapter.listeners.IOnItemClickMesaListener;
import br.ucs.androidlanches.recycleview.adapter.MesaAdapter;

public class EscolherMesaActivity extends AppCompatActivity
{
    private List<Mesa> mesas = new ArrayList<>();
    private DataAccessHelper db = new DataAccessHelper(this);
    private MesasDAO _mesasDAO;
    private RecyclerView recyclerViewMesas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_mesa);
        setTitle("Escolha uma mesa");
        _mesasDAO = new MesasDAO(this);
        carregarMesas();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        carregarMesas();
    }

    private void carregarMesas()
    {
        recyclerViewMesas = findViewById(R.id.recycleViewMesas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMesas.setLayoutManager(layoutManager);

        mesas = _mesasDAO.obterTodasMesasDesocupadas();
        configurarAdapter(mesas,recyclerViewMesas);
    }

    private void configurarAdapter(List<Mesa> mesas, RecyclerView recyclerView)
    {
        MesaAdapter adapter = new MesaAdapter(this, mesas);
        recyclerViewMesas.setAdapter(adapter);

        adapter.setOnItemClickListener(new IOnItemClickMesaListener() {
            @Override
            public void onItemClick(Mesa mesaObtida) {
                //Toast.makeText(getApplicationContext(), mesaObtida.obterNumeroParaView(), Toast.LENGTH_SHORT).show();
                Intent abrirSelecaoPedidoComMesaSelecionada = new Intent(EscolherMesaActivity.this, EscolherTipoProdutoActivity.class);
                abrirSelecaoPedidoComMesaSelecionada.putExtra("mesaId", mesaObtida.getMesaId());
                startActivityForResult(abrirSelecaoPedidoComMesaSelecionada,1);
            }
        });
        adapter.notifyDataSetChanged();
    }
}