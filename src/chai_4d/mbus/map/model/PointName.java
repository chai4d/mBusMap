package chai_4d.mbus.map.model;

import java.sql.ResultSet;

import chai_4d.mbus.map.constant.MapConstants.Mode;
import chai_4d.mbus.map.util.SQLUtil;

public class PointName extends BaseModel
{
    private long pId = 0;
    private String nameTh = null;
    private String nameEn = null;

    public PointName(ResultSet rs)
    {
        super(Mode.SELECT);
        int i = 0;
        pId = SQLUtil.getLong(rs, ++i);
        nameTh = SQLUtil.getString(rs, ++i);
        nameEn = SQLUtil.getString(rs, ++i);
    }

    public PointName(long pId, String nameTh, String nameEn)
    {
        super(Mode.INSERT);
        this.pId = pId;
        this.nameTh = nameTh;
        this.nameEn = nameEn;
    }

    public long getPId()
    {
        return pId;
    }

    public void setPId(long id)
    {
        pId = id;
    }

    public String getNameTh()
    {
        return nameTh;
    }

    public void setNameTh(String nameTh)
    {
        this.nameTh = nameTh;
    }

    public String getNameEn()
    {
        return nameEn;
    }

    public void setNameEn(String nameEn)
    {
        this.nameEn = nameEn;
    }
}
