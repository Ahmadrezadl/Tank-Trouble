package Server;

import java.io.Serializable;

public class Wall implements Serializable {
    public int hp;
    public double x1,x2,y1,y2;
    private boolean breakable;

    public Wall(double x1 , double x2 , double y1 , double y2, boolean breakable,int hp) {
        this.x1 = Math.min(x1 , x2);
        this.x2 = Math.max(x1 , x2);
        this.y1 = Math.min(y1,y2);
        this.y2 = Math.max(y1,y2);
        this.breakable = breakable;
        this.hp = hp;
    }

    public boolean isBreakable() {
        return breakable;
    }
}
