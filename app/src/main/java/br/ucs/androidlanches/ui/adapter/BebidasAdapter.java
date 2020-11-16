package br.ucs.androidlanches.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickBebidaListener;
import br.ucs.androidlanches.ui.R;

public class BebidasAdapter extends RecyclerView.Adapter<BebidasAdapter.ViewHolderBebidas>
{
    private List<Bebida> bebidas;
    private final Context context;
    private IOnItemClickBebidaListener onItemClickListener;

    public BebidasAdapter(Context context,List<Bebida> bebidas)
    {
        this.context =context;
        this.bebidas = bebidas;
    }

    public void setOnItemClickListener(IOnItemClickBebidaListener onItemClickBebidaListener)
    {
        this.onItemClickListener = onItemClickBebidaListener;
    }

    @NonNull
    @Override
    public ViewHolderBebidas onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.item_bebida_card, viewGroup, false);

        return new ViewHolderBebidas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBebidas viewHolder, int position)
    {
        Bebida bebida = bebidas.get(position);
        viewHolder.vincularDados(bebida);
    }

    @Override
    public int getItemCount() {
        return bebidas.size();
    }

    class ViewHolderBebidas extends RecyclerView.ViewHolder
    {
        private final TextView txtNomeBebida;
        private final TextView txtDescricaoBebida;
        private final TextView txtPrecoBebida;
        private final ImageView imgProdutoCard;
        private final TextView txtEmbalagemCard;

        private Bebida bebidaAtual;

        public ViewHolderBebidas(@NonNull View itemView)
        {
            super(itemView);

            txtNomeBebida = itemView.findViewById(R.id.txtNomeBebidaCard);
            txtDescricaoBebida = itemView.findViewById(R.id.txtDescricaoBebidaCard);
            txtPrecoBebida = itemView.findViewById(R.id.txtPrecoBebidaCard);
            txtEmbalagemCard =itemView.findViewById(R.id.txtEmbalagemBebidaCard);
            imgProdutoCard = itemView.findViewById(R.id.imgProdutoCard);

            itemView.findViewById(R.id.btnEscolherBebida).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(bebidaAtual);
                }
            });
        }

        public void vincularDados(Bebida bebida)
        {
            this.bebidaAtual = bebida;
            this.preencherViews(bebida);
        }

        private void preencherViews(Bebida bebida)
        {
            txtNomeBebida.setText(bebida.getNome());
            txtDescricaoBebida.setText(bebida.getDescricao());
            txtEmbalagemCard.setText(bebida.getEmbalagem());

            NumberFormat nf = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
            String precoFormatado =  nf.format(bebida.getPreco());
            txtPrecoBebida.setText("R$ " + precoFormatado);

            String uri = "@drawable/" + bebida.getFoto();

            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

            Drawable res = context.getResources().getDrawable(imageResource);
            imgProdutoCard.setImageDrawable(res);
        }
    }
}