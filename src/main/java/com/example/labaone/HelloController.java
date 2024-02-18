package com.example.labaone;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;


public class HelloController {
    Thread gameThread;
    final public int  BACK_LINE = 330;
    final public int FRONT_LINE = 0;
    final public int IS_ON_PANE = 600;

    public int orientationBig = 1;
    public int orientationSmall = 1;
    public int scoreForShoot = 0;
    public  int scoreForHit = 0;

    private boolean isPaused = false;
    private boolean isShoot = false;

    @FXML
    public Circle Big_Circle;
    @FXML
    public Polygon Strela;
    @FXML
    public Circle Small_Circle;

    @FXML
    public void PauseGame() {
        isPaused = true;
    }
    @FXML
    public void ReplaceGame() {
        isPaused = false;
    }

    @FXML
    public Label shootScore;
    @FXML
    public Label scorePoints;
    @FXML
    public void Shoot() {
        if (!isShoot) {
            isShoot = true;
            scoreForShoot += 1;
            shootScore.setText(Integer.toString(scoreForShoot));
        }
    }

    public int orientCircle(int orent, Circle circle, int BL, int Fl){
        if(orent == 1 && circle.getLayoutY() >= BL){
            orent = -1;
        } else if (orent == -1 && circle.getLayoutY() <= Fl) {
            orent = 1;
        }
        return orent;
    }

    private void checkHit(double arrowX, double arrowY, double targetCenterX, double targetCenterY, double targetRadius, int points) {
        // Проверяем, попала ли стрела в мишень
        if (isHit(arrowX, arrowY, targetCenterX, targetCenterY, targetRadius)) {
            System.out.println("Попала в мишень!");
            scoreForHit += points;
            scorePoints.setText(Integer.toString(scoreForHit));
            Strela.setLayoutX(74);
            Strela.setLayoutY(146);
            isShoot = false;
        }
    }

    private boolean isHit(double arrowX, double arrowY, double targetCenterX, double targetCenterY, double targetRadius) {
        double distance = Math.sqrt(Math.pow(arrowX - targetCenterX, 2) + Math.pow(arrowY - targetCenterY, 2));

        return distance <= targetRadius;
    }
    @FXML
    public void StartGame(){
        if(gameThread != null) return;

        gameThread = new Thread(() -> {
                while (true) {
                    Platform.runLater(()->{

                            orientationBig = orientCircle(orientationBig, Big_Circle, BACK_LINE, FRONT_LINE);
                            orientationSmall = orientCircle(orientationSmall, Small_Circle, BACK_LINE, FRONT_LINE);

                            if (isShoot) {

                                if (Strela.getLayoutX() >= IS_ON_PANE) {
                                    Strela.setLayoutX(74);
                                    Strela.setLayoutY(146);
                                    isShoot = false;
                                } else if (!isPaused) {
                                    Strela.setLayoutX(Strela.getLayoutX() + 5);

                                    checkHit(Strela.getLayoutX(), Strela.getLayoutY(), Big_Circle.getLayoutX() - Big_Circle.getRadius(),
                                            Big_Circle.getLayoutY() - Big_Circle.getRadius(), Big_Circle.getRadius(), 1);
                                    checkHit(Strela.getLayoutX(), Strela.getLayoutY(), Small_Circle.getLayoutX() - Small_Circle.getRadius(),
                                            Small_Circle.getLayoutY() - Small_Circle.getRadius(), Small_Circle.getRadius(), 2);
                                }
                            }

                            if (!isPaused) {
                                Big_Circle.setLayoutY(Big_Circle.getLayoutY() + (2 * orientationBig));
                                Small_Circle.setLayoutY(Small_Circle.getLayoutY() + (4 * orientationSmall));
                            }
                        });

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }
}

//    // Метод для определения пересечения двух прямоугольников
//    private boolean isHit(Node node1, Node node2) {
//        Bounds bounds1 = node1.localToScene(node1.getBoundsInLocal());
//        Bounds bounds2 = node2.localToScene(node2.getBoundsInLocal());
//        return bounds1.intersects(bounds2);
//    }
