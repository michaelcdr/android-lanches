package br.ucs.androidlanches.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import br.ucs.androidlanches.models.Pedido;
import android.util.Log;
import android.widget.Toast;
import br.ucs.androidlanches.data.DAO.*;
import br.ucs.androidlanches.rest.RetrofitApiClient;
import br.ucs.androidlanches.ui.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumoPedidoActivity extends AppCompatActivity
{
    private Pedido pedido;
    private ProdutosDAO _produtosDAO;
    private PedidosDAO _pedidosDAO;
    private String ERRO_PAGAMENTO = "Não foi possivel pagar o pedido";
    private String TAGLOG = "LOG_ANDROID_LANCHES";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_pedido);
        setTitle("Resumo do pedido");

        Intent dadosActivityAnterior = getIntent();
        Long numeroPedido = dadosActivityAnterior.getLongExtra("numeroPedido",0);

        _pedidosDAO = new PedidosDAO(this);
        _produtosDAO = new ProdutosDAO(this);

        pedido = obterPedido(numeroPedido);

        findViewById(R.id.btnPagarPedidoComGorjeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Void> callPagarPedidoComGorjeta = RetrofitApiClient.getPedidoService().pagarComGorjeta(numeroPedido);

                callPagarPedidoComGorjeta.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            finish();
                            Toast.makeText(ResumoPedidoActivity.this, "Pedido pago com sucesso.", Toast.LENGTH_LONG).show();
                        } else {
                            Log.e(TAGLOG, ERRO_PAGAMENTO);
                            Toast.makeText(ResumoPedidoActivity.this, ERRO_PAGAMENTO+ ", ERRO : " +response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("LOG_ANDROID_LANCHES","não foi possivel obter o pedido na api. " + t.getMessage());
                        _pedidosDAO.pagarComGorjeta(numeroPedido);
                        finish();
                    }
                });
            }
        });

        findViewById(R.id.btnPagarPedidoSemGorjeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _pedidosDAO.pagarPedido(pedido);

                Call<Void> callPagarPedido = RetrofitApiClient.getPedidoService().pagar(numeroPedido);

                callPagarPedido.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            finish();
                            Toast.makeText(ResumoPedidoActivity.this, "Pedido pago com sucesso.", Toast.LENGTH_LONG).show();
                        } else {
                            Log.e(TAGLOG, ERRO_PAGAMENTO);
                            Toast.makeText(ResumoPedidoActivity.this, ERRO_PAGAMENTO+ ", ERRO : " +response.message(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("LOG_ANDROID_LANCHES","não foi possivel obter o pedido na api. " + t.getMessage());
                        _pedidosDAO.pagarSemGorjeta(numeroPedido);
                        finish();
                    }
                });
            }
        });
    }

    private Pedido obterPedido(Long numero)
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
                        Toast.LENGTH_LONG
                    ).show();

                    Log.e("LOG_ANDROID_LANCHES","Não foi possivel obter o pedido.");
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Log.e("LOG_ANDROID_LANCHES","não foi possivel obter o pedido na api. " + t.getMessage());
                Log.i("LOG_ANDROID_LANCHES","obtendo pedido localmente");
                pedido = _pedidosDAO.obterPedido(numero);
            }
        });
        return pedido;
    }
}