package com.example.media.dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class FavoriteWordDatabase {
    private static final String TAG = "FavoriteWordDatabase";

    //The columns we'll include in the dictionary table
    public static final String COL_WORD = "WORD";
    public static final String COL_COUNT = "COUNT";

    private static final String DATABASE_NAME = "FavoriteWord.db";
    private static final String FTS_VIRTUAL_TABLE = "FAVORITEWORDTABLE";
    private static final String CountTable = "COUNTTABLE";
    private static final int DATABASE_VERSION = 3;

    public  static String favoriteWord;
    public static int count;
    public static ArrayList<String> listAllWord = new ArrayList<String>();
    //public static ArrayList<String>[] listWord = (ArrayList<String>[])new ArrayList[26];

    private final FavoriteWordDatabase.DatabaseOpenHelper mDatabaseOpenHelper;

    public FavoriteWordDatabase(Context context) {
        mDatabaseOpenHelper = new FavoriteWordDatabase.DatabaseOpenHelper(context);
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        public SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_WORD + ")";
        private static final String CreateCountTable =
                "CREATE VIRTUAL TABLE " + CountTable +
                        " USING fts3 (" +
                        COL_COUNT + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
            SQLiteDatabase db = this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            mDatabase.execSQL(CreateCountTable);
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_COUNT, "0");
            mDatabase.insert(CountTable, null, initialValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CountTable);
            onCreate(db);
        }


    }
    // mới thêm
    public long addWord(String word) {
        mDatabaseOpenHelper.mDatabase = mDatabaseOpenHelper.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_WORD, word);
        int n = getCount();
        String str = Integer.toString(n);
        ContentValues updateValues = new ContentValues();
        updateValues.put(COL_COUNT, Integer.toString(n+1));
        mDatabaseOpenHelper.mDatabase.delete(CountTable, "COUNT = ?", new String[]{str});
        mDatabaseOpenHelper.mDatabase.insert(CountTable, null, updateValues);
        return mDatabaseOpenHelper.mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
    }

    public long deleteWord(String word){
        mDatabaseOpenHelper.mDatabase = mDatabaseOpenHelper.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_WORD, word);
        int n = getCount();
        String str = Integer.toString(n);
        ContentValues updateValues = new ContentValues();
        updateValues.put(COL_COUNT, Integer.toString(n-1));
        mDatabaseOpenHelper.mDatabase.delete(CountTable, "COUNT = ?", new String[]{str});
        mDatabaseOpenHelper.mDatabase.insert(CountTable, null, updateValues);
        return mDatabaseOpenHelper.mDatabase.delete(FTS_VIRTUAL_TABLE, "WORD = ?", new String[]{word});
    }
    //
    public Cursor getWordMatches(String query, String[] columns) {
        String selection = COL_WORD + " = ?";
        String[] selectionArgs = new String[] {query};

        return query(selection, selectionArgs, columns, query);
    }


    @Nullable
    private Cursor query(String selection, String[] selectionArgs, String[] columns, String query) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        String table  = String.valueOf(query.charAt(0));
        builder.setTables(table);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }



    public ArrayList<String> getFavoriteWords(){
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);
        Cursor c = builder.query(mDatabaseOpenHelper.getReadableDatabase(), new String[]{COL_WORD}, null, null, null, null, null);
        String lastWord = null;
        ArrayList<String> list = new ArrayList<String>();
        if (c != null){
            c.moveToFirst();
            while (c.isAfterLast() == false){
                String word = c.getString(0);
                if (lastWord == null || (!word.equals("") && lastWord.charAt(0) != word.charAt(0))){
                    lastWord = word;
                }
                if (!word.equals("")){
                    list.add(word);
                }
                c.moveToNext();

                /*listAllWord.add(c.getString(0));
                c.moveToNext();*/
            }
        }
        return list;
    }

    public int getCount(){
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CountTable);
        Cursor c = builder.query(mDatabaseOpenHelper.getReadableDatabase(), new String[]{COL_COUNT}, null, null, null, null, null);
        String lastWord = null;
        ArrayList<String> list = new ArrayList<String>();
        if (c != null){
            c.moveToFirst();
            String b = c.getString(0);
            count = Integer.parseInt(c.getString(0));
            return count;
        }
        return -1;
    }
}
