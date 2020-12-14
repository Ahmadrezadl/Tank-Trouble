package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            final DataBase dataBase = new DataBase();
            dataBase.readUsers();
            ServerSocket ss = new ServerSocket(8080);
            while (true)
            {
                Socket socket = ss.accept();
                BufferedReader dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter dos = new PrintWriter(socket.getOutputStream(), true);
                dos.flush();
                ObjectInputStream in = new ObjectInputStream((socket.getInputStream()));
                ObjectOutputStream out = new ObjectOutputStream((socket.getOutputStream()));
                Thread t = new Thread(new ClientHandler(dis,dos,in,out,dataBase,socket));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }
}
