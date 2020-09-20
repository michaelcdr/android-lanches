package br.ucs.androidlanches.recycleview.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import br.ucs.androidlanches.models.Mesa;
import br.ucs.androidlanches.ui.EscolherTipoProduto;
import br.ucs.androidlanches.ui.R;

class ViewHolderMesas extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView txtNumeroMesa;
    private Mesa mesaAtual;

    public ViewHolderMesas(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        txtNumeroMesa = itemView.findViewById(R.id.txtNumeroMesaCard);
    }

    public void setData(Mesa mesa) {
        this.mesaAtual = mesa;
        txtNumeroMesa.setText("Mesa " + new Integer(mesa.getNumero()).toString());
    }

    public void onClick(View view)
    {
        Intent intent = new Intent(view.getContext(), EscolherTipoProduto.class);

        Bundle b = new Bundle();
        b.putString("umaString","aqaa");
        intent.putExtra("stringBundle",b);
        intent.putExtra("mensagem","lalalalalalal");
        intent.putExtra("mesaId",mesaAtual.getMesaId());
        view.getContext().startActivity(intent);
    }
}

public class MesasAdapter extends RecyclerView.Adapter<ViewHolderMesas> {
    private List<Mesa> mesas;

    public MesasAdapter(List<Mesa> mesas) {
        this.mesas = mesas;
    }

    @NonNull
    @Override
    public ViewHolderMesas onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_mesa_card, viewGroup, false);

        return new ViewHolderMesas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMesas viewHolder, int position) {
        viewHolder.setData(mesas.get(position));
    }

    @Override
    public int getItemCount() {
        return mesas.size();
    }
}
