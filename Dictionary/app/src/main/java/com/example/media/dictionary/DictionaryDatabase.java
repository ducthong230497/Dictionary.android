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



public class DictionaryDatabase {
    private static final String TAG = "DictionaryDatabase";

    //The columns we'll include in the dictionary table
    public static final String COL_WORD = "WORD";
    public static final String COL_DEFINITION = "DEFINITION";

    private static final String DATABASE_NAME = "Dictionary.db";
    private static final String FTS_VIRTUAL_TABLE = "DICTIONARYTABLE";
    private static String[] TABLES;
    private static final int DATABASE_VERSION = 30;

    public static ArrayList<String> listAllWord = new ArrayList<String>();
    public static ArrayList<String>[] listWord = (ArrayList<String>[])new ArrayList[26];

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public DictionaryDatabase(Context context) {
        TABLES = getTablesName();
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    private String[] getTablesName() {
        String[] tables = new String[27];
        for (int i = 97; i <= 123; i++){
            if (i == 123){
                tables[i - 97] = "number";
            }
            else {
                char a = (char)i;
                tables[i - 97] = String.valueOf(a);
            }
        }
        return tables;
    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_WORD + ", " +
                        COL_DEFINITION + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
            SQLiteDatabase db = this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            for (int i = 0; i < TABLES.length; i++){
               String CREATE_QUERY =
                        "CREATE VIRTUAL TABLE " + TABLES[i] +
                                " USING fts3 (" +
                                COL_WORD + ", " +
                                COL_DEFINITION + ")";
                mDatabase.execSQL(CREATE_QUERY);
            }
            //mDatabase.execSQL(FTS_TABLE_CREATE);
            loadDictionary();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            for(int i = 0; i < TABLES.length; i++)
                db.execSQL("DROP TABLE IF EXISTS " + TABLES[i]);
            onCreate(db);
        }

        private void loadDictionary() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadWords();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void loadWords() throws IOException {
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.anhviet109k);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String WORD=null;
            String DEFINITION="";
            try {
                String line;
                while ((line = (reader.readLine())) != null) {
                    if (!line.equals("")){
                        char a = line.charAt(0);
                        if (line.charAt(0) == '@'){
                            if (WORD == null){
                                WORD = line.replaceFirst("@", "");
                            }
                            else {
                                // add word and definition
                                long id = addWord(WORD.trim(), DEFINITION.trim());
                                DEFINITION = "";
                                if (id < 0) {
                                    Log.e(TAG, "unable to add word: " + WORD.trim());
                                }
                                WORD = line.replaceFirst("@", "");
                                String[] temp = WORD.split("/");
                                WORD = "";
                                if (temp.length == 1){
                                    WORD = temp[0];
                                }
                                else {
                                    for (int i = 0; i < temp.length; i++){
                                        if (i == temp.length - 1){
                                            DEFINITION += temp[1]+"§";
                                        }
                                        else{
                                            WORD += temp[i] + " ";
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            DEFINITION +=line+'§';
                        }
                    }
                }
            } finally {
                reader.close();
            }
            long id = addWord(WORD.trim(), DEFINITION.trim());
            if (id < 0) {
                Log.e(TAG, "unable to add word: " + WORD.trim());
            }
        }

        public long addWord(String word, String definition) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_WORD, word);
            initialValues.put(COL_DEFINITION, definition);
            String TableName = getTableName(word);
            return mDatabase.insert(TableName, null, initialValues);
        }

        @NonNull
        private String getTableName(String word) {
            char c = word.charAt(0);
            switch (word.charAt(0))
            {
                case 'a': return "a";
                case 'b': return "b";
                case 'c': return "c";
                case 'd': return "d";
                case 'e': return "e";
                case 'f': return "f";
                case 'g': return "g";
                case 'h': return "h";
                case 'i': return "i";
                case 'j': return "j";
                case 'k': return "k";
                case 'l': return "l";
                case 'm': return "m";
                case 'n': return "n";
                case 'o': return "o";
                case 'p': return "p";
                case 'q': return "q";
                case 'r': return "r";
                case 's': return "s";
                case 't': return "t";
                case 'u': return "u";
                case 'v': return "v";
                case 'w': return "w";
                case 'x': return "y";
                case 'y': return "w";
                case 'z': return "z";
                default: return "number";
            }
        }
    }
    // mới thêm
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


    public ArrayList<String> getAllWords(){
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);
        Cursor c = builder.query(mDatabaseOpenHelper.getReadableDatabase(), new String[]{COL_WORD}, null, null, null, null, null);
        String lastWord = null;

        if (c != null){
            c.moveToFirst();
            while (c.isAfterLast() == false){
                String word = c.getString(0);
                if (lastWord == null || (!word.equals("") && lastWord.charAt(0) != word.charAt(0))){
                    lastWord = word;
                }
                if (!word.equals("")){
                    int i = word.charAt(0);
                    if (i >= 97){
                        i -= 97;
                    }
                    else if (i >= 65){
                        i -= 65;
                    }
                    else {
                        i = 26;
                    }
                    if (0 <= i && i <= 26){
                        if (listWord[i] == null){
                            listWord[i] = new ArrayList<String>();
                        }
                        listWord[i].add(word);
                    }
                }
                listAllWord.add(word);
                c.moveToNext();

                /*listAllWord.add(c.getString(0));
                c.moveToNext();*/
            }
        }
        return listAllWord;
    }

    public ArrayList<String> getWords(String table){
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(table);
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
}
