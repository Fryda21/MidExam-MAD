package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "student_db";
    private static final int DB_VERSION = 1;
    private static final String TB_NAME = "student";
    private static final String COL_ID = "student_id";
    private static final String COL_NAME = "name";
    private static final String COL_AGE = "age";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_NAME + " ("
                + COL_ID + " TEXT PRIMARY KEY, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_AGE + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public boolean isIdExists(String studentId) {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT " + COL_ID + " FROM " + TB_NAME + " WHERE " + COL_ID + " = ?",
                new String[]{studentId});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean addStudent(StudentModel student) {
        if (isIdExists(student.getStudentId())) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(COL_ID, student.getStudentId());
        values.put(COL_NAME, student.getName());
        values.put(COL_AGE, student.getAge());
        return getWritableDatabase().insert(TB_NAME, null, values) != -1;
    }

    public void updateStudent(StudentModel student) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, student.getName());
        values.put(COL_AGE, student.getAge());
        getWritableDatabase().update(TB_NAME, values, COL_ID + " = ?",
                new String[]{student.getStudentId()});
    }

    public void deleteStudent(String studentId) {
        getWritableDatabase().delete(TB_NAME, COL_ID + " = ?", new String[]{studentId});
    }

    public List<StudentModel> getStudentList() {
        List<StudentModel> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT " + COL_ID + ", " + COL_NAME + ", " + COL_AGE + " FROM " + TB_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new StudentModel(cursor.getString(0), cursor.getString(1), cursor.getInt(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}