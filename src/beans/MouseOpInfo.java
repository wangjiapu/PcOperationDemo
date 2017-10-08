package beans;

import java.util.Map;

public class MouseOpInfo {
    private Map<Integer,Integer> map;
    private boolean click;
    private boolean singleClick;
    private boolean doubleClick;
    private boolean rightClick;



    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Integer> map) {
        this.map = map;
    }

/* public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }*/

    public boolean isSingleClick() {
        return singleClick;
    }

    public void setSingleClick(boolean singleClick) {
        this.singleClick = singleClick;
    }

    public boolean isDoubleClick() {
        return doubleClick;
    }

    public void setDoubleClick(boolean doubleClick) {
        this.doubleClick = doubleClick;
    }

    public boolean isRightClick() {
        return rightClick;
    }

    public void setRightClick(boolean rightClick) {
        this.rightClick = rightClick;
    }
}
