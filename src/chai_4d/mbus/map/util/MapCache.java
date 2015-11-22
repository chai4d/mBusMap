package chai_4d.mbus.map.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapCache extends LinkedHashMap
{
    private static final long serialVersionUID = 4433381720430652150L;

    private final int capacity;
    private long accessCount = 0;
    private long hitCount = 0;

    public MapCache(int capacity)
    {
        super(capacity + 1, 1.1f, true);
        this.capacity = capacity;
    }

    public Object get(Object key)
    {
        accessCount++;
        if (containsKey(key))
        {
            hitCount++;
        }
        Object value = super.get(key);
        return value;
    }

    protected boolean removeEldestEntry(Map.Entry eldest)
    {
        return size() > capacity;
    }

    public long getAccessCount()
    {
        return accessCount;
    }

    public long getHitCount()
    {
        return hitCount;
    }
}