package Client;

import Components.ConvertTime;
import Server.Game;
import Server.User;
import Graphic.KeyboardState;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Network {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static ObjectInputStream objectReader;
    private static ObjectOutputStream objectWriter;

    public static void runServer(){
        try {
            socket = new Socket("localhost",8080);
            socket.setSoTimeout(1000);
            out = new PrintWriter(socket.getOutputStream(), true);
            out.flush();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectWriter = new ObjectOutputStream((socket.getOutputStream()));
            objectReader = new ObjectInputStream((socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean login(String username , String password)
    {
        out.println("login");
        out.println(username);
        out.println(password);
        out.flush();
        try {
            UserInfo.user = (User) objectReader.readObject();
            if(UserInfo.user == null)
            {
                return false;
            }
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    public static void setSettings(Settings settings)
    {
        out.println("settings");
        try {
            objectWriter.writeObject(settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<User> getBests()
    {
        out.println("bests");
        try {
            return (ArrayList<User>) objectReader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean signUp(String username , String password) {
        out.println("signup");
        out.println(username);
        out.println(password);
        try {
            return in.readLine().equals("true");
        } catch (IOException e) {
            return false;
        }
    }

    public static void changeNickName(String input) {
        if(input == null) return;
        if(input.replace(" ","").equals("")) return;
        UserInfo.user.setNickname(input);
        out.println("cn");
        out.println(input);
    }

    public static void changeTank(String tank) {
        out.println("ct");
        out.println(tank);
    }

    public static int createGame(Game game) {
        out.println("cg");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            objectWriter.writeObject(game);
            return Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void join(int id) {
        out.println("join");
        out.println(id);
    }

    public static Game updateRoom() {
        synchronized (out)
        {
            synchronized (objectReader)
            {
                out.flush();
                out.println("ur");
                try {
                    return (Game) objectReader.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }

    public static ArrayList<Game> getRooms() {
        synchronized (out)
        {
            synchronized (objectReader)
            {
                out.println("gr");
                try {
                    return (ArrayList<Game>) objectReader.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static void leaveRoom() {
        synchronized (out)
        {
            UserInfo.game = -1;
            out.flush();
            out.println("leave");
        }
    }

    public static User updateTime() {
        synchronized (out)
        {
            synchronized (objectReader)
            {
                out.println("updateTime");
                try {
                    return (User) objectReader.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }

    public static void startGame() {
        synchronized (out)
        {
            out.println("start");
        }
    }

    public static void started(){
        synchronized (out)
        {
            out.println("r");

        }
    }

    public static Game updateGame(KeyboardState state) {
        synchronized (out)
        {
            synchronized (objectReader)
            {
                synchronized (objectWriter)
                {
                    out.flush();
                    try {
                        objectWriter.reset();
                        objectWriter.writeObject(state);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        return (Game) objectReader.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
    }

}
