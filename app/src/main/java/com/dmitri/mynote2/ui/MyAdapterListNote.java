package com.dmitri.mynote2.ui;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dmitri.mynote2.Note;
import com.dmitri.mynote2.NoteSourceInterface;
import com.dmitri.mynote2.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyAdapterListNote extends RecyclerView.Adapter<MyAdapterListNote.ViewHolder> {

    private static final int X_C = 550;
    private static final int Y_C = 10;
    private NoteSourceInterface dataSource;
    private MyClickListener myClickListener;
    private final Fragment fragment;
    private NoteSourceInterface noteSource;
    private AdapterView.OnItemClickListener itemClickListener;
    private int menuPosition;

    public MyAdapterListNote(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setDataSource(NoteSourceInterface noteSource) {
        this.noteSource = noteSource;
        notifyDataSetChanged();
    }

    public void setOnClickListener(MyClickListener itemClickListener) {
        myClickListener = itemClickListener;
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    @NonNull
    @Override
    public MyAdapterListNote.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterListNote.ViewHolder viewHolder, int i) {
        viewHolder.getTitleTextView().setText(dataSource.getNote(i).getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
        viewHolder.getDateTextView().setText(dataSource.getNote(i).getDate());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
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
                myClickListener.onItemClick(i, dataSource.getNote(i));
            });
            itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onLongClick(View v) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(X_C, Y_C);
                    return true;
                }
            });
        }

        public void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(itemView);
            }
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
