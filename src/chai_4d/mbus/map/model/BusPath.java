package chai_4d.mbus.map.model;

public class BusPath
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

    public BusPath(BusLine busLine, long p1)
    {
        if (busLine.getP1Id() == p1)
        {
            this.p1Id = busLine.getP1Id();
            this.p2Id = busLine.getP2Id();
            this.x1 = busLine.getX1();
            this.y1 = busLine.getY1();
            this.x2 = busLine.getX2();
            this.y2 = busLine.getY2();
        }
        else
        {
            this.p1Id = busLine.getP2Id();
            this.p2Id = busLine.getP1Id();
            this.x1 = busLine.getX2();
            this.y1 = busLine.getY2();
            this.x2 = busLine.getX1();
            this.y2 = busLine.getY1();
        }
        this.distance = busLine.getDistance();
        this.busId = busLine.getBusId();
        this.busNoTh = busLine.getBusNoTh();
        this.busNoEn = busLine.getBusNoEn();
        this.busPrice = busLine.getBusPrice();
    }

    public BusPath(BusPath busPath)
    {
        this.p1Id = busPath.getP1Id();
        this.p2Id = busPath.getP2Id();
        this.x1 = busPath.getX1();
        this.y1 = busPath.getY1();
        this.x2 = busPath.getX2();
        this.y2 = busPath.getY2();
        this.distance = busPath.getDistance();
        this.busId = busPath.getBusId();
        this.busNoTh = busPath.getBusNoTh();
        this.busNoEn = busPath.getBusNoEn();
        this.busPrice = busPath.getBusPrice();
    }

    public void printPath()
    {
        System.out.println(this.getBusNoEn() + ", P1=" + this.getP1Id() + ", P2=" + this.getP2Id());
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
}
