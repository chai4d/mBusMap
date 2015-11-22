package chai_4d.mbus.map.screen;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import chai_4d.mbus.map.util.ImageUtil;

public class ImageFilter extends FileFilter
{
    private String description = null;
    private String[] extensions = null;

    public ImageFilter(String description, String extension)
    {
        this(description, new String[] { extension });
    }

    public ImageFilter(String description, String[] extensions)
    {
        if (description == null)
        {
            // Since no description, use first extension and # of extensions as
            // description
            this.description = extensions[0] + "{" + extensions.length + "}";
        }
        else
        {
            this.description = description;
        }
        this.extensions = (String[]) extensions.clone();
    }

    public boolean accept(File f)
    {
        if (f.isDirectory())
        {
            return true;
        }

        String extension = ImageUtil.getExtension(f);
        for (int i = 0; i < extensions.length; i++)
        {
            String ext = extensions[i];
            if (extension.equals(ext))
            {
                return true;
            }
        }
        return false;
    }

    public String getDescription()
    {
        return description;
    }
}