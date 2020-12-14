package Server;

import Client.Settings;
import Graphic.KeyboardState;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;


public class ClientHandler implements Runnable{
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Socket socket;
    private BufferedReader dis;
    private PrintWriter dos;
    private User user;
    private long loginTime;
    private Game room;
    DataBase dataBase;


    public ClientHandler(BufferedReader dis , PrintWriter dos , ObjectInputStream in , ObjectOutputStream out , DataBase dataBase , Socket socket) {
        this.dis = dis;
        this.dos = dos;
        this.in = in;
        this.out = out;
        this.dataBase = dataBase;
        this.socket = socket;
//        try {
//            socket.setSoTimeout(3000);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
        room=null;
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                String input = dis.readLine();
                if(input.equals("login"))
                    if(login())break;
                if(input.equals("signup"))
                    signUp();

            } catch (IOException e) {
                log(user.getNickname() + " Disconnected");
                return;
            }
        }
        log(" logged.");
        acceptCommands();

    }

    private void acceptCommands() {
        loginTime = System.currentTimeMillis();
        try{
            while(true)
            {
                String input = dis.readLine();
                log(input);
                if(input.equals("cn"))
                {
                    changeNickName(dis.readLine());
                    log(" Changed Nickname to " + user.getNickname());

                }
                else if(input.equals("updateTime"))
                {
                    user.setPlaytime(user.getPlaytime() + (System.currentTimeMillis() - loginTime));
                    loginTime = System.currentTimeMillis();
                    out.writeObject(user);

                }
                else if(input.equals("ct"))
                {
                    String tank = dis.readLine();
                    user.setColor(Integer.parseInt(tank));
                    log(" Changed his/her tank!");
                }
                else if(input.equals("settings"))
                {
                    user.setSettings((Settings) in.readObject());
                    log(" Changed Settings!");
                }
                else if(input.equals("cg"))
                {
                    log("Creating room.");
                    Game room = (Game) in.readObject();
                    dataBase.addGame(room);//games.add();
                    room.setId(dataBase.games.size()-1);;
                    this.room = room;
                    room.joinUser(user);
                    dos.println(dataBase.games.size()-1);
                    log("Created a room!");

                }
                else if(input.equals("bests"))
                {
                    Collections.sort(dataBase.users);
                    ArrayList<User > bests = new ArrayList<User>();
                    int i = 0;
                    for(User user : dataBase.users)
                    {
                        bests.add(user);
                        i++;
                        if(i == 3) break;
                    }
                    out.writeObject(bests);
                }
                else if(input.equals("gr"))
                {
                    out.reset();
                    out.writeObject(dataBase.getGames());
                }
                else if(input.equals("ur"))
                {
                    out.reset();
                    out.writeObject(room);
                }
                else if(input.equals("join"))
                {
                    log("Joining room!");
                    out.reset();
                    this.room = dataBase.games.get((Integer.parseInt(dis.readLine())));
                    room.joinUser(user);
                    log("Joined a room!");
                }
                else if(input.equals("leave"))
                {
                    if(room != null)
                    room.leaveUser(user);
                    this.room = null;
                    log("Left a room!");
                    dos.flush();
                }
                else if(input.equals("start"))
                {
                    room.start();
                }
                //Move_Player
                else if(input.equals("r"))
                {
                    while(true)
                    {
                        out.reset();
                        KeyboardState keyboardState;
                        System.out.println(1);
                        try
                        {
                            keyboardState = (KeyboardState) in.readObject();
                        }
                        catch (Exception e)
                        {
                            keyboardState = new KeyboardState();
                        }
                        room.move(user,keyboardState);
                        synchronized (room)
                        {
                            if(keyboardState.gameOver) {
                                if (room != null)
                                    room.leaveUser(user);
                            }
                            out.writeObject(room);
                        }
                        System.out.println(2);
                        if(keyboardState.gameOver)
                        {
                            room = null;
                            break;
                        }
                    }
                }
                dataBase.writeUsers();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            log(" Terminated!");
            if(room != null)
            {
                room.leaveUser(user);
            }
            user.setPlaytime(user.getPlaytime() + System.currentTimeMillis() - loginTime);
            dataBase.writeUsers();
        }
    }

    private void changeNickName(String newName) {
        user.setNickname(newName);
        dataBase.writeUsers();
    }

    private void log(String l)
    {
        System.out.println(user.getUsername() + " : " + l);
    }

    private void signUp() {
        try {
            String username = dis.readLine();
            String password = dis.readLine();
            for(User user : dataBase.users)
            {
                if(user.getUsername().toLowerCase().equals(username.toLowerCase()))
                {
                    dos.println("false");
                    return;
                }
            }
            dos.println("true");
            dataBase.users.add(new User(username,password));
            dataBase.writeUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean login() {
        try {
            String username = dis.readLine();
            String password = dis.readLine();
            for(User user : dataBase.users)
            {
                if(user.getUsername().toLowerCase().equals(username.toLowerCase()) && user.getPassword().equals(password))
                {
                    out.writeObject(user);
                    this.user = user;
                    return true;
                }
            }
            out.writeObject(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
