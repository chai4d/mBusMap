package chai_4d.mbus.map.util;

import java.text.NumberFormat;

public class StringUtil
{
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

    public static double distance(int x1, int y1, int x2, int y2)
    {
        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);
        double result = Math.sqrt((dx * dx) + (dy * dy));
        return result;
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