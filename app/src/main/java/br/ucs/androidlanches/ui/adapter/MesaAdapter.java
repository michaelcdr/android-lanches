package br.ucs.androidlanches.ui.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickMesaListener;
import br.ucs.androidlanches.ui.R;

public class MesaAdapter extends RecyclerView.Adapter<MesaAdapter.MesaViewHolder>
{
    private List<Mesa> mesas;
    private final Context context;
    private IOnItemClickMesaListener onItemClickListener;

    public MesaAdapter(Context context, List<Mesa> mesas)
    {
        this.context = context;
        this.mesas = mesas;
    }

    public void setOnItemClickListener(IOnItemClickMesaListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MesaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroupParent, int viewType)
    {
        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.item_mesa_card, viewGroupParent, false);

        return new MesaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MesaViewHolder viewHolder, int position)
    {
        Mesa mesa = mesas.get(position);
        viewHolder.vincularDados(mesa);
    }

    @Override
    public int getItemCount() {
        return mesas.size();
    }

    class MesaViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView txtNumeroMesa;
        private Mesa mesa;
        public MesaViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtNumeroMesa = itemView.findViewById(R.id.txtNumeroMesaCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(mesa);
                }
            });
        }

        public void vincularDados(Mesa mesa)
        {
            this.mesa = mesa;
            preencherTextViews(mesa);
        }

        private void preencherTextViews(Mesa mesa)
        {
            txtNumeroMesa.setText(mesa.obterNumeroParaView());
        }
    }
}
