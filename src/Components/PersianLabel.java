package Components;


import javax.swing.*;
import java.awt.*;

public class PersianLabel extends JLabel {
    public PersianLabel(String text) {
        super("<html><div align=right style=\"font-size:20px\">"+ text +"</div></html>");
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setHorizontalTextPosition(JLabel.RIGHT);
        setHorizontalAlignment(JLabel.RIGHT);
        setFont(Fonts.bTitr); setSize(200,40);
    }
}
