package chai_4d.mbus.map.dijkstra.model;

public class Point
{
    final private int id;
    final private int axisX;
    final private int axisY;
    final private String nameTh;
    final private String nameEn;

    public Point(int id, int axisX, int axisY, String nameTh, String nameEn)
    {
        this.id = id;
        this.axisX = axisX;
        this.axisY = axisY;
        this.nameTh = nameTh;
        this.nameEn = nameEn;
    }

    public int getId()
    {
        return id;
    }

    public int getAxisX()
    {
        return axisX;
    }

    public int getAxisY()
    {
        return axisY;
    }

    public String getNameTh()
    {
        return nameTh;
    }

    public String getNameEn()
    {
        return nameEn;
    }

    public String getName()
    {
        return "{ id:" + id + ", X:" + axisX + ", Y:" + axisY + " }";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        String sId = id + "";
        result = prime * result + ((sId == null) ? 0 : sId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        String sId = id + "";
        Point other = (Point) obj;
        String otherId = other.id + "";
        if (!sId.equals(otherId))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
