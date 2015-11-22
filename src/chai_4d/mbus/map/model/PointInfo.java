package chai_4d.mbus.map.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import chai_4d.mbus.map.bean.MapDbBean;
import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.constant.MapConstants.PointType;
import chai_4d.mbus.map.util.SQLUtil;

public class PointInfo extends BaseModel
{
    private long pId = 0;
    private int axisX = 0;
    private int axisY = 0;
    private PointType type = null;
    private List<PointName> pointName = null;

    public PointInfo(ResultSet rs)
    {
        super(Mode.SELECT);
        int i = 0;
        pId = SQLUtil.getLong(rs, ++i);
        axisX = SQLUtil.getInt(rs, ++i);
        axisY = SQLUtil.getInt(rs, ++i);
        type = PointType.valueOf(SQLUtil.getInt(rs, ++i));
    }

    public PointInfo(int axisX, int axisY)
    {
        super(Mode.INSERT);
        this.pId = SQLUtil.genId();
        this.axisX = axisX;
        this.axisY = axisY;
        this.type = PointType.LINK;
    }

    public List<PointName> getPointName()
    {
        if (pointName == null)
        {
            if (pId > 0)
            {
                pointName = MapDbBean.loadPointName(pId);
            }
            else
            {
                pointName = new ArrayList<PointName>();
            }
        }
        return pointName;
    }

    public long getPId()
    {
        return pId;
    }

    public void setPId(long id)
    {
        pId = id;
    }

    public int getAxisX()
    {
        return axisX;
    }

    public void setAxisX(int axisX)
    {
        this.axisX = axisX;
    }

    public int getAxisY()
    {
        return axisY;
    }

    public void setAxisY(int axisY)
    {
        this.axisY = axisY;
    }

    public PointType getType()
    {
        return type;
    }

    public void setType(PointType type)
    {
        this.type = type;
    }
}
