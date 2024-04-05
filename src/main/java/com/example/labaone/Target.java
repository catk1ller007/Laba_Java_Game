package com.example.labaone;

import javafx.scene.shape.Circle;

public class Target {
    private Circle circle;
    private double layoutX;
    private double layoutY;

    public Target(Circle circle){
        this.circle = circle;
    }

    public double getRadius(){
        return circle.getRadius();
    }

    public double getLayoutX() {
        return circle.getLayoutX();
    }
    public double getLayoutY() {
        return circle.getLayoutY();
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
        circle.setLayoutX(layoutX);
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
        circle.setLayoutY(layoutY);
    }

    public void moveTargetY(int a){
        circle.setLayoutY(circle.getLayoutY() + a);
    }

    public void resetPosTarget(double layoutY){
        this.circle.setLayoutY(layoutY);
    }

}
