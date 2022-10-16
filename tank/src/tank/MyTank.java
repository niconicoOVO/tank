package tank;

/**
 * @author nai0
 * @version 1.0
 * 自己的坦克
 */
public class MyTank extends Tank implements Runnable {
    private int type = 0;

    public MyTank(int x, int y, int type) {
        super(x, y, type);
    }

    @Override
    public void run() {
        while (true) {

            if (!isLive())
                break;
        }
    }

}
