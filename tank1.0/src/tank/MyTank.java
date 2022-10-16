package tank;

/**
 * @author nai0
 * @version 1.0
 * 自己的坦克
 */
public class MyTank extends Tank {
    private int type = 0;

    public MyTank(int x, int y) {
        super(x, y);
    }

    @Override
    public int getType() {
        return type;
    }


}
