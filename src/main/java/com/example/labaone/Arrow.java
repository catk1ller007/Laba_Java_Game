package com.example.labaone;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Arrow {
    private Polygon arrow;
    private double layoutX;
    private double layoutY;

    public Arrow(Polygon arrow, double layoutX, double layoutY) {
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.arrow = arrow;
    }
    public void move(int a){
        layoutX += a;
        arrow.setLayoutX(layoutX);
    }

    public double getLayoutX() {
        return layoutX;
    }

    public double getLayoutY() {
        return layoutY;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
        arrow.setLayoutX(layoutX);
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
        arrow.setLayoutY(layoutY);
    }

    public void resetPosArrow(double x, double y, Arrow arrow){
        arrow.setLayoutX(x);
        arrow.setLayoutY(y);
    }
}
