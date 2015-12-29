package chai_4d.mbus.map.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil
{
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private DateUtil()
    {
    }

    public static Date createDateTime(int dd, int mm, int yyyy, int hh, int mi, int ss)
    {
        if (dd <= 0 || mm <= 0 || yyyy <= 0 || hh < 0 || mi < 0 || ss < 0)
        {
            return null;
        }

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(yyyy - 543, mm - 1, dd, hh, mi, ss);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date createDate(int dd, int mm, int yyyy)
    {
        return createDateTime(dd, mm, yyyy, 0, 0, 0);
    }

    public static Date createTime(int hh, int mi, int ss)
    {
        return createDateTime(1, 1, 1000, hh, mi, ss);
    }

    public static Date createDate(String strDate)
    {
        if (StringUtil.isEmpty(strDate) || strDate.length() < 10)
        {
            return null;
        }

        int dd = StringUtil.toInt(strDate.substring(0, 2));
        int mm = StringUtil.toInt(strDate.substring(3, 5));
        int yyyy = StringUtil.toInt(strDate.substring(6, 10));

        return createDate(dd, mm, yyyy);
    }

    public static java.sql.Date createSQLDate(Date date)
    {
        if (date != null)
        {
            return new java.sql.Date(date.getTime());
        }
        else
        {
            return null;
        }
    }

    public static Date createDateTime(String strDate, int hh, int mi, int ss)
    {
        if (StringUtil.isEmpty(strDate) || strDate.length() < 10)
        {
            return null;
        }

        int dd = StringUtil.toInt(strDate.substring(0, 2));
        int mm = StringUtil.toInt(strDate.substring(3, 5));
        int yyyy = StringUtil.toInt(strDate.substring(6, 10));

        return createDateTime(dd, mm, yyyy, hh, mi, ss);
    }

    public static String getDateStr(Date date)
    {
        if (date == null)
        {
            return "";
        }
        else
        {
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTime(date);

            String result = dateFormat.format(cal.getTime());
            return result.substring(0, 6) + (StringUtil.toInt(result.substring(6)) + 543);
        }
    }

    public static int getDate(Date d)
    {
        if (d == null)
        {
            return 0;
        }
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(d);
        return cal.get(Calendar.DATE);
    }

    public static int getMonth(Date d)
    {
        if (d == null)
        {
            return 0;
        }
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(d);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getYear(Date d)
    {
        if (d == null)
        {
            return 0;
        }
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(d);
        return cal.get(Calendar.YEAR);
    }

    public static Date trimDate(Date d)
    {
        if (d != null)
        {
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTime(d);

            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            return cal.getTime();
        }
        return null;
    }
}