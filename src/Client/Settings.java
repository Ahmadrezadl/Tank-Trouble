package Client;

import java.io.Serializable;

public class Settings implements Serializable {
    private int tankDamage;
    private int wallHP;
    private int tankHP;

    public Settings(int tankDamage, int wallHP , int tankHP) {
        this.tankDamage = tankDamage;
        this.wallHP = wallHP;
        this.tankHP = tankHP;
    }

    public int getTankDamage() {
        return tankDamage;
    }

    public void setTankDamage(int tankDamage) {
        this.tankDamage = tankDamage;
    }

    public int getWallHP() {
        return wallHP;
    }

    public void setWallHP(int wallHP) {
        this.wallHP = wallHP;
    }

    public int getTankHP() {
        return tankHP;
    }

    public void setTankHP(int tankHP) {
        this.tankHP = tankHP;
    }
}
