package com.example.labaone;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;


public class HelloController {
    Thread gameThread;
    final public int  BACK_LINE = 325;
    final public int FRONT_LINE = 20;
    final public int IS_ON_PANE = 550;

    public int orientationBig = 1;
    public int orientationSmall = 1;
    public int scoreForShoot = 0;
    public  int scoreForHit = 0;

    private boolean isPaused = false;
    private boolean isShoot = false;
    @FXML
    public Polygon Strela;
    @FXML
    public Circle Big_Circle;
    @FXML
    public Circle Small_Circle;

    public Arrow arrow;

    @FXML
    public void initialize() {
        arrow = new Arrow(Strela, Strela.getLayoutX(), Strela.getLayoutY());
    }
//    @FXML
//    public void PauseGame() {
//        isPaused = !isPaused;
//    }
    @FXML
    public void PauseGame() {
        synchronized (lock) {
            if (isPaused) {
                isPaused = false;
                lock.notify(); // Уведомление о продолжении выполнения
            } else {
                isPaused = true;
            }
        }
    }

    @FXML
    public Label shootScore;
    @FXML
    public Label scorePoints;
    private final Object lock = new Object();
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
        if (HitTheTarget(arrowX, arrowY, targetCenterX, targetCenterY, targetRadius)) {
            System.out.println("Попала в мишень!");
            scoreForHit += points;
            scorePoints.setText(Integer.toString(scoreForHit));
            arrow.setLayoutX(74);
            arrow.setLayoutY(146);
            System.out.println(arrow.getLayoutY() + " 1");
            isShoot = false;
        }
    }

    private  void resetPos(double x, double y, Arrow s){
        s.setLayoutX(x);
        s.setLayoutY(y);
    }
    private void checkShoot() {
        if (isShoot) {
            if (arrow.getLayoutX() >= IS_ON_PANE) {
                resetPos(74, 146, arrow);
                isShoot = false;
            } else if (!isPaused) {
                arrow.move(5);
                checkHit(arrow.getLayoutX(), arrow.getLayoutY(), Big_Circle.getLayoutX() - Big_Circle.getRadius(), Big_Circle.getLayoutY() - Big_Circle.getRadius(), Big_Circle.getRadius(), 1);
                checkHit(arrow.getLayoutX(), arrow.getLayoutY(), Small_Circle.getLayoutX() - Small_Circle.getRadius(), Small_Circle.getLayoutY() - Small_Circle.getRadius(), Small_Circle.getRadius(), 2);
            }
        }
    }

    private boolean HitTheTarget(double arrowX, double arrowY, double targetCenterX, double targetCenterY, double targetRadius) {
        double distance = Math.sqrt(Math.pow(arrowX - targetCenterX, 2) + Math.pow(arrowY - targetCenterY, 2));

        return distance <= targetRadius;
    }
    @FXML
    public void StartGame(){
        if(gameThread != null && gameThread.isAlive()) {
            StopGame();
            return;
        }

        gameThread = new Thread(() -> {

                while (!Thread.currentThread().isInterrupted()) {

                    synchronized (lock) {
                        while (isPaused) {
                            try {
                                lock.wait(); // Приостановка выполнения потока
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.out.println(e);
                            }
                        }
                    }

                    System.out.println("Старт");
                    Platform.runLater(()->{

                            orientationBig = orientCircle(orientationBig, Big_Circle, BACK_LINE, FRONT_LINE + 10);
                            orientationSmall = orientCircle(orientationSmall, Small_Circle,BACK_LINE + 10, FRONT_LINE);

                            checkShoot();

                                Big_Circle.setLayoutY(Big_Circle.getLayoutY() + (2 * orientationBig));
                                Small_Circle.setLayoutY(Small_Circle.getLayoutY() + (4 * orientationSmall));



                        });

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println(e);
                    }
                }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }


    @FXML
    public void StopGame(){
        gameThread.interrupt();

        try {
            gameThread.join(); // Дожидаемся завершения потока
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        if (!gameThread.isAlive()){
            Platform.runLater(()->{
                Big_Circle.setLayoutY(177);
                Small_Circle.setLayoutY(177);
                scoreForShoot = 0;
                shootScore.setText(Integer.toString(0));
                scorePoints.setText(Integer.toString(0));
                resetPos(74, 146, arrow);
                isShoot = false;
            });
        }
    }
}
