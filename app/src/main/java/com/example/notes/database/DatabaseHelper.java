package com.example.notes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.notes.model.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    //Insert text type
    public long insertNote(String note,String desc,String category,String latitude,String longitude, String audio) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Note.COLUMN_TITLE, note);
        values.put(Note.COLUMN_DESCRIPTION, desc);
        values.put(Note.COLUMN_CATEGORY, category);
        values.put(Note.COLUMN_TYPE, "text");
        values.put(Note.COLUMN_LATITUDE,latitude);
        values.put(Note.COLUMN_LONGITUDE, longitude);
        if (audio != null){
            values.put(Note.COLUMN_AUDIO, audio);
        }

        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public long insertImageNote(String title,String desc,String category,String image,String latitude,String longitude, String audio) {
         SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_TITLE, title);
        values.put(Note.COLUMN_DESCRIPTION, desc);
        values.put(Note.COLUMN_CATEGORY, category);
        values.put(Note.COLUMN_IMAGE, image);
        values.put(Note.COLUMN_TYPE, "image");
        values.put(Note.COLUMN_LATITUDE, latitude);
        values.put(Note.COLUMN_LONGITUDE, longitude);
        if (audio != null){
            values.put(Note.COLUMN_AUDIO, audio);
        }

        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);

        // close db connection
        db.close();

         return id;
    }

    public Note getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_TITLE, Note.COLUMN_DESCRIPTION,Note.COLUMN_CATEGORY,Note.COLUMN_TYPE, Note.COLUMN_TIMESTAMP, Note.COLUMN_IMAGE, Note.COLUMN_LATITUDE, Note.COLUMN_LONGITUDE, Note.COLUMN_AUDIO},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
//        byte[] bytes = cursor.getBlob(cursor.getColumnIndex(Note.COLUMN_IMAGE));
//        Byte[] byteObjects = new Byte[bytes.length];
//        int i=0;
//// Associating Byte array values with bytes. (byte[] to Byte[])
//        for(byte b: bytes)
//            byteObjects[i++] = b;
        // prepare note object
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_CATEGORY)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TYPE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_IMAGE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_LATITUDE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_AUDIO)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
                Note.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

                note.setDescription(cursor.getString(cursor.getColumnIndex(Note.COLUMN_DESCRIPTION)));
                note.setCategory(cursor.getString(cursor.getColumnIndex(Note.COLUMN_CATEGORY)));
                note.setType(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TYPE)));
                if (cursor.getBlob(cursor.getColumnIndex(Note.COLUMN_IMAGE))!=null) {
                    note.setImage(cursor.getString(cursor.getColumnIndex(Note.COLUMN_IMAGE)));
                }
                note.setLatitude(cursor.getString(cursor.getColumnIndex(Note.COLUMN_LATITUDE)));
                note.setLongitude(cursor.getString(cursor.getColumnIndex(Note.COLUMN_LONGITUDE)));
                if (cursor.getBlob(cursor.getColumnIndex(Note.COLUMN_AUDIO))!=null) {
                    note.setAudio(cursor.getString(cursor.getColumnIndex(Note.COLUMN_AUDIO)));
                }
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_TITLE, note.getTitle());
        values.put(Note.COLUMN_DESCRIPTION, note.getDescription());
        values.put(Note.COLUMN_CATEGORY, note.getCategory());

        // updating row
        db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public List<Note> search(String keyword) {
        List<Note> contacts = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + Note.TABLE_NAME + " where " + Note.COLUMN_TITLE + " or " + Note.COLUMN_DESCRIPTION + " like ?", new String[] { "%" + keyword + "%" });
            if (cursor.moveToFirst()) {
                contacts = new ArrayList<>();
                do {
                    Note contact = new Note();
                    contact.setId(cursor.getInt(0));
                    contact.setTitle(cursor.getString(1));
                    contact.setDescription(cursor.getString(2));
                    contact.setCategory(cursor.getString(3));
                    contact.setType(cursor.getString(4));
                    contact.setTimestamp(cursor.getString(5));
                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            contacts = null;
        }
        return contacts;
    }
}
