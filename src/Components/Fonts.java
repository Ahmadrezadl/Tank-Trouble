package Components;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Fonts {
    public static Font bTitr;
    public static Font bTitrSmall;
    public static void buildFonts(){
        try {
            bTitr = Font.createFont(Font.TRUETYPE_FONT, new File("src\\Sources\\bTitr.ttf")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(bTitr);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        try {
            bTitrSmall = Font.createFont(Font.TRUETYPE_FONT, new File("src\\Sources\\bTitr.ttf")).deriveFont(15f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(bTitr);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }


}
