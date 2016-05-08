package chai_4d.mbus.map.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chai_4d.mbus.map.constant.MapConstants;
import chai_4d.mbus.map.constant.MapConstants.LineType;
import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.dijkstra.model.Line;
import chai_4d.mbus.map.dijkstra.model.Point;
import chai_4d.mbus.map.model.BusChoice;
import chai_4d.mbus.map.model.BusInfo;
import chai_4d.mbus.map.model.BusLine;
import chai_4d.mbus.map.model.BusPath;
import chai_4d.mbus.map.model.PointInfo;
import chai_4d.mbus.map.model.PointName;
import chai_4d.mbus.map.screen.MapPanel;
import chai_4d.mbus.map.util.DBPoolManager;
import chai_4d.mbus.map.util.DateUtil;
import chai_4d.mbus.map.util.HttpURLUtil;
import chai_4d.mbus.map.util.SQLUtil;
import chai_4d.mbus.map.util.StringUtil;

public class MapDbBean
{
    private static final Logger log = LogManager.getLogger(MapDbBean.class);

    private MapDbBean()
    {
    }

    public static List<PointInfo> loadMapPoint(int mapPointX, int mapPointY)
    {
        List<PointInfo> result = new ArrayList<PointInfo>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select * \n";
            sql += "from point_info \n";
            sql += "where (axis_x between ? and ?) \n";
            sql += " and (axis_y between ? and ?) \n";

            int p1 = (mapPointX - 1) * MapPanel.MAP_POINT_WIDTH;
            int p2 = (mapPointX * MapPanel.MAP_POINT_WIDTH) - 1;
            int p3 = (mapPointY - 1) * MapPanel.MAP_POINT_HEIGHT;
            int p4 = (mapPointY * MapPanel.MAP_POINT_HEIGHT) - 1;

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, p1);
            pstmt.setInt(2, p2);
            pstmt.setInt(3, p3);
            pstmt.setInt(4, p4);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                result.add(new PointInfo(rs));
            }
            SQLUtil.printSQL(sql + "[" + p1 + ", " + p2 + ", " + p3 + ", " + p4 + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static PointInfo loadPointInfoById(long pId)
    {
        PointInfo result = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select * \n";
            sql += "from point_info \n";
            sql += "where p_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pId);

            rs = pstmt.executeQuery();
            if (rs.next())
            {
                result = new PointInfo(rs);
            }
            SQLUtil.printSQL(sql + "[" + pId + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static List<BusLine> loadBusLineByBusId(long busId)
    {
        List<BusLine> result = new ArrayList<BusLine>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select p1.p_id p1_id, p2.p_id p2_id, \n";
            sql += " p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, b.type, \n";
            sql += " min(n1.name_th), min(n1.name_en), min(n2.name_th), min(n2.name_en) \n";
            sql += "from bus_line b, bus_info i, \n";
            sql += " point_info p1 left outer join point_name n1 on p1.p_id = n1.p_id, \n";
            sql += " point_info p2 left outer join point_name n2 on p2.p_id = n2.p_id \n";
            sql += "where b.bus_id = ? \n";
            sql += " and b.p1_id = p1.p_id \n";
            sql += " and b.p2_id = p2.p_id \n";
            sql += " and b.bus_id = i.bus_id \n";
            sql += "group by p1.p_id, p2.p_id, p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, b.type \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busId);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                result.add(new BusLine(rs));
            }
            SQLUtil.printSQL(sql + "[" + busId + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static List<BusPath> loadBusPathByP1P2(long pId1, long pId2, Date timeToGo)
    {
        List<BusPath> result = new ArrayList<BusPath>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "( \n";
            sql += "select p1.p_id p1_id, p2.p_id p2_id, \n";
            sql += " p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, 1 type, \n";
            sql += " min(n1.name_th), min(n1.name_en), min(n2.name_th), min(n2.name_en) \n";
            sql += "from bus_line b, bus_info i, \n";
            sql += " point_info p1 left outer join point_name n1 on p1.p_id = n1.p_id, \n";
            sql += " point_info p2 left outer join point_name n2 on p2.p_id = n2.p_id \n";
            sql += "where b.p1_id = p1.p_id \n";
            sql += " and b.p2_id = p2.p_id \n";
            sql += " and b.bus_id = i.bus_id \n";
            sql += " and b.p1_id = ? and b.p2_id = ? and b.type in (0, 1) \n";
            sql += " and cast(i.start_time as time) <= ? and cast(i.end_time as time) >= ? \n";
            sql += "group by p1.p_id, p2.p_id, p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, type \n";
            sql += ") \n";
            sql += "union \n";
            sql += "( \n";
            sql += "select p1.p_id p1_id, p2.p_id p2_id, \n";
            sql += " p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, 2 type, \n";
            sql += " min(n1.name_th), min(n1.name_en), min(n2.name_th), min(n2.name_en) \n";
            sql += "from bus_line b, bus_info i, \n";
            sql += " point_info p1 left outer join point_name n1 on p1.p_id = n1.p_id, \n";
            sql += " point_info p2 left outer join point_name n2 on p2.p_id = n2.p_id \n";
            sql += "where b.p1_id = p1.p_id \n";
            sql += " and b.p2_id = p2.p_id \n";
            sql += " and b.bus_id = i.bus_id \n";
            sql += " and b.p2_id = ? and b.p1_id = ? and b.type in (0, 2) \n";
            sql += " and cast(i.start_time as time) <= ? and cast(i.end_time as time) >= ? \n";
            sql += "group by p1.p_id, p2.p_id, p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, type \n";
            sql += ") \n";
            sql += "order by bus_id \n";

            Timestamp sqlTimeToGo = DateUtil.createSQLTime(timeToGo);

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pId1);
            pstmt.setLong(2, pId2);
            pstmt.setTimestamp(3, sqlTimeToGo);
            pstmt.setTimestamp(4, sqlTimeToGo);
            pstmt.setLong(5, pId1);
            pstmt.setLong(6, pId2);
            pstmt.setTimestamp(7, sqlTimeToGo);
            pstmt.setTimestamp(8, sqlTimeToGo);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                result.add(new BusPath(new BusLine(rs), pId1));
            }
            SQLUtil.printSQL(sql + "[" + pId1 + ", " + pId2 + ", " + timeToGo + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static BusPath loadNextBusPath(long busId, long pId1, long pId2)
    {
        BusPath result = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "( \n";
            sql += "select p1.p_id p1_id, p2.p_id p2_id, \n";
            sql += " p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, 1 type, \n";
            sql += " min(n1.name_th), min(n1.name_en), min(n2.name_th), min(n2.name_en) \n";
            sql += "from bus_line b, bus_info i, \n";
            sql += " point_info p1 left outer join point_name n1 on p1.p_id = n1.p_id, \n";
            sql += " point_info p2 left outer join point_name n2 on p2.p_id = n2.p_id \n";
            sql += "where b.p1_id = p1.p_id \n";
            sql += " and b.p2_id = p2.p_id \n";
            sql += " and b.bus_id = i.bus_id \n";
            sql += " and b.bus_id = ? \n";
            sql += " and b.p1_id = ? and b.p2_id <> ? and b.type in (0, 1) \n";
            sql += "group by p1.p_id, p2.p_id, p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, type \n";
            sql += ") \n";
            sql += "union \n";
            sql += "( \n";
            sql += "select p1.p_id p1_id, p2.p_id p2_id, \n";
            sql += " p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, 2 type, \n";
            sql += " min(n1.name_th), min(n1.name_en), min(n2.name_th), min(n2.name_en) \n";
            sql += "from bus_line b, bus_info i, \n";
            sql += " point_info p1 left outer join point_name n1 on p1.p_id = n1.p_id, \n";
            sql += " point_info p2 left outer join point_name n2 on p2.p_id = n2.p_id \n";
            sql += "where b.p1_id = p1.p_id \n";
            sql += " and b.p2_id = p2.p_id \n";
            sql += " and b.bus_id = i.bus_id \n";
            sql += " and b.bus_id = ? \n";
            sql += " and b.p2_id = ? and b.p1_id <> ? and b.type in (0, 2) \n";
            sql += "group by p1.p_id, p2.p_id, p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " b.distance, b.bus_id, i.bus_no_th, i.bus_no_en, i.bus_price, type \n";
            sql += ") \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busId);
            pstmt.setLong(2, pId2);
            pstmt.setLong(3, pId1);
            pstmt.setLong(4, busId);
            pstmt.setLong(5, pId2);
            pstmt.setLong(6, pId1);

            rs = pstmt.executeQuery();
            if (rs.next())
            {
                result = new BusPath(new BusLine(rs), pId2);
            }
            SQLUtil.printSQL(sql + "[" + busId + ", " + pId1 + ", " + pId2 + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static void loadBusLine(List<String> busNoTh, List<String> busNoEn, List<BusLine> busLine)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select p1.p_id p1_id, p2.p_id p2_id, \n";
            sql += " p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " bl.distance, b.bus_id, b.bus_no_th, b.bus_no_en, b.bus_price, bl.type, \n";
            sql += " min(n1.name_th), min(n1.name_en), min(n2.name_th), min(n2.name_en) \n";
            sql += "from bus_info b, bus_line bl, \n";
            sql += " point_info p1 left outer join point_name n1 on p1.p_id = n1.p_id, \n";
            sql += " point_info p2 left outer join point_name n2 on p2.p_id = n2.p_id \n";
            sql += "where b.bus_id = bl.bus_id \n";
            sql += " and b.bus_no_th in " + SQLUtil.getIn(busNoTh) + " \n";
            sql += " and b.bus_no_en in " + SQLUtil.getIn(busNoEn) + " \n";
            sql += " and bl.p1_id = p1.p_id \n";
            sql += " and bl.p2_id = p2.p_id \n";
            sql += "group by p1.p_id, p2.p_id, p1.axis_x, p1.axis_y, p2.axis_x, p2.axis_y, \n";
            sql += " bl.distance, b.bus_id, b.bus_no_th, b.bus_no_en, b.bus_price, bl.type \n";
            sql += "order by b.bus_id \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                busLine.add(new BusLine(rs));
            }
            SQLUtil.printSQL(sql);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    public static List<PointName> loadPointName(String lang)
    {
        List<PointName> result = new ArrayList<PointName>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select * \n";
            sql += "from point_name \n";
            sql += "order by " + ("en".equalsIgnoreCase(lang) ? "name_en" : "name_th") + " \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                result.add(new PointName(rs));
            }
            SQLUtil.printSQL(sql + "[" + lang + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static List<PointName> loadPointName(String lang, String query)
    {
        List<PointName> result = new ArrayList<PointName>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select * \n";
            sql += "from point_name \n";
            sql += "where " + ("en".equalsIgnoreCase(lang) ? "name_en" : "name_th") + " like ? \n";
            sql += "order by " + ("en".equalsIgnoreCase(lang) ? "name_en" : "name_th") + " \n";
            sql += "limit 40 \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + query + "%");

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                result.add(new PointName(rs));
            }
            SQLUtil.printSQL(sql + "[" + lang + ", " + query + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static List<PointName> loadPointNameById(long pId)
    {
        List<PointName> result = new ArrayList<PointName>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select * \n";
            sql += "from point_name \n";
            sql += "where p_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pId);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                result.add(new PointName(rs));
            }
            SQLUtil.printSQL(sql + "[" + pId + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static List<String> loadPointName(int colNo)
    {
        List<String> result = new ArrayList<String>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String col = (colNo == 1 ? "name_th" : "name_en");

            String sql = "";
            sql += "select concat(" + col + ", ' (', p_id, ')') \n";
            sql += "from point_name \n";
            sql += "order by 1 \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                result.add(rs.getString(1));
            }
            SQLUtil.printSQL(sql);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static List<String> loadBusNo(int colNo)
    {
        List<String> result = new ArrayList<String>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String col = (colNo == 1 ? "bus_no_th" : "bus_no_en");

            String sql = "";
            sql += "select " + col + " \n";
            sql += "from bus_info \n";
            sql += "order by 1 \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                result.add(rs.getString(1));
            }
            SQLUtil.printSQL(sql);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static BusInfo loadBusInfo(int colNo, String busNo)
    {
        BusInfo result = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String col = (colNo == 1 ? "bus_no_th" : "bus_no_en");

            String sql = "";
            sql += "select * \n";
            sql += "from bus_info \n";
            sql += "where " + col + " = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, busNo);

            rs = pstmt.executeQuery();
            if (rs.next())
            {
                result = new BusInfo(rs);
            }
            SQLUtil.printSQL(sql + "[" + busNo + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static BusInfo loadBusInfoById(long busId)
    {
        BusInfo result = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select * \n";
            sql += "from bus_info \n";
            sql += "where bus_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busId);

            rs = pstmt.executeQuery();
            if (rs.next())
            {
                result = new BusInfo(rs);
            }
            SQLUtil.printSQL(sql + "[" + busId + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    private static int countBusLine(long pId)
    {
        int result = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select count(1) \n";
            sql += "from bus_line \n";
            sql += "where p1_id = ? or p2_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pId);
            pstmt.setLong(2, pId);

            rs = pstmt.executeQuery();
            if (rs.next())
            {
                result = rs.getInt(1);
            }
            SQLUtil.printSQL(sql + "[" + pId + ", " + pId + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    private static int countBusInfo(long busId, String busNoTh, String busNoEn)
    {
        int result = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select count(1) \n";
            sql += "from bus_info \n";
            sql += "where bus_id <> ? and (bus_no_th = ? or bus_no_en = ?) \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busId);
            pstmt.setString(2, busNoTh);
            pstmt.setString(3, busNoEn);

            rs = pstmt.executeQuery();
            if (rs.next())
            {
                result = rs.getInt(1);
            }
            SQLUtil.printSQL(sql + "[" + busNoTh + ", " + busNoEn + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    private static String validateBus(BusInfo busInfo)
    {
        if (StringUtil.isEmpty(busInfo.getBusNoTh()))
        {
            return "Bus No (TH) can't be empty.";
        }
        else if (StringUtil.isEmpty(busInfo.getBusNoEn()))
        {
            return "Bus No (EN) can't be empty.";
        }
        else if (countBusInfo(busInfo.getBusId(), busInfo.getBusNoTh(), busInfo.getBusNoEn()) > 0)
        {
            return "Bus No is not unique.";
        }
        return "true";
    }

    private static String validateBusLine(BusInfo busInfo)
    {
        List<BusLine> list = busInfo.getBusLine();
        int count = 0;
        HashMap<String, String> pointMap = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++)
        {
            BusLine busLine = list.get(i);
            if (busLine.getMode() != Mode.DELETE && busLine.getType() == LineType.BIDIRECT)
            {
                count++;
                String p1 = busLine.getP1Id() + "";
                String p2 = busLine.getP2Id() + "";
                if (pointMap.containsKey(p1))
                {
                    pointMap.remove(p1);
                }
                else
                {
                    pointMap.put(p1, null);
                }
                if (pointMap.containsKey(p2))
                {
                    pointMap.remove(p2);
                }
                else
                {
                    pointMap.put(p2, null);
                }
            }
        }
        for (int i = 0; i < list.size(); i++)
        {
            BusLine busLine = list.get(i);
            if (busLine.getMode() != Mode.DELETE && busLine.getType() != LineType.BIDIRECT)
            {
                count++;
                String p1 = busLine.getP1Id() + "";
                String p2 = busLine.getP2Id() + "";
                if (pointMap.containsKey(p1))
                {
                    pointMap.remove(p1);
                }
                if (pointMap.containsKey(p2))
                {
                    pointMap.remove(p2);
                }
            }
        }
        if (count == 0)
        {
            return "Bus Line can't be zero.";
        }
        if (pointMap.size() > 2)
        {
            return "Bus Line can't be broken.";
        }
        return "true";
    }

    private static void updateBusLinePoint(long oldId, long newId, BusInfo busInfo)
    {
        if (busInfo != null)
        {
            List<BusLine> list = busInfo.getBusLine();
            for (int i = 0; i < list.size(); i++)
            {
                BusLine busLine = list.get(i);
                if (busLine.getP1Id() == oldId)
                {
                    busLine.setP1Id(newId);
                }
                if (busLine.getP2Id() == oldId)
                {
                    busLine.setP2Id(newId);
                }
            }
        }
    }

    private static void insertPointName(long pId, PointName pointName)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "insert into point_name (p_id, name_th, name_en) \n";
            sql += "values (?, ?, ?) \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pId);
            pstmt.setString(2, pointName.getNameTh());
            pstmt.setString(3, pointName.getNameEn());

            pstmt.executeUpdate();
            pointName.setMode(Mode.SELECT);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static void deletePointName(long pId, PointName pointName)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "delete from point_name \n";
            sql += "where p_id = ? and name_th = ? and name_en = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pId);
            pstmt.setString(2, pointName.getNameTh());
            pstmt.setString(3, pointName.getNameEn());

            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static void savePointName(PointInfo pointInfo)
    {
        List<PointName> list = pointInfo.getPointName();
        for (int i = list.size() - 1; i >= 0; i--)
        {
            PointName pointName = list.get(i);
            switch (pointName.getMode())
            {
                case DELETE:
                    deletePointName(pointInfo.getPId(), pointName);
                    list.remove(i);
                    break;
            }
        }
        for (int i = list.size() - 1; i >= 0; i--)
        {
            PointName pointName = list.get(i);
            switch (pointName.getMode())
            {
                case INSERT:
                    insertPointName(pointInfo.getPId(), pointName);
                    break;
            }
        }
    }

    private static void insertPointInfo(PointInfo pointInfo, BusInfo busInfo)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "insert into point_info (axis_x, axis_y, type) \n";
            sql += "values (?, ?, ?) \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, pointInfo.getAxisX());
            pstmt.setInt(2, pointInfo.getAxisY());
            pstmt.setInt(3, pointInfo.getType().getValue());

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())
            {
                long oldId = pointInfo.getPId();
                pointInfo.setPId(rs.getLong(1));
                long newId = pointInfo.getPId();
                updateBusLinePoint(oldId, newId, busInfo);
            }
            pointInfo.setMode(Mode.SELECT);
            savePointName(pointInfo);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static void updatePointInfo(PointInfo pointInfo)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "update point_info set axis_x = ?, axis_y = ?, type = ? \n";
            sql += "where p_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pointInfo.getAxisX());
            pstmt.setInt(2, pointInfo.getAxisY());
            pstmt.setInt(3, pointInfo.getType().getValue());
            pstmt.setLong(4, pointInfo.getPId());

            pstmt.executeUpdate();
            pointInfo.setMode(Mode.SELECT);
            savePointName(pointInfo);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static void deletePointInfo(PointInfo pointInfo)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "delete from point_name \n";
            sql += "where p_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pointInfo.getPId());
            pstmt.executeUpdate();
            pstmt.close();

            sql = "";
            sql += "delete from point_info \n";
            sql += "where p_id = ? \n";

            //conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pointInfo.getPId());
            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static void insertBusLine(long busId, BusLine busLine)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "insert into bus_line (p1_id, p2_id, distance, bus_id, type) \n";
            sql += "values (?, ?, ?, ?, ?) \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busLine.getP1Id());
            pstmt.setLong(2, busLine.getP2Id());
            pstmt.setDouble(3, busLine.getDistance());
            pstmt.setLong(4, busId);
            pstmt.setInt(5, busLine.getType().getValue());

            pstmt.executeUpdate();
            busLine.setMode(Mode.SELECT);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static void deleteBusLine(long busId, BusLine busLine)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "delete from bus_line \n";
            sql += "where p1_id = ? and p2_id = ? and bus_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busLine.getP1Id());
            pstmt.setLong(2, busLine.getP2Id());
            pstmt.setLong(3, busId);

            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static void saveBusLine(BusInfo busInfo)
    {
        List<BusLine> list = busInfo.getBusLine();
        for (int i = list.size() - 1; i >= 0; i--)
        {
            BusLine busLine = list.get(i);
            switch (busLine.getMode())
            {
                case DELETE:
                    deleteBusLine(busInfo.getBusId(), busLine);
                    list.remove(i);
                    break;
            }
        }
        for (int i = list.size() - 1; i >= 0; i--)
        {
            BusLine busLine = list.get(i);
            switch (busLine.getMode())
            {
                case INSERT:
                    insertBusLine(busInfo.getBusId(), busLine);
                    break;
            }
        }
    }

    private static void insertBusInfo(BusInfo busInfo)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "insert into bus_info (bus_no_th, bus_no_en, detail_th, detail_en, bus_pic, start_time, end_time, bus_price) \n";
            sql += "values (?, ?, ?, ?, ?, ?, ?, ?) \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, busInfo.getBusNoTh());
            pstmt.setString(2, busInfo.getBusNoEn());
            pstmt.setString(3, busInfo.getDetailTh());
            pstmt.setString(4, busInfo.getDetailEn());
            pstmt.setString(5, busInfo.getBusPic());
            pstmt.setTimestamp(6, DateUtil.createSQLTime(busInfo.getStartTime()));
            pstmt.setTimestamp(7, DateUtil.createSQLTime(busInfo.getEndTime()));
            pstmt.setString(8, busInfo.getBusPrice());

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())
            {
                busInfo.setBusId(rs.getLong(1));
            }
            busInfo.setMode(Mode.SELECT);
            saveBusLine(busInfo);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static void updateBusInfo(BusInfo busInfo)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "update bus_info set bus_no_th = ?, bus_no_en = ?, detail_th = ?, detail_en = ?, bus_pic = ?, \n";
            sql += " start_time = ?, end_time = ?, bus_price = ? \n";
            sql += "where bus_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, busInfo.getBusNoTh());
            pstmt.setString(2, busInfo.getBusNoEn());
            pstmt.setString(3, busInfo.getDetailTh());
            pstmt.setString(4, busInfo.getDetailEn());
            pstmt.setString(5, busInfo.getBusPic());
            pstmt.setTimestamp(6, DateUtil.createSQLTime(busInfo.getStartTime()));
            pstmt.setTimestamp(7, DateUtil.createSQLTime(busInfo.getEndTime()));
            pstmt.setString(8, busInfo.getBusPrice());
            pstmt.setLong(9, busInfo.getBusId());

            pstmt.executeUpdate();
            busInfo.setMode(Mode.SELECT);
            saveBusLine(busInfo);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static void deleteBusInfo(BusInfo busInfo)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "delete from bus_line \n";
            sql += "where bus_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busInfo.getBusId());
            pstmt.executeUpdate();
            pstmt.close();

            sql = "";
            sql += "delete from bus_info \n";
            sql += "where bus_id = ? \n";

            //conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busInfo.getBusId());
            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    public static String saveBus(Hashtable<String, List<PointInfo>> mapPoint, BusInfo busInfo)
    {
        String result = null;
        if (!(result = validateBus(busInfo)).equals("true"))
        {
            return result;
        }
        if (!(result = validateBusLine(busInfo)).equals("true"))
        {
            return result;
        }

        savePoint(mapPoint, busInfo);

        switch (busInfo.getMode())
        {
            case INSERT:
                insertBusInfo(busInfo);
                break;
            case UPDATE:
                updateBusInfo(busInfo);
                break;
        }

        return "true";
    }

    public static String deleteBus(BusInfo busInfo)
    {
        deleteBusInfo(busInfo);
        return "true";
    }

    public static String savePoint(Hashtable<String, List<PointInfo>> mapPoint)
    {
        return savePoint(mapPoint, null);
    }

    private static String savePoint(Hashtable<String, List<PointInfo>> mapPoint, BusInfo busInfo)
    {
        Enumeration<String> mapEnum = mapPoint.keys();
        while (mapEnum.hasMoreElements())
        {
            String key = mapEnum.nextElement();
            List<PointInfo> list = mapPoint.get(key);
            for (int i = list.size() - 1; i >= 0; i--)
            {
                PointInfo pointInfo = list.get(i);
                switch (pointInfo.getMode())
                {
                    case DELETE:
                        if (countBusLine(pointInfo.getPId()) > 0)
                        {
                            updatePointInfo(pointInfo);
                        }
                        else
                        {
                            deletePointInfo(pointInfo);
                            list.remove(i);
                        }
                        break;
                }
            }
            for (int i = list.size() - 1; i >= 0; i--)
            {
                PointInfo pointInfo = list.get(i);
                switch (pointInfo.getMode())
                {
                    case INSERT:
                        insertPointInfo(pointInfo, busInfo);
                        break;
                    case UPDATE:
                        updatePointInfo(pointInfo);
                        break;
                }
            }
        }
        return "true";
    }

    /*    public static void resetBusPath()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "delete from bus_path \n";
    
            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
    
            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }*/

    public static Map<Integer, Point> loadPointInfo()
    {
        Map<Integer, Point> result = new HashMap<Integer, Point>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select p.p_id, p.axis_x, p.axis_y, min(n.name_th), min(n.name_en) \n";
            sql += "from point_info p left outer join point_name n \n";
            sql += "  on p.p_id = n.p_id \n";
            sql += "group by p.p_id \n";
            sql += "order by 1 \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt(1);
                int axisX = rs.getInt(2);
                int axisY = rs.getInt(3);
                String nameTh = rs.getString(4);
                String nameEn = rs.getString(5);
                result.put(id, new Point(id, axisX, axisY, nameTh, nameEn));
            }
            SQLUtil.printSQL(sql);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static List<Line> loadBusLine(Map<Integer, Point> points)
    {
        List<Line> result = new ArrayList<Line>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select p1_id, p2_id, distance, type \n";
            sql += "from bus_line \n";
            sql += "order by 1 \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                Point p1Id = points.get(rs.getInt(1));
                Point p2Id = points.get(rs.getInt(2));
                double distance = rs.getDouble(3);
                int type = rs.getInt(4);

                switch (LineType.valueOf(type))
                {
                    case BIDIRECT:
                        result.add(new Line(p1Id, p2Id, distance));
                        result.add(new Line(p2Id, p1Id, distance));
                        break;
                    case P1_P2:
                        result.add(new Line(p1Id, p2Id, distance));
                        break;
                    case P2_P1:
                        result.add(new Line(p2Id, p1Id, distance));
                        break;
                    default:
                        break;
                }
            }
            SQLUtil.printSQL(sql);
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static int countBusPath(long sourceId)
    {
        int result = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select count(1) \n";
            sql += "from bus_path \n";
            sql += "where source_id = ? \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, sourceId);

            rs = pstmt.executeQuery();
            if (rs.next())
            {
                result = SQLUtil.getInt(rs, 1);
            }
            SQLUtil.printSQL(sql + "[" + sourceId + "]");
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
        return result;
    }

    public static void insertBusPath(long sourceId, long destinationId, String busPath)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "insert into bus_path (source_id, destination_id, bus_path) \n";
            sql += "values (?, ?, compress(?)) \n";

            conn = DBPoolManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, sourceId);
            pstmt.setLong(2, destinationId);
            pstmt.setString(3, busPath);

            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            log.error(e);
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
            SQLUtil.closeConnection(conn);
        }
    }

    private static String getBusPathsStr(long sourceId, long destinationId)
    {
        if (HttpURLUtil.online.equals("true"))
        {
            return HttpURLUtil.sendGet("?act=getBusPaths&sourceId=" + sourceId + "&destinationId=" + destinationId);
        }
        else
        {
            String result = null;
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try
            {
                String sql = "";
                sql += "select cast(uncompress(bus_path) as char) \n";
                sql += "from bus_path \n";
                sql += "where source_id = ? \n";
                sql += " and destination_id = ? \n";

                conn = DBPoolManager.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setLong(1, sourceId);
                pstmt.setLong(2, destinationId);

                rs = pstmt.executeQuery();
                if (rs.next())
                {
                    result = SQLUtil.getString(rs, 1);
                }
                SQLUtil.printSQL(sql + "[" + sourceId + ", " + destinationId + "]");
            }
            catch (Exception e)
            {
                log.error(e);
            }
            finally
            {
                SQLUtil.closeResultSet(rs);
                SQLUtil.closePreparedStatement(pstmt);
                SQLUtil.closeConnection(conn);
            }
            return result;
        }
    }

    private static void addFirstBusPath(List<BusChoice> busChoices, BusPath busPath)
    {
        BusChoice busChoice = new BusChoice(busChoices.size() + 1);
        busChoice.getBusPaths().add(busPath);
        busChoices.add(busChoice);
    }

    private static void hasAlternativeChoice(List<BusChoice> alternatives, List<BusChoice> busChoices, BusPath busPath)
    {
        if (busChoices.size() >= MapConstants.MAX_CHOICES)
        {
            return;
        }

        for (int i = 0; i < busChoices.size(); i++)
        {
            BusChoice busChoice = busChoices.get(i);
            BusPath lastBusPath = busChoice.getLastBusPath();
            if (lastBusPath.getBusId() != busPath.getBusId()
                && busChoice.isContainBus(busPath.getBusId()) == false
                && busChoices.size() < MapConstants.MAX_CHOICES)
            {
                BusPath newBusPath = loadNextBusPath(lastBusPath.getBusId(), lastBusPath.getP1Id(), lastBusPath.getP2Id());
                if (newBusPath != null)
                {
                    BusChoice alternative = busChoice.clone(alternatives.size() + 1);
                    alternative.getBusPaths().add(newBusPath);
                    alternatives.add(alternative);
                }
            }
        }
    }

    private static void addBusPathWithBusId(List<BusChoice> busChoices, long sourceId, BusPath busPath)
    {
        for (int i = 0; i < busChoices.size(); i++)
        {
            BusChoice busChoice = busChoices.get(i);
            BusPath lastBusPath = busChoice.getLastBusPath();
            if (lastBusPath.getP2Id() == sourceId && lastBusPath.getBusId() == busPath.getBusId())
            {
                busChoice.getBusPaths().add(busPath);
            }
        }
    }

    private static void addBusPathWithAnotherBus(List<BusChoice> busChoices, long sourceId, BusPath busPath)
    {
        for (int i = 0; i < busChoices.size(); i++)
        {
            BusChoice busChoice = busChoices.get(i);
            BusPath lastBusPath = busChoice.getLastBusPath();
            if (lastBusPath.getP2Id() == sourceId && lastBusPath.getBusId() != busPath.getBusId() && StringUtil.isEmpty(lastBusPath.getP2NameEn()) == false)
            {
                busChoice.getBusPaths().add(busPath);
            }
        }
    }

    private static void addBusPathAlternativeBus(List<BusChoice> busChoices, long sourceId, BusPath busPath)
    {
        if (busChoices.size() >= MapConstants.MAX_CHOICES)
        {
            return;
        }

        List<BusChoice> alternativeChoices = new ArrayList<BusChoice>();
        for (int i = 0; i < busChoices.size(); i++)
        {
            BusChoice busChoice = busChoices.get(i);
            BusPath lastBusPath = busChoice.getLastBusPath();
            if (lastBusPath.getP1Id() == sourceId
                && lastBusPath.getBusId() != busPath.getBusId()
                && busChoice.isContainBus(busPath.getBusId()) == false
                && StringUtil.isEmpty(lastBusPath.getP1NameEn()) == false)
            {
                BusChoice alternative = busChoice.clone(alternativeChoices.size() + 1);
                alternative.getBusPaths().remove(alternative.getBusPaths().size() - 1);
                alternative.getBusPaths().add(busPath);
                alternativeChoices.add(alternative);
            }
        }
        for (int i = 0; i < alternativeChoices.size(); i++)
        {
            BusChoice alternative = alternativeChoices.get(i);
            boolean duplicate = false;
            for (int j = 0; j < busChoices.size(); j++)
            {
                BusChoice busChoice = busChoices.get(j);
                if (busChoice.equalChoice(alternative))
                {
                    duplicate = true;
                    break;
                }
            }
            if (duplicate == false && busChoices.size() < MapConstants.MAX_CHOICES)
            {
                alternative.setChoiceNo(busChoices.size() + 1);
                busChoices.add(alternative);
            }
        }
    }

    /*    private static void addBusPath(List<BusChoice> busChoices, long sourceId, BusPath busPath)
    {
            if (lastBusPath.getP1Id() != busPath.getP2Id())
            {
                if (busChoice.isContainPoint(busPath.getP2Id()) == false)
                {
                    busChoice.getBusPaths().add(busPath);
                    return busPath;
                }
            }
            else
            {
                BusLine newBusLine = loadNextBusLine(lastBusPath.getBusId(), lastBusPath.getP1Id(), lastBusPath.getP2Id());
                if (newBusLine != null)
                {
                    BusPath newBusPath = new BusPath(newBusLine, lastBusPath.getP2Id());
                    busChoice.getBusPaths().add(newBusPath);
                    return newBusPath;
                }
            }
    }*/

    private static void recursiveCalcBusChoice(long originalSourceId, List<BusChoice> busChoices, long sourceId, long destinationId, Date timeToGo)
    {
        String busPathsStr = getBusPathsStr(sourceId, destinationId);

        log.info("P1=" + sourceId + ", P2=" + destinationId + " : " + busPathsStr);

        if (StringUtil.isEmpty(busPathsStr) == false)
        {
            StringTokenizer token = new StringTokenizer(busPathsStr, "->");
            token.nextToken();
            if (token.hasMoreTokens())
            {
                long p2 = StringUtil.toLong(token.nextToken());

                List<BusPath> busPaths = loadBusPathByP1P2(sourceId, p2, timeToGo);

                log.info("count=" + busPaths.size());

                List<BusChoice> alternatives = new ArrayList<BusChoice>();
                if (originalSourceId == sourceId)
                {
                    for (int i = 0; i < busPaths.size(); i++)
                    {
                        BusPath busPath = busPaths.get(i);
                        addFirstBusPath(busChoices, busPath);
                    }
                }
                else
                {
                    for (int i = 0; i < busPaths.size(); i++)
                    {
                        BusPath busPath = busPaths.get(i);
                        hasAlternativeChoice(alternatives, busChoices, busPath);
                    }
                    for (int i = 0; i < busPaths.size(); i++)
                    {
                        BusPath busPath = busPaths.get(i);
                        addBusPathWithBusId(busChoices, sourceId, busPath);
                    }
                    for (int i = 0; i < busPaths.size(); i++)
                    {
                        BusPath busPath = busPaths.get(i);
                        addBusPathWithAnotherBus(busChoices, sourceId, busPath);
                    }
                    for (int i = 0; i < busPaths.size(); i++)
                    {
                        BusPath busPath = busPaths.get(i);
                        addBusPathAlternativeBus(busChoices, sourceId, busPath);
                    }
                }

                for (int i = 0; i < busChoices.size(); i++)
                {
                    BusChoice busChoice = busChoices.get(i);
                    log.info("[B-" + busChoice.getChoiceNo() + "] " + busChoice.printBusPathsStr());
                }
                recursiveCalcBusChoice(originalSourceId, busChoices, p2, destinationId, timeToGo);

                for (int i = 0; i < alternatives.size(); i++)
                {
                    BusChoice busChoice = alternatives.get(i);
                    log.info("[A-" + busChoice.getChoiceNo() + "] " + busChoice.printBusPathsStr());
                }
                for (int i = 0; i < alternatives.size(); i++)
                {
                    BusChoice busChoice = alternatives.get(i);
                    BusPath busPath = busChoice.getLastBusPath();
                    recursiveCalcBusChoice(originalSourceId, busChoices, busPath.getP2Id(), destinationId, timeToGo);
                }
            }
        }
    }

    public static List<BusChoice> calcBusChoices(long sourceId, long destinationId, Date timeToGo)
    {
        List<BusChoice> busChoices = new ArrayList<BusChoice>();

        // 1. Calculate potential Bus Choices 
        recursiveCalcBusChoice(sourceId, busChoices, sourceId, destinationId, timeToGo);

        // 2. Remove the Bus Choice that can't reach to destination
        for (int i = busChoices.size() - 1; i >= 0; i--)
        {
            BusChoice busChoice = busChoices.get(i);
            if (busChoice.getFirstBusPath().getP1Id() != sourceId)
            {
                busChoices.remove(i);
            }
            else if (busChoice.getLastBusPath().getP2Id() != destinationId)
            {
                busChoices.remove(i);
            }
        }

        // 3. Remove the Bus Choice that return the same Bus No
        for (int i = busChoices.size() - 1; i >= 0; i--)
        {
            BusChoice busChoice = busChoices.get(i);
            if (busChoice.isReturnSameBusNo())
            {
                busChoices.remove(i);
            }
        }

        // 4. Calculate score per each Bus Choice
        int maxInterchange = 0;
        double maxPrice = 0.0;
        double maxDistance = 0.0;
        for (int i = 0; i < busChoices.size(); i++)
        {
            BusChoice busChoice = busChoices.get(i);
            busChoice.calcScore();

            maxInterchange = Math.max(maxInterchange, busChoice.getNoOfInterchange());
            maxPrice = Math.max(maxPrice, busChoice.getTotalPrice());
            maxDistance = Math.max(maxDistance, busChoice.getTotalDistance());
        }
        for (int i = 0; i < busChoices.size(); i++)
        {
            BusChoice busChoice = busChoices.get(i);
            busChoice.calcScorePercent(maxInterchange, maxPrice, maxDistance);
        }

        // 5. Sorting the Bus Choice per scorePercent
        List<BusChoice> result = new ArrayList<BusChoice>();
        for (int i = 0; i < busChoices.size(); i++)
        {
            BusChoice busChoice = busChoices.get(i);
            double score = busChoice.getScorePercent();

            int index = result.size();
            for (int j = 0; j < result.size(); j++)
            {
                BusChoice resultChoice = result.get(j);
                if (score > resultChoice.getScorePercent())
                {
                    index = j;
                    break;
                }
            }
            result.add(index, busChoice);
        }

        // 6. Remove duplicated and cut only top prefer choices
        BusChoice previousBusChoice = null;
        for (int i = result.size() - 1; i >= 0; i--)
        {
            BusChoice busChoice = result.get(i);
            if (previousBusChoice == null)
            {
                previousBusChoice = busChoice;
            }
            else
            {
                if (busChoice.equalChoice(previousBusChoice))
                {
                    result.remove(i + 1);
                }
                previousBusChoice = busChoice;
            }
        }
        for (int i = result.size() - 1; i >= 0; i--)
        {
            if (i >= MapConstants.PREFER_CHOICES)
            {
                result.remove(i);
            }
        }

        // 7. (info) Printing the Bus Choices
        log.info("========================");
        log.info("Printing the Bus Choices");
        for (int i = 0; i < result.size(); i++)
        {
            BusChoice resultChoice = result.get(i);

            log.info("[Choice " + (i + 1) + "]");

            List paths = resultChoice.printPathStr();
            for (int j = 0; paths != null && j < paths.size(); j++)
            {
                log.info(paths.get(j));
            }

            log.info(
                " -> Score ("
                    + StringUtil.toNumString(resultChoice.getScorePercent())
                    + "%) : Interchange="
                    + resultChoice.getNoOfInterchange()
                    + ", Price="
                    + resultChoice.getTotalPrice()
                    + ", Distance="
                    + resultChoice.getTotalDistance());
        }
        log.info("========================");
        return result;
    }
}
