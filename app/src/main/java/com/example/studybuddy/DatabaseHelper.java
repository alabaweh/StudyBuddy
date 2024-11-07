package com.example.studybuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "studybuddy.db";
    private static final int DATABASE_VERSION = 1;

    // Tables
    private static final String TABLE_USERS = "users";
    private static final String TABLE_GROUPS = "groups";
    private static final String TABLE_SESSIONS = "sessions";

    // Common columns
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    // User table columns
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Groups table
        String CREATE_GROUPS_TABLE = "CREATE TABLE " + TABLE_GROUPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + "course_id TEXT" + ")";
        db.execSQL(CREATE_GROUPS_TABLE);

        // Create Sessions table
        String CREATE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_SESSIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT,"
                + "date TEXT,"
                + "start_time TEXT,"
                + "end_time TEXT,"
                + "location TEXT,"
                + "group_id INTEGER" + ")";
        db.execSQL(CREATE_SESSIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        onCreate(db);
    }

    // User methods
    public long addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        return db.insert(TABLE_USERS, null, values);
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID},
                KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Group methods
    public long createGroup(String name, String courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put("course_id", courseId);
        return db.insert(TABLE_GROUPS, null, values);
    }

    // Session methods
    public long createSession(String title, Date date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date.toString());
        values.put("location", location);
        return db.insert(TABLE_SESSIONS, null, values);
    }
}