package com.dmitri.mynote2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Note implements Parcelable {

    private String title;
    private String content;
    private Calendar date;

    public Note(String title, String content, Calendar date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }


    protected Note(Parcel in) {
        title = in.readString();
        content = in.readString();
        date = (Calendar) in.readSerializable();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeSerializable(date);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Calendar getDate() {
        return date;
    }
}
