package chai_4d.mbus.map.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import chai_4d.mbus.map.constant.MapConstants.LineType;
import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.dijkstra.model.Line;
import chai_4d.mbus.map.dijkstra.model.Point;
import chai_4d.mbus.map.model.BusInfo;
import chai_4d.mbus.map.model.BusLine;
import chai_4d.mbus.map.model.PointInfo;
import chai_4d.mbus.map.model.PointName;
import chai_4d.mbus.map.screen.MapPanel;
import chai_4d.mbus.map.util.DBManager;
import chai_4d.mbus.map.util.SQLUtil;
import chai_4d.mbus.map.util.StringUtil;

public class MapDbBean
{
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
        }
        return result;
    }

    public static List<BusLine> loadBusLineById(long busId)
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
            sql += " b.distance, b.bus_id, b.type \n";
            sql += "from bus_line b, point_info p1, point_info p2 \n";
            sql += "where b.bus_id = ? \n";
            sql += " and b.p1_id = p1.p_id \n";
            sql += " and b.p2_id = p2.p_id \n";

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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
            sql += " bl.distance, b.bus_id, bl.type \n";
            sql += "from bus_info b, bus_line bl, point_info p1, point_info p2 \n";
            sql += "where b.bus_id = bl.bus_id \n";
            sql += " and b.bus_no_th in " + SQLUtil.getIn(busNoTh) + " \n";
            sql += " and b.bus_no_en in " + SQLUtil.getIn(busNoEn) + " \n";
            sql += " and bl.p1_id = p1.p_id \n";
            sql += " and bl.p2_id = p2.p_id \n";
            sql += "order by b.bus_id \n";

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
        }
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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
            if (busLine.getMode() != Mode.DELETE)
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

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pId);
            pstmt.setString(2, pointName.getNameTh());
            pstmt.setString(3, pointName.getNameEn());

            pstmt.executeUpdate();
            pointName.setMode(Mode.SELECT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pId);
            pstmt.setString(2, pointName.getNameTh());
            pstmt.setString(3, pointName.getNameEn());

            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pointInfo.getPId());
            pstmt.executeUpdate();
            pstmt.close();

            sql = "";
            sql += "delete from point_info \n";
            sql += "where p_id = ? \n";

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, pointInfo.getPId());
            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busLine.getP1Id());
            pstmt.setLong(2, busLine.getP2Id());
            pstmt.setLong(3, busId);

            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
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
            sql += "insert into bus_info (bus_no_th, bus_no_en, detail_th, detail_en, bus_pic) \n";
            sql += "values (?, ?, ?, ?, ?) \n";

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, busInfo.getBusNoTh());
            pstmt.setString(2, busInfo.getBusNoEn());
            pstmt.setString(3, busInfo.getDetailTh());
            pstmt.setString(4, busInfo.getDetailEn());
            pstmt.setString(5, busInfo.getBusPic());

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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
        }
    }

    private static void updateBusInfo(BusInfo busInfo)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "update bus_info set bus_no_th = ?, bus_no_en = ?, detail_th = ?, detail_en = ?, bus_pic = ? \n";
            sql += "where bus_id = ? \n";

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, busInfo.getBusNoTh());
            pstmt.setString(2, busInfo.getBusNoEn());
            pstmt.setString(3, busInfo.getDetailTh());
            pstmt.setString(4, busInfo.getDetailEn());
            pstmt.setString(5, busInfo.getBusPic());
            pstmt.setLong(6, busInfo.getBusId());

            pstmt.executeUpdate();
            busInfo.setMode(Mode.SELECT);
            saveBusLine(busInfo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busInfo.getBusId());
            pstmt.executeUpdate();
            pstmt.close();

            sql = "";
            sql += "delete from bus_info \n";
            sql += "where bus_id = ? \n";

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, busInfo.getBusId());
            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
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

    public static void resetBusPath()
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            String sql = "";
            sql += "delete from bus_path \n";

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
        }
    }

    public static Map<Integer, Point> loadPointInfo()
    {
        Map<Integer, Point> result = new HashMap<Integer, Point>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            String sql = "";
            sql += "select p_id, axis_x, axis_y \n";
            sql += "from point_info \n";
            sql += "order by 1 \n";

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt(1);
                int axisX = rs.getInt(2);
                int axisY = rs.getInt(3);
                result.put(id, new Point(id, axisX, axisY));
            }
            SQLUtil.printSQL(sql);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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

            conn = DBManager.getConnection();
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
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closePreparedStatement(pstmt);
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
            sql += "values (?, ?, ?) \n";

            conn = DBManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, sourceId);
            pstmt.setLong(2, destinationId);
            pstmt.setString(3, busPath);

            pstmt.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            SQLUtil.closePreparedStatement(pstmt);
        }
    }
}
