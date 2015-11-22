package chai_4d.mbus.map.screen;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.constant.MapConstants.ViewType;

public class LineItem extends JPanel
{
    private static final long serialVersionUID = 59050949573220737L;

    private ViewType lineType = null;

    public LineItem(ViewType lineType)
    {
        this.lineType = lineType;
        initialize();
    }

    private void initialize()
    {
        setSize(new Dimension(40, 20));
        setPreferredSize(new Dimension(40, 20));
        setMinimumSize(new Dimension(40, 20));
        setBackground(MapConstants.controlPanel);
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        BasicStroke lineStroke = new BasicStroke(MapConstants.lineSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2d.setStroke(lineStroke);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (lineType)
        {
            case VIEW_TWO_WAY:
                g2d.setColor(MapConstants.lineColorBiDirect);
                g2d.drawLine(5, (getHeight() / 2), getWidth() - 5, (getHeight() / 2));
                break;
            case VIEW_ONE_WAY:
                g2d.setColor(MapConstants.lineColorOneWay);
                g2d.drawLine(5, (getHeight() / 2), getWidth() - 5, (getHeight() / 2));
                break;
        }
    }
}
