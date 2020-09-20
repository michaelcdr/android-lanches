package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Pedido;

public class EscolherTipoProduto extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_tipo_produto);
        setTitle("Escolha o tipo de produto desejado");

        Intent intent = getIntent();
        Bundle bundleMesa = intent.getBundleExtra("stringBundle");
        String stringNoBundle = bundleMesa.getString("umaString");
        Integer mesaId = intent.getIntExtra("mesaId",0);

        Log.i("escolheu mesa", "mesaId: " +  mesaId.toString());

        findViewById(R.id.btn_bebidas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListaDeBebidas.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_lanches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListaDePratosActivity.class);
                startActivity(intent);
            }
        });
    }
}