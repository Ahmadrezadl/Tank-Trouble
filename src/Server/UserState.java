package Server;

import Graphic.KeyboardState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class UserState implements Serializable {
    private User user = null;
    private KeyboardState pressedKeys;
    private int hp;
    private double x , y;
    private int angle;
    private boolean dead;
    private boolean shooted = false;
    private int kills = 0;
    private int deaths = 0;
    private int reload = 0;
    private int shield;

    //AI
    public UserState(int hp)
    {
        pressedKeys = new KeyboardState();
        pressedKeys.random();
        x = 20;
        y = 20;
        angle = 0;
        dead = false;
        this.hp = hp;
        kills = 0;
        shield = 0;
        deaths = 0;
    }

    public UserState(User user,int hp) {
        this.user = user;
        pressedKeys = null;
        x = 20;
        y = 20;
        angle = 0;
        dead = false;
        this.hp = hp;
        kills = 0;
        deaths = 0;
    }

    /*
    returns is dead?
     */
    public boolean doDamage(int damage)
    {
        hp -= damage;
        return hp <= 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public KeyboardState getPressedKeys() {
        return pressedKeys;
    }

    public void setPressedKeys(KeyboardState pressedKeys) {
        this.pressedKeys = pressedKeys;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void move(Game game,int damage) {
        if(pressedKeys == null || isDead()) return;
        int z = pressedKeys.isKeyUP()?1:pressedKeys.isKeyDOWN()?-1:0;

        double beforeX = x;
        double beforeY = y;

        x += z * Math.cos(Math.toRadians(angle));
        y += z * Math.sin(Math.toRadians(angle));

        for(Wall wall : game.walls)
        {
            if(wall.x1 == wall.x2)
            {
                if(Math.abs(wall.x1 - (x+20)) < 30 && y <= wall.y2+7 && y >= wall.y1 - 7)
                {
                    x = beforeX;
                }
            }
            if(wall.y1 == wall.y2)
            {
                if(Math.abs(wall.y1 - (y+20)) < 30 && x <= wall.x2+7 && x >= wall.x1 -7 )
                {
                    y = beforeY;
                }
            }
        }
        if(shield > 0) shield--;
        for(PowerUp powerUp : game.powerUps)
        {
            if(Math.sqrt(Math.pow((powerUp.x) + 12 -(x+20),2) + Math.pow((powerUp.y + 12) -(y+20),2)) < 20 )
            {
                if(powerUp.powerUpType == PowerUpType.shield)
                {
                    shield = 1500;
                }
                if(powerUp.powerUpType == PowerUpType.heal)
                {
                    hp += hp /10;
                }
                if(powerUp.powerUpType == PowerUpType.damage2)
                {
                    damage *= 2;
                }
                if(powerUp.powerUpType == PowerUpType.damage3)
                {
                    damage *= 3;
                }
                game.powerUps.remove(powerUp);
                break;
            }
        }

        if(pressedKeys.isKeyRIGHT())
        {
            angle++;
            if(angle == 361)
                angle = 1;
        }
        if(pressedKeys.isKeyLEFT())
        {
            angle--;
            if(angle == -1)
                angle = 359;
        }
        if(reload > 0)
        reload--;
        if(pressedKeys.isKeySPACE())
        {
            shooted = true;
        }
        if(!pressedKeys.isKeySPACE() && shooted)
        {
            shooted = false;
            if(reload <=50) {
                game.getBullets().add(new Bullet(x + 21 , y + 23 , 2 * Math.cos(Math.toRadians(angle)) , 2 * Math.sin(Math.toRadians(angle)) , damage , this));
                reload += 50;
            }
        }
        if(user == null)
        {
            if(System.currentTimeMillis() % 100 == 0)
            {
                game.getBullets().add(new Bullet(x+21,y+23,2*Math.cos(Math.toRadians(angle)),2*Math.sin(Math.toRadians(angle)),damage,this));
            }
        }
    }

    public int getHP() {
        return hp;
    }

    public void killed() {
        kills++;
    }

    public void dead() {
        deaths++;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public boolean hasShield() {
        return shield > 0;
    }
}
