package com.dmitri.mynote2;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class NoteMapping {

    public static class Fields {
        public static final String PICTURE = "picture";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String DATE = "date";
    }

    public static Note toNote(String id, Map<String, Object> doc) {
        long indexPic = (long) doc.get(Fields.PICTURE);
        Timestamp timeStamp = (Timestamp) doc.get(Fields.DATE);
        Note answer = new Note((String) doc.get(Fields.TITLE), (String) doc.get(Fields.CONTENT), (String) doc.get(Fields.DATE));
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(Note note) {
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.TITLE, note.getTitle());
        answer.put(Fields.CONTENT, note.getContent());
        answer.put(Fields.DATE, note.getDate());
        return answer;
    }
}
