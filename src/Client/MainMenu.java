package Client;

import Components.Fonts;
import Components.MenuFrame;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends MenuFrame {

    public MainMenu()
    {
        super("منوی اصلی");
    }

    public void addComponents(){
        //Add Others
        JButton welcome = new JButton(UserInfo.user.getNickname());
        welcome.setFont(new Font("Arial",Font.BOLD,20));
//        welcome.setAlignmentX(CENTER_ALIGNMENT);
        add(welcome);
        welcome.setBorderPainted(false);
        welcome.setContentAreaFilled(false);
        welcome.setLocation(75,160);
        welcome.setSize(150,50);

        JButton newRoom = new JButton("ساخت اتاق جدید");
        newRoom.setFont(Fonts.bTitr);
        add(newRoom);
        newRoom.setLocation(50,200);
        newRoom.setSize(200,60);

        JButton joinRoom = new JButton("ورود به اتاق");
        joinRoom.setFont(Fonts.bTitr);
        add(joinRoom);
        joinRoom.setLocation(50,260);
        joinRoom.setSize(200,60);

        JButton settings = new JButton("تنظیمات");
        settings.setFont(Fonts.bTitr);
        add(settings);
        settings.setLocation(50,320);
        settings.setSize(200,60);

        JButton exit = new JButton("خروج");
        exit.setFont(Fonts.bTitr);
        add(exit);
        exit.setLocation(50,380);
        exit.setSize(200,60);


        settings.addActionListener(e -> new SettingsFrame());

        newRoom.addActionListener(e->{
            new NewRoomFrame();
            setVisible(false);
            dispose();
        });

        joinRoom.addActionListener(e->{
            new RoomsFrame();
            setVisible(false);
            dispose();
        });

        welcome.addActionListener(e->{
            Network.changeNickName(JOptionPane.showInputDialog("نام جدید را وارد کنید\nتوجه کنید این نام کاربری شما نیست"));
            welcome.setText(UserInfo.user.getNickname());
        });



        exit.addActionListener(e->System.exit(0));
    }
}
