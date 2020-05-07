package jcu.cp3406.educationapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements ShakeDetector.Listener {
    public static String GAME_TYPE_PREF = "GamePref";
    private int scoreCount = 0;
    private int livesCount = 3;
    private String equationType;
    private String newEquation;
    private String mathType;
    private String result;
    private String answer;
    private MediaPlayer mp;
    TextView score;
    TextView equationTitle;
    TextView equation;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getIntent();

        //setup game visuals
        score = findViewById(R.id.score);
        equationTitle = findViewById(R.id.equation_title);
        equation = findViewById(R.id.equation);
        input = findViewById(R.id.answer);
        SharedPreferences gamePreference = getSharedPreferences(GAME_TYPE_PREF, MODE_PRIVATE);
        mathType = gamePreference.getString("GamePref", mathType);

        //start gesture monitor
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

        //get equation and play music
        equation();
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

    @Override
    public void hearShake() {
        //reset game values
        scoreCount = 0;
        livesCount = 3;
        String string = "Score: " + scoreCount + " Lives: " + livesCount;
        score.setText(string);
        input.setText("");
        Button shareButton = findViewById(R.id.shareButton);
        shareButton.setVisibility(View.INVISIBLE);
        equation();
    }

    public void onClickCheck(View view) {
        //check answer given
        if (livesCount != 0) {
            answer = String.valueOf(input.getText());
            checkAnswer();
            input.setText("");
        }
    }

    public void onClickShare(View view) {
        //redirect to twitter
        openTwitter();
    }

    public void checkAnswer() {
        //check answer against result and get next question
        if (result.equals(answer)) {
            resultSound("correct");
            updateScore();
            equation();
        } else {
            resultSound("incorrect");
            updateLives();
            if (livesCount != 0)
                equation();
        }
    }

    public void updateScore() {
        //update score
        scoreCount++;
        String string = "Score: " + scoreCount + " Lives: " + livesCount;
        score.setText(string);
    }

    public void updateLives() {
        //update lives
        livesCount--;
        String string = "Score: " + scoreCount + " Lives: " + livesCount;
        score.setText(string);
        if (livesCount == 0) {
            gameOver();
        }
    }

    public void gameOver() {
        //sets game over on 0 lives
        updateHighscore();
        String gameOver = "Game Over!";
        equationTitle.setText(gameOver);
        equation.setText("");

        //shows share button
        Button shareButton = findViewById(R.id.shareButton);
        shareButton.setVisibility(View.VISIBLE);
    }

    public void updateHighscore() {
        //insert new score to database
        HighscoreDatabaseHelper db = new HighscoreDatabaseHelper(this);
        db.addScore(new Score(scoreCount));
    }

    public void playMusic() {
        //play music on loop
        mp = MediaPlayer.create(this, R.raw.game_music);
        mp.setLooping(true);
        mp.start();
    }

    public void resultSound(String string) {
        //play result sound
        switch (string) {
            case "correct":
                mp = MediaPlayer.create(this, R.raw.correct);
                mp.start();
                break;
            case "incorrect":
                mp = MediaPlayer.create(this, R.raw.incorrect);
                mp.start();
                break;
        }
    }

    public void openTwitter() {
        //tweet to twitter
        String tweetScore = ("Well look at this, I made an app! This app is " +
                "available only to me, and this is a tweet example of sharing " +
                "game scores from the app. \n \nI just got a score of " + scoreCount +
                " on the QuizMe Maths app! You can't download it today!");
        String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                urlEncode(tweetScore), urlEncode(""));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
        startActivity(intent);
    }

    public String urlEncode(String s) {
        //ensure encoder is correct
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public void equation() {
        //calculate and display equations
        Random rand = new Random();
        int a, b, c, d, z;
        switch (mathType) {
            case "Addition and Subtraction Negative Numbers":
                a = rand.nextInt(20);
                b = rand.nextInt(20);
                z = rand.nextInt(6);
                if (z == 5) {
                    equationType = "Negative Addition";
                    newEquation = (-a + " + " + -b);
                    result = String.valueOf(-a + -b);
                } else if (z == 4) {
                    equationType = "Negative Addition";
                    newEquation = (a + " + " + -b);
                    result = String.valueOf(a + -b);
                } else if (z == 3){
                    equationType = "Negative Addition";
                    newEquation = (-a + " + " + b);
                    result = String.valueOf(-a + b);
                } else if (z == 2) {
                    equationType = "Negative Subtraction";
                    newEquation = (-a + " - " + -b);
                    result = String.valueOf(-a - -b);
                } else if (z == 1) {
                    equationType = "Negative Subtraction";
                    newEquation = (a + " - " + -b);
                    result = String.valueOf(a - -b);
                } else {
                    equationType = "Negative Subtraction";
                    newEquation = (-a + " - " + b);
                    result = String.valueOf(-a - b);
                }
                break;
            case "Multiplication and Division Negative Numbers":
                a = rand.nextInt(10);
                b = rand.nextInt(10);
                z = rand.nextInt(6);
                if (z == 5) {
                    equationType = "Negative Multiplication";
                    newEquation = (-a + " x " + -b);
                    result = String.valueOf(-a * -b);
                } else if (z == 4) {
                    equationType = "Negative Multiplication";
                    newEquation = (a + " x " + -b);
                    result = String.valueOf(a * -b);
                } else if (z == 3){
                    equationType = "Negative Multiplication";
                    newEquation = (-a + " x " + b);
                    result = String.valueOf(-a * b);
                } else if (z == 2) {
                    equationType = "Negative Division";
                    newEquation = (-a + " / " + -b);
                    result = String.valueOf(-a / -b);
                } else if (z == 1) {
                    equationType = "Negative Division";
                    newEquation = (a + " / " + -b);
                    result = String.valueOf(a / -b);
                } else {
                    equationType = "Negative Division";
                    newEquation = (-a + " / " + b);
                    result = String.valueOf(-a / b);
                }
                break;
            case "Area and Circumference of a Circle":
                a = rand.nextInt(10);
                z = rand.nextInt(2);
                if (z == 1) {
                    equationType = "Circle Area";
                    newEquation = ("A = π " + a + "^2");
                    result = String.valueOf(3.14*(a*a));
                } else {
                    equationType = "Circle Circumference";
                    newEquation = ("C = 2 π " + a);
                    result = String.valueOf(3.14*(2*a));
                }
                break;
            case "Volume and Surface Area":
                a = rand.nextInt(15);
                b = rand.nextInt(15);
                c = rand.nextInt(15);
                z = rand.nextInt(8);
                if (z == 7) {
                    equationType = "Volume";
                    newEquation = ("V = " + a + "^3");
                    result = String.valueOf(a*a*a);
                } else if (z == 6) {
                    equationType = "Volume";
                    newEquation = ("V = " + a + " x " + b + " x " + c);
                    result = String.valueOf(a*b*c);
                } else if (z == 5){
                    equationType = "Volume";
                    newEquation = ("V = " + a + " x " + b);
                    result = String.valueOf(a*b);
                } else if (z == 4) {
                    equationType = "Surface Area";
                    newEquation = ("A = " + a + "^2");
                    result = String.valueOf(a*a);
                } else if (z == 3) {
                    equationType = "Surface Area";
                    newEquation = ("A = " + a + " x " + b);
                    result = String.valueOf(a*b);
                } else if (z == 2) {
                    equationType = "Surface Area";
                    newEquation = ("A = " + 1/2 + " x " + a + " x " + b);
                    result = String.valueOf(.5*a*b);
                } else if (z == 1) {
                    equationType = "Surface Area";
                    newEquation = ("A = " + a + " x " + b);
                    result = String.valueOf(a*b);
                } else {
                    equationType = "Surface Area";
                    newEquation = ("A = " + a + " + " + b + " /2 " + c);
                    int e = a+b;
                    int f = (e/2)*c;
                    result = String.valueOf(f);
                }
                break;
            case "Square and Cube Root":
                z = rand.nextInt(2);
                if (z == 1) {
                    a = rand.nextInt(10);
                    equationType = "Square root";
                    newEquation = ("√" + a*a);
                    result = String.valueOf(a);
                } else {
                    a = rand.nextInt(7);
                    equationType = "Cube root";
                    newEquation = ("√" + (a*a)*a);
                    result = String.valueOf(a);
                }
                break;
            case "Linear Equations":
                a = rand.nextInt(10);
                b = rand.nextInt(10);
                d = rand.nextInt(6);
                z = rand.nextInt(2);
                if (z == 1) {
                    c = ((a*d)+b);
                    newEquation = (c + " = " + a + "x + " + b);
                    result = String.valueOf(d);
                } else {
                    c = (a+(b*d));
                    newEquation = (c + " = " + a + " + " + b + "x");
                    result = String.valueOf(d);
                }
                equationType = "Linear Equations";
                break;
            case "Pythagorean Theorem":
                a = rand.nextInt(10);
                b = rand.nextInt(10);
                equationType = "Pythagorean Theorem";
                newEquation = (a + "^2 + " + b + "^2 = √");
                result = String.valueOf((a*a)+(b*b));
                break;
        }
        equationTitle.setText(equationType);
        equation.setText(newEquation);
    }
}
