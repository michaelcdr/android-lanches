package br.ucs.androidlanches.recycleview.adapter;
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
import br.ucs.androidlanches.ui.R;

class ViewHolderPratos extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private final TextView txtNomePrato;
    private final TextView txtDescricaoPrato;
    private final TextView txtPrecoPrato;
    private final ImageView imgPratoCard;

    private Prato pratoAtual;

    public ViewHolderPratos(@NonNull View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        txtNomePrato = itemView.findViewById(R.id.txtNomePratoCard);
        txtDescricaoPrato = itemView.findViewById(R.id.txtDescricaoPratoCard);
        txtPrecoPrato = itemView.findViewById(R.id.txtPrecoPratoCard);
        imgPratoCard = itemView.findViewById(R.id.imgPratoCard);
    }

    public void setData(Prato prato)
    {
        this.pratoAtual = prato;
        txtNomePrato.setText(prato.getNome());
        txtDescricaoPrato.setText(prato.getDescricao());

        NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        String precoFormatado =  nf.format(prato.getPreco());
        txtPrecoPrato.setText("R$ " + precoFormatado);
        imgPratoCard.setImageResource(R.drawable.xis_calabresa);
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

public class PratosAdapter extends RecyclerView.Adapter<ViewHolderPratos>
{
    private List<Prato> pratos;

    public PratosAdapter(List<Prato> pratos)
    {
        this.pratos = pratos;
    }

    @NonNull
    @Override
    public ViewHolderPratos onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_prato_card, viewGroup, false);

        return new ViewHolderPratos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPratos viewHolder, int position) {
        viewHolder.setData(pratos.get(position));
    }

    @Override
    public int getItemCount() {
        return pratos.size();
    }
}