package chai_4d.mbus.map.status;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import chai_4d.mbus.map.screen.MainFrame;

public class MapStatusBar extends JPanel
{
    private static final long serialVersionUID = -2396986921097672862L;

    private static StatusItem messageLabel = null;
    private static StatusItem coordinateLabel = null;
    private static StatusItem modeLabel = null;

    public MapStatusBar(MainFrame mainFrame)
    {
        initialize();
    }

    private void initialize()
    {
        setPreferredSize(new Dimension(10, 23));
        setLayout(new GridBagLayout());
        //setBackground(MapConstants.controlPanel);

        messageLabel = new StatusItem("");
        coordinateLabel = new StatusItem("", true);
        modeLabel = new StatusItem("", true);
        StatusItem corner = new StatusItem(null, false, new AngledLinesCornerIcon());

        add(
            messageLabel,
            new GridBagConstraints(0, 0, 1, 1, 6.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        add(coordinateLabel, new GridBagConstraints(
            1,
            0,
            1,
            1,
            1.0,
            1.0,
            GridBagConstraints.WEST,
            GridBagConstraints.BOTH,
            new Insets(2, 2, 2, 2),
            0,
            0));
        add(modeLabel, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        add(
            corner,
            new GridBagConstraints(3, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(4, 0, 0, 0), 0, 0));
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        int y = 0;
        g.setColor(new Color(156, 154, 140));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(196, 194, 183));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(218, 215, 201));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(233, 231, 217));
        g.drawLine(0, y, getWidth(), y);

        y = getHeight() - 3;
        g.setColor(new Color(233, 232, 218));
        g.drawLine(0, y, getWidth(), y);
        y++;
        g.setColor(new Color(233, 231, 216));
        g.drawLine(0, y, getWidth(), y);
        y = getHeight() - 1;
        g.setColor(new Color(221, 221, 220));
        g.drawLine(0, y, getWidth(), y);
    }

    public static void setMessage(String message)
    {
        messageLabel.setLabel(message);
    }

    public static void setCoordinate(String coordinate)
    {
        coordinateLabel.setLabel(coordinate);
    }

    public static void setMode(String mode)
    {
        modeLabel.setLabel(mode);
    }
}