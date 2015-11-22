package chai_4d.mbus.map.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

public class SQLUtil
{
    private static final boolean debug_sql = false;

    private SQLUtil()
    {
    }

    public static void closeResultSet(ResultSet rs)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
            }
        }
    }

    public static void closeStatement(Statement stmt)
    {
        if (stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch (SQLException e)
            {
            }
        }
    }

    public static void closePreparedStatement(PreparedStatement pstmt)
    {
        if (pstmt != null)
        {
            try
            {
                pstmt.close();
            }
            catch (SQLException e)
            {
            }
        }
    }

    public static void closeCallableStatement(CallableStatement cstmt)
    {
        if (cstmt != null)
        {
            try
            {
                cstmt.close();
            }
            catch (SQLException e)
            {
            }
        }
    }

    public static void closeConnection(Connection conn)
    {
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
            }
        }
    }

    public static String getString(ResultSet rs, String columnName)
    {
        String result = "";
        try
        {
            result = rs.getString(columnName);
            return result == null ? "" : result.trim();
        }
        catch (Throwable t)
        {
            return result;
        }
    }

    public static String getString(ResultSet rs, int column)
    {
        String result = "";
        try
        {
            result = rs.getString(column);
            return result == null ? "" : result.trim();
        }
        catch (Throwable t)
        {
            return result;
        }
    }

    public static Date getDate(ResultSet rs, String columnName)
    {
        try
        {
            return rs.getTimestamp(columnName);
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    public static Date getDate(ResultSet rs, int column)
    {
        try
        {
            return rs.getTimestamp(column);
        }
        catch (Throwable t)
        {
            return null;
        }
    }

    public static int getInt(ResultSet rs, String columnName)
    {
        try
        {
            return rs.getInt(columnName);
        }
        catch (Throwable t)
        {
            return 0;
        }
    }

    public static int getInt(ResultSet rs, int column)
    {
        try
        {
            return rs.getInt(column);
        }
        catch (Throwable t)
        {
            return 0;
        }
    }

    public static long getLong(ResultSet rs, String columnName)
    {
        try
        {
            return rs.getLong(columnName);
        }
        catch (Throwable t)
        {
            return 0;
        }
    }

    public static long getLong(ResultSet rs, int column)
    {
        try
        {
            return rs.getLong(column);
        }
        catch (Throwable t)
        {
            return 0;
        }
    }

    public static double getDouble(ResultSet rs, String columnName)
    {
        try
        {
            return rs.getDouble(columnName);
        }
        catch (Throwable t)
        {
            return 0;
        }
    }

    public static double getDouble(ResultSet rs, int column)
    {
        try
        {
            return rs.getDouble(column);
        }
        catch (Throwable t)
        {
            return 0;
        }
    }

    public static boolean getBoolean(ResultSet rs, String columnName)
    {
        try
        {
            return rs.getBoolean(columnName);
        }
        catch (Throwable t)
        {
            return false;
        }
    }

    public static boolean getBoolean(ResultSet rs, int column)
    {
        try
        {
            return rs.getBoolean(column);
        }
        catch (Throwable t)
        {
            return false;
        }
    }

    public static void printSQL(String sql)
    {
        if (debug_sql)
        {
            System.out.println("======== sql ========");
            System.out.println(sql);
            System.out.println("=====================");
        }
    }

    public static long genId()
    {
        return System.nanoTime() * -1;
    }

    public static String getIn(List<String> value)
    {
        String result = "(";
        String concat = "";
        for (int i = 0; i < value.size(); i++)
        {
            String s = value.get(i);
            result += concat + "'" + s + "'";
            concat = ", ";
        }
        if (value.size() == 0)
        {
            result += "''";
        }
        result += ")";
        return result;
    }
}
