package chai_4d.mbus.map.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager
{
    private static Connection conn = null;

    private DBManager()
    {
    }

    private static String getUrl(String host, String port, String sid)
    {
        return "jdbc:mysql://" + host + ":" + port + "/" + sid + "?useUnicode=true&characterEncoding=UTF-8";
    }

    public static synchronized Connection getConnection()
    {
        if (conn == null)
        {
            String host = PropertyUtil.DB.getString("host");
            String port = PropertyUtil.DB.getString("port");
            String sid = PropertyUtil.DB.getString("sid");
            String user = PropertyUtil.DB.getString("user");
            String password = PropertyUtil.DB.getString("password");

            try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection(getUrl(host, port, sid), user, password);
                conn.setAutoCommit(true);
                System.out.println("New connection object has been created.");
            }
            catch (Throwable t)
            {
                System.err.println("Can't create connection object.");
                t.printStackTrace();
            }
        }
        return conn;
    }

    public static synchronized void destroy()
    {
        if (conn != null)
        {
            try
            {
                conn.close();
                System.out.println("The connection object has been destroyed.");
            }
            catch (SQLException e)
            {
            }
            conn = null;
        }
    }
}