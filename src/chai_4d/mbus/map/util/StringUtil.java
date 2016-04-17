package chai_4d.mbus.map.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import chai_4d.mbus.map.constant.MapConstants;

public class StringUtil
{
    public static final String NEW_LINE = System.getProperty("line.separator");

    public static final String ENCODING_ISO88591 = "ISO-8859-1";
    public static final String ENCODING_UTF8 = "UTF-8";

    private static final DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###,###,###,###,###,###.##");

    private StringUtil()
    {
    }

    public static boolean isEmpty(String s)
    {
        if (s == null || s.trim().equals(""))
        {
            return true;
        }
        return false;
    }

    public static boolean isNumEmpty(String s)
    {
        if (s == null || s.trim().equals("") || s.trim().equals("0"))
        {
            return true;
        }
        return false;
    }

    public static String toString(String s)
    {
        return s == null ? "" : s.trim();
    }

    public static String toString(String s, String def)
    {
        return s == null ? toString(def) : s.trim();
    }

    public static String toStringUTF8(String s)
    {
        String result = toString(s);
        try
        {
            result = new String(result.getBytes(ENCODING_ISO88591), ENCODING_UTF8);
        }
        catch (Exception e)
        {
        }
        return result;
    }

    public static int toInt(String s)
    {
        int result = 0;
        try
        {
            result = Integer.parseInt(toString(s));
        }
        catch (Throwable t)
        {
        }
        return result;
    }

    public static int toInt(String s, int def)
    {
        int result = def;
        try
        {
            result = Integer.parseInt(toString(s));
        }
        catch (Throwable t)
        {
        }
        return result;
    }

    public static long toLong(String s)
    {
        long result = 0;
        try
        {
            result = Long.parseLong(toString(s));
        }
        catch (Throwable t)
        {
        }
        return result;
    }

    public static long toLong(String s, long def)
    {
        long result = def;
        try
        {
            result = Long.parseLong(toString(s));
        }
        catch (Throwable t)
        {
        }
        return result;
    }

    public static double toDouble(String s)
    {
        double result = 0;
        try
        {
            result = Double.parseDouble(toString(s));
        }
        catch (Throwable t)
        {
        }
        return result;
    }

    public static double toDouble(String s, double def)
    {
        double result = def;
        try
        {
            result = Double.parseDouble(toString(s));
        }
        catch (Throwable t)
        {
        }
        return result;
    }

    public static String toNumString(String s)
    {
        return toNumString(toDouble(s));
    }

    public static String toNumString(long l)
    {
        return NumberFormat.getNumberInstance().format(l);
    }

    public static String toNumString(double d)
    {
        return NumberFormat.getNumberInstance().format(d);
    }

    public static String toAmountString(double d)
    {
        return decimalFormat.format(d);
    }

    public static double distance(int x1, int y1, int x2, int y2)
    {
        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);
        double result = Math.sqrt((dx * dx) + (dy * dy));
        return result;
    }

    public static String toDistance(double distance)
    {
        double metre = distance * MapConstants.DISTANCE_RATIO;
        if (metre >= 1000)
        {
            double km = (metre / 1000);
            return toAmountString(km) + " km.";
        }
        else
        {
            return toAmountString(metre) + " m.";
        }
    }

    public static long getIdValue(String text)
    {
        // text = "abc abc (id)"
        // return = "id"
        long result = 0;
        if (isEmpty(text) == false)
        {
            result = toLong(text.substring(text.lastIndexOf("(") + 1, text.length() - 1));
        }
        return result;
    }
}