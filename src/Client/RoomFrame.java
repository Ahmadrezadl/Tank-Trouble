package Client;

import Components.Fonts;
import Components.MenuFrame;
import Components.NameColors;
import Server.Game;
import Server.UserState;
import Graphic.GameFrame;
import Graphic.GameLoop;
import Graphic.ThreadPool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RoomFrame extends MenuFrame {
    Thread thread;

    public RoomFrame(String label) {
        super(label);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                Network.leaveRoom();
            }
        });
    }

    @Override
    public void addComponents() {
        JFrame jFrame = this;
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1));
        panel.setBackground(Color.WHITE);
        panel.setSize(250,220);
        panel.setLocation(25,180);
        add(panel);
        JButton startGameButton = new JButton("شروع بازی");
        JButton backButton = new JButton("بازگشت");
        add(startGameButton);
        add(backButton);
        startGameButton.setSize(125,50);
        backButton.setSize(125,50);
        backButton.setLocation(25,400);
        startGameButton.setLocation(150,400);
        startGameButton.setFont(Fonts.bTitr);
        backButton.setFont(Fonts.bTitr);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
        JScrollPane jScrollPane = new JScrollPane(jPanel);
        panel.add(jScrollPane);

        backButton.addActionListener(e->{
            Network.leaveRoom();
        });

        startGameButton.addActionListener(e->{
            if(Network.updateRoom().isHost(UserInfo.user))
            {
                Network.startGame();
            }
            else
                JOptionPane.showMessageDialog(this,"شما سازنده اتاق نیستید.","خطا",JOptionPane.ERROR_MESSAGE);
        });

        thread = new Thread(() -> {
            while (UserInfo.game != -1) {
                for (Component component : jPanel.getComponents()) {
                    jPanel.remove(component);
                }
                Game game = Network.updateRoom();

                if (!game.isAvailable()) {
                    new MainMenu();
                    Network.leaveRoom();
                    setVisible(false);
                    jFrame.dispose();
                }
                int i = 0;
                for (UserState user : game.getTeam1()) {
                    if(user.getUser() == null) continue;
                    JButton jButton = new JButton(user.getUser().getNickname());
                    if(game.isTeamMatch())
                    jButton.setBackground(Color.GREEN);
                    else
                    {
                        jButton.setBackground(NameColors.getColor(i));
                        i++;
                    }
                    jPanel.add(jButton);
                    jButton.setMinimumSize(new Dimension(245 , 25));
                    jButton.setPreferredSize(new Dimension(245 , 25));
                }
                for (UserState user : game.getTeam2()) {
                    if(user.getUser() == null) continue;

                    JButton jButton = new JButton(user.getUser().getNickname());
                    jButton.setBackground(Color.RED);
                    jPanel.add(jButton);
                    jButton.setMinimumSize(new Dimension(245 , 25));
                    jButton.setPreferredSize(new Dimension(245 , 25));
                }

                setVisible(true);

                if(game.isStarted())
                {
                    GameFrame frame = new GameFrame(game.getName());
                    frame.setLocationRelativeTo(null); // put frame at center of screen
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    frame.addWindowListener(new WindowAdapter()
                    {
                        @Override
                        public void windowClosing(WindowEvent e)
                        {
                            Network.leaveRoom();
                        }
                    });
                    frame.setVisible(true);
                    frame.initBufferStrategy();
                    // Create and execute the game-loop
                    GameLoop g = new GameLoop(frame);
                    g.init();
                    ThreadPool.execute(g);
                    setVisible(false);
                    jFrame.dispose();
                    return;
                }
                try{
                    Thread.sleep(500);
                }
                catch (InterruptedException ignored) {
                }
            }
            new MainMenu();
            setVisible(false);
            jFrame.dispose();

        });
        thread.start();
    }
}
