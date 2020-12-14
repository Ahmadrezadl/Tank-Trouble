package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


public class HintTextField extends JTextField implements FocusListener {

    private final String hint;
    boolean showingHint;
    Color color;
    public HintTextField(final String hint,Color color) {
        super(hint);
        setFont(new Font("Arial" ,Font.BOLD,20));
        setForeground(Color.GRAY);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
        this.color = color;
    }

    public HintTextField(final String hint)
    {
        super(hint);
        setFont(new Font("Arial" ,Font.BOLD,20));
        setForeground(Color.GRAY);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
        this.color = Color.BLACK;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText("");
            setForeground(color);
            showingHint = false;
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText(hint);
            setForeground(Color.GRAY);
            showingHint = true;
        }
    }

    @Override
    public String getText() {
        return hint.equals(super.getText()) ? "" : super.getText();
    }
}