package Server;

import Client.*;
import Graphic.KeyboardState;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static Components.Fonts.bTitr;
import static Graphic.GameFrame.GAME_HEIGHT;
import static Graphic.GameFrame.GAME_WIDTH;

public class Game implements Runnable , Serializable {
    private final String name;
    private boolean started;
    private final int playersNum;
    private final boolean teamMatch;
    private final User host;
    private final ArrayList<UserState> team1;
    private final ArrayList<UserState> team2;
    private Mode mode;
    private ArrayList<Bullet> bullets;
    private int[][] map;
    private final int tankDamage;
    private final int tankHP;
    private int wallHP;
    private int id;
    private boolean available ;
    private boolean ai;
    private String winner;
    public ArrayList<PowerUp> powerUps = new ArrayList<>();
    public ArrayList<Wall> walls= new ArrayList<>();
    public ArrayList<Point> points = new ArrayList<>();

    public Game(String roomName , boolean team , boolean leauge , int maximumPlayer , int tankDamage , int tankHP , int wallHP , User user) {
        this.name = roomName;
        this.teamMatch = team;
        this.mode = leauge?Mode.DEATH_MATCH :Mode.SHOWDOWN;
        this.playersNum = maximumPlayer;
        this.tankDamage = tankDamage;
        this.tankHP = tankHP;
        this.wallHP = wallHP;
        this.host = user;
        winner = "";
        available = true;
        started = false;
        ai = false;
        team1 = new ArrayList<>(teamMatch?playersNum/2:playersNum);
        team2 = new ArrayList<>(teamMatch?playersNum/2:0);
        try {
            File file = null;
            if(Math.random() > 0.5)
                file  = (new File("src\\Sources\\doubletrouble.map"));
            else
                file = (new File("src\\Sources\\thousandlakes.map"));
            FileInputStream fis = new FileInputStream(file);
            byte[] byteArray = new byte[(int)file.length()];
            fis.read(byteArray);
            String data = new String(byteArray);
            String[] lines = data.split("\r\n");
            map = new int[lines.length][lines[0].length()];
            for(int i = 0;i < lines.length;i++)
            {
                for(int j = 0;j < lines[0].length();j++)
                {
                    map[i][j] = Character.getNumericValue(lines[i].charAt(j));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            map = new int[][]{{1 , 1 , 1 , 1 , 1 , 1 , 1 , 1 , 1 , 1},
                    {1 , 0 , 0 , 0 , 1 , 1 , 1 , 1 , 1 , 1},
                    {1 , 1 , 1 , 0 , 1 , 1 , 0 , 0 , 0 , 1},
                    {1 , 1 , 0 , 0 , 0 , 1 , 0 , 0 , 1 , 1},
                    {1 , 1 , 1 , 0 , 0 , 0 , 0 , 1 , 1 , 1},
                    {1 , 1 , 1 , 1 , 0 , 1 , 1 , 1 , 1 , 1},
                    {1 , 1 , 1 , 1 , 1 , 1 , 1 , 1 , 1 , 1}};
        }

        int xLine = GAME_WIDTH/(map[0].length-1);
        int yLine = GAME_HEIGHT/(map.length-1);
        for(int i =0 ;i < map.length;i++) {
            for(int j = 0; j < map[0].length;j++) {
                if(map[i][j] == 0)
                {
                    points.add(new Point(xLine*j,yLine * i ));
                    continue;
                }
                if(i != 0 && map[i-1][j] != 0)
                {
                    walls.add(new Wall(j*xLine,j*xLine,i*yLine,(i-1) * yLine,map[i-1][j] == 2|| map[i][j] == 2,wallHP));
                }
                if(j != 0 && map[i][j-1] != 0)
                {
                    walls.add(new Wall(j*xLine,(j-1)*xLine,i*yLine,(i) * yLine,map[i][j-1] == 2|| map[i][j] == 2,wallHP));

                }
            }
        }


    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean joinUser(User user)
    {
        if(team1.size() + team2.size() >= playersNum)
            return false;
        UserState userState;
        if(user != null)
            userState = new UserState(user,tankHP);
        else
            userState = new UserState(tankHP);
        int xLine = GAME_WIDTH/(map[0].length-1);
        int yLine = GAME_HEIGHT/(map.length-1);
        if(points.size() == 0 )
        {
            for(int i =0 ;i < map.length;i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j] == 0) {
                        points.add(new Point(xLine * j , yLine * i));
                    }
                }
            }
        }

        int rand = (int) (System.currentTimeMillis()%points.size());
        userState.setX(points.get(rand).x);
        userState.setY(points.get(rand).y);
        points.remove(rand);

        if((teamMatch && playersNum/2 > team1.size()) || (!teamMatch && playersNum > team1.size()))
        {
            team1.add(userState);
        }
        else
        {
            team2.add(userState);
        }
        return true;
    }

    @Override
    public void run() {
        int FPS = 100;
        while(true)
        {
            long start = System.currentTimeMillis();
            synchronized (this)
            {
                if(Math.random() > 0.9995)
                {
                    Point point = points.get((int)(System.currentTimeMillis()%points.size()));
                    powerUps.add(new PowerUp(point.x , point.y , PowerUpType.values()[(int)(Math.random()*3)]));
                }
                for(UserState userState : team1)
                {
                    userState.move(this,tankDamage);
                    if(userState.getUser()==null)
                    {
                        userState.getPressedKeys().generateRandom();
                    }
                }
                for(UserState userState : team2)
                {
                    userState.move(this,tankDamage);
                }
                for(Bullet bullet : bullets)
                {
                    bullet.move(this);
                }
                for(int i = 0;i < bullets.size();i++)
                {
                    Bullet bullet = bullets.get(i);
                    if(bullet.isTimeOut() || bullet.getX() < 0 || bullet.getX() > GAME_WIDTH || bullet.getY() < 0 || bullet.getY() > GAME_HEIGHT)
                    {
                        bullets.remove(bullet);
                        i--;
                    }
                }
                for(int i = 0; i < walls.size();i++)
                {
                    if(walls.get(i).hp <= 0) {
                        walls.remove(i);
                        i--;
                    }

                }
            }

            //Check win:
            if(teamMatch)
            {
                boolean t1 = false;
                boolean t2 = false;
                for(UserState userState : team1)
                {
                    if (!userState.isDead()) {
                        t1 = true;
                        break;
                    }
                }
                for(UserState userState : team2)
                {
                    if (!userState.isDead()) {
                        t2 = true;
                        break;
                    }
                }
                if(t1 && !t2 || t2 && !t1)
                {
                    if(ai)
                    {
                        winner = t1?team1.get(0).getUser().getNickname():"AI";
                        if(t1){
                            team1.get(0).getUser().addAiWin();
                        }
                        return;
                    }
                    for(UserState userState : t1?team1:team2) {
                        winner += userState.getUser().getNickname() + " - ";
                        userState.getUser().addWin();
                    }
                    return;
                }
            }
            else{
                UserState winner = null;
                boolean end = false;
                for(UserState userState : team1)
                {
                    if(!userState.isDead() && !end)
                    {
                        end = true;
                        winner = userState;
                    }
                    else if(!userState.isDead() && end)
                    {
                        end = false;
                        winner = null;
                        break;
                    }
                }
                if(end)
                {
                    if(ai)
                    {
                        if(winner.getUser() != null)
                        winner.getUser().addAiWin();
                    }
                    else
                        winner.getUser().addWin();

                    try{
                        this.winner = winner.getUser().getNickname();
                    }
                    catch (NullPointerException e)
                    {
                        this.winner = "AI";
                    }
                    return;
                }
            }

            long delay = (1000 / 100) - (System.currentTimeMillis() - start);
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public JPanel getPanel(){
        Font font = new Font("Arial",Font.BOLD,13);
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.WHITE);
        jPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        jPanel.setLayout(new GridLayout(2,2));
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(font);
        jPanel.add(nameLabel);
        JLabel hostName = new JLabel("Host: " + host.getNickname());
        hostName.setFont(font);
        jPanel.add(hostName);
        jPanel.add(new JLabel((team1.size() + team2.size()) + "/" + playersNum));
        JButton jButton = new JButton("ورود");
        jButton.setFont(bTitr);
        jPanel.add(jButton);
        jButton.addActionListener(e->{
            Network.join(id);
            if(UserInfo.game == -1)
                UserInfo.game = 0;
            else
                return;
            new RoomFrame("Room");
        });
        return jPanel;
    }

    public boolean isTeamMatch() {
        return teamMatch;
    }

    public ArrayList<UserState> getTeam1() {
        return team1;
    }

    public ArrayList<UserState> getTeam2() {
        if(teamMatch)
        return team2;
        else
            return new ArrayList<UserState>();
    }

    public boolean isHost(User user)
    {
        return user.equals(host);
    }

    public void leaveUser(User user) {
        if(user == null)return;
        if(isHost(user))
        {
            available = false;
        }
        for(UserState userState : team1)
        {
            if(userState.getUser() == null) continue;
            if(userState.getUser().equals(user))
            {
                team1.remove(userState);
                return;
            }
        }
        for(UserState userState : team2)
        {
            if(userState.getUser() == null) continue;
            if(userState.getUser().equals(user))
            {
                team2.remove(userState);
                return;
            }
        }
    }

    public boolean isAvailable() {
        return available;
    }


    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void start() {
        bullets = new ArrayList<Bullet>();
        started = true;
        if(team1.size() == 1 && team2.size() == 0)
        {
            ai = true;
            for(int i = 1; i < playersNum;i++)
            {
                joinUser(null);
            }
        }
        Thread thread = new Thread(this);
        thread.start();
    }

    public boolean isStarted() {
        return started;
    }

    public String getName() {
        return name;
    }

    public void move(User user , KeyboardState keyboardState) {
        if(ai)
        {
            team1.get(0).setPressedKeys(keyboardState);
            return;
        }
        try{
            for(UserState userState: team1)
            {
                if(userState.getUser().equals(user))
                {
                    userState.setPressedKeys(keyboardState);
                }
            }
            for(UserState userState: team2)
            {
                if(userState.getUser().equals(user))
                {
                    userState.setPressedKeys(keyboardState);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public int[][] getMap() {
        return map;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isJoined(User user) {
        for(UserState userState : team1)
        {
            if(userState.getUser()== null)continue;
            if(userState.getUser().equals(user)){
                return true;
            }
        }
        for(UserState userState : team2)
        {
            if(userState.getUser()== null)continue;
            if(userState.getUser().equals(user)){
                return true;
            }
        }
        return false;
    }

    public int getTankHp() {
        return tankHP;
    }
}
