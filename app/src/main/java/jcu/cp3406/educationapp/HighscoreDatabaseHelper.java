package jcu.cp3406.educationapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class HighscoreDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "highscore";
    private static final int DB_VERSION = 1;
    private static final String TABLE_SCORES = "scores";
    private static final String KEY_ID = "id";
    private static final String KEY_SCORE = "score";

    HighscoreDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SCORE + " INTEGER)";
        db.execSQL(CREATE_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void addScore(Score score) {
        //add new score to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues scoreValues = new ContentValues();
        scoreValues.put(KEY_SCORE, score.getScore());
        db.insert(TABLE_SCORES, null, scoreValues);
        System.out.println("New Score: "+ score.getScore());
        db.close();
    }

    List<Score> getAllScores() {
        //retrieve database to view
        List<Score> scoreList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SCORES;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Score score = new Score();
                score.setScore(cursor.getInt(1));
                scoreList.add(score);
                System.out.println("Score: " + score);
            } while (cursor.moveToNext());
        }
        return scoreList;
    }
}
