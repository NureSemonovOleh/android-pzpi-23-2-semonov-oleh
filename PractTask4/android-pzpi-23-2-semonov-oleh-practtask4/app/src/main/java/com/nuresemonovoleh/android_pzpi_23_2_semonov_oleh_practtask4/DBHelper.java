package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_practtask4;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        super(context, "MyDB", null,1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, age INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public class User{
        private String name;
        private int age;

        public User(String name, int age){
            this.name = name;
            this.age = age;
        }

        public String getName(){
            return name;
        }
        public int getAge(){
            return age;
        }
    }

    public void addUser(String Name, int Age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", Name);
        values.put("age",Age);
        db.insert("users",null,values);
        db.close();
    }

    public List<User> getAllUsers(){

        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("users",null,null,
                null,null,null,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex("age"));
            userList.add(new User(name,age));
            cursor.moveToNext();

        }
        cursor.close();

        db.close();
        return userList;
    }
}
