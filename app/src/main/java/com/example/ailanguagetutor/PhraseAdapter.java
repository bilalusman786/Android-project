package com.example.ailanguagetutor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.PhraseViewHolder> {

    private List<Phrase> phraseList;
    private OnPhraseClickListener listener;

    public interface OnPhraseClickListener {
        void onPhraseClick(Phrase phrase);
    }

    public PhraseAdapter(List<Phrase> phraseList, OnPhraseClickListener listener) {
        this.phraseList = phraseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhraseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phrase, parent, false);
        return new PhraseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhraseViewHolder holder, int position) {
        Phrase phrase = phraseList.get(position);
        holder.bind(phrase, listener);
    }

    @Override
    public int getItemCount() {
        return phraseList.size();
    }

    static class PhraseViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHanzi, tvPinyin, tvEnglish;

        public PhraseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHanzi = itemView.findViewById(R.id.tvHanzi);
            tvPinyin = itemView.findViewById(R.id.tvPinyin);
            tvEnglish = itemView.findViewById(R.id.tvEnglish);
        }

        public void bind(final Phrase phrase, final OnPhraseClickListener listener) {
            tvHanzi.setText(phrase.getHanzi());
            tvPinyin.setText(phrase.getPinyin());
            tvEnglish.setText(phrase.getEnglish());
            itemView.setOnClickListener(v -> listener.onPhraseClick(phrase));
        }
    }
}