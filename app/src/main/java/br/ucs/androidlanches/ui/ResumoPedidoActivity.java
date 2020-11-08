package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.ucs.androidlanches.data.DAO.PedidosDAO;
import br.ucs.androidlanches.data.DAO.ProdutosDAO;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumoPedidoActivity extends AppCompatActivity
{
    private Pedido pedido;
    private ProdutosDAO _produtosDAO;
    private PedidosDAO _pedidosDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_pedido);
        setTitle("Resumo do pedido");

        Intent dadosActivityAnterior = getIntent();
        int numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);

        _pedidosDAO = new PedidosDAO(this);
        _produtosDAO = new ProdutosDAO(this);

        pedido = obterPedido(numeroPedido);

        findViewById(R.id.btnPagarPedidoComGorjeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adicionarNovoProduto = new Intent(ResumoPedidoActivity.this, PagarPedidoComGorjeta.class);
                adicionarNovoProduto.putExtra("numeroPedido", pedido.getNumero());
                startActivityForResult(adicionarNovoProduto, 1);
            }
        });

        findViewById(R.id.btnPagarPedidoSemGorjeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _pedidosDAO.pagarPedido(pedido);
                Intent irParaListaDePedidos = new Intent(ResumoPedidoActivity.this, MainActivity.class);
                startActivityForResult(irParaListaDePedidos, 1);
            }
        });
    }

    private Pedido obterPedido(int numero)
    {
        Call<Pedido> pedidoCall = RetrofitApiClient.getPedidoService().obter(numero);
        pedidoCall.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful()){
                    pedido = response.body();

                    TextView textView = findViewById(R.id.txtDetalhesItens);
                    textView.setText(pedido.detalharPedido());
                    TextView textViewMesa = findViewById(R.id.txtNomeMesa);
                    textViewMesa.setText("Mesa " + pedido.getMesa().getNumero());
                } else {
                    Toast.makeText(
                            ResumoPedidoActivity.this,
                            "Não foi possível obter os dados devido a um erro na API de destino, tente novamente mais tarde. " ,
                            Toast.LENGTH_LONG).show();

                    Log.e("LOG_ANDROID_LANCHES","Não foi possivel obter o pedido.");
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","não foi possivel obter o pedido na api. " + t.getMessage());
                pedido = _pedidosDAO.obterPedido(numero);
            }
        });
        return pedido;
    }
}