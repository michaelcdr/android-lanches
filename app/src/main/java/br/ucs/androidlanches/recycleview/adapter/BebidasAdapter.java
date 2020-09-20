package br.ucs.androidlanches.recycleview.adapter;

import android.media.Image;
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
import br.ucs.androidlanches.models.Bebida;
import br.ucs.androidlanches.ui.R;

class ViewHolderBebidas extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView txtNomeBebida;
    private final TextView txtDescricaoBebida;
    private final TextView txtPrecoBebida;
    private final ImageView imgProdutoCard;
    private final TextView txtEmbalagemCard;

    private Bebida bebidaAtual;

    public ViewHolderBebidas(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        txtNomeBebida = itemView.findViewById(R.id.txtNomeBebidaCard);
        txtDescricaoBebida = itemView.findViewById(R.id.txtDescricaoBebidaCard);
        txtPrecoBebida = itemView.findViewById(R.id.txtPrecoBebidaCard);
        txtEmbalagemCard =itemView.findViewById(R.id.txtEmbalagemBebidaCard);
        imgProdutoCard = itemView.findViewById(R.id.imgProdutoCard);
    }

    public void setData(Bebida bebida) {
        this.bebidaAtual = bebida;
        txtNomeBebida.setText(bebida.getNome());
        txtDescricaoBebida.setText(bebida.getDescricao());
        txtEmbalagemCard.setText(bebida.getEmbalagem());
        NumberFormat nf = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        String precoFormatado =  nf.format(bebida.getPreco());
        txtPrecoBebida.setText("R$ " + precoFormatado);
        imgProdutoCard.setImageResource(R.drawable.cocacola_2l);
    }


    public void onClick(View view)
    {
        /*
        Intent intent = new Intent(view.getContext(), EscolherTipoProduto.class);
        Bundle b = new Bundle();
        b.putString("umaString","aqaa");
        intent.putExtra("stringBundle",b);
        intent.putExtra("mensagem","lalalalalalal");
        intent.putExtra("mesaId",mesaAtual.getMesaId());
        view.getContext().startActivity(intent);*/
    }
}

public class BebidasAdapter extends RecyclerView.Adapter<ViewHolderBebidas>
{
    private List<Bebida> bebidas;

    public BebidasAdapter(List<Bebida> bebidas)
    {
        this.bebidas = bebidas;
    }

    @NonNull
    @Override
    public ViewHolderBebidas onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.item_bebida_card, viewGroup, false);

        return new ViewHolderBebidas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBebidas viewHolder, int position) {
        viewHolder.setData(bebidas.get(position));
    }

    @Override
    public int getItemCount() {
        return bebidas.size();
    }
}