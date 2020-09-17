package br.ucs.androidlanches.recycleview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ucs.androidlanches.models.Pedido;
import br.ucs.androidlanches.ui.R;

class ViewHolderPedidos extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private TextView txtNumeroMesa;
    private TextView txtNumeroPedido;

    public ViewHolderPedidos(@NonNull View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);

        txtNumeroMesa =  itemView.findViewById(R.id.txtNumeroMesaCard);
        txtNumeroPedido = itemView.findViewById(R.id.txtNumeroPedidoCard);
    }

    public void setData(Pedido pedido)
    {
        txtNumeroMesa.setText("Mesa " + new Integer(pedido.getMesa().getNumero()).toString());
        txtNumeroPedido.setText("Nº " + new Integer(pedido.getNumero()).toString());
    }

    public void onClick(View view)
    {
        //Toast.makeText(view.getContext(),"Você selecionou " + pedidos.get(getLayoutPosition()).getTitulo(), Toast.LENGTH_LONG ).show();
        Toast.makeText(view.getContext(),"Você selecionou " , Toast.LENGTH_LONG ).show();
    }
}

public class PedidosAdapter extends RecyclerView.Adapter<ViewHolderPedidos>
{
    private List<Pedido> pedidos;

    public PedidosAdapter(List<Pedido> pedidos)
    {
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public ViewHolderPedidos onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.item_pedido_card, viewGroup,false);

        return new ViewHolderPedidos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPedidos viewHolder, int position)
    {
        viewHolder.setData(pedidos.get(position));
    }

    @Override
    public int getItemCount()
    {
        return pedidos.size();
    }
}
