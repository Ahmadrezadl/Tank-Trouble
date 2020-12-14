package Server;

import java.io.Serializable;

public class PowerUp implements Serializable {
    public int x , y;
    public PowerUpType powerUpType;

    public PowerUp(int x , int y , PowerUpType powerUpType) {
        this.x = x;
        this.y = y;
        this.powerUpType = powerUpType;
    }
}
