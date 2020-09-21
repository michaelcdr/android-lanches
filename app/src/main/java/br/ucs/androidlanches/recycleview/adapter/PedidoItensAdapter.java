package br.ucs.androidlanches.recycleview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.models.PedidoItem;
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.ui.R;

public class PedidoItensAdapter extends RecyclerView.Adapter<PedidoItensAdapter.PedidoItensViewHolder>
{
    private Pedido pedido;
    private final Context context;
    private IOnItemClickBtnDecrementarQtdItemPedido onItemClickBtnDecrementarQtdItemPedido;
    private IOnItemClickBtnIncrementarQtdItemPedido onItemClickBtnIncrementarQtdItemPedido;

    public PedidoItensAdapter(Context context,  Pedido pedido)
    {
        this.context = context;
        this.pedido = pedido;
    }

    public void setOnItemClickBtnDecrementarQtdItemPedido(IOnItemClickBtnDecrementarQtdItemPedido onItemClickBtnDecrementarQtdItemPedido)
    {
        this.onItemClickBtnDecrementarQtdItemPedido = onItemClickBtnDecrementarQtdItemPedido;
    }

    public void setOnItemClickBtnIncrementarQtdItemPedido(IOnItemClickBtnIncrementarQtdItemPedido onItemClickBtnIncrementarQtdItemPedido)
    {
        this.onItemClickBtnIncrementarQtdItemPedido = onItemClickBtnIncrementarQtdItemPedido;
    }

    @NonNull
    @Override
    public PedidoItensAdapter.PedidoItensViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_produto_detalhes_pedido_card, viewGroup, false);

        return new PedidoItensAdapter.PedidoItensViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoItensAdapter.PedidoItensViewHolder viewHolder, int position)
    {
        PedidoItem pedidoItem = pedido.getItens().get(position);
        viewHolder.vincularDados(pedidoItem);
    }

    @Override
    public int getItemCount() {
        return pedido.getItens().size();
    }

    class PedidoItensViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView txtNomeProduto;
        private PedidoItem pedidoItem;

        public PedidoItensViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txtNomeProduto = itemView.findViewById(R.id.txtNomeProdutoItemPedidoCard);
            itemView.findViewById(R.id.btnIncrementarQtd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickBtnIncrementarQtdItemPedido.onItemClick(pedidoItem);
                }
            });
            itemView.findViewById(R.id.btnDecrementarQtd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickBtnDecrementarQtdItemPedido.onItemClick(pedidoItem);
                }
            });
        }

        public void vincularDados(PedidoItem pedidoItem)
        {
            this.pedidoItem = pedidoItem;
            this.preencherViews(pedidoItem);
        }

        private void preencherViews(PedidoItem pedidoItem)
        {
            txtNomeProduto.setText(pedidoItem.getNomeProduto());
        }
    }
}
