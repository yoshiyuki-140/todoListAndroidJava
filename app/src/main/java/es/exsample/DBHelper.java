package es.exsample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todoDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "todoTable";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TODO = "todo";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TODO + " TEXT)";
        db.execSQL(CREATE_TODO_TABLE);
        // Initialize lock table
        db.execSQL("CREATE TABLE lockState (id INTEGER PRIMARY KEY, isLocked INTEGER DEFAULT 0)");
        ContentValues initialValues = new ContentValues();
        initialValues.put("id", 1);
        initialValues.put("isLocked", 0);
        db.insert("lockState", null, initialValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS lockState");
        onCreate(db);
    }

    public void addTodo(String todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO, todo);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<String> getAllTodos() {
        ArrayList<String> todoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                todoList.add(cursor.getString(cursor.getColumnIndex(COLUMN_TODO)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todoList;
    }

    public void deleteTodo(String todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TODO + " = ?", new String[]{todo});
        db.close();
    }

    public void deleteAllTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public boolean isLocked() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("lockState", new String[]{"isLocked"}, "id = 1", null, null, null, null);
        boolean isLocked = false;
        if (cursor != null && cursor.moveToFirst()) {
            isLocked = cursor.getInt(cursor.getColumnIndex("isLocked")) > 0;
            cursor.close();
        }
        db.close();
        return isLocked;
    }

    public void setLocked(boolean locked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isLocked", locked ? 1 : 0);
        db.update("lockState", values, "id = 1", null);
        db.close();
    }
}
