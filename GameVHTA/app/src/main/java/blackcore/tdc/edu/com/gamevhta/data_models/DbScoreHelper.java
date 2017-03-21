package blackcore.tdc.edu.com.gamevhta.data_models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;

import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.DATABASE_NAME;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.DATABASE_VERSION;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.S_COLUMN_ID_PLAYER;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.S_COLUMN_PLAYER_NAME;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.S_COLUMN_PLAYER_SCORE;
import static blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication.TABLE_SCORE;

/**
 * Created by Hoang on 3/20/2017.
 */

public class DbScoreHelper extends SQLiteOpenHelper {

    private static DbScoreHelper sInstance;

    public DbScoreHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DbScoreHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DbScoreHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_SCORE + "( " +
                S_COLUMN_ID_PLAYER + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                S_COLUMN_PLAYER_NAME + " TEXT," +
                S_COLUMN_PLAYER_SCORE + " INTEGER )";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);
    }

    public boolean doInsertScore(ScoreObject scoreObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(S_COLUMN_PLAYER_NAME, scoreObject.getsPlayer());
        contentValues.put(S_COLUMN_PLAYER_SCORE, scoreObject.getsScore());
        db.insert(TABLE_SCORE, null, contentValues);
        return true;
    }

    public boolean doUpdateScore(ScoreObject scoreObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(S_COLUMN_PLAYER_NAME, scoreObject.getsPlayer());
        contentValues.put(S_COLUMN_PLAYER_SCORE, scoreObject.getsScore());

        db.update(TABLE_SCORE, contentValues, S_COLUMN_ID_PLAYER + " = ?", new String[]{scoreObject.getsID()});
        return true;
    }

    public Integer doDeleteScore(String wID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCORE, S_COLUMN_ID_PLAYER + " = ? ", new String[]{wID});
    }

    public ArrayList<ScoreObject> getAllScore() {
        ArrayList<ScoreObject> arrWord = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_SCORE + " ORDER BY " + S_COLUMN_PLAYER_SCORE + " DESC", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            ScoreObject scoreObject = new ScoreObject();
            scoreObject.setsPlayer(res.getString(res.getColumnIndex(S_COLUMN_PLAYER_NAME)));
            scoreObject.setsScore(res.getColumnIndex(S_COLUMN_PLAYER_SCORE));
            arrWord.add(scoreObject);
        }
        return arrWord;
    }

    public ArrayList<ScoreObject> getScoreObject(int topScore) {
        ArrayList<ScoreObject> arrWord = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_SCORE + "  ORDER BY " + S_COLUMN_PLAYER_SCORE + " DESC LIMIT " + topScore, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            ScoreObject scoreObject = new ScoreObject();
            scoreObject.setsID(res.getString(res.getColumnIndex(S_COLUMN_ID_PLAYER)));
            scoreObject.setsPlayer(res.getString(res.getColumnIndex(S_COLUMN_PLAYER_NAME)));
            scoreObject.setsScore(Integer.parseInt(res.getString(res.getColumnIndex(S_COLUMN_PLAYER_SCORE))));
            arrWord.add(scoreObject);
            res.moveToNext();
        }
        return arrWord;
    }

}
