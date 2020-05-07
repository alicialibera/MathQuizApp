package jcu.cp3406.educationapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LandingActivity extends AppCompatActivity {
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    @Override
    public void onStart() {
        //play music on start
        playMusic();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        //stop music when back button pressed
        mp.stop();
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        mp.stop();
        super.onPause();
    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        mp.stop();
        startActivity(intent);
    }

    public void onClickHighScore(View view) {
        Intent intent = new Intent(this, HighscoreActivity.class);
        mp.stop();
        startActivity(intent);
    }

    public void onClickPlay(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        mp.stop();
        startActivity(intent);
    }

    public void playMusic() {
        //start playing music on loop
        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.start();
    }
}
