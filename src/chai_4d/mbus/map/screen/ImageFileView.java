package chai_4d.mbus.map.screen;

import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

import chai_4d.mbus.map.util.ImageUtil;

public class ImageFileView extends FileView
{
    private ImageIcon bmpIcon = ImageUtil.createImageIcon("/icons/file_bmp.gif");
    private ImageIcon jpgIcon = ImageUtil.createImageIcon("/icons/file_jpg.gif");
    private ImageIcon gifIcon = ImageUtil.createImageIcon("/icons/file_gif.gif");
    private ImageIcon tifIcon = ImageUtil.createImageIcon("/icons/file_tif.gif");
    private ImageIcon pngIcon = ImageUtil.createImageIcon("/icons/file_png.gif");

    public String getName(File f)
    {
        return null; //let the L&F FileView figure this out
    }

    public String getDescription(File f)
    {
        return null; //let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f)
    {
        return null; //let the L&F FileView figure this out
    }

    public String getTypeDescription(File f)
    {
        String extension = ImageUtil.getExtension(f);
        String type = null;

        if (extension != null)
        {
            if (extension.equals(ImageUtil.bmp))
            {
                type = "BMP Image";
            }
            else if (extension.equals(ImageUtil.jpeg) || extension.equals(ImageUtil.jpg))
            {
                type = "JPEG Image";
            }
            else if (extension.equals(ImageUtil.gif))
            {
                type = "GIF Image";
            }
            else if (extension.equals(ImageUtil.tiff) || extension.equals(ImageUtil.tif))
            {
                type = "TIFF Image";
            }
            else if (extension.equals(ImageUtil.png))
            {
                type = "PNG Image";
            }
        }
        return type;
    }

    public Icon getIcon(File f)
    {
        String extension = ImageUtil.getExtension(f);
        Icon icon = null;

        if (extension != null)
        {
            if (extension.equals(ImageUtil.bmp))
            {
                icon = bmpIcon;
            }
            else if (extension.equals(ImageUtil.jpeg) || extension.equals(ImageUtil.jpg))
            {
                icon = jpgIcon;
            }
            else if (extension.equals(ImageUtil.gif))
            {
                icon = gifIcon;
            }
            else if (extension.equals(ImageUtil.tiff) || extension.equals(ImageUtil.tif))
            {
                icon = tifIcon;
            }
            else if (extension.equals(ImageUtil.png))
            {
                icon = pngIcon;
            }
        }
        return icon;
    }
}
