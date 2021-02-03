package com.dmitri.mynote2;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListNoteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_note, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        String[] data = getResources().getStringArray(R.array.titles);
        recyclerView.setHasFixedSize(true);
        MyAdapterListNote adapter = new MyAdapterListNote(data);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_background));
        recyclerView.addItemDecoration(itemDecoration);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, String[] data) {

    }
}