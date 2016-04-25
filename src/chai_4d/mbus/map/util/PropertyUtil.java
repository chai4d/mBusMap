package chai_4d.mbus.map.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertyUtil
{
    private PropertyUtil()
    {
    }

    private static String getString(ResourceBundle RES, String key)
    {
        try
        {
            return RES.getString(key);
        }
        catch (MissingResourceException e)
        {
            return "!" + key + "!";
        }
    }

    static class DB
    {
        private static final ResourceBundle RES = ResourceBundle.getBundle("prop.db");

        public static String getString(String key)
        {
            return PropertyUtil.getString(RES, key);
        }
    }

    static class URL
    {
        private static final ResourceBundle RES = ResourceBundle.getBundle("prop.url");

        public static String getString(String key)
        {
            return PropertyUtil.getString(RES, key);
        }
    }
}
