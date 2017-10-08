package pcOp;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
public class PcMouse {
    private Dimension dimension;
    private Robot robot;
    private Point point;
    private final static String DIRECTION_UP = "up";
    private final static String DIRECTION_DOWN = "down";

    public PcMouse() {
        dimension = Toolkit.getDefaultToolkit().getScreenSize();    //获取屏幕尺寸
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动鼠标位置
     */
    public void move(int x,int y){
        point = MouseInfo.getPointerInfo().getLocation();
        point.x += x;
        point.y += y;
        robot.mouseMove(point.x,point.y);       /**移动鼠标到x,y位置*/
    }

    /**
     * 单击
     */
    public void singleClick(){
        point = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(point.x, point.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);      //按下鼠标上按键
        robot.mouseRelease(InputEvent.BUTTON1_MASK);    //释放鼠标上按键
    }

    /**
     * 双击
     */
    public void doubleClick(){
        point = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(point.x, point.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);      //按下鼠标左按键
        robot.mouseRelease(InputEvent.BUTTON1_MASK);    //释放鼠标左按键
        robot.delay(100);
        robot.mousePress(InputEvent.BUTTON1_MASK);      //再次按下鼠标左按键
        robot.mouseRelease(InputEvent.BUTTON1_MASK);    //释放鼠标左按键
    }

    /**
     * 右击
     */
    public void rightClick(){
        point = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(point.x, point.y);
        robot.mousePress(InputEvent.BUTTON3_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);

    }

    /**
     * 鼠标滚轮的滑动
     */
    public void wheel(String direction){
        if(direction.equals(DIRECTION_UP)) {
            robot.mouseWheel(-1);                       //滚轮上滑
        } else if(direction.equals(DIRECTION_DOWN)){
            robot.mouseWheel(1);                        //滚轮下滑
        }
    }

    public void direction(String key){
        switch (key){
            case "up" :
                robot.keyPress(KeyEvent.VK_UP);
                break;
            case "down" :
                robot.keyPress(KeyEvent.VK_DOWN);
                break;
            case "left" :
                robot.keyPress(KeyEvent.VK_LEFT);
                break;
            case "right" :
                robot.keyPress(KeyEvent.VK_RIGHT);
                break;
            case "home" :
                robot.keyPress(KeyEvent.VK_HOME);
                break;
            case "end" :
                robot.keyPress(KeyEvent.VK_END);
                break;
        }
    }

}
