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
import br.ucs.androidlanches.models.Prato;
import br.ucs.androidlanches.ui.adapter.listeners.IOnItemClickPratoListener;
import br.ucs.androidlanches.ui.R;

public class PratoAdapter extends RecyclerView.Adapter<PratoAdapter.PratoViewHolder>
{
    private List<Prato> pratos;
    private final Context context;
    private IOnItemClickPratoListener onItemClickListener;

    public PratoAdapter(Context context, List<Prato> pratos)
    {
        this.context = context;
        this.pratos = pratos;
    }

    public void setOnItemClickListener(IOnItemClickPratoListener onItemClickPratoListener)
    {
        this.onItemClickListener = onItemClickPratoListener;
    }

    @NonNull
    @Override
    public PratoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.item_prato_card, viewGroup, false);

        return new PratoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PratoViewHolder viewHolder, int position)
    {
        Prato prato = pratos.get(position);
        viewHolder.vincularDados(prato);
    }

    @Override
    public int getItemCount() {
        return pratos.size();
    }

    class PratoViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView txtNomePrato;
        private final TextView txtDescricaoPrato;
        private final TextView txtPrecoPrato;
        private final ImageView imgPratoCard;
        private Prato prato;

        public PratoViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txtNomePrato = itemView.findViewById(R.id.txtNomePratoCard);
            txtDescricaoPrato = itemView.findViewById(R.id.txtDescricaoPratoCard);
            txtPrecoPrato = itemView.findViewById(R.id.txtPrecoPratoCard);
            imgPratoCard = itemView.findViewById(R.id.imgPratoCard);

            itemView.findViewById(R.id.btnEscolherPrato).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(prato);
                }
            });
        }

        public void vincularDados(Prato prato)
        {
            this.prato = prato;
            this.preencherViews(prato);
        }

        private void preencherViews(Prato prato)
        {
            txtNomePrato.setText(prato.getNome());
            txtDescricaoPrato.setText(prato.getDescricao());

            NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
            String precoFormatado =  nf.format(prato.getPreco());
            txtPrecoPrato.setText("R$ " + precoFormatado);

            String uri = "@drawable/" + prato.getFoto();
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());

            Drawable res = context.getResources().getDrawable(imageResource);
            imgPratoCard.setImageDrawable(res);
        }
    }
}