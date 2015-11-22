package chai_4d.mbus.map.util;

import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

public class ImageUtil
{
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
            //System.out.println("imgURL: " + path);
            return new ImageIcon(imgURL);
        }
        else
        {
            System.err.println("Couldn't find file: " + path);
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
        return new File(path);
    }
}
