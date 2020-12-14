package Client;

import Components.*;
import Server.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static Components.Fonts.bTitr;
import static Components.Fonts.bTitrSmall;

public class NewRoomFrame extends MenuFrame {
    public NewRoomFrame() {
        super("ساخت اتاق جدید");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                new MainMenu();
                setVisible(false);
                e.getWindow().dispose();
            }
        });
    }

    @Override
    public void addComponents() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(8,2));
        panel.setSize(250,300-3);
        panel.setLocation(25,170-2);
        add(panel);
        HintTextField roomName = new HintTextField("Tank  ");
        panel.add(roomName);
        panel.add(new PersianLabel("نام بازی:"));

        JButton soloTeam = new JButton("فردی");
        soloTeam.setFont(bTitr);
        soloTeam.setBorderPainted(false);
        soloTeam.setContentAreaFilled(false);
        panel.add(soloTeam);
        panel.add(new PersianLabel("تیم بندی:"));


        JButton mode = new JButton("لیگی");
        mode.setFont(bTitr);
        mode.setBorderPainted(false);
        mode.setContentAreaFilled(false);
        panel.add(mode);
        panel.add(new PersianLabel("مود بازی:"));

        JTextField maximum = new JTextField("6");
        add(maximum);maximum.setFont(bTitrSmall);
        panel.add(maximum);
        maximum.addKeyListener(Listeners.justNumber);
        panel.add(new PersianLabel("ظرفیت:"));

        JTextField tankHP = new JTextField(String.valueOf(UserInfo.user.getSettings().getTankHP()));
        add(tankHP);tankHP.setFont(bTitrSmall);
        panel.add(tankHP);
        tankHP.addKeyListener(Listeners.justNumber);
        panel.add(new PersianLabel("جان تانک:"));

        JTextField damage = new JTextField(String.valueOf(UserInfo.user.getSettings().getTankDamage()));
        add(damage);damage.setFont(bTitrSmall);
        panel.add(damage);
        damage.addKeyListener(Listeners.justNumber);
        panel.add(new PersianLabel("قدرت تیر:"));

        JTextField wallHP = new JTextField(String.valueOf(UserInfo.user.getSettings().getWallHP()));
        add(wallHP);wallHP.setFont(bTitrSmall);
        panel.add(wallHP);
        wallHP.addKeyListener(Listeners.justNumber);
        panel.add(new PersianLabel("جان دیوار:"));

        JButton back = new JButton("بازگشت");
        panel.add(back);
        back.setFont(bTitr);
        JButton createRoom = new JButton("ساخت اتاق");
        createRoom.setFont(bTitr);
        panel.add(createRoom);

        back.addActionListener(e->{
            new MainMenu();
            setVisible(false);
            dispose();
        });

        soloTeam.addActionListener(e->
                {
                    soloTeam.setText(soloTeam.getText().equals("فردی")?"تیمی":"فردی");
                });
        createRoom.addActionListener(e->{
            int numberOfPlayers = Integer.parseInt(maximum.getText());
            boolean team = soloTeam.getText().equals("تیمی");
            if(roomName.getText().replace(" ","").equals(""))
            {
                JOptionPane.showMessageDialog(this,"نام اتاق نمیتواند خالی باشد","خطا",JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if(numberOfPlayers <= 0)
            {
                JOptionPane.showMessageDialog(this,"تعداد بازیکنان باید بیشتر از 0 باشد","خطا",JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if(numberOfPlayers%2 == 1 && team)
            {
                JOptionPane.showMessageDialog(this,"در حالت تیمی, تعداد بازیکنان باید زوج باشد","خطا",JOptionPane.ERROR_MESSAGE);
                return;
            }
            Game game = new Game(roomName.getText(),team,mode.getText().equals("لیگی"),Integer.parseInt(maximum.getText()),
                    Integer.valueOf(damage.getText()),Integer.valueOf(tankHP.getText()),Integer.valueOf(wallHP.getText()),UserInfo.user);
            UserInfo.game = Network.createGame(game);
            new RoomFrame(roomName.getText());
            setVisible(false);
            dispose();
        });
    }
}
