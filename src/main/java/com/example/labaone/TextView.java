package com.example.labaone;

import javafx.scene.control.Label;

public class TextView {
    private Label shoot;
    private Label shootScore;

    public TextView(Label shoot, Label shootScore){
        this.shoot = shoot;
        this.shootScore = shootScore;
    }

    public void addShot(int numShot){
        shoot.setText(Integer.toString(numShot));
    }
    public void addShootScore(int numHits){
        shootScore.setText(Integer.toString(numHits));
    }

    public void resetScore(int numShot, int numHits){
        shoot.setText(Integer.toString(numShot));
        shootScore.setText(Integer.toString(numHits));
    }


}
