package com.dmitri.mynote2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dmitri.mynote2.Note;
import com.dmitri.mynote2.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyAdapterListNote extends RecyclerView.Adapter<MyAdapterListNote.ViewHolder> {

    private Note[] notes;
    private MyClickListener myClickListener;

    public MyAdapterListNote(Note[] notes) {
        this.notes = notes;
    }

    public void setOnClickListener(MyClickListener itemClickListener) {
        myClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyAdapterListNote.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterListNote.ViewHolder viewHolder, int i) {
        viewHolder.getTitleTextView().setText(notes[i].getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
        viewHolder.getDateTextView().setText(formatter.format(notes[i].getDate().getTime()));
    }

    @Override
    public int getItemCount() {
        return notes.length;
    }

    public interface MyClickListener {
        void onItemClick(int i, Note note);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView titleTextView;
        private LinearLayout itemLayout;
        private TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            itemLayout = itemView.findViewById(R.id.element_recycler);
            titleTextView = itemView.findViewById(R.id.text_view);
            dateTextView = itemView.findViewById(R.id.date_item);
            itemLayout.setOnClickListener(v -> {
                int i = getAdapterPosition();
                myClickListener.onItemClick(i, notes[i]);
            });
        }

        public LinearLayout getItemLayout() {
            return itemLayout;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }
    }
}
