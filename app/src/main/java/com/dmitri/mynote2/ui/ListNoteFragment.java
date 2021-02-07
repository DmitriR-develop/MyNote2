package com.dmitri.mynote2.ui;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dmitri.mynote2.Note;
import com.dmitri.mynote2.R;

import java.util.Calendar;

import static com.dmitri.mynote2.ui.NoteFragment.CURRENT_NOTE;

public class ListNoteFragment extends Fragment {

    private boolean isLandscape;
    private Note[] notes;
    private Note currentNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNotes();
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        initRecyclerView(recyclerView, notes);
    }

    private void initNotes() {
        notes = new Note[]{
                new Note(getString(R.string.first_title), getString(R.string.first_content), Calendar.getInstance()),
                new Note(getString(R.string.second_title), getString(R.string.second_content), Calendar.getInstance()),
                new Note(getString(R.string.third_title), getString(R.string.third_content), Calendar.getInstance()),
                new Note(getString(R.string.fourth_title), getString(R.string.fourth_content), Calendar.getInstance()),
                new Note(getString(R.string.fifth_title), getString(R.string.fifth_content), Calendar.getInstance()),
                new Note(getString(R.string.sixth_title), getString(R.string.sixth_content), Calendar.getInstance()),
                new Note(getString(R.string.seventh_title), getString(R.string.seventh_content), Calendar.getInstance())
        };
    }

    private void initRecyclerView(RecyclerView recyclerView, Note[] notes) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        MyAdapterListNote adapter = new MyAdapterListNote(notes);
        adapter.setOnClickListener((i, note) -> {
            currentNote = note;
            showNote(currentNote);
        });
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_background));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentNote = notes[0];
        }
        if (isLandscape) {
            showLandNote(currentNote);
        }
    }

    private void showNote(Note currentNote) {
        if (isLandscape) {
            showLandNote(currentNote);
        } else {
            showPortNote(currentNote);
        }
    }

    private void showPortNote(Note currentNote) {
        NoteFragment fragment = NoteFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("list_fragment");
        fragmentTransaction.replace(R.id.list_note_fragment, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showLandNote(Note currentNote) {
        NoteFragment fragment = NoteFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.note_layout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}