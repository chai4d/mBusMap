package chai_4d.mbus.map.screen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import chai_4d.mbus.map.bean.MapDbBean;
import chai_4d.mbus.map.constant.MapConstants.LineType;
import chai_4d.mbus.map.constant.MapConstants.MapMode;
import chai_4d.mbus.map.constant.MapConstants.ViewBusLine;
import chai_4d.mbus.map.constant.MapConstants.ViewType;
import chai_4d.mbus.map.menu.MapMenuBar;
import chai_4d.mbus.map.model.BusChoice;
import chai_4d.mbus.map.model.BusInfo;
import chai_4d.mbus.map.model.PointInfo;
import chai_4d.mbus.map.status.MapStatusBar;
import chai_4d.mbus.map.util.DBManager;
import chai_4d.mbus.map.util.ImageUtil;
import chai_4d.mbus.map.util.SwingUtil;

public class MainFrame extends JFrame
{
    private static final long serialVersionUID = -7237892527602350628L;

    public static final String TITLE_NAME = "m-Bus Map Screen";
    public static final String VERSION = "1.0";
    public static final String AUTHOR = "Chai_4D";

    private static final String ICON_PATH = "/icons/mbus_icon.gif";

    private MapMenuBar mapMenuBar = null;
    private MapPanel mapPanel = null;
    private RightPanel rightPanel = null;
    private MapStatusBar mapStatusBar = null;

    private MapMode _mapMode = MapMode.VIEW;
    private LineType _lineType = LineType.BIDIRECT;
    private List<ViewType> _viewType = new ArrayList<ViewType>()
    {
        private static final long serialVersionUID = -6898590205538578737L;

        {
            add(ViewType.VIEW_POINT_LINK);
            add(ViewType.VIEW_POINT_NAME);
            add(ViewType.VIEW_TWO_WAY);
            add(ViewType.VIEW_ONE_WAY);
            add(ViewType.VIEW_BUS_NO);
        }
    };
    private ViewBusLine _viewBusLine = ViewBusLine.VIEW_NONE;

    private MainFrame()
    {
        super(TITLE_NAME + " " + VERSION);
        initialize();
    }

    private void initialize()
    {
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                onExit();
            }
        });

        ImageIcon icon = ImageUtil.createImageIcon(ICON_PATH);
        if (icon != null)
        {
            setIconImage(icon.getImage());
        }

        mapMenuBar = new MapMenuBar(this);
        setJMenuBar(mapMenuBar);

        mapPanel = new MapPanel(this);
        rightPanel = new RightPanel(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mapPanel, rightPanel);
        splitPane.setResizeWeight(1);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        mapStatusBar = new MapStatusBar(this);
        getContentPane().add(mapStatusBar, BorderLayout.SOUTH);

        setMapMode(_mapMode);
        setLineType(_lineType);
        setViewBusLine(_viewBusLine);

        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void onExit()
    {
        DBManager.destroy();
        System.exit(0);
    }

    public void onSave()
    {
        String result = null;
        switch (_mapMode)
        {
            case ADD_BUS:
                BusPanel busPanel = new BusPanel(this);
                busPanel.setPreferredSize(new Dimension(400, 370));
                busPanel.setBusInfo(mapPanel.getBusSelect());
                OptionDialog dialog = new OptionDialog(this, "Bus Info", busPanel);
                dialog.setVisible(true);

                if (dialog.isOK() == false)
                {
                    onSelectBus(mapPanel.getBusSelect());
                    return;
                }
                result = MapDbBean.saveBus(mapPanel.getMapPoint(), mapPanel.getBusSelect());
                if (!result.equals("true"))
                {
                    onSelectBus(mapPanel.getBusSelect());
                    SwingUtil.alertWarning(result);
                    return;
                }
                break;
            case EDIT_BUS:
                result = MapDbBean.saveBus(mapPanel.getMapPoint(), mapPanel.getBusSelect());
                if (!result.equals("true"))
                {
                    SwingUtil.alertWarning(result);
                    return;
                }
                break;
            case DEL_BUS:
                result = MapDbBean.deleteBus(mapPanel.getBusSelect());
                if (!result.equals("true"))
                {
                    SwingUtil.alertWarning(result);
                }
                break;
            case EDIT_POINT:
                result = MapDbBean.savePoint(mapPanel.getMapPoint());
                if (!result.equals("true"))
                {
                    SwingUtil.alertWarning(result);
                    return;
                }
                break;
        }
        setMapMode(MapMode.VIEW);
    }

    public void onReset()
    {
        mapPanel.getMapPoint().clear();
        setMapMode(MapMode.VIEW);
    }

    public void onCalculateData()
    {
        BusPathCalculator busPathCalculator = new BusPathCalculator(this, "Calculate Data");
        busPathCalculator.setVisible(true);
    }

    public void onTestRoute()
    {
        RouteSelectPanel routeSelectPanel = new RouteSelectPanel(this);
        OptionDialog dialog = new OptionDialog(this, "Route Select", routeSelectPanel);
        dialog.setVisible(true);

        if (dialog.isOK() == false)
        {
            return;
        }
        PointInfo sourcePoint = routeSelectPanel.getSourcePoint();
        PointInfo destinationPoint = routeSelectPanel.getDestinationPoint();
        List<BusChoice> busChoices = MapDbBean.calcBusChoices(sourcePoint.getPId(), destinationPoint.getPId());
        mapPanel.setBusChoices(busChoices);
        setMapMode(MapMode.TEST_ROUTE);
        new MapFocus(mapPanel, sourcePoint, destinationPoint).start();
    }

    public void onAddBusLine()
    {
        setMapMode(MapMode.ADD_BUS);
    }

    public void onEditBusLine()
    {
        BusSelectPanel busSelectPanel = new BusSelectPanel(this);
        OptionDialog dialog = new OptionDialog(this, "Bus Select", busSelectPanel);
        dialog.setVisible(true);

        if (dialog.isOK() == false)
        {
            return;
        }
        mapPanel.setBusSelect(busSelectPanel.getBusSelect());
        setMapMode(MapMode.EDIT_BUS);
        new MapFocus(mapPanel, mapPanel.getBusSelect()).start();
    }

    public void onDeleteBusLine()
    {
        BusSelectPanel busSelectPanel = new BusSelectPanel(this);
        OptionDialog dialog = new OptionDialog(this, "Bus Select", busSelectPanel);
        dialog.setVisible(true);

        if (dialog.isOK() == false)
        {
            return;
        }
        mapPanel.setBusSelect(busSelectPanel.getBusSelect());
        setMapMode(MapMode.DEL_BUS);
        new MapFocus(mapPanel, mapPanel.getBusSelect()).start();

        String msg = "Are you sure to delete this Bus Line?";
        int result = JOptionPane.showConfirmDialog(null, msg, "Confirm to delete the Bus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION)
        {
            onSave();
        }
        else
        {
            setMapMode(MapMode.VIEW);
        }
    }

    public void onEditPoint()
    {
        setMapMode(MapMode.EDIT_POINT);
    }

    public void onSelectPoint(PointInfo pointSelect)
    {
        rightPanel.getPointPanel().setPointInfo(pointSelect);
    }

    public void onSelectBus(BusInfo busSelect)
    {
        rightPanel.getBusPanel().setBusInfo(busSelect);
    }

    public MapMode getMapMode()
    {
        return _mapMode;
    }

    public void setMapMode(MapMode mode)
    {
        _mapMode = mode;
        mapMenuBar.setActiveMenuItem();
        if (_mapMode == MapMode.VIEW || _mapMode == MapMode.TEST_ROUTE)
        {
            _viewBusLine = ViewBusLine.VIEW_NONE;
            mapMenuBar.setActiveViewType();
        }
        mapPanel.refreshMode();
        MapStatusBar.setMode(_mapMode.name());
    }

    public LineType getLineType()
    {
        return _lineType;
    }

    public void setLineType(LineType type)
    {
        _lineType = type;
    }

    public boolean containsViewType(ViewType type)
    {
        return _viewType.contains(type);
    }

    public void addViewType(ViewType type)
    {
        _viewType.add(type);
        mapPanel.repaint();
    }

    public void removeViewType(ViewType type)
    {
        _viewType.remove(type);
        mapPanel.repaint();
    }

    public ViewBusLine getViewBusLine()
    {
        return _viewBusLine;
    }

    public void setViewBusLine(ViewBusLine busLine)
    {
        switch (busLine)
        {
            case VIEW_SELECTED:
                BusSelectViewPanel busSelectViewPanel = new BusSelectViewPanel(this);
                OptionDialog dialog = new OptionDialog(this, "Bus Select", busSelectViewPanel);
                dialog.setVisible(true);

                if (dialog.isOK() == false)
                {
                    if (mapPanel.getBusSelect() == null)
                    {
                        _viewBusLine = ViewBusLine.VIEW_NONE;
                        mapMenuBar.setActiveViewType();
                    }
                    return;
                }
                mapPanel.setBusSelect(busSelectViewPanel.getBusSelect());
                _viewBusLine = busLine;
                mapPanel.repaint();
                new MapFocus(mapPanel, mapPanel.getBusSelect()).start();
                break;
            case VIEW_NONE:
                mapPanel.setBusSelect(null);
                _viewBusLine = busLine;
                mapPanel.repaint();
                break;
        }
    }

    public MapMenuBar getMapMenuBar()
    {
        return mapMenuBar;
    }

    public MapPanel getMapPanel()
    {
        return mapPanel;
    }

    public RightPanel getRightPanel()
    {
        return rightPanel;
    }

    public MapStatusBar getMapStatusBar()
    {
        return mapStatusBar;
    }

    private static void setUIFont(FontUIResource f)
    {
        Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof FontUIResource) UIManager.put(key, f);
        }
    }

    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e)
        {
        }

        JFrame.setDefaultLookAndFeelDecorated(false);
        JDialog.setDefaultLookAndFeelDecorated(false);
        setUIFont(new FontUIResource("Tahoma", Font.PLAIN, 12));

        new MainFrame();
    }
}
