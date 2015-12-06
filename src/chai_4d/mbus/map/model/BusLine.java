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
    private LineType type = null;

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
        type = LineType.valueOf(SQLUtil.getInt(rs, ++i));
    }

    public BusLine(long p1Id, long p2Id, int x1, int y1, int x2, int y2, double distance, long busId, LineType type)
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
        this.type = type;
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
}
