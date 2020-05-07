package jcu.cp3406.educationapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity {
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        getIntent();

        //gets, sorts and displays scores
        ListView highScoreList = findViewById(R.id.high_score);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.score);
        adapter.clear();
        highScoreList.setAdapter(adapter);
        HighscoreDatabaseHelper db = new HighscoreDatabaseHelper(this);
        List<Score> scoreList = db.getAllScores();
        List<String> highscoreList = new ArrayList<>();
        for (Score s : scoreList) {
            String string = "Score: " + s.getScore();
            highscoreList.add(s.getScore());
            System.out.println(string);
        }

        Collections.sort(highscoreList);
        Collections.reverse(highscoreList);
        adapter.addAll(highscoreList);

        //play music
        playMusic();
    }

    @Override
    public void onBackPressed() {
        //stop music when back button pressed
        mp.stop();
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        //on home button pressed
        mp.stop();
        super.onPause();
    }

    public void playMusic() {
        //start playing music on loop
        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.start();
    }
}
