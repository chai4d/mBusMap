package chai_4d.mbus.map.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import chai_4d.mbus.map.bean.MapDbBean;
import chai_4d.mbus.map.constant.MapConstants.LineType;
import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.util.SQLUtil;

public class BusInfo extends BaseModel
{
    private long busId = 0;
    private String busNoTh = null;
    private String busNoEn = null;
    private String detailTh = null;
    private String detailEn = null;
    private String busPic = null;
    private List<BusLine> busLine = null;

    public BusInfo(ResultSet rs)
    {
        super(Mode.SELECT);
        int i = 0;
        busId = SQLUtil.getLong(rs, ++i);
        busNoTh = SQLUtil.getString(rs, ++i);
        busNoEn = SQLUtil.getString(rs, ++i);
        detailTh = SQLUtil.getString(rs, ++i);
        detailEn = SQLUtil.getString(rs, ++i);
        busPic = SQLUtil.getString(rs, ++i);
    }

    public BusInfo()
    {
        super(Mode.INSERT);
        this.busId = SQLUtil.genId();
        this.busNoTh = "";
        this.busNoEn = "";
        this.detailTh = "";
        this.detailEn = "";
        this.busPic = "";
    }

    public List<BusLine> getBusLine()
    {
        if (busLine == null)
        {
            if (busId > 0)
            {
                busLine = MapDbBean.loadBusLine(busId);
            }
            else
            {
                busLine = new ArrayList<BusLine>();
            }
        }
        return busLine;
    }

    public int getCountTwoWay()
    {
        int count = 0;
        List<BusLine> list = getBusLine();
        for (int i = 0; i < list.size(); i++)
        {
            BusLine busLine = list.get(i);
            if (busLine.getMode() != Mode.DELETE && busLine.getType() == LineType.BIDIRECT)
            {
                count++;
            }
        }
        return count;
    }

    public int getCountOneWay()
    {
        int count = 0;
        List<BusLine> list = getBusLine();
        for (int i = 0; i < list.size(); i++)
        {
            BusLine busLine = list.get(i);
            if (busLine.getMode() != Mode.DELETE && (busLine.getType() == LineType.P1_P2 || busLine.getType() == LineType.P2_P1))
            {
                count++;
            }
        }
        return count;
    }

    public long getBusId()
    {
        return busId;
    }

    public void setBusId(long busId)
    {
        this.busId = busId;
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

    public String getDetailTh()
    {
        return detailTh;
    }

    public void setDetailTh(String detailTh)
    {
        this.detailTh = detailTh;
    }

    public String getDetailEn()
    {
        return detailEn;
    }

    public void setDetailEn(String detailEn)
    {
        this.detailEn = detailEn;
    }

    public String getBusPic()
    {
        return busPic;
    }

    public void setBusPic(String busPic)
    {
        this.busPic = busPic;
    }
}
