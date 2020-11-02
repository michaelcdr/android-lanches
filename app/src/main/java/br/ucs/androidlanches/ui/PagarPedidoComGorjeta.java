package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PagarPedidoComGorjeta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_pedido_com_gorjeta);
        setTitle("Pagar pedido com gorjeta");
    }
}