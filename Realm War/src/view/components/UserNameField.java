package view.components;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static view.components.Field.*;

public class UserNameField extends JTextField {

    public UserNameField() {
        super();
        initPasswordField();
    }

    private void initPasswordField() {

        // ایجاد فیلد متنی
        setPreferredSize(FIELD_SIZE);
        setForeground(TEXT_COLOR);
        setBackground(BACKGROUND_COLOR);
        setBorder(createFieldBorder(BORDER_COLOR));
        setFont(FIELD_FONT);
        setCaretColor(FOCUS_BORDER_COLOR);

        // تغییر استایل هنگام فوکوس
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(createFieldBorder(FOCUS_BORDER_COLOR));
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(createFieldBorder(BORDER_COLOR));
            }
        });

    }
}
