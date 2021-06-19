package com.example.notes.model;

import java.io.Serializable;

public class Note implements Serializable {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_AUDIO = "audio";

    private Integer id;
    private String title;
    private String timestamp;
    private String description;
    private String category;
    private String type;
    private String image, audio;
    private String latitude,longitude;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_CATEGORY + " TEXT,"
                    + COLUMN_TYPE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_IMAGE + " TEXT,"
                    + COLUMN_LATITUDE + " TEXT,"
                    + COLUMN_LONGITUDE + " TEXT,"
                    + COLUMN_AUDIO + " TEXT"
                    + ")";

/*    CREATE TABLE " + DB_TABLE + "("+
    KEY_NAME + " TEXT," +
    KEY_IMAGE + " BLOB);"*/

    public Note() {
    }

    public Note(Integer id, String title, String description, String category, String type, String timestamp, String image,String latitude,String longitude, String audio
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.type = type;
        this.timestamp = timestamp;
        this.image = image;
        this.latitude=latitude;
        this.longitude=longitude;
        this.audio = audio;
    }



    public int getId() {
        return id;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {

//        Byte[] byteObjects = image;
//        byte[] bytes = new byte[byteObjects.length];
//        int j=0;
//// Unboxing Byte values. (Byte[] to byte[])
//        for(Byte b: byteObjects)
//            bytes[j++] = b.byteValue();
        return image;
    }

    public void setImage(String  image) {
//        byte[] bytes = image;
//        Byte[] byteObjects = new Byte[bytes.length];
//
//        int i=0;
//// Associating Byte array values with bytes. (byte[] to Byte[])
//        for(byte b: bytes)
//            byteObjects[i++] = b;
        this.image = image;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}

