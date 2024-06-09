package es.exsample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        // データベースオブジェクト生成時の処理
        super(context,"todoTable",null,1);
    }

    public void onCreate(SQLiteDatabase db) {
        // データベース作成時の処理
        db.execSQL("CREATE TABLE todoTable (_id INGETEGER PRIMARY KEY, todo TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // データベースアップデート時の処理
    }
}

