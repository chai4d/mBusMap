package chai_4d.mbus.map.model;

import chai_4d.mbus.map.constant.MapConstants.Mode;

public abstract class BaseModel
{
    protected Mode mode = null;

    public BaseModel(Mode mode)
    {
        this.mode = mode;
    }

    public Mode getMode()
    {
        return mode;
    }

    public void setMode(Mode mode)
    {
        this.mode = mode;
    }

    public void setEdited()
    {
        if (mode == Mode.SELECT)
        {
            mode = Mode.UPDATE;
        }
    }
}
