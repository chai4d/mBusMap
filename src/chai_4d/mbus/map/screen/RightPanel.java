package chai_4d.mbus.map.screen;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import chai_4d.mbus.map.constant.MapConstants;

public class RightPanel extends JPanel
{
    private static final long serialVersionUID = 8816883815657054856L;

    private MainFrame mainFrame = null;

    private static final int MAX_WIDTH = 200;

    private BusPanel busPanel = null;
    private PointPanel pointPanel = null;

    public RightPanel(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize()
    {
        setSize(new Dimension(MAX_WIDTH, 600));
        setPreferredSize(new Dimension(MAX_WIDTH, 600));
        setMinimumSize(new Dimension(MAX_WIDTH, 600));
        setLayout(new GridBagLayout());
        setBackground(MapConstants.controlPanel);

        busPanel = new BusPanel(mainFrame);
        pointPanel = new PointPanel(mainFrame);

        add(busPanel, new GridBagConstraints(
            0,
            0,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL,
            new Insets(2, 2, 2, 2),
            0,
            0));
        add(new JSeparator(SwingConstants.HORIZONTAL), new GridBagConstraints(
            0,
            1,
            1,
            1,
            1.0,
            0.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.HORIZONTAL,
            new Insets(2, 2, 2, 2),
            0,
            0));
        add(
            pointPanel,
            new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    public BusPanel getBusPanel()
    {
        return busPanel;
    }

    public PointPanel getPointPanel()
    {
        return pointPanel;
    }
}
