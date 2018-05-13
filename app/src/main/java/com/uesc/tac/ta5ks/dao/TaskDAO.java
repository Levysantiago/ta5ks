package com.uesc.tac.ta5ks.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.uesc.tac.ta5ks.database.Database;
import com.uesc.tac.ta5ks.model.Tag;
import com.uesc.tac.ta5ks.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by levy on 12/05/18.
 */

public class TaskDAO extends Database{
    private Context context;

    public TaskDAO(Context context) {
        super(context);
        this.context = context;
    }

    public String addTask(Task task){
        SQLiteDatabase db = super.getWritableDatabase();
        long result;

        ContentValues values = new ContentValues();

        values.put(getColumnTitle(), task.getTitle());
        values.put(getColumnDescription(), task.getDescription());
        values.put(getColumnStatus(), task.getStatus());
        values.put(getColumnTag(), task.getTag().getId());

        result = db.insert(getTableTask(), null, values);

        if(result == -1){
            return "Error while inserting task.";
        }else{
            return "Task registered";
        }
    }

    public List<Task> listTasks(){
        List<Task> tasks = new ArrayList<Task>();
        String query = "SELECT * FROM " + getTableTask();
        SQLiteDatabase db = super.getWritableDatabase();
        TagDAO tagDAO = new TagDAO(this.context);
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setDescription(cursor.getString(2));
                task.setStatus(Integer.parseInt(cursor.getString(3)));
                //Searching the corresponding tag
                Tag tag = tagDAO.searchTag( Integer.parseInt(cursor.getString(4)) );
                task.setTag(tag);

                tasks.add(task);
            }while(cursor.moveToNext());
        }

        return tasks;
    }
}
