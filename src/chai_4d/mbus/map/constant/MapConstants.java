package chai_4d.mbus.map.constant;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;

public class MapConstants
{
    public static final int NULL = -9999;

    public static final int MOUSE_BUTTON_LEFT = MouseEvent.BUTTON1;
    public static final int MOUSE_BUTTON_RIGHT = MouseEvent.BUTTON3;

    public static final int pointSizeOut = 14;
    public static final int pointSizeIn = 10;

    public static final int lineSize = 6;

    public static final Color controlPanel = SystemColor.control;

    public static final Color pointColorOut = new Color(0, 128, 255, 80);
    public static final Color pointColorIn = Color.white;
    public static final Color pointColorSelect = new Color(0, 128, 255);

    public static final Color lineColorBiDirect = new Color(0, 128, 255, 80);
    public static final Color lineColorOneWay = new Color(255, 128, 128, 150);
    public static final Color lineColorRoute = new Color(125, 245, 0);

    public static final Color[] lineColor = new Color[] {
        new Color(0, 128, 255, 120),
        new Color(255, 128, 0, 135),
        new Color(0, 128, 64, 90),
        new Color(255, 0, 128, 120),
        new Color(128, 0, 255, 100),
        new Color(128, 0, 0, 90),
        new Color(230, 230, 50, 130),
        new Color(64, 128, 128, 120),
        new Color(128, 128, 0, 90),
        new Color(128, 128, 128, 110) };

    public static final Color labelBG = new Color(255, 255, 255, 150);
    public static final Color labelFG = Color.black;

    public static final int MAX_CHOICES = 5000;
    public static final int PREFER_CHOICES = 5;

    public static final Color labelBusBG = new Color(255, 255, 255, 200);
    public static final Color labelBusFG = Color.black;
    public static final Color labelBusBGHL = new Color(122, 200, 255, 200);
    public static final Color labelBusFGHL = Color.blue;

    public static final double DISTANCE_RATIO = 2.5;

    private MapConstants()
    {
    }

    public enum Mode
    {
        SELECT, INSERT, UPDATE, DELETE
    }

    public enum MapMode
    {
        VIEW, TEST_ROUTE, ADD_BUS, EDIT_BUS, DEL_BUS, COPY_BUS, EDIT_POINT
    }

    public enum ViewType
    {
        VIEW_POINT_LINK, VIEW_POINT_NAME, VIEW_TWO_WAY, VIEW_ONE_WAY, VIEW_BUS_NO
    }

    public enum ViewBusLine
    {
        VIEW_SELECTED, VIEW_NONE
    }

    public enum PointType
    {
        LINK(1), NAME(2);

        private final int value;

        PointType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public static PointType valueOf(int value)
        {
            for (int i = 0; i < values().length; i++)
            {
                PointType aItem = values()[i];
                if (aItem.getValue() == value)
                {
                    return aItem;
                }
            }
            return null;
        }
    }

    public enum LineType
    {
        BIDIRECT(0), P1_P2(1), P2_P1(2);

        private final int value;

        LineType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public static LineType valueOf(int value)
        {
            for (int i = 0; i < values().length; i++)
            {
                LineType aItem = values()[i];
                if (aItem.getValue() == value)
                {
                    return aItem;
                }
            }
            return null;
        }
    }
}
