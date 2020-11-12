package br.ucs.androidlanches.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.ucs.androidlanches.data.DataAccessHelper;
import br.ucs.androidlanches.models.Pedido;
=======
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
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb

public class ResumoPedidoActivity extends AppCompatActivity
{
    private Pedido pedido;
<<<<<<< HEAD
    private DataAccessHelper db = new DataAccessHelper(this);
=======
    private ProdutosDAO _produtosDAO;
    private PedidosDAO _pedidosDAO;

>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumo_pedido);
        setTitle("Resumo do pedido");

        Intent dadosActivityAnterior = getIntent();
        int numeroPedido = dadosActivityAnterior.getIntExtra("numeroPedido",0);
<<<<<<< HEAD
        pedido = db.obterPedido(numeroPedido);

        TextView textView = findViewById(R.id.txtDetalhesItens);
        textView.setText(pedido.detalharPedido());

        TextView textViewMesa = findViewById(R.id.txtNomeMesa);
        textViewMesa.setText("Mesa " + pedido.getMesa().getNumero());
=======

        _pedidosDAO = new PedidosDAO(this);
        _produtosDAO = new ProdutosDAO(this);

        pedido = obterPedido(numeroPedido);
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb

        findViewById(R.id.btnPagarPedidoComGorjeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                //Toast.makeText(DetalhesDoPedidoActivity.this, "adicionar item no pedido " , Toast.LENGTH_SHORT).show();
=======
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb
                Intent adicionarNovoProduto = new Intent(ResumoPedidoActivity.this, PagarPedidoComGorjeta.class);
                adicionarNovoProduto.putExtra("numeroPedido", pedido.getNumero());
                startActivityForResult(adicionarNovoProduto, 1);
            }
        });

        findViewById(R.id.btnPagarPedidoSemGorjeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                db.pagarPedido(pedido);
=======
                _pedidosDAO.pagarPedido(pedido);
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb
                Intent irParaListaDePedidos = new Intent(ResumoPedidoActivity.this, MainActivity.class);
                startActivityForResult(irParaListaDePedidos, 1);
            }
        });
    }
<<<<<<< HEAD
=======

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
>>>>>>> efa72b5e768a053d4f05f6c0560cc3cd7be935eb
}