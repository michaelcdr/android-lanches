package br.ucs.androidlanches.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import br.ucs.androidlanches.ui.R;

public class EscolherTipoProdutoActivity extends AppCompatActivity
{
    private int mesaId;
    private Long numeroPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_tipo_produto);
        setTitle("Escolha o tipo de produto desejado");

        Intent dadosActivityAnterior = getIntent();
        mesaId = dadosActivityAnterior.getIntExtra("mesaId",0);
        numeroPedido = dadosActivityAnterior.getLongExtra("numeroPedido",0);
        findViewById(R.id.btn_bebidas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent abrirSelecaoBebidas = new Intent(getBaseContext(), ListaDeBebidasActivity.class);
                abrirSelecaoBebidas.putExtra("mesaId", mesaId);
                abrirSelecaoBebidas.putExtra("numeroPedido", numeroPedido);
                startActivityForResult(abrirSelecaoBebidas, 1);
            }
        });

        findViewById(R.id.btn_lanches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent abrirSelecaoPratos = new Intent(getBaseContext(), ListaDePratosActivity.class);
                abrirSelecaoPratos.putExtra("mesaId",mesaId);
                abrirSelecaoPratos.putExtra("numeroPedido", numeroPedido);
                startActivityForResult(abrirSelecaoPratos,1);
            }
        });
    }
}