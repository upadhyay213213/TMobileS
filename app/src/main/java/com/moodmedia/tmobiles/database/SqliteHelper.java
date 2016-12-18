package com.moodmedia.tmobiles.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.moodmedia.tmobiles.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by pkatya on 11/6/16.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME ="tmobile_songs.sqlite";
    public static final int DB_VERSION =1;
    private static final String TAG = SqliteHelper.class.getCanonicalName();
    private static String DB_PATH =null ;
    private static SqliteHelper dbHelper;
    protected static SQLiteDatabase db;

    public static SqliteHelper init(Context context){
        dbHelper= new SqliteHelper(context.getApplicationContext(),DB_VERSION);
        db=dbHelper.getWritableDatabase();
        return dbHelper;

    }
    public static SQLiteDatabase getSqliteDatabase(){
        if(db==null){
            throw new RuntimeException("Please call init method");
        }
        return db;

    }

    private SqliteHelper(Context context, int version) {
        super(context,DB_NAME,null, version);
        DB_PATH = "/data/data/" + context.getPackageName().replace("/", "")
                + "/databases/";
        dbHelper = this;
        if (!databaseExists()) {
            try {
                copyDataBase(context);
            } catch (IOException e) {
                Log.e(TAG, "Error while copying database to device: " + e);
            }
        } else {
            Log.i(TAG, "Database already copied.");
        }
    }

    private boolean databaseExists() {
        File file=new File(DB_PATH+DB_NAME);
        return file.exists();
    }
    private void copyDataBase(Context applicationContext) throws IOException {
        InputStream input = null;
        FileOutputStream output = null;
        int c;
        byte[] tmp;
        try {
            File dbPath = new File(DB_PATH);
            dbPath.mkdirs();
            File databaseFile = new File(DB_PATH, DB_NAME);
            databaseFile.createNewFile();
            output = new FileOutputStream(DB_PATH + DB_NAME);
            int i = 0;

            input = applicationContext.getResources().openRawResource(
                    R.raw.tmobile_songs);
            tmp = new byte[1024];
            while ((c = input.read(tmp)) != -1) {
                i++;
                output.write(tmp, 0, c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
                output.close();
            }
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static Cursor executeSelectQuery(SQLiteDatabase db, String query,
                                            String[] selectionArgs) {
        Log.v(TAG, query);
        if (selectionArgs != null) {
            for (String s : selectionArgs) {
                Log.v(TAG, "selectionArgs: " + s);
            }
        }
        Cursor cur = null;
        cur = db.rawQuery(query, selectionArgs);
        return cur;
    }
}
