package UserInterface;

//import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class JPanel_Ex extends JPanel {
    public JPanel_Ex()
    {
        super();
    }
    public JPanel_Ex(GridBagLayout GBL)
    {
        super(GBL);
    }
    public void add(Component cpt,  GridBagConstraints cons, int gridX, int gridY, int width, int height)
    {
        cons.gridx = gridX;
        cons.gridy = gridY;
        cons.gridwidth = width;
        cons.gridheight = height;
        add(cpt, cons);
    }
}