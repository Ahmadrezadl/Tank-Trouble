package Components;

import java.awt.*;

public class NameColors {
    public static Color getColor(int i )
    {
        switch (i)
        {
            case 0: return Color.RED;
            case 1: return Color.BLUE;
            case 2: return Color.YELLOW;
            case 3: return Color.PINK;
            case 4: return Color.ORANGE;
            case 5: return Color.green;
            case 6: return Color.CYAN;
            case 7: return Color.MAGENTA;
            default: return Color.BLACK;
        }
    }
}
