package Server;

import java.io.*;
import java.util.ArrayList;

public class DataBase {
    public final ArrayList<Game> games = new ArrayList<Game>();;
    public volatile ArrayList<User> users;

    public DataBase()
    {

        users = new ArrayList<User>();
    }

    public void readUsers(){
        try{
            File saveFile = new File("users.tt");
            if(saveFile.exists())
            {
                FileInputStream fis = new FileInputStream(saveFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                users = (ArrayList<User>) ois.readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void writeUsers() {
        try{
            FileOutputStream fos = new FileOutputStream(new File("users.tt"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users); 
            oos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addGame(Game room) {
        games.add(room);
    }

    public synchronized ArrayList<Game> getGames() {
        return games;
    }
}
