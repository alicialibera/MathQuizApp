package jcu.cp3406.educationapp;

class Score {
    private int _score;

    Score(){   }
    Score(int score){
        this._score = score;
    }

    String getScore(){
        return String.valueOf(this._score);
    }

    void setScore(int score){
        this._score = score;
    }
}