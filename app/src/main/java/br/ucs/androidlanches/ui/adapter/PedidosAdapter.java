package br.ucs.androidlanches.ui.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickBtnPagarPedidoListener;
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickBtnVerPedidoListener;
import br.ucs.androidlanches.ui.R;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolderPedidos>
{
    private List<Pedido> pedidos;
    private final Context context;
    private IOnItemClickBtnVerPedidoListener onItemClickVerPedidoListener;
    private IOnItemClickBtnPagarPedidoListener onItemClickPagarPedidoListener;

    public PedidosAdapter(Context context, List<Pedido> pedidos)
    {
        this.context = context;
        this.pedidos = pedidos;
    }

    public void setOnItemClickVerPedidoListener(IOnItemClickBtnVerPedidoListener onItemClickVerPedidoListener)
    {
        this.onItemClickVerPedidoListener = onItemClickVerPedidoListener;
    }

    public void setOnItemClickBtnPagarListener(IOnItemClickBtnPagarPedidoListener onItemClickListener)
    {
        this.onItemClickPagarPedidoListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolderPedidos onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.item_pedido_card, viewGroup,false);

        return new ViewHolderPedidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPedidos viewHolder, int position)
    {
        Pedido pedido = pedidos.get(position);
        viewHolder.vincularDados(pedido);
    }

    @Override
    public int getItemCount()
    {
        return pedidos.size();
    }

    class ViewHolderPedidos extends RecyclerView.ViewHolder
    {
        private TextView txtNumeroMesa;
        private TextView txtNumeroPedido;
        private TextView txtValor;
        private Pedido pedido;

        public ViewHolderPedidos(@NonNull View itemView)
        {
            super(itemView);
            txtNumeroMesa =  itemView.findViewById(R.id.txtNumeroMesaCard);
            txtNumeroPedido = itemView.findViewById(R.id.txtNumeroPedidoCard);
            txtValor = itemView.findViewById(R.id.txtPrecoPedido);
            itemView.findViewById(R.id.btnVerPedidoCard).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickVerPedidoListener.onItemClick(pedido);
                }
            });

            itemView.findViewById(R.id.btnPagarPedidoCard).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickPagarPedidoListener.onItemClick(pedido);
                }
            });
        }

        public void vincularDados(Pedido pedido)
        {
            this.pedido = pedido;
            txtNumeroMesa.setText("Mesa " + new Integer(pedido.getMesa().getNumero()).toString());
            txtNumeroPedido.setText("Nº " + new Long(pedido.getNumero()).toString());
            txtValor.setText(pedido.obterTotalFormatado());
        }
    }
}