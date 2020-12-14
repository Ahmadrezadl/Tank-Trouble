package Client;

import Components.MenuFrame;
import Server.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class RoomsFrame extends MenuFrame implements Runnable{

    Thread thread;
    private JPanel roomsPanel;
    public RoomsFrame() {
        super("اتاق ها");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                new MainMenu();
                setVisible(false);
                thread.interrupt();
                e.getWindow().dispose();
            }
        });
    }

    @Override
    public void addComponents() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(5,1));
        panel.setSize(250,300-3);
        panel.setLocation(25,180);
        add(panel);
        roomsPanel = panel;
        thread = new Thread(this);
        thread.start();
    }

    private void refreshRooms(){
        for(Component component : roomsPanel.getComponents())
        {
            roomsPanel.remove(component);
        }
        ArrayList<Game> rooms = Network.getRooms();
        if (rooms != null) {
            for(Game room : rooms)
            {
                if(room.isAvailable() && !room.isStarted())
                roomsPanel.add(room.getPanel());
            }
        }
        roomsPanel.setVisible(true);
        setVisible(true);
    }

    @Override
    public void run() {
        while(UserInfo.game == -1)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                setVisible(false);
                dispose();
                return;
            }
            refreshRooms();
        }
        setVisible(false);
        dispose();
    }
}
