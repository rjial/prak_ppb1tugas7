package com.rizal.cardbiasa.adapter.recyclerview;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rizal.cardbiasa.R;
import com.rizal.cardbiasa.UpdateCardActivity;
import com.rizal.cardbiasa.model.Card;

import java.util.List;

public class CardAdapter  extends RecyclerView.Adapter<CardAdapter.ViewHolder>{

    private List<Card> cardList;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public CardAdapter(List<Card> cardList, ActivityResultLauncher<Intent> activityResultLauncher) {
        this.cardList = cardList;
        this.activityResultLauncher = activityResultLauncher;
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        holder.txtTitleCard.setText(cardList.get(position).getTitle());
        holder.txtIsiCard.setText(cardList.get(position).getIsi());
        holder.itemView.setOnClickListener(i -> {
            Intent intent = new Intent(holder.itemView.getContext(), UpdateCardActivity.class);
            intent.putExtra("ID_CARD", cardList.get(position).getId());
            activityResultLauncher.launch(intent);
        });

    }


    @Override
    public int getItemCount() {
        return this.cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitleCard,txtIsiCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitleCard = (TextView) itemView.findViewById(R.id.txtTitleCard);
            txtIsiCard = (TextView) itemView.findViewById(R.id.txtIsiCard);
        }
    }
    public List<Card> getAllCard() {
        return cardList;
    }
    public void removeItem(int position) {
        cardList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Card item, int position) {
        cardList.add(position, item);
        notifyItemInserted(position);
    }
}
