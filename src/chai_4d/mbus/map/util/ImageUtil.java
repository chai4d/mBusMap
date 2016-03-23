package chai_4d.mbus.map.util;

import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageUtil
{
    private static final Logger log = LogManager.getLogger(ImageUtil.class);

    public static final String bmp = "bmp";
    public static final String jpeg = "jpeg";
    public static final String jpg = "jpg";
    public static final String gif = "gif";
    public static final String tiff = "tiff";
    public static final String tif = "tif";
    public static final String png = "png";

    private ImageUtil()
    {
    }

    public static ImageIcon createImageIcon(String path)
    {
        URL imgURL = ImageUtil.class.getResource(path);
        if (imgURL != null)
        {
            return new ImageIcon(imgURL);
        }
        else
        {
            log.error("Couldn't find file: " + path);
            return null;
        }
    }

    public static String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf(".");

        if (i > 0 && i < s.length() - 1)
        {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static File getFile(String path)
    {
        //return new File(path);
        URL url = ImageUtil.class.getResource(path);
        if (url != null)
        {
            return new File(url.getPath());
        }
        else
        {
            return null;
        }
    }
}
