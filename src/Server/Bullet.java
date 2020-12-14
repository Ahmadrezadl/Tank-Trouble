package Server;

import java.io.Serializable;
import java.util.ArrayList;

public class Bullet implements Serializable {
    private double x , y;
    private long createdTime;
    private double speedX;
    private double speedY;
    private int damage;
    private int antiSpawnKill;
    private UserState owner;
    private boolean alive;

    public Bullet(double x , double y , double speedX , double speedY , int damage , UserState userState) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.createdTime = System.currentTimeMillis();
        this.damage = damage;
        alive = true;
        antiSpawnKill = 50;
        owner = userState;
    }

    public void move(Game game)
    {
        x += speedX;
        y += speedY;
        if(antiSpawnKill > 0)
        antiSpawnKill--;
        for(Wall wall : game.walls)
        {
            if(wall.x1 == wall.x2)
            {
                if(Math.abs(wall.x1 - x) < 5 && y <= wall.y2 && y >= wall.y1)
                {
                    speedX = -speedX;
                    antiSpawnKill = 0;
                    if(wall.isBreakable())
                    {
                        wall.hp -= damage;
                        alive = false;
                    }
                }
            }
            else
            {
                if(Math.abs(wall.y1 - y) < 5 && x <= wall.x2 && x >= wall.x1)
                {
                    speedY = -speedY;
                    antiSpawnKill = 0;
                    if(wall.isBreakable())
                    {
                        wall.hp -= damage;
                        alive = false;
                    }
                }
            }
        }
        for(UserState userState : game.getTeam1())
        {
            if(userState.isDead() || userState.hasShield()) continue;
            if(Math.sqrt(Math.pow(x-(userState.getX()+20),2) + Math.pow(y-(userState.getY()+20),2)) < 20 && (!userState.equals(this.owner) || antiSpawnKill == 0))
            {
                alive = false;
                userState.setDead(userState.doDamage(damage));
                if(userState.isDead())
                {
                    owner.killed();
                    userState.dead();
                }
            }
        }
        for(UserState userState : game.getTeam2())
        {
            if(userState.isDead() || userState.hasShield()) continue;
            if(Math.sqrt(Math.pow(x-(userState.getX()+20),2) + Math.pow(y-(userState.getY()+20),2)) < 20 && (!userState.equals(this.owner) || antiSpawnKill == 0))
            {
                alive = false;
                userState.setDead(userState.doDamage(damage));
                if(userState.isDead())
                {
                    owner.killed();
                    userState.dead();
                }
            }
        }
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

    public boolean isTimeOut() {
        return System.currentTimeMillis() - createdTime >= 4000 || !alive;
    }


    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }
}
