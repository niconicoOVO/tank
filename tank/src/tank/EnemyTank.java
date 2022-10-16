package tank;

/**
 * @author nai0
 * @version 1.0
 * 敌人的坦克
 */
public class EnemyTank extends Tank implements Runnable {
    private int type = 1;

    public EnemyTank(int x, int y, int type) {
        super(x, y, type);
    }

    //自动移动
    @Override
    public void run() {
        while (true) {
            int step = (int) (Math.random() * 30);
            //根据坦克的方向移动
            switch (getDirect()) {
                case 0:
                    for (int i = 0; i < step; i++) {
                        moveUp();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < step; i++) {
                        moveRight();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < step; i++) {
                        moveDown();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case 3:
                    for (int i = 0; i < step; i++) {
                        moveLeft();
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;


            }

            setDirect((int) (Math.random() * 4));

            if (!isLive()) {
                break;
            }

        }
    }
}
