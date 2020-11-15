package br.ucs.androidlanches.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import br.ucs.androidlanches.ui.R;

public class PagarPedidoComGorjeta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_pedido_com_gorjeta);
        setTitle("Pagar pedido com gorjeta");
    }
}