package dev.djoxer.cartomancer.fragments.sorting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import dev.djoxer.cartomancer.MainActivity;
import dev.djoxer.cartomancer.R;
import dev.djoxer.cartomancer.util.tarot.Card;

import java.util.ArrayList;
import java.util.List;

public class SortingRecViewAdapter extends RecyclerView.Adapter<SortingRecViewAdapter.ViewHolder> {

    private Context context;
    private List<Card> list;
    private SortingFragment parent;

    public int card1;
    public int card2;

    public SortingRecViewAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        card1 = -1;
        card2 = -1;
    }

    public void setList(List<Card> list) {
        this.list = list;
    }

    public void setParent(SortingFragment fragment) {
        this.parent = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Card card = list.get(position);

        holder.imageCardIllustration.setImageResource(((MainActivity)context).getImageSrc(card));
        holder.cardBase.setCardBackgroundColor(context.getColor(R.color.white));

        if (parent.isRunning()) {
            if (card.value == position + 1) {
                holder.cardBase.setCardBackgroundColor(context.getColor(R.color.white));
                holder.imageCardIllustration.setBackgroundColor(context.getColor(R.color.green));
            } else {
                holder.cardBase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
                        holder.cardBase.setCardBackgroundColor(context.getColor(R.color.gray));

                        if (card1 == -1) {
                            card1 = holder.getAdapterPosition();
                        } else {
                            card2 = holder.getAdapterPosition();
                            parent.update(card1, card2);
                            parent.adapter.notifyDataSetChanged();
                            parent.finish();
                            card1 = card2 = -1;
                        }
                    }
                });
            }
        } else {
            if (!parent.isSorted()) {
                holder.cardBase.setBackgroundResource(R.drawable.cardback);
                holder.imageCardIllustration.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageCardIllustration;
        private CardView cardBase;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCardIllustration = itemView.findViewById(R.id.image_card_illustration);
            cardBase = itemView.findViewById(R.id.card_base);
        }
    }
}
