package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class MenuFrame extends JFrame{

    public MenuFrame(String label) {
        super(label);
        //Set frame size and location:
        setSize(300,500);
        getContentPane().setBackground(Color.WHITE);
        setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        JLabel logo = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(new File("src\\Sources\\LoginLogo.png")));
            logo.setIcon(icon);
            add(logo);
            logo.setSize(icon.getIconWidth(),icon.getIconWidth());
            logo.setFocusable(true);
            logo.setLocation(40,0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addComponents();
        setVisible(true);
    }

    public abstract void addComponents();

}
