package chai_4d.mbus.map.model;

import java.sql.ResultSet;

import chai_4d.mbus.map.constant.MapConstants.LineType;
import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.util.SQLUtil;

public class BusLine extends BaseModel
{
    private long p1Id = 0;
    private long p2Id = 0;
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 0;
    private int y2 = 0;
    private double distance = 0;
    private long busId = 0;
    private String busNoTh = null;
    private String busNoEn = null;
    private String busPrice = null;
    private LineType type = null;
    private String p1NameTh = null;
    private String p1NameEn = null;
    private String p2NameTh = null;
    private String p2NameEn = null;

    public BusLine(ResultSet rs)
    {
        super(Mode.SELECT);
        int i = 0;
        p1Id = SQLUtil.getLong(rs, ++i);
        p2Id = SQLUtil.getLong(rs, ++i);
        x1 = SQLUtil.getInt(rs, ++i);
        y1 = SQLUtil.getInt(rs, ++i);
        x2 = SQLUtil.getInt(rs, ++i);
        y2 = SQLUtil.getInt(rs, ++i);
        distance = SQLUtil.getInt(rs, ++i);
        busId = SQLUtil.getLong(rs, ++i);
        busNoTh = SQLUtil.getString(rs, ++i);
        busNoEn = SQLUtil.getString(rs, ++i);
        busPrice = SQLUtil.getString(rs, ++i);
        type = LineType.valueOf(SQLUtil.getInt(rs, ++i));
        p1NameTh = SQLUtil.getString(rs, ++i);
        p1NameEn = SQLUtil.getString(rs, ++i);
        p2NameTh = SQLUtil.getString(rs, ++i);
        p2NameEn = SQLUtil.getString(rs, ++i);
    }

    public BusLine(
        long p1Id,
        long p2Id,
        int x1,
        int y1,
        int x2,
        int y2,
        double distance,
        long busId,
        String busNoTh,
        String busNoEn,
        String busPrice,
        LineType type,
        String p1NameTh,
        String p1NameEn,
        String p2NameTh,
        String p2NameEn)
    {
        super(Mode.INSERT);
        this.p1Id = p1Id;
        this.p2Id = p2Id;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.distance = distance;
        this.busId = busId;
        this.busNoTh = busNoTh;
        this.busNoEn = busNoEn;
        this.busPrice = busPrice;
        this.type = type;
        this.p1NameTh = p1NameTh;
        this.p1NameEn = p1NameEn;
        this.p2NameTh = p2NameTh;
        this.p2NameEn = p2NameEn;
    }

    public int getX1()
    {
        return x1;
    }

    public void setX1(int x1)
    {
        this.x1 = x1;
    }

    public int getY1()
    {
        return y1;
    }

    public void setY1(int y1)
    {
        this.y1 = y1;
    }

    public int getX2()
    {
        return x2;
    }

    public void setX2(int x2)
    {
        this.x2 = x2;
    }

    public int getY2()
    {
        return y2;
    }

    public void setY2(int y2)
    {
        this.y2 = y2;
    }

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    public long getBusId()
    {
        return busId;
    }

    public void setBusId(long busId)
    {
        this.busId = busId;
    }

    public LineType getType()
    {
        return type;
    }

    public void setType(LineType type)
    {
        this.type = type;
    }

    public long getP1Id()
    {
        return p1Id;
    }

    public void setP1Id(long id)
    {
        p1Id = id;
    }

    public long getP2Id()
    {
        return p2Id;
    }

    public void setP2Id(long id)
    {
        p2Id = id;
    }

    public String getBusNoTh()
    {
        return busNoTh;
    }

    public void setBusNoTh(String busNoTh)
    {
        this.busNoTh = busNoTh;
    }

    public String getBusNoEn()
    {
        return busNoEn;
    }

    public void setBusNoEn(String busNoEn)
    {
        this.busNoEn = busNoEn;
    }

    public String getBusPrice()
    {
        return busPrice;
    }

    public void setBusPrice(String busPrice)
    {
        this.busPrice = busPrice;
    }

    public String getP1NameTh()
    {
        return p1NameTh;
    }

    public void setP1NameTh(String p1NameTh)
    {
        this.p1NameTh = p1NameTh;
    }

    public String getP1NameEn()
    {
        return p1NameEn;
    }

    public void setP1NameEn(String p1NameEn)
    {
        this.p1NameEn = p1NameEn;
    }

    public String getP2NameTh()
    {
        return p2NameTh;
    }

    public void setP2NameTh(String p2NameTh)
    {
        this.p2NameTh = p2NameTh;
    }

    public String getP2NameEn()
    {
        return p2NameEn;
    }

    public void setP2NameEn(String p2NameEn)
    {
        this.p2NameEn = p2NameEn;
    }
}
