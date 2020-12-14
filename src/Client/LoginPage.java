package Client;

import Components.Fonts;
import Components.HintPasswordField;
import Components.HintTextField;
import Components.MenuFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LoginPage extends MenuFrame {
    private JTextField username;
    private JPasswordField password;
    public LoginPage(){
        super("Tank Trouble");
    }

    public void addComponents(){
        username = new HintTextField("Username");
        password = new HintPasswordField("Password");
        JButton loginButton = new JButton("ورود");
        JButton signUpButton = new JButton("ثبت نام");
        JCheckBox rememberMe = new JCheckBox("مرا به خاطر بسپار");
        add(loginButton);
        add(signUpButton);
        add(username);
        add(password);
        add(rememberMe);
        username.setSize(200,50);
        password.setSize(200,50);
        loginButton.setSize(100,50);
        signUpButton.setSize(100,50);
        rememberMe.setSize(100,50);
        username.setLocation(50,220);
        password.setLocation(50,270);
        rememberMe.setLocation(50,310);
        signUpButton.setLocation(50,350);
        loginButton.setLocation(150,350);
        username.setFont(new Font("Arial",Font.BOLD,17));
        password.setFont(new Font("Arial",Font.BOLD,17));
        loginButton.setFont(Fonts.bTitr);
        signUpButton.setFont(Fonts.bTitr);
        rememberMe.setSelected(true);

        try {
            Scanner sc = new Scanner(new File("login.info"));
            username.setText(sc.nextLine());
            password.setText(sc.nextLine());
        } catch (Exception ignored) {}

        loginButton.addActionListener(e->{
            if(checkError())
            {
                JOptionPane.showMessageDialog(this,"نام کاربری معتبر نیست","خطا",JOptionPane.ERROR_MESSAGE);
            }
            else if(Network.login(username.getText(), String.valueOf(password.getPassword())))
            {
                if(rememberMe.isSelected())
                {
                    try {
                        FileWriter fileWriter = new FileWriter(new File("login.info"));
                        fileWriter.write(username.getText() + "\n" + String.valueOf(password.getPassword()));
                        fileWriter.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else
                {
                    try {
                        FileWriter fileWriter = new FileWriter(new File("login.info"));
                        fileWriter.write("");
                        fileWriter.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                new MainMenu();
                setVisible(false);
                dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(this,"نام کاربری یا رمز عبور اشتباه است","خطا",JOptionPane.ERROR_MESSAGE);
            }
        });

        signUpButton.addActionListener(e->{
            if(checkError())
            {
                JOptionPane.showMessageDialog(this,"نام کاربری معتبر نیست","خطا",JOptionPane.ERROR_MESSAGE);
            }
            else if(Network.signUp(username.getText(), String.valueOf(password.getPassword())))
            {
                JOptionPane.showMessageDialog(this,"ثبت نام شما تکمیل شد. اکنون میتوانید وارد شوید.","ثبت نام",JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(this,"این نام کاربر انتخاب شده است","خطا",JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private boolean checkError() {
        return (username.getText().contains(" ") || username.getText().length() == 0 || String.valueOf(password.getPassword()).length() == 0);
    }
}
