package br.ucs.androidlanches.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DetalhesDoPedidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_do_pedido);
        setTitle("Detalhes do pedido");
    }
}