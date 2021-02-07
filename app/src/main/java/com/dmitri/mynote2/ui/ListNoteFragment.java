package com.dmitri.mynote2.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dmitri.mynote2.MainActivity;
import com.dmitri.mynote2.Navigation;
import com.dmitri.mynote2.Note;
import com.dmitri.mynote2.NoteSourceImpl;
import com.dmitri.mynote2.NoteSourceInterface;
import com.dmitri.mynote2.R;
import com.dmitri.mynote2.observe.Observer;
import com.dmitri.mynote2.observe.Publisher;

import java.util.Calendar;
import java.util.Objects;

import static com.dmitri.mynote2.ui.NoteFragment.CURRENT_DATA;
import static com.dmitri.mynote2.ui.NoteFragment.CURRENT_NOTE;

public class ListNoteFragment extends Fragment {

    private boolean isLandscape;
    private Note currentNote;
    private NoteSourceImpl data;
    private MyAdapterListNote adapter;
    private RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToLastPosition;

    public static ListNoteFragment newInstance() {
        return new ListNoteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (data == null) {
            data = new NoteSourceImpl(getResources()).init();
        }
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
        if (moveToLastPosition) {
            recyclerView.smoothScrollToPosition(data.size() - 1);
            moveToLastPosition = false;
        }
        adapter = new MyAdapterListNote(data, this);
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
        outState.putParcelable(CURRENT_DATA, data);
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
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();
        if (item.getItemId() == R.id.delete_menu) {
            data.deleteNote(position);
            adapter.notifyItemRemoved(position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem addNote = menu.findItem(R.id.add);
        addNote.setOnMenuItemClickListener(item -> {
            navigation.addFragment(NoteFragment.newInstance(), true);
            publisher.subscribe(new Observer() {
                @Override
                public void updateNote(Note note) {
                    data.addNote(note);
                    adapter.notifyItemInserted(data.size() - 1);
                    moveToLastPosition = true;
                }
            });
            return true;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}