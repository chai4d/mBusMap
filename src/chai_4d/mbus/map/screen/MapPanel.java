package chai_4d.mbus.map.screen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import chai_4d.mbus.map.bean.MapDbBean;
import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.constant.MapConstants.MapMode;
import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.constant.MapConstants.PointType;
import chai_4d.mbus.map.constant.MapConstants.ViewType;
import chai_4d.mbus.map.model.BusChoice;
import chai_4d.mbus.map.model.BusChoice.ABus;
import chai_4d.mbus.map.model.BusInfo;
import chai_4d.mbus.map.model.BusLine;
import chai_4d.mbus.map.model.BusPath;
import chai_4d.mbus.map.model.PointInfo;
import chai_4d.mbus.map.status.MapStatusBar;
import chai_4d.mbus.map.util.ImageUtil;
import chai_4d.mbus.map.util.MapCache;
import chai_4d.mbus.map.util.StringUtil;

public class MapPanel extends JComponent implements MouseInputListener, MouseWheelListener, KeyListener
{
    private static final long serialVersionUID = -3380532741187727970L;

    private MainFrame mainFrame = null;

    public static final int MAP_MAX_WIDTH = 3000;
    public static final int MAP_MAX_HEIGHT = 3000;

    public static final int MAP_PIECE_WIDTH = 1000;
    public static final int MAP_PIECE_HEIGHT = 1000;

    public static final int MAP_POINT_WIDTH = 400;
    public static final int MAP_POINT_HEIGHT = 400;

    private int startX = MapConstants.NULL;
    private int startY = MapConstants.NULL;
    private int endX = MapConstants.NULL;
    private int endY = MapConstants.NULL;

    private int dragX = MapConstants.NULL;
    private int dragY = MapConstants.NULL;
    private PointInfo dragPoint = null;

    private int tmpDragX = MapConstants.NULL;
    private int tmpDragY = MapConstants.NULL;

    private int tmpCountClick = 0;

    private MapCache mapCache = new MapCache(50);
    private Hashtable<String, List<PointInfo>> mapPoint = new Hashtable<String, List<PointInfo>>();

    private PointInfo pointSelect = null;
    private BusInfo busSelect = null;
    private List<BusChoice> busChoices = null;
    private int busChoiceIndex = 0;

    public MapPanel(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        initialize();
    }

    private void initialize()
    {
        setSize(new Dimension(800, 600));
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(650, 540));
        setFocusable(true);

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);
    }

    private void validateXY(Dimension size)
    {
        if (startX == MapConstants.NULL && startY == MapConstants.NULL)
        {
            startX = (MAP_MAX_WIDTH - size.width) / 2;
            startY = (MAP_MAX_HEIGHT - size.height) / 2;
        }
        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;
        if ((startX + size.width) >= MAP_MAX_WIDTH) startX = MAP_MAX_WIDTH - size.width;
        if ((startY + size.height) >= MAP_MAX_HEIGHT) startY = MAP_MAX_HEIGHT - size.height;

        endX = startX + size.width;
        endY = startY + size.height;
        if (size.width > 0) endX -= 1;
        if (size.height > 0) endY -= 1;
    }

    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        Dimension size = getSize();

        validateXY(size);
        drawMapBackground(g2d);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(1f));

        drawBusLine(g2d);
        drawPoint(g2d);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setStroke(new BasicStroke(1f));

        drawBusNo(g2d);

        updateStatusMessage();
        g2d.dispose();
    }

    private void drawMapBackground(Graphics2D g2d)
    {
        int mapPieceX1 = (startX / MAP_PIECE_WIDTH) + 1;
        int mapPieceX2 = (endX / MAP_PIECE_WIDTH) + 1;
        int mapPieceY1 = (startY / MAP_PIECE_HEIGHT) + 1;
        int mapPieceY2 = (endY / MAP_PIECE_HEIGHT) + 1;

        for (int y = mapPieceY1; y <= mapPieceY2; y++)
        {
            for (int x = mapPieceX1; x <= mapPieceX2; x++)
            {
                String mapNumber = (x < 10 ? "0" + x : "" + x) + (y < 10 ? "0" + y : "" + y);
                String mapName = "/maps/map" + mapNumber + ".gif";

                ImageIcon icon = null;
                if (mapCache.containsKey(mapName))
                {
                    icon = (ImageIcon) mapCache.get(mapName);
                }
                else
                {
                    icon = ImageUtil.createImageIcon(mapName);
                    mapCache.put(mapName, icon);
                }

                if (icon != null)
                {
                    int pieceX = (x - 1) * MAP_PIECE_WIDTH;
                    int pieceY = (y - 1) * MAP_PIECE_HEIGHT;
                    icon.paintIcon(this, g2d, pieceX - startX, pieceY - startY);
                }
            }
        }
    }

    private int xCor(int len, double dir)
    {
        return (int) (len * Math.sin(dir));
    }

    private int yCor(int len, double dir)
    {
        return (int) (len * Math.cos(dir));
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2, float stroke)
    {
        double aDir = Math.atan2(x1 - x2, y1 - y2);
        g2d.setStroke(new BasicStroke(1f));
        Polygon tmpPoly = new Polygon();
        int i1 = 12 + (int) (stroke * 2);
        int i2 = 6 + (int) stroke;
        tmpPoly.addPoint(x2, y2);
        tmpPoly.addPoint(x2 + xCor(i1, aDir + .5), y2 + yCor(i1, aDir + .5));
        tmpPoly.addPoint(x2 + xCor(i2, aDir), y2 + yCor(i2, aDir));
        tmpPoly.addPoint(x2 + xCor(i1, aDir - .5), y2 + yCor(i1, aDir - .5));
        tmpPoly.addPoint(x2, y2);
        g2d.fillPolygon(tmpPoly);
    }

    private void drawBusLine(Graphics2D g2d)
    {
        if (busSelect != null)
        {
            Stroke oldStroke = g2d.getStroke();
            BasicStroke lineStroke = new BasicStroke(MapConstants.lineSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

            Hashtable<Long, Color> hLineColor = new Hashtable<Long, Color>();
            List<BusLine> mapBusLine = busSelect.getBusLine();
            for (int i = 0; i < mapBusLine.size(); i++)
            {
                BusLine busLine = mapBusLine.get(i);
                if (busLine.getMode() == Mode.DELETE)
                {
                    continue;
                }

                Color hColor = null;
                if (mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
                {
                    if (hLineColor.containsKey(busLine.getBusId()))
                    {
                        hColor = hLineColor.get(busLine.getBusId());
                    }
                    else
                    {
                        hColor = MapConstants.lineColor[(hLineColor.size() % MapConstants.lineColor.length)];
                        hLineColor.put(busLine.getBusId(), hColor);
                    }
                }

                int x1 = busLine.getX1() - startX;
                int y1 = busLine.getY1() - startY;
                int x2 = busLine.getX2() - startX;
                int y2 = busLine.getY2() - startY;

                switch (busLine.getType())
                {
                    case BIDIRECT:
                        if ((mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
                            && !mainFrame.containsViewType(ViewType.VIEW_TWO_WAY))
                        {
                            break;
                        }
                        if (mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
                        {
                            g2d.setColor(hColor);
                        }
                        else
                        {
                            g2d.setColor(MapConstants.lineColorBiDirect);
                        }
                        g2d.setStroke(lineStroke);
                        g2d.drawLine(x1, y1, x2, y2);
                        break;
                    case P1_P2:
                        if ((mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
                            && !mainFrame.containsViewType(ViewType.VIEW_ONE_WAY))
                        {
                            break;
                        }
                        if (mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
                        {
                            g2d.setColor(hColor);
                        }
                        else
                        {
                            g2d.setColor(MapConstants.lineColorOneWay);
                        }
                        g2d.setStroke(lineStroke);
                        g2d.drawLine(x1, y1, x2, y2);
                        drawArrow(g2d, x1, y1, x2, y2, MapConstants.lineSize);
                        break;
                    case P2_P1:
                        if ((mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
                            && !mainFrame.containsViewType(ViewType.VIEW_ONE_WAY))
                        {
                            break;
                        }
                        if (mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
                        {
                            g2d.setColor(hColor);
                        }
                        else
                        {
                            g2d.setColor(MapConstants.lineColorOneWay);
                        }
                        g2d.setStroke(lineStroke);
                        g2d.drawLine(x2, y2, x1, y1);
                        drawArrow(g2d, x2, y2, x1, y1, MapConstants.lineSize);
                        break;
                }
            }

            g2d.setStroke(oldStroke);
        }
        else if (busChoices != null)
        {
            Stroke oldStroke = g2d.getStroke();
            BasicStroke lineStroke = new BasicStroke(MapConstants.lineSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

            BusChoice busChoice = busChoices.get(busChoiceIndex);
            List<ABus> buses = busChoice.getBuses();
            for (int i = 0; i < buses.size(); i++)
            {
                ABus aBus = buses.get(i);
                List<BusPath> busPaths = aBus.getBusPaths();
                for (int j = 0; j < busPaths.size(); j++)
                {
                    BusPath busPath = busPaths.get(j);

                    int x1 = busPath.getX1() - startX;
                    int y1 = busPath.getY1() - startY;
                    int x2 = busPath.getX2() - startX;
                    int y2 = busPath.getY2() - startY;

                    g2d.setColor(MapConstants.lineColor[i % MapConstants.lineColor.length]);
                    g2d.setStroke(lineStroke);
                    g2d.drawLine(x1, y1, x2, y2);
                    drawArrow(g2d, x1, y1, x2, y2, MapConstants.lineSize);
                }
            }

            g2d.setStroke(oldStroke);
        }
    }

    private void drawPointOut(Graphics2D g2d, int x, int y, PointInfo pointInfo)
    {
        Shape shape1 = new Ellipse2D.Double(
            x - (MapConstants.pointSizeOut / 2),
            y - (MapConstants.pointSizeOut / 2),
            MapConstants.pointSizeOut,
            MapConstants.pointSizeOut);
        if (pointInfo.equals(pointSelect))
        {
            g2d.setColor(MapConstants.pointColorSelect);
        }
        else
        {
            g2d.setColor(MapConstants.pointColorOut);
        }
        g2d.fill(shape1);
    }

    private void drawPointIn(Graphics2D g2d, int x, int y)
    {
        Shape shape2 = new Ellipse2D.Double(
            x - (MapConstants.pointSizeIn / 2),
            y - (MapConstants.pointSizeIn / 2),
            MapConstants.pointSizeIn,
            MapConstants.pointSizeIn);
        g2d.setColor(MapConstants.pointColorIn);
        g2d.fill(shape2);
    }

    private void drawPoint(Graphics2D g2d)
    {
        int mapPointX1 = (startX / MAP_POINT_WIDTH) + 1;
        int mapPointX2 = (endX / MAP_POINT_WIDTH) + 1;
        int mapPointY1 = (startY / MAP_POINT_HEIGHT) + 1;
        int mapPointY2 = (endY / MAP_POINT_HEIGHT) + 1;

        Stroke oldStroke = g2d.getStroke();

        for (int y = mapPointY1; y <= mapPointY2; y++)
        {
            for (int x = mapPointX1; x <= mapPointX2; x++)
            {
                String mapPointKey = (x < 10 ? "0" + x : "" + x) + (y < 10 ? "0" + y : "" + y);

                List<PointInfo> points = null;
                if (mapPoint.containsKey(mapPointKey))
                {
                    points = mapPoint.get(mapPointKey);
                }
                else
                {
                    points = MapDbBean.loadMapPoint(x, y);
                    mapPoint.put(mapPointKey, points);
                }

                for (int i = 0; i < points.size(); i++)
                {
                    PointInfo pointInfo = points.get(i);
                    if (pointInfo.getMode() == Mode.DELETE)
                    {
                        continue;
                    }

                    int p_x = pointInfo.getAxisX() - startX;
                    int p_y = pointInfo.getAxisY() - startY;

                    if (pointInfo.getType() == PointType.LINK)
                    {
                        if (!((mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
                            && !mainFrame.containsViewType(ViewType.VIEW_POINT_LINK)))
                        {
                            drawPointOut(g2d, p_x, p_y, pointInfo);
                        }
                    }
                    else if (pointInfo.getType() == PointType.NAME)
                    {
                        if (!((mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
                            && !mainFrame.containsViewType(ViewType.VIEW_POINT_NAME)))
                        {
                            drawPointOut(g2d, p_x, p_y, pointInfo);
                            drawPointIn(g2d, p_x, p_y);
                        }
                    }
                }
            }
        }

        g2d.setStroke(oldStroke);
    }

    private void drawBusNo(Graphics2D g2d)
    {
        if (busSelect != null && (mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
            && mainFrame.containsViewType(ViewType.VIEW_BUS_NO))
        {
            LinkedHashMap<String, String> hLabel = new LinkedHashMap<String, String>();
            Hashtable<Long, BusInfo> hBusNo = new Hashtable<Long, BusInfo>();
            List<BusLine> mapBusLine = busSelect.getBusLine();
            for (int i = 0; i < mapBusLine.size(); i++)
            {
                BusLine busLine = mapBusLine.get(i);
                if (busLine.getMode() == Mode.DELETE)
                {
                    continue;
                }

                BusInfo busInfo = null;
                if (hBusNo.containsKey(busLine.getBusId()))
                {
                    busInfo = hBusNo.get(busLine.getBusId());
                }
                else
                {
                    busInfo = MapDbBean.loadBusInfoById(busLine.getBusId());
                    hBusNo.put(busLine.getBusId(), busInfo);
                }

                int x1 = busLine.getX1() - startX;
                int y1 = busLine.getY1() - startY;
                int x2 = busLine.getX2() - startX;
                int y2 = busLine.getY2() - startY;

                String sBus = null;
                if (busInfo.getBusNoTh().equals(busInfo.getBusNoEn()))
                {
                    sBus = busInfo.getBusNoTh();
                }
                else
                {
                    sBus = busInfo.getBusNoTh() + "/" + busInfo.getBusNoEn();
                }
                int x = x1 + (x2 - x1) / 2;
                int y = y1 + (y2 - y1) / 2;

                String key = x + "|" + y;
                String label = null;
                if (hLabel.containsKey(key))
                {
                    label = hLabel.get(key) + "," + sBus;
                }
                else
                {
                    label = sBus;
                }
                hLabel.put(key, label);
            }

            g2d.setFont(new Font("Tahoma", Font.PLAIN, 10));

            Iterator<String> it = hLabel.keySet().iterator();
            while (it.hasNext())
            {
                String key = it.next();
                String label = hLabel.get(key);

                StringTokenizer token = new StringTokenizer(key, "|");
                int x = Integer.parseInt(token.nextToken());
                int y = Integer.parseInt(token.nextToken());

                int w = g2d.getFontMetrics().stringWidth(label);
                int h = (int) g2d.getFontMetrics().getLineMetrics(label, g2d).getHeight();

                g2d.setColor(MapConstants.labelBG);
                g2d.fillRect(x - (w / 2) - 1, y - h + 2, w + 2, h);

                g2d.setColor(MapConstants.labelFG);
                g2d.drawString(label, x - (w / 2), y);
            }
        }
    }

    private void updateStatusMessage()
    {
        String msg = "";
        if (dragX != MapConstants.NULL && dragY != MapConstants.NULL)
        {
            msg = "Moving the map to (" + StringUtil.toNumString(startX) + " : " + StringUtil.toNumString(startY) + " - "
                + StringUtil.toNumString(endX) + " : " + StringUtil.toNumString(endY) + ") of (" + StringUtil.toNumString(MAP_MAX_WIDTH) + " : "
                + StringUtil.toNumString(MAP_MAX_HEIGHT) + ")";
        }
        else if (dragPoint != null)
        {
            msg = "Moving the point to (" + StringUtil.toNumString(dragPoint.getAxisX()) + " : " + StringUtil.toNumString(dragPoint.getAxisY()) + ")";
        }
        else if (pointSelect != null)
        {
            if (pointSelect.getType() == PointType.LINK)
            {
                msg =
                    "Linked Point (" + StringUtil.toNumString(pointSelect.getAxisX()) + " : " + StringUtil.toNumString(pointSelect.getAxisY()) + ")";
            }
            else if (pointSelect.getType() == PointType.NAME)
            {
                msg = "Named Point (" + StringUtil.toNumString(pointSelect.getAxisX()) + " : " + StringUtil.toNumString(pointSelect.getAxisY()) + ")";
            }
        }
        else
        {
            msg = "Now showing (" + StringUtil.toNumString(startX) + " : " + StringUtil.toNumString(startY) + " - " + StringUtil.toNumString(endX)
                + " : " + StringUtil.toNumString(endY) + ") of (" + StringUtil.toNumString(MAP_MAX_WIDTH) + " : "
                + StringUtil.toNumString(MAP_MAX_HEIGHT) + ")";
        }
        MapStatusBar.setMessage(msg);
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 1)
        {
            tmpCountClick = e.getClickCount();
            final MouseEvent e2 = e;
            new Thread()
            {
                public void run()
                {
                    try
                    {
                        Thread.sleep(250);
                    }
                    catch (Exception ignore)
                    {
                    }
                    if (tmpCountClick == 1)
                    {
                        tmpCountClick = 0;
                        oneClicked(e2);
                    }
                }
            }.start();
        }
        else if (e.getClickCount() == 2 && tmpCountClick == 1)
        {
            tmpCountClick = e.getClickCount();
            doubleClicked(e);
        }
    }

    private void oneClicked(MouseEvent e)
    {
        if (e.getButton() == MapConstants.MOUSE_BUTTON_LEFT)
        {
            if (mainFrame.getMapMode() == MapMode.ADD_BUS || mainFrame.getMapMode() == MapMode.EDIT_BUS)
            {
                PointInfo aPoint = addPoint(e.getX(), e.getY());
                if (pointSelect == null)
                {
                    pointSelect = aPoint;
                }
                else
                {
                    addBusLine(pointSelect, aPoint);
                    pointSelect = aPoint;
                }
                mainFrame.onSelectPoint(pointSelect);
                repaint();
            }
            else if (mainFrame.getMapMode() == MapMode.EDIT_POINT || mainFrame.getMapMode() == MapMode.VIEW
                || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
            {
                PointInfo aPoint = findPoint(e.getX(), e.getY());
                if (aPoint != null)
                {
                    pointSelect = aPoint;
                    mainFrame.onSelectPoint(pointSelect);
                    repaint();
                }
            }
        }
        else if (e.getButton() == MapConstants.MOUSE_BUTTON_RIGHT)
        {
            if (mainFrame.getMapMode() == MapMode.ADD_BUS || mainFrame.getMapMode() == MapMode.EDIT_BUS
                || mainFrame.getMapMode() == MapMode.EDIT_POINT)
            {
                PointInfo aPoint = findPoint(e.getX(), e.getY());
                if (aPoint != null)
                {
                    removePoint(aPoint);
                    pointSelect = null;
                    mainFrame.onSelectPoint(pointSelect);
                    repaint();
                }
            }
        }
    }

    private void doubleClicked(MouseEvent e)
    {
        if (e.getButton() == MapConstants.MOUSE_BUTTON_LEFT)
        {
            if (mainFrame.getMapMode() == MapMode.ADD_BUS || mainFrame.getMapMode() == MapMode.EDIT_BUS
                || mainFrame.getMapMode() == MapMode.EDIT_POINT)
            {
                mainFrame.onSave();
            }
        }
        else if (e.getButton() == MapConstants.MOUSE_BUTTON_RIGHT)
        {
            if (mainFrame.getMapMode() == MapMode.ADD_BUS || mainFrame.getMapMode() == MapMode.EDIT_BUS
                || mainFrame.getMapMode() == MapMode.EDIT_POINT)
            {
                mainFrame.onReset();
            }
        }
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
        MapStatusBar.setCoordinate("");
    }

    public void mousePressed(MouseEvent e)
    {
        if (e.getButton() == MapConstants.MOUSE_BUTTON_LEFT && e.getClickCount() == 1)
        {
            if (mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
            {
                dragX = e.getX();
                dragY = e.getY();
            }
            else if (mainFrame.getMapMode() == MapMode.ADD_BUS || mainFrame.getMapMode() == MapMode.EDIT_BUS
                || mainFrame.getMapMode() == MapMode.EDIT_POINT)
            {
                PointInfo aPoint = findPoint(e.getX(), e.getY());
                if (aPoint == null)
                {
                    dragX = e.getX();
                    dragY = e.getY();
                }
                else
                {
                    dragPoint = aPoint;
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if (this.contains(e.getPoint()) == false)
        {
            MapStatusBar.setCoordinate("");
        }

        if (e.getButton() == MapConstants.MOUSE_BUTTON_LEFT && e.getClickCount() == 1)
        {
            if (mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE)
            {
                dragX = MapConstants.NULL;
                dragY = MapConstants.NULL;
                updateStatusMessage();
            }
            else if (mainFrame.getMapMode() == MapMode.ADD_BUS || mainFrame.getMapMode() == MapMode.EDIT_BUS
                || mainFrame.getMapMode() == MapMode.EDIT_POINT)
            {
                dragX = MapConstants.NULL;
                dragY = MapConstants.NULL;
                dragPoint = null;
                updateStatusMessage();
            }
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        MapStatusBar.setCoordinate(StringUtil.toNumString(e.getX()) + " : " + StringUtil.toNumString(e.getY()));

        dragX = MapConstants.NULL;
        dragY = MapConstants.NULL;
        updateStatusMessage();
    }

    private void movingMap(int x, int y)
    {
        MapStatusBar.setCoordinate(StringUtil.toNumString(x) + " : " + StringUtil.toNumString(y));

        if (mainFrame.getMapMode() == MapMode.VIEW || mainFrame.getMapMode() == MapMode.TEST_ROUTE || mainFrame.getMapMode() == MapMode.ADD_BUS
            || mainFrame.getMapMode() == MapMode.EDIT_BUS || mainFrame.getMapMode() == MapMode.EDIT_POINT)
        {
            if (dragX != MapConstants.NULL && dragY != MapConstants.NULL)
            {
                int diffX = x - dragX;
                int diffY = y - dragY;
                dragX = x;
                dragY = y;

                startX -= diffX;
                startY -= diffY;
                repaint();
            }
        }
        if (mainFrame.getMapMode() == MapMode.ADD_BUS || mainFrame.getMapMode() == MapMode.EDIT_BUS || mainFrame.getMapMode() == MapMode.EDIT_POINT)
        {
            if (dragPoint != null)
            {
                List<PointInfo> oldMapPoint = selectMapPoint(dragPoint.getAxisX(), dragPoint.getAxisY());

                dragPoint.setAxisX(x + startX);
                dragPoint.setAxisY(y + startY);
                dragPoint.setEdited();
                if (dragPoint.equals(pointSelect))
                {
                    mainFrame.onSelectPoint(pointSelect);
                }

                List<PointInfo> newMapPoint = selectMapPoint(dragPoint.getAxisX(), dragPoint.getAxisY());
                if (!oldMapPoint.equals(newMapPoint))
                {
                    oldMapPoint.remove(dragPoint);
                    newMapPoint.add(dragPoint);
                }

                List<BusLine> busLines = findBusLines(dragPoint);
                for (int i = 0; i < busLines.size(); i++)
                {
                    BusLine busLine = busLines.get(i);
                    if (busLine.getP1Id() == dragPoint.getPId())
                    {
                        busLine.setX1(dragPoint.getAxisX());
                        busLine.setY1(dragPoint.getAxisY());
                    }
                    else if (busLine.getP2Id() == dragPoint.getPId())
                    {
                        busLine.setX2(dragPoint.getAxisX());
                        busLine.setY2(dragPoint.getAxisY());
                    }
                }
                repaint();
            }
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        movingMap(e.getX(), e.getY());
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
        {
            dragX = e.getX();
            dragY = e.getY();

            if (e.isShiftDown())
            {
                tmpDragX = dragX - e.getUnitsToScroll();
                tmpDragY = dragY;
            }
            else
            {
                tmpDragX = dragX;
                tmpDragY = dragY - e.getUnitsToScroll();
            }
            movingMap(tmpDragX, tmpDragY);
        }
    }

    public void keyTyped(KeyEvent e)
    {
    }

    public void keyPressed(KeyEvent e)
    {
        onPressed(e);
    }

    public void keyReleased(KeyEvent e)
    {
    }

    private void onPressed(KeyEvent e)
    {
        if (busChoices != null)
        {
            int keyCode = e.getKeyCode();
            if (e.isActionKey() && (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN))
            {
                int maxIndex = busChoices.size() - 1;
                int nowIndex = this.busChoiceIndex;

                if (keyCode == KeyEvent.VK_UP)
                {
                    nowIndex--;
                }
                else if (keyCode == KeyEvent.VK_DOWN)
                {
                    nowIndex++;
                }

                if (nowIndex > maxIndex)
                {
                    nowIndex = 0;
                }
                else if (nowIndex < 0)
                {
                    nowIndex = maxIndex;
                }
                this.busChoiceIndex = nowIndex;
                repaint();
            }
        }
    }

    public void refreshMode()
    {
        MapMode mode = mainFrame.getMapMode();
        switch (mode)
        {
            case VIEW:
                pointSelect = null;
                busSelect = null;
                busChoices = null;
                break;
            case TEST_ROUTE:
                pointSelect = null;
                busSelect = null;
                //busChoices = new ArrayList<BusChoice>(); // From Popup Route Select Screen
                break;
            case ADD_BUS:
                pointSelect = null;
                busSelect = new BusInfo();
                busChoices = null;
                break;
            case EDIT_BUS:
                pointSelect = null;
                //busSelect = new BusInfo(); // From Popup Bus Select screen
                busChoices = null;
                break;
            case DEL_BUS:
                pointSelect = null;
                //busSelect = new BusInfo(); // From Popup Bus Select screen
                busChoices = null;
                break;
            case EDIT_POINT:
                pointSelect = null;
                busSelect = null;
                busChoices = null;
                break;
        }
        mainFrame.onSelectPoint(pointSelect);
        mainFrame.onSelectBus(busSelect);
        repaint();
    }

    private List<PointInfo> selectMapPoint(int x, int y)
    {
        int mapPointX = (x / MAP_POINT_WIDTH) + 1;
        int mapPointY = (y / MAP_POINT_HEIGHT) + 1;
        String mapPointKey = (mapPointX < 10 ? "0" + mapPointX : "" + mapPointX) + (mapPointY < 10 ? "0" + mapPointY : "" + mapPointY);

        List<PointInfo> points = mapPoint.get(mapPointKey);
        if (points == null)
        {
            points = new ArrayList<PointInfo>();
            mapPoint.put(mapPointKey, points);
        }
        return points;
    }

    private PointInfo findPoint(int x, int y)
    {
        List<PointInfo> points = selectMapPoint(x + startX, y + startY);
        for (int i = 0; i < points.size(); i++)
        {
            PointInfo pointInfo = points.get(i);
            if (pointInfo.getMode() == Mode.DELETE)
            {
                continue;
            }

            int p_x = pointInfo.getAxisX() - startX;
            int p_y = pointInfo.getAxisY() - startY;
            int area = MapConstants.pointSizeOut / 2;

            if ((p_x - area) <= x && x <= (p_x + area) && (p_y - area) <= y && y <= (p_y + area))
            {
                return pointInfo;
            }
        }
        return null;
    }

    private PointInfo addPoint(int x, int y)
    {
        PointInfo aPoint = findPoint(x, y);
        if (aPoint == null)
        {
            aPoint = new PointInfo(x + startX, y + startY);
            List<PointInfo> points = selectMapPoint(x + startX, y + startY);
            points.add(aPoint);
        }
        return aPoint;
    }

    private void removePoint(PointInfo aPoint)
    {
        List<BusLine> busLines = findBusLines(aPoint);
        if (busLines.size() > 0)
        {
            for (int i = 0; i < busLines.size(); i++)
            {
                BusLine busLine = busLines.get(i);
                removeBusLine(busLine);
            }
        }
        else
        {
            if (aPoint.getMode() == Mode.INSERT)
            {
                List<PointInfo> points = selectMapPoint(aPoint.getAxisX(), aPoint.getAxisY());
                points.remove(aPoint);
            }
            else
            {
                aPoint.setMode(Mode.DELETE);
            }
        }
    }

    private List<BusLine> findBusLines(PointInfo p)
    {
        List<BusLine> result = new ArrayList<BusLine>();
        if (busSelect != null)
        {
            List<BusLine> mapBusLine = busSelect.getBusLine();
            for (int i = 0; mapBusLine != null && i < mapBusLine.size(); i++)
            {
                BusLine busLine = mapBusLine.get(i);
                if (busLine.getMode() == Mode.DELETE)
                {
                    continue;
                }

                if (busLine.getP1Id() == p.getPId() || busLine.getP2Id() == p.getPId())
                {
                    result.add(busLine);
                }
            }
        }
        return result;
    }

    private BusLine findBusLine(PointInfo p1, PointInfo p2)
    {
        if (busSelect != null)
        {
            List<BusLine> mapBusLine = busSelect.getBusLine();
            for (int i = 0; mapBusLine != null && i < mapBusLine.size(); i++)
            {
                BusLine busLine = mapBusLine.get(i);
                if (busLine.getMode() == Mode.DELETE)
                {
                    continue;
                }

                if ((busLine.getP1Id() == p1.getPId() && busLine.getP2Id() == p2.getPId())
                    || (busLine.getP1Id() == p2.getPId() && busLine.getP2Id() == p1.getPId()))
                {
                    return busLine;
                }
            }
        }
        return null;
    }

    private BusLine addBusLine(PointInfo p1, PointInfo p2)
    {
        if (p1.equals(p2))
        {
            return null;
        }
        BusLine aBusLine = findBusLine(p1, p2);
        if (aBusLine == null)
        {
            aBusLine = new BusLine(
                p1.getPId(),
                p2.getPId(),
                p1.getAxisX(),
                p1.getAxisY(),
                p2.getAxisX(),
                p2.getAxisY(),
                StringUtil.distance(p1.getAxisX(), p1.getAxisY(), p2.getAxisX(), p2.getAxisY()),
                busSelect.getBusId(),
                busSelect.getBusNoTh(),
                busSelect.getBusNoEn(),
                busSelect.getBusPrice(),
                mainFrame.getLineType());
            busSelect.getBusLine().add(aBusLine);
            busSelect.setEdited();
            mainFrame.onSelectBus(busSelect);
        }
        return aBusLine;
    }

    private void removeBusLine(BusLine aBusLine)
    {
        if (aBusLine.getMode() == Mode.INSERT)
        {
            busSelect.getBusLine().remove(aBusLine);
        }
        else
        {
            aBusLine.setMode(Mode.DELETE);
        }
        busSelect.setEdited();
        mainFrame.onSelectBus(busSelect);
    }

    public Hashtable<String, List<PointInfo>> getMapPoint()
    {
        return mapPoint;
    }

    public BusInfo getBusSelect()
    {
        return busSelect;
    }

    public void setBusSelect(BusInfo busSelect)
    {
        this.busSelect = busSelect;
    }

    public List<BusChoice> getBusChoices()
    {
        return busChoices;
    }

    public void setBusChoices(List<BusChoice> busChoices)
    {
        this.busChoices = busChoices;
        this.busChoiceIndex = 0;
    }

    public int getStartX()
    {
        return startX;
    }

    public void setStartX(int startX)
    {
        this.startX = startX;
    }

    public int getStartY()
    {
        return startY;
    }

    public void setStartY(int startY)
    {
        this.startY = startY;
    }
}
