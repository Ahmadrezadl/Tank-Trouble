package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Images {
    private static ArrayList<ImageIcon> tanks;
    private static ArrayList<BufferedImage> tanksBuffered;

    public static void init(){
        tanks = new ArrayList<>(5);
        tanksBuffered = new ArrayList<>();
        try {
            for(TankColor t : TankColor.values())
            {
                Image img = ImageIO.read(new File("src\\Sources\\tank_" + t + ".png"));
                tanks.add(new ImageIcon(img));
                BufferedImage image = ImageIO.read(new File("src\\Sources\\tank_" + t + ".png"));
                tanksBuffered.add(image);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ImageIcon getTank(int i)
    {
        return tanks.get(i);
    }

    public static BufferedImage getTankBuffer(int i)
    {
        return tanksBuffered.get(i);
    }
}
