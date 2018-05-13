package com.uesc.tac.ta5ks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by levy on 11/05/18.
 */

public class Database extends SQLiteOpenHelper{
    private static final int VERSAO_BANCO = 1;
    private static final String NOME_BANCO = "bd_ta5ks";

    private static final String TABLE_TAG = "tb_tag";
    private static final String TABLE_TASK = "tb_task";

    private static final String COLUMN_ID = "id";

    //TAGS ATTRIBUTES
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COLOR = "color";

    //TASKS ATTRIBUTES
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_TAG = "tag";

    public Database(Context context){
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String QUERY_TAG = "CREATE TABLE "+ TABLE_TAG + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL UNIQUE, "
                + COLUMN_COLOR + " INTEGER);";
        String QUERY_TASK = "CREATE TABLE "+ TABLE_TASK + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_STATUS + " INTEGER NOT NULL, "
                + COLUMN_TAG + " INTEGER NOT NULL,"
                + " FOREIGN KEY ("+COLUMN_TAG+") REFERENCES "+TABLE_TAG+"("+COLUMN_ID+"));";
        db.execSQL(QUERY_TAG);
        db.execSQL(QUERY_TASK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static String getTableTag() {
        return TABLE_TAG;
    }

    public static String getTableTask() {
        return TABLE_TASK;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnName() {
        return COLUMN_NAME;
    }

    public static String getColumnColor() {
        return COLUMN_COLOR;
    }

    public static String getColumnTitle() {
        return COLUMN_TITLE;
    }

    public static String getColumnDescription() {
        return COLUMN_DESCRIPTION;
    }

    public static String getColumnStatus() {
        return COLUMN_STATUS;
    }

    public static String getColumnTag() {
        return COLUMN_TAG;
    }
}
