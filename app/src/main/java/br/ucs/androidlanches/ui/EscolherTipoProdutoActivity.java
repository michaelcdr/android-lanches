package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.ucs.androidlanches.models.Mesa;

public class EscolherTipoProdutoActivity extends AppCompatActivity
{
    private Mesa mesa;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_tipo_produto);
        setTitle("Escolha o tipo de produto desejado");

        Intent dadosActivityAnterior = getIntent();
        mesa = (Mesa)dadosActivityAnterior.getSerializableExtra("mesaParaSelecaoTipo");

        findViewById(R.id.btn_bebidas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentListaBebidas = new Intent(getBaseContext(), ListaDeBebidasActivity.class);
                intentListaBebidas.putExtra("mesaAuxTeste2",mesa);
                startActivity(intentListaBebidas);
            }
        });

        findViewById(R.id.btn_lanches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentListaPratos = new Intent(getBaseContext(), ListaDePratosActivity.class);
                intentListaPratos.putExtra("mesaAuxTeste2",mesa);
                startActivity(intentListaPratos);
            }
        });
    }
}