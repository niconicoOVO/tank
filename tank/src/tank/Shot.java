package tank;

public class Shot implements Runnable {

    //子弹销毁：打到敌人、到边界

    private int x;
    private int y;
    private int direct;
    private int speed = 3;
    private boolean isLive = true;
    private int stance;

    //shotEnemyTank()先创建一个shot对象再启动线程
    public Shot(int x, int y, int direct, int stance) {
        this.x = x;
        this.y = y;
        this.direct = direct;
        this.stance = stance;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 / 60);//每隔1000/30ms改变一次坐标
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //根据方向改变子弹坐标
            switch (direct) {
                case 0://up
                    y -= speed;
                    break;
                case 1://right
                    x += speed;
                    break;
                case 2://down
                    y += speed;
                    break;
                case 3://left
                    x -= speed;
                    break;
            }
            //碰到边界 或者 销毁
            if (!(x >= 0 && x <= 1000 && y >= 0 && y <= 750 && isLive)) {
                isLive = false;//碰到边界
                break;
            }
        }
    }


    public int getStance() {
        return stance;
    }

    public void setStance(int stance) {
        this.stance = stance;
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

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}
