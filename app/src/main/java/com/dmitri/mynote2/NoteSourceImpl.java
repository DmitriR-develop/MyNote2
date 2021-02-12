package com.dmitri.mynote2;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class NoteSourceImpl implements NoteSourceInterface, Parcelable {

    private ArrayList<Note> notes;
    private Resources resources;
    private int counter = 0;

    public NoteSourceImpl(Resources resources) {
        this.resources = resources;
        notes = new ArrayList<>();
    }

    public NoteSourceImpl init(NoteSourceResponse noteSourceResponse) {
        String[] titles = resources.getStringArray(R.array.titles);
        String[] description = resources.getStringArray(R.array.description);
        int[] pictures = getImageArray();
        for (int i = 0; i < description.length; i++) {
            notes.add(new Note(titles[i], description[i], getDate()));
        }
        if (noteSourceResponse != null) {
            noteSourceResponse.initialized(this);
        }
        return this;
    }

    private int[] getImageArray() {
        TypedArray pictures = resources.obtainTypedArray(R.array.pictures);
        int length = pictures.length();
        int[] answer = new int[length];
        for (int i = 0; i < length; i++) {
            answer[i] = pictures.getResourceId(i, 0);
        }
        return answer;
    }

    protected NoteSourceImpl(Parcel in) {
        notes = in.createTypedArrayList(Note.CREATOR);
    }

    public static final Creator<NoteSourceImpl> CREATOR = new Creator<NoteSourceImpl>() {
        @Override
        public NoteSourceImpl createFromParcel(Parcel in) {
            return new NoteSourceImpl(in);
        }

        @Override
        public NoteSourceImpl[] newArray(int size) {
            return new NoteSourceImpl[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(notes);
    }

    public NoteSourceImpl init() {
        Note[] notesArray = new Note[]{
                new Note(resources.getString(R.string.first_title), resources.getString(R.string.first_content), getDate()),
                new Note(resources.getString(R.string.second_title), resources.getString(R.string.second_content), getDate()),
                new Note(resources.getString(R.string.third_title), resources.getString(R.string.third_content), getDate()),
                new Note(resources.getString(R.string.fourth_title), resources.getString(R.string.fourth_content), getDate()),
                new Note(resources.getString(R.string.fifth_title), resources.getString(R.string.fifth_content), getDate()),
                new Note(resources.getString(R.string.sixth_title), resources.getString(R.string.sixth_content), getDate()),
                new Note(resources.getString(R.string.seventh_title), resources.getString(R.string.seventh_content), getDate())
        };
        Collections.addAll(notes, notesArray);
        return this;
    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int size() {
        return notes.size();
    }

    @Override
    public void deleteNote(int position) {
        notes.remove(position);
    }

    @Override
    public void changeNote(int position, Note note) {
        notes.set(position, note);
    }

    @Override
    public void addNote(Note note) {
        notes.add(note);
    }

    @Override
    public void clearNotes() {
        notes.clear();
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
        return String.format("%s: %s", "Дата создания", formatter.format(Calendar.getInstance().getTime()));
    }
}
