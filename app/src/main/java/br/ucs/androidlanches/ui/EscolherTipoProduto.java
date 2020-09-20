package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.models.Pedido;

public class EscolherTipoProduto extends AppCompatActivity
{
    private Mesa mesa;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_tipo_produto);
        setTitle("Escolha o tipo de produto desejado");

        Intent dadosActivityAnterior = getIntent();
        mesa = (Mesa)dadosActivityAnterior.getSerializableExtra("mesaAux");

        findViewById(R.id.btn_bebidas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentListaBebidas = new Intent(getBaseContext(), ListaDeBebidas.class);
          //      intentListaBebidas.putExtra("mesa",mesa);
                startActivity(intentListaBebidas);
            }
        });

        findViewById(R.id.btn_lanches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentListaPratos = new Intent(getBaseContext(), ListaDePratosActivity.class);
                //intentListaPratos.putExtra("mesa",mesa);
                startActivity(intentListaPratos);
            }
        });
    }
}