package Client;

import Components.Fonts;
import Components.Images;
import Server.User;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Fonts.buildFonts();
        Images.init();
        Network.runServer();
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        new LoginPage();
    }
}
