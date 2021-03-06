package chai_4d.mbus.map.model;

import java.util.ArrayList;
import java.util.List;

import chai_4d.mbus.map.util.StringUtil;

public class BusChoice
{
    private int choiceNo = 0;
    private List<BusPath> busPaths = new ArrayList<BusPath>();

    public BusChoice(int choiceNo)
    {
        this.choiceNo = choiceNo;
    }

    public BusPath getFirstBusPath()
    {
        if (busPaths.size() == 0)
        {
            return null;
        }
        else
        {
            return busPaths.get(0);
        }
    }

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

    public String printBusPathsStr()
    {
        String prevKey = "";
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < busPaths.size(); i++)
        {
            BusPath busPath = busPaths.get(i);
            String key = busPath.getBusNoEn();
            if (!prevKey.equals(key))
            {
                result.append(" [" + key + "]" + busPath.getP1Id() + "->" + busPath.getP2Id());
            }
            else
            {
                result.append("->" + busPath.getP2Id());
            }
            prevKey = key;
        }
        return result.toString();
    }

    public BusChoice clone()
    {
        BusChoice cloned = new BusChoice(-1);
        for (int i = 0; i < busPaths.size(); i++)
        {
            BusPath originalBusPath = busPaths.get(i);
            cloned.getBusPaths().add(new BusPath(originalBusPath));
        }
        return cloned;
    }

    public boolean equalChoice(BusChoice anotherChoice)
    {
        if (anotherChoice == null || anotherChoice.getBusPaths() == null || anotherChoice.getBusPaths().size() != busPaths.size())
        {
            return false;
        }
        for (int i = 0; i < busPaths.size(); i++)
        {
            BusPath busPath = busPaths.get(i);
            BusPath anotherBusPath = anotherChoice.getBusPaths().get(i);

            if (busPath.getBusId() != anotherBusPath.getBusId()
                || busPath.getP1Id() != anotherBusPath.getP1Id()
                || busPath.getP2Id() != anotherBusPath.getP2Id())
            {
                return false;
            }
        }
        return true;
    }

    public boolean equalBus(BusChoice anotherChoice)
    {
        if (anotherChoice == null || anotherChoice.getBusPaths() == null)
        {
            return false;
        }

        List key1 = new ArrayList<String>();
        for (int i = 0; i < busPaths.size(); i++)
        {
            BusPath busPath = busPaths.get(i);
            if (key1.contains(busPath.getBusId()) == false)
            {
                key1.add(busPath.getBusId());
            }
        }

        List key2 = new ArrayList<String>();
        for (int i = 0; i < anotherChoice.getBusPaths().size(); i++)
        {
            BusPath busPath = anotherChoice.getBusPaths().get(i);
            if (key2.contains(busPath.getBusId()) == false)
            {
                key2.add(busPath.getBusId());
            }
        }

        return (key1.equals(key2));
    }

    public boolean isContainPoint(long pId)
    {
        for (int i = 0; i < busPaths.size(); i++)
        {
            BusPath busPath = busPaths.get(i);
            if (busPath.getP1Id() == pId || busPath.getP2Id() == pId)
            {
                return true;
            }
        }
        return false;
    }

    public boolean isContainBus(long busId)
    {
        for (int i = 0; i < busPaths.size(); i++)
        {
            BusPath busPath = busPaths.get(i);
            if (busPath.getBusId() == busId)
            {
                return true;
            }
        }
        return false;
    }

    public boolean isReturnSameBusNo()
    {
        String prevKey = "";
        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < busPaths.size(); i++)
        {
            BusPath busPath = busPaths.get(i);
            String key = busPath.getBusNoEn();
            if (!prevKey.equals(key))
            {
                prevKey = key;
                if (keys.contains(key))
                {
                    return true;
                }
                keys.add(key);
            }
        }
        return false;
    }

    public class ABus
    {
        private long busId = 0;
        private String busNo = null;
        private List<BusPath> busPaths = new ArrayList<BusPath>();
        private double busPrice = 0.0;
        private double busDistance = 0.0;

        public ABus(long busId, String busNo, List<BusPath> busPaths)
        {
            this.busId = busId;
            this.busNo = busNo;
            this.busPaths = busPaths;
        }

        private void calcBusPrice()
        {
            double totalPrice = 0.0;
            String busPrice = busPaths.get(0).getBusPrice();
            // ‘0’ = Free
            // ‘0,6.5’ = Free or 6.5 Baht
            // ’10-35(2.5)’ = Minimum is 10 Baht, Maximum is 35 Baht, Increase every 2.5 Baht per Bus Point

            if (!StringUtil.isEmpty(busPrice))
            {
                if (busPrice.indexOf("-") == -1) // Case fixed amount
                {
                    double price = Double.MAX_VALUE;
                    String[] prices = busPrice.split(",");
                    for (int i = 0; i < prices.length; i++)
                    {
                        price = Math.min(price, StringUtil.toDouble(prices[i]));
                    }
                    if (price != Double.MAX_VALUE)
                    {
                        totalPrice = price;
                    }
                }
                else // Case dynamic amount
                {
                    String priceRange = busPrice.substring(0, busPrice.indexOf("("));
                    String priceUnit = busPrice.substring(busPrice.indexOf("(") + 1, busPrice.length() - 1);

                    String[] ranges = priceRange.split("-");
                    double min = StringUtil.toDouble(ranges[0]);
                    double max = StringUtil.toDouble(ranges[1]);
                    double unit = StringUtil.toDouble(priceUnit);

                    totalPrice = min + (busPaths.size() * unit);
                    totalPrice = Math.min(totalPrice, max);
                }
            }
            this.busPrice = totalPrice;
        }

        private void calcBusDistance()
        {
            double totalDistance = 0.0;
            for (int i = 0; i < busPaths.size(); i++)
            {
                BusPath busPath = busPaths.get(i);
                totalDistance += busPath.getDistance();
            }
            this.busDistance = totalDistance;
        }

        public long getBusId()
        {
            return busId;
        }

        public String getBusNo()
        {
            return busNo;
        }

        public List<BusPath> getBusPaths()
        {
            return busPaths;
        }

        public double getBusPrice()
        {
            return busPrice;
        }

        public double getBusDistance()
        {
            return busDistance;
        }
    }

    private List<ABus> buses = new ArrayList<ABus>();
    private int noOfInterchange = 0;
    private double totalPrice = 0.0;
    private double totalDistance = 0.0;

    public void calcScore()
    {
        int no = 0;
        String prevKey = "";
        for (int i = 0; i < busPaths.size(); i++)
        {
            BusPath busPath = busPaths.get(i);
            long id = busPath.getBusId();
            String key = busPath.getBusNoEn();
            if (!prevKey.equals(key))
            {
                prevKey = key;
                no++;

                List<BusPath> busPaths = new ArrayList<BusPath>();
                busPaths.add(busPath);
                buses.add(new ABus(id, key, busPaths));
            }
            else
            {
                ABus aBus = buses.get(no - 1);
                aBus.getBusPaths().add(busPath);
            }
        }

        double sTotalPrice = 0.0;
        double sTotalDistance = 0.0;
        for (int i = 0; i < buses.size(); i++)
        {
            ABus aBus = buses.get(i);
            aBus.calcBusPrice();
            aBus.calcBusDistance();

            sTotalPrice += aBus.getBusPrice();
            sTotalDistance += aBus.getBusDistance();
        }
        this.noOfInterchange = buses.size();
        this.totalPrice = sTotalPrice;
        this.totalDistance = sTotalDistance;
    }

    public List<String> printPathStr()
    {
        List result = new ArrayList<String>();
        for (int i = 0; i < buses.size(); i++)
        {
            ABus aBus = buses.get(i);
            result.add(" -> " + (i + 1) + ") " + aBus.getBusNo() + " : Price=" + aBus.getBusPrice() + ", Distance=" + aBus.getBusDistance());
        }
        return result;
    }

    private double scorePercent = 0.0;

    public void calcScorePercent(int maxInterchange, double maxPrice, double maxDistance)
    {
        // Weight of Interchange = 40%, Price = 25%, Distance = 35%

        double percentInterchange = 0.4;
        double percentPrice = 0.25;
        double percentDistance = 0.35;

        // Ex: Interchange = 2/5, Price = 0/20, Distance = 245/345
        //     ScorePercent = 24.06% ((3/5 x 40%) + (20/20 x 25%) + (100/345 x 35%))
        // Ex: Interchange = 5/5, Price = 20/20, Distance = 345/345
        //     ScorePercent = 0% ((0/5 x 40%) + (0/20 x 25%) + (0/345 x 35%))

        double scoreInterchange = ((maxInterchange - this.noOfInterchange) * percentInterchange) / maxInterchange;
        double scorePrice = ((maxPrice - this.totalPrice) * percentPrice) / maxPrice;
        double scoreDistance = ((maxDistance - this.totalDistance) * percentDistance) / maxDistance;

        if (Double.isNaN(scorePrice))
        {
            scorePrice = percentPrice;
        }

        this.scorePercent = (scoreInterchange + scorePrice + scoreDistance) * 100;
    }

    public int getChoiceNo()
    {
        return choiceNo;
    }

    public void setChoiceNo(int choiceNo)
    {
        this.choiceNo = choiceNo;
    }

    public List<BusPath> getBusPaths()
    {
        return busPaths;
    }

    public void setBusPaths(List<BusPath> busPaths)
    {
        this.busPaths = busPaths;
    }

    public List<ABus> getBuses()
    {
        return buses;
    }

    public void setBuses(List<ABus> buses)
    {
        this.buses = buses;
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

    public double getScorePercent()
    {
        return scorePercent;
    }

    public void setScorePercent(double scorePercent)
    {
        this.scorePercent = scorePercent;
    }
}
