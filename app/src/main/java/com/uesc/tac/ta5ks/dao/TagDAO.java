package com.uesc.tac.ta5ks.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.uesc.tac.ta5ks.database.Database;
import com.uesc.tac.ta5ks.model.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by levy on 11/05/18.
 */

public class TagDAO extends Database {
    public TagDAO(Context context) {
        super(context);
    }

    public String addTag(Tag tag){
        SQLiteDatabase db = super.getWritableDatabase();
        long result;

        ContentValues values = new ContentValues();

        values.put(getColumnName(), tag.getName());
        values.put(getColumnColor(), tag.getColor());

        result = db.insert(getTableTag(), null, values);

        if(result == -1){
            return "Error while inserting tag.";
        }else{
            return "Tag registered";
        }
    }

    public void removeTag(Tag tag){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(getTableTag(), getColumnName() + "= ?", new String[] {tag.getName()});

        db.close();
    }

    public Tag searchTag(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = getColumnId() + " = '" + id + "'";
        Tag selectedTag = null;
        Cursor cursor = db.query(getTableTag(), new String[] {getColumnId(), getColumnName(), getColumnColor()}, where,
                null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        if(cursor.getCount() > 0){
            selectedTag = new Tag(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)) );
        }

        return selectedTag;
    }

    public Tag searchTag(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = getColumnName() + " = '" + name + "'";
        Tag selectedTag = null;
        Cursor cursor = db.query(getTableTag(), new String[] {getColumnId(), getColumnName(), getColumnColor()}, where,
                null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }

        if(cursor.getCount() > 0){
            selectedTag = new Tag(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    Integer.parseInt(cursor.getString(2)) );
        }

        return selectedTag;
    }

    public List<Tag> listTags(){
        List<Tag> tags = new ArrayList<Tag>();
        String query = "SELECT * FROM " + getTableTag();
        SQLiteDatabase db = super.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Tag tag = new Tag();
                tag.setId(Integer.parseInt(cursor.getString(0)));
                tag.setName(cursor.getString(1));
                tag.setColor(Integer.parseInt(cursor.getString(2)));

                tags.add(tag);
            }while(cursor.moveToNext());
        }

        return tags;
    }
}
