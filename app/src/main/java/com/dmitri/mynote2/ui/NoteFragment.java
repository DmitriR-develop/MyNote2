package com.dmitri.mynote2.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitri.mynote2.Note;
import com.dmitri.mynote2.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NoteFragment extends Fragment {

    public static final String CURRENT_NOTE = "currentNote";
    private Note note;
    private boolean isLandscape;

    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(CURRENT_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        TextInputEditText titleText = view.findViewById(R.id.note_title);
        TextInputEditText contentText = view.findViewById(R.id.note_content);
        TextView dateText = view.findViewById(R.id.note_date);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
        dateText.setText(String.format("%s: %s", "Дата создания", formatter.format(note.getDate().getTime())));
        titleText.setText(note.getTitle());
        contentText.setText(note.getContent());
        setHasOptionsMenu(true);
        return view;
    }
}