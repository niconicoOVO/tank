package tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Vector;

/**
 * @author nai0
 * @version 1.0
 * 坦克大战的游戏区域
 */

//为了画面不停的重画，要把MyPanel作为一个线程
public class MyPanel extends JPanel implements KeyListener, Runnable {

    MyTank myTank = null;//创建我的坦克
    Vector<Shot> allShots = new Vector<>();//所有射出去的子弹
    Vector<EnemyTank> enemyTanks = new Vector<>();//敌人的坦克

    int enemyTankSize = 3;//敌方坦克数量
    int enemyTankMax = 5;//敌方坦克最多的数量

    static int startX = 0;//背景起始位置x
    static int startY = 0;//背景起始位置y
    static int panelWidth = 1000;//背景宽度
    static int panelHeight = 750;//背景高度

    Vector<Bomb> bombs = new Vector<>();//存放炸弹
    Image image1 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/bomb_1.gif"));
    Image image2 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/bomb_2.gif"));
    Image image3 = Toolkit.getDefaultToolkit().getImage(MyPanel.class.getResource("/bomb_3.gif"));


    //初始化坦克
    public MyPanel() {
        //创建我的坦克
        myTank = new MyTank(200, 300, 0);

        //创建敌人的坦克
        for (int i = 0; i < enemyTankSize; i++) {
            //敌人坦克的位置
            EnemyTank enemyTank = new EnemyTank((i + 1) * 100 + 100, 100, 1);
            //敌人坦克的方向
            enemyTank.setDirect((int) (Math.random() * 4));
            //启动敌人坦克线程
            new Thread(enemyTank).start();
            enemyTanks.add(enemyTank);//放到敌人的坦克容器中
        }

    }

    //绘制：每次刷新都会从这里进入
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(startX, startY, panelWidth, panelHeight);//默认背景为黑色

        //绘制我的坦克
        if (null != myTank && myTank.isLive())
            paintTank(myTank, g);

        //绘制敌人的坦克
        for (EnemyTank enemyTank : enemyTanks) {
            //坦克还存活才能画
            if (null != enemyTank && enemyTank.isLive()) {
                paintTank(enemyTank, g);
                //坦克存在才能射击
                //当敌人的弹夹enemyTank.shots为0时才可以再次填充
                if (enemyTank.shots.size() <= 0) {
                    Shot shot = enemyTank.shotTank();
                    //添加到弹夹
                    enemyTank.shots.add(shot);
                    //添加到panel的allShots用于绘图
                    allShots.add(shot);
                }
            }

        }

        //绘制子弹
        paintShot(g);

        //如果bombs有对象，就画出
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            //根据life值画不同的图片
            if (bomb.life > 14) {
                g.drawImage(image1, bomb.x, bomb.y, 60, 60, this);
            } else if (bomb.life > 7) {
                g.drawImage(image2, bomb.x, bomb.y, 60, 60, this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60, 60, this);
            }
            //让这个炸弹的生命值减少
            bomb.lifeDown();
            //如果bomb 的life就从集合中删除
            if (bomb.life == 0) {
                bombs.remove(bomb);
            }
        }

    }

    //判断我是否敌人的坦克
    public void hit(Tank tank) {
        for (int i = 0; i < tank.shots.size(); i++) {
            Shot shot = tank.shots.get(i);
            if (null != shot && shot.isLive()) {
                //我的子弹还存活时
                //遍历敌人的坦克
                hitTank(shot, enemyTanks);
            }
        }
    }

    //判断是否击中敌方坦克
    public void hit(Vector<EnemyTank> tanks) {
        for (EnemyTank enemyTank : enemyTanks) {
            //遍历敌人的坦克
            if (null != enemyTank && enemyTank.isLive())
                //遍历敌人的坦克的弹夹
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    Shot shot = enemyTank.shots.get(j);
                    //如果子弹还存活
                    if (null != shot && shot.isLive()) {
                        hitTank(shot, myTank);
                    }
                }
        }
    }

    //判断是否击中我的坦克
    public boolean hitTank(Shot shot, Tank tank) {
        switch (tank.getDirect()) {
            //判断子弹和坦克的位置关系
            //并且坦克和子弹不是同一个属性的
            case 0:
            case 2:
                if (shot.getX() > tank.getX() && shot.getX() < tank.getX() + 40
                        && shot.getY() > tank.getY() && shot.getY() < tank.getY() + 60
                        && shot.getStance() != tank.getType()) {
                    //如果被击中
                    //被击中的这个子弹被销毁
                    shot.setLive(false);
                    //坦克被销毁
                    tank.setLive(false);
                    //爆炸特效
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                    return true;
                }
                break;
            case 1:
            case 3:
                if (shot.getX() > tank.getX() && shot.getX() < tank.getX() + 60
                        && shot.getY() > tank.getY() && shot.getY() < tank.getY() + 40
                        && shot.getStance() != tank.getType()) {
                    shot.setLive(false);
                    tank.setLive(false);
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                    return true;
                }
                break;
        }
        return false;
    }

    //判断是否击中敌方坦克
    public boolean hitTank(Shot shot, Vector<EnemyTank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            EnemyTank tank = tanks.get(i);
            if (hitTank(shot, tank)) {
                tanks.remove(tank);
                return true;
            }
        }
        return false;

    }


    public void paintTank(Tank tank, Graphics g) {
        if (null != tank && tank.isLive()) {
            drawTank(tank.getX(), tank.getY(), g, tank.getDirect(), tank.getType());//我的坦克

            //遍历坦克的弹夹
            for (int i = 0; i < tank.shots.size(); i++) {
                Shot shot = tank.shots.get(i);
                if (null == shot || !shot.isLive()) {
                    tank.shots.remove(shot);
                }
            }

        }
    }

    //绘制panel的子弹
    //只要还在allShots就能绘制
    public void paintShot(Graphics g) {
        //遍历allShots的弹夹
        for (int i = 0; i < allShots.size(); i++) {
            Shot shot = allShots.get(i);
            //子弹还存活才能画
            if (null != shot && shot.isLive()) {
                if (shot.getStance() == 0) g.setColor(Color.cyan);
                else g.setColor(Color.yellow);
                if (shot.isLive()) {
                    drawShot(shot.getX(), shot.getY(), g);
                } else allShots.remove(shot);
            }
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 / 120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //判断是否击中敌方坦克
            //遍历我的坦克的弹夹
            if (null != myTank && myTank.isLive())
                hit(myTank);

            //判断是否击中我的坦克
            if (null != myTank && myTank.isLive())
                hit(enemyTanks);

            this.repaint();
        }
    }

    /**
     * 画坦克
     *
     * @param x      坦克左上角横坐标
     * @param y      坦克右上角纵坐标
     * @param g      画笔
     * @param direct 坦克方向
     * @param type   坦克类型
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        //根据不同类型坦克，选择颜色
        switch (type) {
            case 0://我们的坦克
                g.setColor(Color.cyan);
                break;
            case 1://敌人的坦克
                g.setColor(Color.yellow);
                break;
        }
        //根据坦克方向，绘制坦克
        int tankX = x;
        int tankY = y;
        int wheelWidth = 10;
        int wheelHeight = 60;
        int bodyWidth = 20;
        int bodyHeight = 40;
        int r = bodyWidth;
        switch (direct) {
            //up
            case 0:
                //左边
                g.fill3DRect(tankX, tankY, wheelWidth, wheelHeight, false);
                //中间
                g.fill3DRect(tankX + wheelWidth, tankY + (wheelHeight - bodyHeight) / 2, bodyWidth, bodyHeight, false);
                //右边
                g.fill3DRect(tankX + wheelWidth + bodyWidth, tankY, wheelWidth, wheelHeight, false);
                //盖子
                g.fillOval(tankX + wheelWidth, tankY + (wheelHeight - bodyWidth) / 2, r, r);
                //炮管
                g.drawLine(tankX + wheelWidth + bodyWidth / 2, tankY + wheelHeight / 2, tankX + wheelWidth + bodyWidth / 2, tankY);
                break;

            //right
            case 1:
                //左边
                g.fill3DRect(tankX, tankY, wheelHeight, wheelWidth, false);
                //中间
                g.fill3DRect(tankX + wheelWidth, tankY + (wheelHeight - bodyHeight) / 2,
                        bodyHeight, bodyWidth, false);
                //右边
                g.fill3DRect(tankX, tankY + wheelWidth + bodyWidth, wheelHeight, wheelWidth, false);
                //盖子
                g.fillOval(tankX + (wheelHeight - bodyWidth) / 2, tankY + wheelWidth, r, r);
                //炮管
                g.drawLine(tankX + wheelHeight / 2, tankY + wheelWidth + bodyWidth / 2,
                        tankX + wheelHeight, tankY + wheelWidth + bodyWidth / 2);
                break;

            //down
            case 2:
                //左边
                g.fill3DRect(tankX, tankY, wheelWidth, wheelHeight, false);
                //中间
                g.fill3DRect(tankX + wheelWidth, tankY + (wheelHeight - bodyHeight) / 2, bodyWidth, bodyHeight, false);
                //右边
                g.fill3DRect(tankX + wheelWidth + bodyWidth, tankY, wheelWidth, wheelHeight, false);
                //盖子
                g.fillOval(tankX + wheelWidth, tankY + (wheelHeight - bodyWidth) / 2, r, r);
                //炮管
                g.drawLine(tankX + wheelWidth + bodyWidth / 2, tankY + wheelHeight / 2, tankX + wheelWidth + bodyWidth / 2, tankY + wheelHeight);
                break;

            //left
            case 3:
                //左边
                g.fill3DRect(tankX, tankY, wheelHeight, wheelWidth, false);
                //中间
                g.fill3DRect(tankX + wheelWidth, tankY + (wheelHeight - bodyHeight) / 2, bodyHeight, bodyWidth, false);
                //右边
                g.fill3DRect(tankX, tankY + wheelWidth + bodyWidth, wheelHeight, wheelWidth, false);
                //盖子
                g.fillOval(tankX + (wheelHeight - bodyWidth) / 2, tankY + wheelWidth, r, r);
                //炮管
                g.drawLine(tankX + wheelHeight / 2, tankY + wheelWidth + bodyWidth / 2, tankX, tankY + wheelWidth + bodyWidth / 2);
                break;
        }

    }

    //画子弹
    public void drawShot(int x, int y, Graphics g) {
        g.draw3DRect(x, y, 1, 1, false);

    }

    //有字符输出时就会触发
    @Override
    public void keyTyped(KeyEvent e) {

    }

    //某个键松开就会触发
    @Override
    public void keyReleased(KeyEvent e) {
    }

    //某个键按下就会触发
    //为了防止误触，第一次按下后会有500ms的延迟才会连续按
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_H) {
            //增加敌方坦克
            if (enemyTanks.size() < enemyTankMax) {
                EnemyTank enemyTank = new EnemyTank((int) (Math.random() * panelWidth + startX - 40),
                        (int) (Math.random() * panelHeight + startY - 60), 1);
                new Thread(enemyTank).start();
                enemyTanks.add(enemyTank);
                //重绘
                this.repaint();
            }
        }

        //如果myTank被销毁 直接return
        if (!(null != myTank && myTank.isLive())) {
            if (e.getKeyCode() == KeyEvent.VK_K)
                myTank = new MyTank(200, 300, 0);
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            myTank.setDirect(0);
            myTank.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            myTank.setDirect(1);
            myTank.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            myTank.setDirect(2);
            myTank.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            myTank.setDirect(3);
            myTank.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_J) {
            //每按一次就会重新启动一个子弹线程
            Shot shot = myTank.shotTank();
            myTank.shots.add(shot);
            allShots.add(shot);
        }
        //重绘
        this.repaint();
    }

}

