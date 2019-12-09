package Enemies;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import sample.GameObject;

public class Tank extends GameObject {

    ImageView Tank;
    int i, j;
    int x;
    int y;
    Image img;
    Image gunImg;
    int gunRotation;
    double speed;
    Direction direction;
    int health;
    double reward;

    enum Direction {
        LEFT(180), UP(270), RIGHT(0), DOWN(90);

        int degree;

        Direction(int i) {
            degree = i;
        }

        int getDegree() {
            return degree;
        }
    }

    public Tank createTank() {
        Tank tank = new Tank();
        tank.i = 0;
        tank.j = 6;
        tank.x = tank.i * 128 + 64;
        tank.y = tank.j * 128;
        tank.speed = 5;
        tank.direction = Direction.UP;
        tank.img = new Image("file:Source/Enemies/towerDefense_tile268.png");
        tank.gunImg = new Image("file:Source/Enemies/towerDefense_tile291.png");
        return tank;
    }

    protected static sample.GameObject.Point[] wayPoints;
    int wayPointIndex = 0;

    public GameObject.Point getNextWayPoint() {
        if (wayPointIndex < sample.GameObject.wayPoints.length - 1)
            return GameObject.wayPoints[++wayPointIndex];
        return null;
    }
    void render(GraphicsContext gc) {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        ImageView iv = new ImageView(img);
        iv.setRotate(this.direction.getDegree());
        Image base = iv.snapshot(params, null);

        ImageView iv2 = new ImageView(gunImg);
        iv2.setRotate(this.direction.getDegree());
        Image gun = iv2.snapshot(params, null);

        gc.drawImage(base, x, y);
        gc.drawImage(gun, x, y);

        gc.setFill(Color.RED);
        gc.fillOval(GameObject.wayPoints[wayPointIndex].x, GameObject.wayPoints[wayPointIndex].y, 10, 10);

        gc.setFill(Color.BLUE);
        gc.fillOval(x, y, 10, 10);
    }
    void calculateDirection() {
        if (wayPointIndex >= GameObject.wayPoints.length) {
            return;
        }
        Point currentWP = GameObject.wayPoints[wayPointIndex];
        if (GameObject.distance(x, y, currentWP.x, currentWP.y) <= speed) {
            x = (int) currentWP.x;
            y = (int) currentWP.y;
            Point nextWayPoint = getNextWayPoint();
            if (nextWayPoint == null) return;
            double deltaX = nextWayPoint.x - x;
            double deltaY = nextWayPoint.y - y;
            if (deltaX > speed) direction = Direction.RIGHT;
            else if (deltaX < -speed) direction = Direction.LEFT;
            else if (deltaY > speed) direction = Direction.DOWN;
            else if (deltaY <= -speed) direction = Direction.UP;
        }
    }
    void update() {

        calculateDirection();

        switch (direction) {
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
        }
    }
}