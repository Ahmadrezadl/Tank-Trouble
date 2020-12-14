package Server;

import Client.Settings;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable , Comparable<User> {
    private String username;
    private String password;
    private String nickname;
    private long playtime;
    private int winsVsComputer;
    private int wins;
    private int color;
    private Settings settings;
    private double xp = 0;
    public User(String username , String password) {
        this.username = username;
        this.password = password;
        this.nickname = username;
        this.playtime = 0;
        this.winsVsComputer = 0;
        this.wins = 0;
        settings = new Settings(100,100,200);
        this.color = (int) (System.currentTimeMillis()%5);
        this.xp = 1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public int getWinsVsComputer() {
        return winsVsComputer;
    }

    public void setWinsVsComputer(int winsVsComputer) {
        this.winsVsComputer = winsVsComputer;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        wins +=1 ;
        xp += 1;
    }

    public int getLevel()
    {
        return (int) Math.log(xp) + 1;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    public void addAiWin() {
        winsVsComputer++;
        xp += 0.5;
    }


    @Override
    public int compareTo(User o) {
        return Double.compare(o.xp , xp);
    }
}
