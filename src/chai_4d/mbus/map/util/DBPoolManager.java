package chai_4d.mbus.map.util;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBPoolManager
{
    private static final Logger log = LogManager.getLogger(DBPoolManager.class);

    private static final String POOL_NAME = "m-Bus";

    private static final String host = PropertyUtil.DB.getString("host");
    private static final String port = PropertyUtil.DB.getString("port");
    private static final String sid = PropertyUtil.DB.getString("sid");
    private static final String connectURI = "jdbc:mysql://" + host + ":" + port + "/" + sid + "?useUnicode=true&characterEncoding=UTF-8";

    private static final String user = PropertyUtil.DB.getString("user");
    private static final byte[] pwdEncoded = PropertyUtil.DB.getString("password").getBytes();
    private static final String password = new String(Base64.decodeBase64(pwdEncoded));

    static
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            log.error(e);
        }

        try
        {
            setupDriver(connectURI, user, password);
        }
        catch (Exception e)
        {
            log.error(e);
        }
    }

    private DBPoolManager()
    {
    }

    public static void test()
    {
        String str = "chai";

        // encode data on your side using BASE64
        byte[] bytesEncoded = Base64.encodeBase64(str.getBytes());
        log.debug("ecncoded value is " + new String(bytesEncoded));

        // Decode data on other side, by processing encoded data
        byte[] valueDecoded = Base64.decodeBase64(bytesEncoded);
        log.debug("Decoded value is " + new String(valueDecoded));
    }

    public static Connection getConnection()
    {
        Connection conn = null;
        try
        {
            conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:" + POOL_NAME);
            conn.setAutoCommit(true);
            log.debug("New connection object has been created.");
        }
        catch (Exception e)
        {
            log.error("Can't create connection object.");
            log.error(e);

            destroy();
            try
            {
                setupDriver(connectURI, user, password);

                conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:" + POOL_NAME);
                conn.setAutoCommit(true);
                log.debug("New connection object has been created (#2).");
            }
            catch (Exception e2)
            {
                log.error("Can't create connection object (#2).");
                log.error(e2);
            }
        }

        try
        {
            printDriverStats();
        }
        catch (Exception e)
        {
            log.error(e);
        }
        return conn;
    }

    public static void destroy()
    {
        try
        {
            shutdownDriver();
            log.debug("The connection object has been destroyed.");
        }
        catch (Exception e)
        {
            log.error(e);
        }
    }

    public static void setupDriver(String connectURI, String user, String password) throws Exception
    {
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, user, password);

        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);

        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);

        poolableConnectionFactory.setPool(connectionPool);

        Class.forName("org.apache.commons.dbcp2.PoolingDriver");
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");

        driver.registerPool(POOL_NAME, connectionPool);
    }

    public static void printDriverStats() throws Exception
    {
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        ObjectPool<? extends Connection> connectionPool = driver.getConnectionPool(POOL_NAME);

        log.debug("NumActive: " + connectionPool.getNumActive());
        log.debug("NumIdle: " + connectionPool.getNumIdle());
    }

    public static void shutdownDriver() throws Exception
    {
        PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        driver.closePool(POOL_NAME);
    }
}