package tank;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.Vector;

/**
 * @author nai0
 * @version 1.0
 * 坦克
 */
public class Tank {
    private int x;//坦克的横坐标
    private int y;//坦克的纵坐标
    private int speed = 3;//坦克速度
    private int direct = 0;//坦克的方向
    private int type = 0;//坦克的类型 0自己 1敌人

    private int wheelWidth = 10;//坦克轮胎的宽度
    private int wheelHeight = 60;//坦克轮胎的长度
    private int bodyWidth = 20;//坦克身体的宽度
    private int bodyHeight = 40;//坦克身体的长度
    private int r = bodyWidth;//盖子的直径

    private boolean isLive = true;
    Vector<Shot> shots = new Vector<>();//存放子弹

    public void moveUp() {
        if (!(getY() - getSpeed() >= MyPanel.startY)) {
            setY(MyPanel.startY);
            return;
        }
        setY(getY() - getSpeed());

    }

    public void moveRight() {
        if (!(getX() + getSpeed() + getWheelHeight() <= MyPanel.startX + MyPanel.panelWidth)) {
            setX(MyPanel.startX + MyPanel.panelWidth - getWheelHeight());
            return;
        }
        setX(getX() + getSpeed());

    }

    public void moveDown() {
        if (!(getY() + getSpeed() + getWheelHeight() <= MyPanel.startY + MyPanel.panelHeight)) {
            setY(MyPanel.startY + MyPanel.panelHeight - getWheelHeight());
            return;
        }
        setY(getY() + getSpeed());

    }

    public void moveLeft() {
        if (!(getX() - getSpeed() >= MyPanel.startX)) {
            setX(MyPanel.startX);
            return;
        }
        setX(getX() - getSpeed());

    }

    public Shot shotTank() {
        Shot shot = null;
        switch (getDirect()) {
            case 0:
                shot = new Shot(getX() + 20, getY(), 0, getType());
                break;
            case 1:
                shot = new Shot(getX() + 60, getY() + 20, 1, getType());
                break;
            case 2:
                shot = new Shot(getX() + 20, getY() + 60, 2, getType());
                break;
            case 3:
                shot = new Shot(getX(), getY() + 20, 3, getType());
                break;
        }
        new Thread(shot).start();
        return shot;


    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWheelWidth() {
        return wheelWidth;
    }

    public void setWheelWidth(int wheelWidth) {
        this.wheelWidth = wheelWidth;
    }

    public int getWheelHeight() {
        return wheelHeight;
    }

    public void setWheelHeight(int wheelHeight) {
        this.wheelHeight = wheelHeight;
    }

    public int getBodyWidth() {
        return bodyWidth;
    }

    public void setBodyWidth(int bodyWidth) {
        this.bodyWidth = bodyWidth;
    }

    public int getBodyHeight() {
        return bodyHeight;
    }

    public void setBodyHeight(int bodyHeight) {
        this.bodyHeight = bodyHeight;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }


}
