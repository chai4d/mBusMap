package chai_4d.mbus.map.screen;

import java.util.List;

import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.model.BusInfo;
import chai_4d.mbus.map.model.BusLine;

public class MapFocus extends Thread
{
    private static final int GAP_WIDTH = 50;
    private static final int MAX_MOVE_MS = 1000;
    private static final int SLEEP_MS = 50;
    private static final int MOVE_STEP = 10;

    private MapPanel mapPanel = null;
    private BusInfo busSelect = null;

    public MapFocus(MapPanel mapPanel, BusInfo busSelect)
    {
        this.mapPanel = mapPanel;
        this.busSelect = busSelect;
    }

    public void run()
    {
        int startX = mapPanel.getStartX();
        int startY = mapPanel.getStartY();
        int newStartX = MapConstants.NULL;
        int newStartY = MapConstants.NULL;

        int focusX1 = MapConstants.NULL;
        int focusX2 = MapConstants.NULL;
        int focusY1 = MapConstants.NULL;
        int focusY2 = MapConstants.NULL;

        int pointX = MapConstants.NULL;
        int pointY = MapConstants.NULL;

        List<BusLine> mapBusLine = busSelect.getBusLine();
        for (int i = 0; i < mapBusLine.size(); i++)
        {
            BusLine busLine = mapBusLine.get(i);
            if (busLine.getMode() == Mode.DELETE)
            {
                continue;
            }

            if (focusX1 == MapConstants.NULL)
            {
                focusX1 = busLine.getX1();
            }
            if (focusX2 == MapConstants.NULL)
            {
                focusX2 = busLine.getX2();
            }
            if (focusY1 == MapConstants.NULL)
            {
                focusY1 = busLine.getY1();
            }
            if (focusY2 == MapConstants.NULL)
            {
                focusY2 = busLine.getY2();
            }

            if (pointX == MapConstants.NULL || pointY == MapConstants.NULL)
            {
                pointX = busLine.getX1();
                pointY = busLine.getY1();
            }

            focusX1 = Math.min(focusX1, busLine.getX1());
            focusX1 = Math.min(focusX1, busLine.getX2());
            focusY1 = Math.min(focusY1, busLine.getY1());
            focusY1 = Math.min(focusY1, busLine.getY2());

            focusX2 = Math.max(focusX2, busLine.getX1());
            focusX2 = Math.max(focusX2, busLine.getX2());
            focusY2 = Math.max(focusY2, busLine.getY1());
            focusY2 = Math.max(focusY2, busLine.getY2());

            if (pointX - busLine.getX1() + pointY - busLine.getY1() > 0)
            {
                pointX = busLine.getX1();
                pointY = busLine.getY1();
            }
            if (pointX - busLine.getX2() + pointY - busLine.getY2() > 0)
            {
                pointX = busLine.getX2();
                pointY = busLine.getY2();
            }
        }

        if (focusX1 != MapConstants.NULL && focusY1 != MapConstants.NULL && focusX2 != MapConstants.NULL && focusY2 != MapConstants.NULL)
        {
            int width = mapPanel.getWidth();
            int height = mapPanel.getHeight();

            boolean onX = (focusX2 - focusX1) < (width - GAP_WIDTH);
            boolean onY = (focusY2 - focusY1) < (height - GAP_WIDTH);

            if (onX && onY)
            {
                newStartX = focusX1 - ((width - (focusX2 - focusX1)) / 2);
                newStartY = focusY1 - ((height - (focusY2 - focusY1)) / 2);
            }
            else if (onX)
            {
                newStartX = focusX1 - ((width - (focusX2 - focusX1)) / 2);
                newStartY = focusY1 - GAP_WIDTH;
            }
            else if (onY)
            {
                newStartX = focusX1 - GAP_WIDTH;
                newStartY = focusY1 - ((height - (focusY2 - focusY1)) / 2);
            }
            else
            {
                newStartX = pointX - GAP_WIDTH;
                newStartY = pointY - GAP_WIDTH;
            }
        }

        if (newStartX != MapConstants.NULL && newStartY != MapConstants.NULL)
        {
            int distanceX = newStartX - startX;
            int distanceY = newStartY - startY;
            if (distanceX == 0 && distanceY == 0)
            {
                return;
            }

            int numStep = MAX_MOVE_MS / SLEEP_MS;
            int moveX = distanceX / numStep;
            int moveY = distanceY / numStep;
            while (numStep > 1 && Math.abs(moveX) < MOVE_STEP && Math.abs(moveY) < MOVE_STEP)
            {
                numStep--;
                moveX = distanceX / numStep;
                moveY = distanceY / numStep;
            }

            for (int i = 0; i < numStep; i++)
            {
                startX += moveX;
                startY += moveY;
                mapPanel.setStartX(startX);
                mapPanel.setStartY(startY);
                mapPanel.repaint();
                try
                {
                    Thread.sleep(SLEEP_MS);
                }
                catch (InterruptedException e)
                {
                }
            }
            if (startX != newStartX || startY != newStartY)
            {
                mapPanel.setStartX(newStartX);
                mapPanel.setStartY(newStartY);
                mapPanel.repaint();
            }
        }
    }
}
