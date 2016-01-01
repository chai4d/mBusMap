package chai_4d.mbus.map.model;

import java.util.ArrayList;
import java.util.List;

public class BusChoice
{
    private List<BusPath> busPaths = new ArrayList<BusPath>();
    private int noOfInterchange = 0;
    private double totalPrice = 0.0;
    private double totalDistance = 0.0;

    public BusPath getLastBusPath()
    {
        if (busPaths.size() == 0)
        {
            return null;
        }
        else
        {
            return busPaths.get(busPaths.size() - 1);
        }
    }

    public BusChoice clone()
    {
        BusChoice cloned = new BusChoice();
        for (int i = 0; i < busPaths.size(); i++)
        {
            BusPath originalBusPath = busPaths.get(i);
            cloned.getBusPaths().add(new BusPath(originalBusPath));
        }
        return cloned;
    }

    public List<BusPath> getBusPaths()
    {
        return busPaths;
    }

    public void setBusPaths(List<BusPath> busPaths)
    {
        this.busPaths = busPaths;
    }

    public int getNoOfInterchange()
    {
        return noOfInterchange;
    }

    public void setNoOfInterchange(int noOfInterchange)
    {
        this.noOfInterchange = noOfInterchange;
    }

    public double getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public double getTotalDistance()
    {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance)
    {
        this.totalDistance = totalDistance;
    }
}
