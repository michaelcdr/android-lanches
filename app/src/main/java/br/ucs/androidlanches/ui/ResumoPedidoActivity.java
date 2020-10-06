package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Pedido;

public class ResumoPedidoActivity extends AppCompatActivity
{
    private Pedido pedido;
    private DataAccessHelper db = new DataAccessHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_pedido);
        setTitle("Resumo do pedido");

        Intent dadosActivityAnterior = getIntent();
        int numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);
        pedido = db.obterPedido(numeroPedido);

        TextView textView = findViewById(R.id.txtDetalhesItens);
        textView.setText(pedido.detalharPedido());

        TextView textViewMesa = findViewById(R.id.txtNomeMesa);
        textViewMesa.setText("Mesa " + pedido.getMesa().getNumero());

        findViewById(R.id.btnPagarPedidoComGorjeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(DetalhesDoPedidoActivity.this, "adicionar item no pedido " , Toast.LENGTH_SHORT).show();
                Intent adicionarNovoProduto = new Intent(ResumoPedidoActivity.this, PagarPedidoComGorjeta.class);
                adicionarNovoProduto.putExtra("numeroPedido", pedido.getNumero());
                startActivityForResult(adicionarNovoProduto, 1);
            }
        });

        findViewById(R.id.btnPagarPedidoSemGorjeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.pagarPedido(pedido);
                Intent irParaListaDePedidos = new Intent(ResumoPedidoActivity.this, MainActivity.class);
                startActivityForResult(irParaListaDePedidos, 1);
            }
        });
    }
}