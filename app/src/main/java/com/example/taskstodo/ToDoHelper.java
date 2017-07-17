package com.example.taskstodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ToDoHelper extends SQLiteOpenHelper {

    //schema for the TasksTODO database
    final static String DATABASE_NAME = "TasksToDo.db";
    final static int DATABASE_VERSION = 1;
    final static String TASKS_TO_DO_TABLE_NAME = "Tasks";
    final static String TASKS_TO_DO_COLUMN_TITLE = "title";
    final static String TASKS_TO_DO_TABLE_COLUMN_DESCRIPTION = "description";
    final static String TASKS_TO_DO_TABLE_COLUMN_DATE = "date";
    final static String TASKS_TO_DO_TABLE_COLUMN_TIME = "time";
    final static String TASKS_TO_DO_TABLE_COLUMN_ID = "_id";
    public ToDoHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_Query = "CREATE TABLE " +TASKS_TO_DO_TABLE_NAME  +
                "( "+TASKS_TO_DO_TABLE_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TASKS_TO_DO_COLUMN_TITLE + " TEXT, " +
                TASKS_TO_DO_TABLE_COLUMN_DESCRIPTION + " TEXT, " +
                TASKS_TO_DO_TABLE_COLUMN_DATE + " TEXT, " +
                TASKS_TO_DO_TABLE_COLUMN_TIME + " TEXT); ";
        db.execSQL(sql_Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+TASKS_TO_DO_TABLE_NAME);
        onCreate(db);
    }
    public void addEntry(String title, String description, String date, String time){
        ContentValues values = new ContentValues();
        values.put(TASKS_TO_DO_COLUMN_TITLE,title);
        values.put(TASKS_TO_DO_TABLE_COLUMN_DESCRIPTION,description);
        values.put(TASKS_TO_DO_TABLE_COLUMN_TIME,time);
        values.put(TASKS_TO_DO_TABLE_COLUMN_DATE,date);
        SQLiteDatabase db = getWritableDatabase();

        db.insert(TASKS_TO_DO_TABLE_NAME,null,values);
        db.close();
    }
    public void deleteEntry(long id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+TASKS_TO_DO_TABLE_NAME + " WHERE "+TASKS_TO_DO_TABLE_COLUMN_ID + "=\"" + id + "\";");
        db.close();
    }
}
