package com.dmitri.mynote2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterListNote extends RecyclerView.Adapter<MyAdapterListNote.ViewHolder> {

    private String[] dataSource;

    public MyAdapterListNote(String[] dataSource) {
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public MyAdapterListNote.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterListNote.ViewHolder viewHolder, int i) {
        viewHolder.getTextView().setText(dataSource[i]);
    }

    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private LinearLayout itemLayout;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.element_recycler);
            textView = itemView.findViewById(R.id.text_view);
            date = itemView.findViewById(R.id.date_item);
            itemLayout.setOnClickListener(v -> {

            });
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
