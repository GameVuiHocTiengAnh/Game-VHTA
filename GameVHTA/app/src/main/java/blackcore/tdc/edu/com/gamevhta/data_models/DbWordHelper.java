package blackcore.tdc.edu.com.gamevhta.data_models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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

    private static DbWordHelper sInstance;

    public DbWordHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbWordHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbWordHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_WORD + "( " +
                W_COLUMN_ID_WORD + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                W_COLUMN_WORD_ENG + " TEXT," +
                W_COLUMN_WORD_VIE + " TEXT," +
                W_COLUMN_PATH_IMAGE + " TEXT," +
                W_COLUMN_PATH_SOUND + " TEXT," +
                W_COLUMN_OBJECT + " TEXT," +
                W_COLUMN_LEVEL + " TEXT )";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD);
        onCreate(db);
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
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE " + W_COLUMN_OBJECT + " = " + strObject + " LIMIT " + size, null);
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
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE " + W_COLUMN_OBJECT + " = " + strObject, null);
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

    public ArrayList<WordObject> getWordObjectLevel(String strObject,int size, int level) {
        ArrayList<WordObject> arrWord = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE " + W_COLUMN_OBJECT + " = " + strObject + " AND " + W_COLUMN_LEVEL + " = " + level + " LIMIT " + size, null);
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
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORD + " WHERE " + W_COLUMN_OBJECT + " = " + strObject + " AND " + W_COLUMN_LEVEL + " = " + level, null);
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
