package com.dmitri.mynote2.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dmitri.mynote2.MainActivity;
import com.dmitri.mynote2.Navigation;
import com.dmitri.mynote2.Note;
import com.dmitri.mynote2.NoteSourceFirebaseImpl;
import com.dmitri.mynote2.NoteSourceInterface;
import com.dmitri.mynote2.NoteSourceResponse;
import com.dmitri.mynote2.R;
import com.dmitri.mynote2.observe.Observer;
import com.dmitri.mynote2.observe.Publisher;

import java.util.Objects;

import static com.dmitri.mynote2.ui.NoteFragment.CURRENT_DATA;
import static com.dmitri.mynote2.ui.NoteFragment.CURRENT_NOTE;

public class ListNoteFragment extends Fragment {

    private boolean isLandscape;
    private Note currentNote;
    private NoteSourceFirebaseImpl data;
    private MyAdapterListNote adapter;
    private RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToFirstPosition;

    public static ListNoteFragment newInstance() {
        return new ListNoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler);
        initRecyclerView(recyclerView, data);
        setHasOptionsMenu(true);
        data = new NoteSourceFirebaseImpl().init(new NoteSourceResponse() {
            @Override
            public void initialized(NoteSourceInterface noteSourceData) {
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setDataSource(data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    private void initRecyclerView(RecyclerView recyclerView, NoteSourceInterface data) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        if (moveToFirstPosition && data.size() > 0) {
            recyclerView.scrollToPosition(0);
            moveToFirstPosition = false;
        }
        adapter = new MyAdapterListNote(this);
        NoteSourceInterface finalData = data;
        adapter.setOnClickListener((position, note) -> {
            navigation.addFragment(NoteFragment.newInstance(finalData.getNote(position)), true);
            publisher.subscribe(new Observer() {
                @Override
                public void updateNote(Note note) {
                    finalData.changeNote(position, note);
                    adapter.notifyItemChanged(position);
                }
            });
        });
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_background)));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        outState.putParcelable(CURRENT_DATA, (Parcelable) data);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelable(CURRENT_DATA);
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentNote = data.getNote(0);
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return  onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    private boolean onItemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.add:
                navigation.addFragment(NoteFragment.newInstance(), true);
                publisher.subscribe(new Observer() {
                    @Override
                    public void updateNote(Note note) {
                        data.addNote(note);
                        adapter.notifyItemInserted(data.size() - 1);
                        moveToFirstPosition = true;
                    }
                });
                return true;
            case R.id.delete_menu:
                int deletePosition = adapter.getMenuPosition();
                data.deleteNote(deletePosition);
                return true;
            case R.id.clear:
                data.clearNotes();
                adapter.notifyDataSetChanged();
                return true;
        }
        return false;
    }
}