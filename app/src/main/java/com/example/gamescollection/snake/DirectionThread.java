package com.example.gamescollection.snake;

public class DirectionThread extends Thread{

    private com.example.gamescollection.snake.GamePanel mv;
    private int direction;
    public boolean canRun;

    public DirectionThread(com.example.gamescollection.snake.GamePanel mv, int direction){
        this.mv=mv;
        this.direction=direction;
    }

    @Override
    public void run() {
        while (canRun) {
            mv.setDirection(direction);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
