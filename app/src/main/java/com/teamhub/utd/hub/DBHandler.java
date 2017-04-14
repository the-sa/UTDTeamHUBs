package com.teamhub.utd.hub;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.id;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "RMDB.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE User_Table ( ID INTEGER PRIMARY KEY, Name TEXT, Username TEXT, " +
                "Password TEXT )";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS User_Table");
        // Creating tables again
        onCreate(db);
    }

    // read one user info
    public String readUserPassword (String username) {
        String password = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("User_Table", new String [] { "ID, NAME, USERNAME, PASSWORD" }, "USERNAME=?", new String [] { username },
                            null,null,null);
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex("PASSWORD");
            password = cursor.getString(id);
        }
        return password;
    }

    // add one user info to database
    public void addUser (String name, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("USERNAME", username);
        contentValues.put("PASSWORD", password);
        db.insert("User_Table", null, contentValues);
        db.close();
    }
}
