package tank;

import javax.swing.*;
import java.awt.*;

/**
 * @author nai0
 * @version 1.0
 */
public class MyJFrame extends JFrame {

    //定义MyPanel
    MyPanel mp;

    public static void main(String[] args) {
        new MyJFrame();
    }

    public MyJFrame() {

        mp = new MyPanel();
        Thread thread = new Thread(mp);
        thread.start();//启动画板线程
        this.add(mp);//把mp添加到容器里面
        this.addKeyListener(mp);//监听mp
        this.setSize(mp.panelWidth + mp.startX + 15, mp.panelHeight + mp.startY + 35);//打开的界面大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击关闭按钮直接关闭应用程序
        this.setVisible(true);//设置可见
    }


}
