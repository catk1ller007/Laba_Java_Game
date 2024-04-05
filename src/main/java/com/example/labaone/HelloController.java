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
    public int numberOfShots = 0;
    public  int numberOfHits = 0;

    private boolean isPaused = false;
    private boolean isShoot = false;
    @FXML
    public Polygon Strela;
    @FXML
    public Circle Big_Circle;
    @FXML
    public Circle Small_Circle;
    public Arrow arrow;
    public Target BigTarget;
    public Target SmalTarget;
    public TextView Shoot;

    @FXML
    public void initialize() {
        arrow = new Arrow(Strela, Strela.getLayoutX(), Strela.getLayoutY());
        BigTarget = new Target(Big_Circle);
        SmalTarget = new Target(Small_Circle);
        Shoot = new TextView(shoots, shootsPoints);
    }
    @FXML
    public Label shoots;
    @FXML
    public Label shootsPoints;
    private final Object lock = new Object();

    @FXML
    public void PauseGame() {
        isPaused = !isPaused;
    }
    @FXML
    public void Shoot() {
        if (gameThread !=null){
            if (isPaused){
                numberOfHits = numberOfShots;
            }
            else {
                if (!isShoot) {
                    isShoot = true;
                    numberOfShots += 1;
                    Shoot.addShot(numberOfShots);
                    //shootScore.setText(Integer.toString(scoreForShoot));
                }
            }
        }
    }

    public int orientCircle(int orent, Target circle, int BL, int Fl){
        if(circle.getLayoutY() >= BL){
            orent = -1;
        } else if (circle.getLayoutY() <= Fl) {
            orent = 1;
        }
        return orent;
    }

//    private void checkHit(double arrowX, double arrowY, double targetCenterX, double targetCenterY, double targetRadius, int points) {
//        // Проверяем, попала ли стрела в мишень
//        if (HitTheTarget(arrowX, arrowY, targetCenterX, targetCenterY, targetRadius)) {
//            scoreForHit += points;
//            scorePoints.setText(Integer.toString(scoreForHit));
//            arrow.resetPosArrow(74, 146, arrow);
//            isShoot = false;
//        }
//    }

    private void checkShoot() {
        if (isShoot) {
            if (arrow.getLayoutX() >= IS_ON_PANE) {
                arrow.resetPosArrow(74, 146, arrow);
                isShoot = false;
            } else if (!isPaused) {
                arrow.move(5);

                if(HitTheTarget(BigTarget, arrow)){
                    numberOfHits += 1;
                    Shoot.addShootScore(numberOfHits);
                    //scorePoints.setText(Integer.toString(scoreForHit));
                    arrow.resetPosArrow(74, 146, arrow);
                    isShoot = false;
                }
                else if(HitTheTarget(SmalTarget, arrow)){
                    numberOfHits += 2;
                    Shoot.addShootScore(numberOfHits);
                    //scorePoints.setText(Integer.toString(scoreForHit));
                    arrow.resetPosArrow(74, 146, arrow);
                    isShoot = false;
                }
            }
        }
    }

    private boolean HitTheTarget(Target target, Arrow polygon) {
        double arrowX = polygon.getLayoutX();
        double arrowY = polygon.getLayoutY();
        double targetCenterX = target.getLayoutX() - target.getRadius();
        double targetCenterY = target.getLayoutY() - target.getRadius();
        double targetRadius = target.getRadius();

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
                    if (isPaused){
                        continue;
                    }

                    System.out.println("Start");
                    Platform.runLater(()->{

                            orientationBig = orientCircle(orientationBig, BigTarget, BACK_LINE, FRONT_LINE + 10);
                            orientationSmall = orientCircle(orientationSmall, SmalTarget,BACK_LINE + 10, FRONT_LINE);

                            checkShoot();
                            BigTarget.moveTargetY(2 * orientationBig);
                            SmalTarget.moveTargetY(4 * orientationSmall);

                        });

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }

    @FXML
    public void StopGame(){
        if (gameThread != null) {
            gameThread.interrupt();
            try {
                gameThread.join(); // Дожидаемся завершения потока
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if (!gameThread.isAlive()) {
                Platform.runLater(() -> {
                    BigTarget.resetPosTarget(177);
                    SmalTarget.resetPosTarget(177);

                    numberOfShots = 0;
                    numberOfHits = 0;
                    Shoot.resetScore(numberOfShots, numberOfHits);
                    arrow.resetPosArrow(74, 146, arrow);
                    isShoot = false;
                });
            }
        }
    }
}
