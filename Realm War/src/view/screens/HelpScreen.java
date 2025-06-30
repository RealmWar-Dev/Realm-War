package view.screens;

import view.components.BaseBackgroundPanel;
import view.components.TopIconPanel;

import java.awt.*;

public class HelpScreen extends BaseBackgroundPanel {
    public static String name = "HELP";
    public HelpScreen() {
        super(false , false );
        setName(name);
        initializeComponents();
    }

    @Override
    public void initializeComponents() {
    }
}
