package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                        TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
                        TaskContract.TaskEntry.COLUMN_TITLE + " TEXT)";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE, task);
        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> getTasks() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TaskContract.TaskEntry.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            String task = cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TITLE));
            taskList.add(task);
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public void deleteTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE_NAME,
                TaskContract.TaskEntry.COLUMN_TITLE + " = ?",
                new String[]{task});
        db.close();
    }
}

