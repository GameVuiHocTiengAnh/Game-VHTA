package blackcore.tdc.edu.com.gamevhta.data_models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;

import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.DATABASE_NAME;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.DATABASE_VERSION;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.TABLE_WORD;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.W_COLUMN_ID_WORD;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.W_COLUMN_LEVEL;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.W_COLUMN_OBJECT;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.W_COLUMN_PATH_IMAGE;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.W_COLUMN_PATH_SOUND;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.W_COLUMN_WORD_ENG;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.W_COLUMN_WORD_VIE;

/**
 * Created by Hoang on 3/20/2017.
 */

public class DbWordHelper extends SQLiteOpenHelper {

    private Context myContext;
    private SQLiteDatabase myDatabase;
    //private String dbPath = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";
    private String dbPath = "/data/data/blackcore.tdc.edu.com.gamevhta/databases/";
    private static DbWordHelper sInstance;

    public DbWordHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        boolean db_exist = checkdatabase();
        if (db_exist) {
            System.out.println("Database exists");
            openDatabase();
        } else {
            System.out.println("Database doesn't exist");
            try {
                createDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static DbWordHelper getInstance(Context context) throws IOException {
        if (sInstance == null) {
            sInstance = new DbWordHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String script = "CREATE TABLE " + TABLE_WORD + "( " +
//                W_COLUMN_ID_WORD + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                W_COLUMN_WORD_ENG + " TEXT," +
//                W_COLUMN_WORD_VIE + " TEXT," +
//                W_COLUMN_PATH_IMAGE + " TEXT," +
//                W_COLUMN_PATH_SOUND + " TEXT," +
//                W_COLUMN_OBJECT + " TEXT," +
//                W_COLUMN_LEVEL + " TEXT )";
//        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD);
//        onCreate(db);
    }

    private boolean checkdatabase() {

        boolean check_db = false;
        try {
            String myPath = dbPath + ConfigApplication.DATABASE_NAME;
            File db_file = new File(myPath);
            check_db = db_file.exists();
        } catch (SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return check_db;
    }

    public void createDatabase() throws IOException {
        boolean db_exist = checkdatabase();
        if (db_exist) {
            System.out.println(" Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openDatabase() throws SQLException {
        //Open the database
        String my_path = dbPath + ConfigApplication.DATABASE_NAME;
        myDatabase = SQLiteDatabase.openDatabase(my_path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void copyDatabase() throws IOException {
        InputStream inputStream = myContext.getAssets().open("databases/"+ConfigApplication.DATABASE_NAME+".db");
        String outFile = dbPath + ConfigApplication.DATABASE_NAME;
        OutputStream outputStream = new FileOutputStream(outFile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public synchronized void close() {
        if (myDatabase != null) {
            myDatabase.close();
        }
        super.close();
    }

    public boolean doInsertWord(WordObject wordObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(W_COLUMN_WORD_ENG, wordObject.getwEng());
        contentValues.put(W_COLUMN_WORD_VIE, wordObject.getwEng());
        contentValues.put(W_COLUMN_PATH_IMAGE, wordObject.getwEng());
        contentValues.put(W_COLUMN_PATH_SOUND, wordObject.getwEng());
        contentValues.put(W_COLUMN_OBJECT, wordObject.getwEng());
        contentValues.put(W_COLUMN_LEVEL, wordObject.getwEng());
        db.insert(TABLE_WORD, null, contentValues);
        return true;
    }

    public boolean doUpdateWord(WordObject wordObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(W_COLUMN_WORD_ENG, wordObject.getwEng());
        contentValues.put(W_COLUMN_WORD_VIE, wordObject.getwEng());
        contentValues.put(W_COLUMN_PATH_IMAGE, wordObject.getwEng());
        contentValues.put(W_COLUMN_PATH_SOUND, wordObject.getwEng());
        contentValues.put(W_COLUMN_OBJECT, wordObject.getwEng());
        contentValues.put(W_COLUMN_LEVEL, wordObject.getwEng());
        db.update(TABLE_WORD, contentValues, W_COLUMN_ID_WORD + " = ?", new String[]{wordObject.getwID()});
        return true;
    }

    public Integer doDeleteWord(String wID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_WORD, W_COLUMN_ID_WORD + " = ? ", new String[]{wID});
    }

    public ArrayList<WordObject> getAllWord() {
        ArrayList<WordObject> arrWord = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORD, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            WordObject wordObject = new WordObject();
            wordObject.setwEng(res.getString(res.getColumnIndex(W_COLUMN_WORD_ENG)));
            wordObject.setwVie(res.getString(res.getColumnIndex(W_COLUMN_WORD_VIE)));
            wordObject.setwPathImage(res.getString(res.getColumnIndex(W_COLUMN_PATH_IMAGE)));
            wordObject.setwPathSound(res.getString(res.getColumnIndex(W_COLUMN_PATH_SOUND)));
            wordObject.setwObject(res.getString(res.getColumnIndex(W_COLUMN_OBJECT)));
            wordObject.setưLevel(res.getString(res.getColumnIndex(W_COLUMN_LEVEL)));
            arrWord.add(wordObject);
            res.moveToNext();
        }
        return arrWord;
    }

    public ArrayList<WordObject> getWordObject(String strObject, int size) {
        ArrayList<WordObject> arrWord = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE " + W_COLUMN_OBJECT + " = '" + strObject + "' LIMIT " + size, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            WordObject wordObject = new WordObject();
            wordObject.setwEng(res.getString(res.getColumnIndex(W_COLUMN_WORD_ENG)));
            wordObject.setwVie(res.getString(res.getColumnIndex(W_COLUMN_WORD_VIE)));
            wordObject.setwPathImage(res.getString(res.getColumnIndex(W_COLUMN_PATH_IMAGE)));
            wordObject.setwPathSound(res.getString(res.getColumnIndex(W_COLUMN_PATH_SOUND)));
            wordObject.setwObject(res.getString(res.getColumnIndex(W_COLUMN_OBJECT)));
            wordObject.setưLevel(res.getString(res.getColumnIndex(W_COLUMN_LEVEL)));
            arrWord.add(wordObject);
            res.moveToNext();
        }
        return arrWord;
    }

    public ArrayList<WordObject> getWordObject(String strObject) {
        ArrayList<WordObject> arrWord = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE " + W_COLUMN_OBJECT + " = '" + strObject +"'", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            WordObject wordObject = new WordObject();
            wordObject.setwEng(res.getString(res.getColumnIndex(W_COLUMN_WORD_ENG)));
            wordObject.setwVie(res.getString(res.getColumnIndex(W_COLUMN_WORD_VIE)));
            wordObject.setwPathImage(res.getString(res.getColumnIndex(W_COLUMN_PATH_IMAGE)));
            wordObject.setwPathSound(res.getString(res.getColumnIndex(W_COLUMN_PATH_SOUND)));
            wordObject.setwObject(res.getString(res.getColumnIndex(W_COLUMN_OBJECT)));
            wordObject.setưLevel(res.getString(res.getColumnIndex(W_COLUMN_LEVEL)));
            arrWord.add(wordObject);
            res.moveToNext();
        }
        return arrWord;
    }

    public ArrayList<WordObject> getWordObjectLevel(String strObject, int size, int level) {
        ArrayList<WordObject> arrWord = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE " + W_COLUMN_OBJECT + " = '" + strObject + "' AND " + W_COLUMN_LEVEL + " = " + level + " LIMIT " + size, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            WordObject wordObject = new WordObject();
            wordObject.setwEng(res.getString(res.getColumnIndex(W_COLUMN_WORD_ENG)));
            wordObject.setwVie(res.getString(res.getColumnIndex(W_COLUMN_WORD_VIE)));
            wordObject.setwPathImage(res.getString(res.getColumnIndex(W_COLUMN_PATH_IMAGE)));
            wordObject.setwPathSound(res.getString(res.getColumnIndex(W_COLUMN_PATH_SOUND)));
            wordObject.setwObject(res.getString(res.getColumnIndex(W_COLUMN_OBJECT)));
            wordObject.setưLevel(res.getString(res.getColumnIndex(W_COLUMN_LEVEL)));
            arrWord.add(wordObject);
            res.moveToNext();
        }
        return arrWord;
    }

    public ArrayList<WordObject> getWordObjectLevel(String strObject, int level) {
        ArrayList<WordObject> arrWord = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE " + W_COLUMN_OBJECT + " = '" + strObject + "' AND " + W_COLUMN_LEVEL + " = " + level, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            WordObject wordObject = new WordObject();
            wordObject.setwEng(res.getString(res.getColumnIndex(W_COLUMN_WORD_ENG)));
            wordObject.setwVie(res.getString(res.getColumnIndex(W_COLUMN_WORD_VIE)));
            wordObject.setwPathImage(res.getString(res.getColumnIndex(W_COLUMN_PATH_IMAGE)));
            wordObject.setwPathSound(res.getString(res.getColumnIndex(W_COLUMN_PATH_SOUND)));
            wordObject.setwObject(res.getString(res.getColumnIndex(W_COLUMN_OBJECT)));
            wordObject.setưLevel(res.getString(res.getColumnIndex(W_COLUMN_LEVEL)));
            arrWord.add(wordObject);
            res.moveToNext();
        }
        return arrWord;
    }
}
