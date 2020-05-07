package jcu.cp3406.educationapp;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {
    public static String GAME_TYPE_PREF = "GamePref";
    private SharedPreferences gamePreference;
    private String mathType;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getIntent();

        //setup spinner
        Spinner spinner = findViewById(R.id.difficulty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        gamePreference = getSharedPreferences(GAME_TYPE_PREF, MODE_PRIVATE);

        //play music
        playMusic();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get spinner value and stop music when back arrow pressed
        Spinner spinner = findViewById(R.id.difficulty);
        mathType = spinner.getSelectedItem().toString();
        SharedPreferences.Editor editor = gamePreference.edit();
        editor.putString("GamePref", mathType);
        editor.apply();
        mp.stop();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //get spinner value and stop music when back button pressed
        Spinner spinner = findViewById(R.id.difficulty);
        mathType = spinner.getSelectedItem().toString();
        SharedPreferences.Editor editor = gamePreference.edit();
        editor.putString("GamePref", mathType);
        editor.apply();
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
