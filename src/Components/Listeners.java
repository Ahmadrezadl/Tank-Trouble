package Components;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Listeners {
    public static KeyAdapter justNumber = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                e.consume();
            }
        }
    };
}
