package Client;

import Components.*;
import Server.User;
import sun.nio.ch.Net;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class SettingsFrame extends JFrame {
    public SettingsFrame(){
        UserInfo.user = Network.updateTime();
        try {
            setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src\\Sources\\settingsBackground.png")))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSize(1280,760);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        addComponents();
        setVisible(true);
    }

    private void addComponents() {
        ArrayList<User> bests  = Network.getBests();
        int i = 0;
        for(User user : bests)
        {
            JLabel jLabel;
            jLabel = new JLabel(user.getNickname() + " (" + user.getLevel() + ")");
            jLabel.setHorizontalAlignment(JLabel.CENTER);
            jLabel.setVerticalAlignment(JLabel.CENTER);
            add(jLabel);
            jLabel.setSize(100,60);
            if(i == 0)
            {
                jLabel.setLocation(905,440);
            }
            else if(i==1)
            {
                jLabel.setLocation(790,440+130);
            }
            else
            {
                jLabel.setLocation(900+125,440+130);
            }
            jLabel.setFont(new Font("Arial",Font.BOLD,20));
            i++;
        }
        PersianLabel level = new PersianLabel("سطح" + UserInfo.user.getLevel());
        add(level) ; level.setSize(100,60); level.setLocation(20,50); level.setFont(level.getFont().deriveFont(35.0f));

        JButton saveAndExit = new JButton("ذخیره و خروج");
        add(saveAndExit); saveAndExit.setSize(300,120); saveAndExit.setLocation(10,590); saveAndExit.setFont(Fonts.bTitr);

        JButton defaultSettings = new JButton("تنظیمات پیشفرض");
        add(defaultSettings); defaultSettings.setSize(300,120); defaultSettings.setLocation(320,590); defaultSettings.setFont(Fonts.bTitr);

        JTextField hp = new JTextField(String.valueOf(UserInfo.user.getSettings().getTankHP()));
        add(hp); hp.setSize(200,40); hp.setLocation(720,80); hp.setFont(Fonts.bTitr);

        JTextField damage = new JTextField(String.valueOf(UserInfo.user.getSettings().getTankDamage()));
        add(damage); damage.setSize(200,40); damage.setLocation(720,155); damage.setFont(Fonts.bTitr);

        JTextField wallHP = new JTextField(String.valueOf(UserInfo.user.getSettings().getWallHP()));
        add(wallHP); wallHP.setSize(200,40); wallHP.setLocation(720,230); wallHP.setFont(Fonts.bTitr);

        PersianLabel username = new PersianLabel(UserInfo.user.getUsername());
        add(username); username.setLocation(230,120); username.setFont(new Font("Arial",Font.BOLD,15));

        JTextField nickname = new JTextField(UserInfo.user.getNickname());
        add(nickname); nickname.setLocation(230,190); nickname.setFont(new Font("Arial",Font.BOLD,18)); nickname.setSize(200,40);

        PersianLabel time = new PersianLabel(ConvertTime.toTime(UserInfo.user.getPlaytime()));
        add(time); time.setLocation(150,260);

        PersianLabel wins = new PersianLabel(String.valueOf(UserInfo.user.getWinsVsComputer()));
        add(wins); wins.setLocation(86,323);

        PersianLabel winsVsPC = new PersianLabel(String.valueOf(UserInfo.user.getWins()));
        add(winsVsPC); winsVsPC.setLocation(92,392);

        JButton tank = new JButton(Images.getTank(UserInfo.user.getColor()));
        add(tank);
        tank.setBorderPainted(false);
        tank.setContentAreaFilled(false);
        tank.setLocation(460,475);
        tank.setSize(42,46);

        //Forbid characters

        hp.addKeyListener(Listeners.justNumber); damage.addKeyListener(Listeners.justNumber); wallHP.addKeyListener(Listeners.justNumber);


        tank.addActionListener(e->{
            UserInfo.user.setColor((UserInfo.user.getColor()+1)%5);
            tank.setIcon(Images.getTank(UserInfo.user.getColor()));
            Network.changeTank(String.valueOf(UserInfo.user.getColor()));
        });

        defaultSettings.addActionListener(e->{
            damage.setText("100");
            hp.setText("200");
            wallHP.setText("100");
        });

        saveAndExit.addActionListener(e->{
            Settings newSettings = new Settings(Integer.valueOf(damage.getText()),Integer.valueOf(wallHP.getText()),Integer.valueOf(hp.getText()));
            UserInfo.user.setSettings(newSettings);
            Network.setSettings(newSettings);
            Network.changeNickName(nickname.getText());
            setVisible(false);
            dispose();
        });

    }
}
