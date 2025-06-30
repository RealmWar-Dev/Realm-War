package view.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class Field {
    public static final Color BACKGROUND_COLOR = new Color(30, 40, 60);
    public static final Color BORDER_COLOR = new Color(60, 90, 130);
    public static final Color FOCUS_BORDER_COLOR = new Color(80, 150, 220);
    public static final Color TEXT_COLOR = new Color(240, 245, 255);
    public static final Color LABEL_COLOR = new Color(180, 200, 230);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Dimension FIELD_SIZE = new Dimension(300, 40);

    public static Border createFieldBorder(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 5 , true),
                new EmptyBorder(0, 20,0 , 50)
        );
    }
}